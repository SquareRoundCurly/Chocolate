package dev.chocolate.screen;

import dev.chocolate.menu.ActorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ActorScreen extends AbstractContainerScreen<ActorMenu> {
    private static final Identifier BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");

    public ActorScreen(ActorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, leftPos, topPos,
                0, 0, imageWidth, imageHeight, 256, 256);
        // Cover the dispenser's 3x3 grid and restore only its center slot.
        graphics.fill(leftPos + 61, topPos + 16, leftPos + 115, topPos + 70, 0xFFC6C6C6);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, leftPos + 79, topPos + 34,
                79, 34, 18, 18, 256, 256);
    }
}
