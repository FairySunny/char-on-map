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
import java.util.Arrays;

public class Mod implements ModInitializer {
	public static final String MOD_ID = "charonmap";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static byte[] FONT_BITMAP;

	public static byte[] getFontBitmap(int codePoint) {
		if (FONT_BITMAP == null || codePoint >= FONT_BITMAP.length / 32) {
			return new byte[32];
		}
		return Arrays.copyOfRange(FONT_BITMAP, codePoint * 32, (codePoint + 1) * 32);
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
						long t = System.nanoTime();
						FONT_BITMAP = is.readAllBytes();
						LOGGER.info("Loaded font bitmap in {}ms", (System.nanoTime() - t) / 1e6);
					} catch (IOException e) {
						LOGGER.error("Failed to load font bitmap", e);
					}
				});
			}
		});
	}
}
