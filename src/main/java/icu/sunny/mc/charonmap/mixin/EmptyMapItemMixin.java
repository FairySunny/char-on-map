package icu.sunny.mc.charonmap.mixin;

import icu.sunny.mc.charonmap.Mod;
import net.minecraft.block.MapColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EmptyMapItem.class)
public class EmptyMapItemMixin {
    @Nullable
    private static String getCustomName(ItemStack itemStack) {
        NbtCompound nbt = itemStack.getSubNbt("display");
        if (nbt != null && nbt.contains("Name", 8)) {
            try {
                Text text = Text.Serializer.fromJson(nbt.getString("Name"));
                if (text != null) {
                    return text.getString();
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static void fillRect(MapState state, int x, int z, int width, int height, byte color) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                state.colors[(x + j) + (z + i) * 128] = color;
            }
        }
    }

    private static void fillBitmap(MapState state, long bitmap, int x, int z, int pixelWidth, int pixelHeight, byte color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((bitmap & (1L << (i * 8 + j))) != 0) {
                    fillRect(state, x + j * pixelWidth, z + i * pixelHeight, pixelWidth, pixelHeight, color);
                }
            }
        }
    }

    private static ItemStack createMap(World world, String name) {
        ItemStack map = new ItemStack(Items.FILLED_MAP);
        MapState state = MapState.of((byte)0, true, world.getRegistryKey());
        int id = world.getNextMapId();

        NbtCompound nbt = map.getOrCreateNbt();
        nbt.putInt("map", id);
        nbt.putBoolean("map_to_lock", true);
        map.setCustomName(Text.of(name));
        map.addHideFlag(ItemStack.TooltipSection.ADDITIONAL);

        if (name.length() == 1) {
            char ch = name.charAt(0);
            if (ch < 256) {
                fillBitmap(state, Mod.getFontBitmap(ch), 32, 32, 8, 8, MapColor.LIME.getRenderColorByte(MapColor.Brightness.HIGH));
            }
        } else {
            for (int i = 0; i < 2; i++) {
                char ch = name.charAt(i);
                if (ch < 256) {
                    fillBitmap(state, Mod.getFontBitmap(ch), i * 64, 32, 8, 8, MapColor.LIME.getRenderColorByte(MapColor.Brightness.HIGH));
                }
            }
        }

        world.putMapState(FilledMapItem.getMapName(id), state);

        return map;
    }

    @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (world.isClient) {
            return;
        }
        ItemStack emptyMap = user.getStackInHand(hand);
        String name = getCustomName(emptyMap);
        if (name != null) {
            if (!user.getAbilities().creativeMode) {
                emptyMap.decrement(1);
            }
            ItemStack filledMap = createMap(world, name);
            if (emptyMap.isEmpty()) {
                cir.setReturnValue(TypedActionResult.consume(filledMap));
            } else {
                if (!user.getInventory().insertStack(filledMap.copy())) {
                    user.dropItem(filledMap, false);
                }
                cir.setReturnValue(TypedActionResult.consume(emptyMap));
            }
        }
    }
}
