package icu.sunny.mc.charonmap;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("charonmap");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}
