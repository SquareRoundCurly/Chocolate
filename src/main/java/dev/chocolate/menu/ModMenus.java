package dev.chocolate.menu;

import dev.chocolate.Chocolate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

public final class ModMenus {
    public static final MenuType<ActorMenu> ACTOR = Registry.register(BuiltInRegistries.MENU,
            Identifier.fromNamespaceAndPath(Chocolate.MOD_ID, "actor"),
            new MenuType<>(ActorMenu::new, FeatureFlagSet.of()));

    private ModMenus() {
    }

    public static void initialize() {
        // Trigger registration during common initialization.
    }
}
