package icu.sunny.mc.charonmap.mixin;

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
    private static final long[] FONT_BITMAP = {
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
            0x0000000000000000L, 0x0001000101010101L, 0x0000000000000505L, 0x000a0a1f0a1f0a0aL, 0x00040f100e011e04L, 0x0011120204080911L, 0x0016090d16040a04L, 0x0000000000000101L, 0x0004020101010204L, 0x0001020404040201L, 0x0000000000050205L, 0x000004041f040400L, 0x0101000000000000L, 0x000000001f000000L, 0x0001000000000000L, 0x0001020204080810L,
            0x000e11131519110eL, 0x001f040404040604L, 0x001f11020c10110eL, 0x000e11100c10110eL, 0x0010101f11121418L, 0x000e1110100f011fL, 0x000e11110f01020cL, 0x000404040810111fL, 0x000e11110e11110eL, 0x000608101e11110eL, 0x0001000000010000L, 0x0101000000010000L, 0x0008040201020408L, 0x00001f00001f0000L, 0x0001020408040201L, 0x000400040810110eL,
            0x1e013d252d211e00L, 0x00111111111f110eL, 0x000f1111110f110fL, 0x000e11010101110eL, 0x000f11111111110fL, 0x001f01010107011fL, 0x000101010107011fL, 0x000e11111119011eL, 0x00111111111f1111L, 0x0007020202020207L, 0x000e111010101010L, 0x0011111109070911L, 0x001f010101010101L, 0x0011111111151b11L, 0x0011111119151311L, 0x000e11111111110eL,
            0x00010101010f110fL, 0x001609111111110eL, 0x00111111110f110fL, 0x000e1110100e011eL, 0x000404040404041fL, 0x000e111111111111L, 0x00040a0a11111111L, 0x00111b1511111111L, 0x001111110a040a11L, 0x0004040404040a11L, 0x001f01020408101fL, 0x0007010101010107L, 0x0010080804020201L, 0x0007040404040407L, 0x0000000000110a04L, 0x1f00000000000000L,
            0x0000000000000201L, 0x001e111e100e0000L, 0x000f1111130d0101L, 0x000e1101110e0000L, 0x001e111119161010L, 0x001e011f110e0000L, 0x00020202020f020cL, 0x0f101e11111e0000L, 0x00111111130d0101L, 0x0001010101010001L, 0x0e11111010100010L, 0x0009050305090101L, 0x0002010101010101L, 0x00111115150b0000L, 0x00111111110f0000L, 0x000e1111110e0000L,
            0x01010f11130d0000L, 0x10101e1119160000L, 0x00010101130d0000L, 0x000f100e011e0000L, 0x0004020202070202L, 0x001e111111110000L, 0x00040a1111110000L, 0x001e151511110000L, 0x00110a040a110000L, 0x0f101e1111110000L, 0x001f0204081f0000L, 0x0004020201020204L, 0x0001010101010101L, 0x0001020204020201L, 0x0000000000001926L, 0x0000000000000000L,
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x001f02020f02120cL, 0x0000000000000000L, 0x0000000000000000L, 0x020504040e041408L,
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x00000f000e0f0806L, 0x00000f0006090906L, 0x0000000000000000L, 0x0000000000000000L, 0x000010101f000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000241209122400L, 0x0000091224120900L,
            0x1144114411441144L, 0x55aa55aa55aa55aaL, 0x77dbeedb77dbeedbL, 0x1818181818181818L, 0x1818181f18181818L, 0x1818181f181f1818L, 0x6c6c6c6f6c6c6c6cL, 0x6c6c6c7f00000000L, 0x1818181f181f0000L, 0x6c6c6c6f606f6c6cL, 0x6c6c6c6c6c6c6c6cL, 0x6c6c6c6f607f0000L, 0x0000007f606f6c6cL, 0x0000007f6c6c6c6cL, 0x0000001f181f1818L, 0x1818181f00000000L,
            0x000000f818181818L, 0x000000ff18181818L, 0x181818ff00000000L, 0x181818f818181818L, 0x000000ff00000000L, 0x181818ff18181818L, 0x181818f818f81818L, 0x6c6c6cec6c6c6c6cL, 0x000000fc0cec6c6cL, 0x6c6c6cec0cfc0000L, 0x000000ff00ef6c6cL, 0x6c6c6cef00ff0000L, 0x6c6c6cec0cec6c6cL, 0x000000ff00ff0000L, 0x6c6c6cef00ef6c6cL, 0x000000ff00ff1818L,
            0x000000ff6c6c6c6cL, 0x181818ff00ff0000L, 0x6c6c6cff00000000L, 0x000000fc6c6c6c6cL, 0x000000f818f81818L, 0x181818f818f80000L, 0x6c6c6cfc00000000L, 0x6c6c6cff6c6c6c6cL, 0x181818ff18ff1818L, 0x0000001f18181818L, 0x181818f800000000L, 0xffffffffffffffffL, 0xffffffff00000000L, 0x0f0f0f0f0f0f0f0fL, 0xf0f0f0f0f0f0f0f0L, 0x00000000ffffffffL,
            0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x001d22454951225cL, 0x001c02011f01021cL, 0x0000000000000000L,
            0x00003f003f003f00L, 0x001f0004041f0404L, 0x001f000204080402L, 0x001f000804020408L, 0x0808080808484830L, 0x0609090808080808L, 0x000004001f000400L, 0x0000192600192600L, 0x0000000006090906L, 0x0000001818000000L, 0x0000000000000000L, 0x000c0a0908080838L, 0x0000000909090700L, 0x0000000f02040906L, 0x00001f1f1f1f1f00L, 0x0000000000000000L,
    };

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

        if (name.length() == 1) {
            char ch = name.charAt(0);
            if (ch < 256) {
                fillBitmap(state, FONT_BITMAP[ch], 32, 32, 8, 8, MapColor.LIME.getRenderColorByte(MapColor.Brightness.HIGH));
            }
        } else {
            for (int i = 0; i < 2; i++) {
                char ch = name.charAt(i);
                if (ch < 256) {
                    fillBitmap(state, FONT_BITMAP[ch], i * 64, 32, 8, 8, MapColor.LIME.getRenderColorByte(MapColor.Brightness.HIGH));
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
