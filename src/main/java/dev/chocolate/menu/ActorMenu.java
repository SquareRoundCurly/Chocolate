package dev.chocolate.menu;

import dev.chocolate.block.ActorBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ActorMenu extends AbstractContainerMenu {
    private final Container container;

    public ActorMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(ActorBlockEntity.SIZE));
    }

    public ActorMenu(int containerId, Inventory inventory, Container container) {
        super(ModMenus.ACTOR, containerId);
        checkContainerSize(container, ActorBlockEntity.SIZE);
        this.container = container;
        addSlot(new Slot(container, 0, 80, 35));
        addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (!isValidSlotIndex(index)) {
            return ItemStack.EMPTY;
        }
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index == 0) {
            if (!moveItemStackTo(stack, 1, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, 0, 1, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, stack);
        return original;
    }
}
