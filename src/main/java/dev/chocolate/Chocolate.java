package dev.chocolate;

import dev.chocolate.block.ModBlocks;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chocolate implements ModInitializer {
    public static final String MOD_ID = "chocolate";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.initialize();
        LOGGER.info("Chocolate initialized");
    }
}
