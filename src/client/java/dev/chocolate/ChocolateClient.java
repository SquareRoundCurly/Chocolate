package dev.chocolate;

import dev.chocolate.menu.ModMenus;
import dev.chocolate.screen.ActorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class ChocolateClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenus.ACTOR, ActorScreen::new);
    }
}
