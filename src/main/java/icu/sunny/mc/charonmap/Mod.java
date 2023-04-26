package icu.sunny.mc.charonmap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Mod implements ModInitializer {
	public static final String MOD_ID = "charonmap";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static byte[] FONT_BITMAPS;

	public static long getFontBitmap(char ch) {
		if (FONT_BITMAPS == null || ch >= FONT_BITMAPS.length / 8) {
			return 0;
		}
		long bitmap = 0L;
		for (int i = 0; i < 8; i++) {
			bitmap |= (long)FONT_BITMAPS[ch * 8 + i] << i * 8;
		}
		return bitmap;
	}

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier(MOD_ID, "bitmaps");
			}

			@Override
			public void reload(ResourceManager manager) {
				manager.getResource(new Identifier(MOD_ID, "bitmaps/font.bin")).ifPresent(resource -> {
					try(InputStream is = resource.getInputStream()) {
						FONT_BITMAPS = is.readAllBytes();
					} catch (IOException e) {
						LOGGER.error("Failed to load font bitmap", e);
					}
				});
			}
		});
	}
}
