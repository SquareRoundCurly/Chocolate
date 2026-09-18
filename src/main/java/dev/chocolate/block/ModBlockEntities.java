package dev.chocolate.block;

import dev.chocolate.Chocolate;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
    public static final BlockEntityType<ActorBlockEntity> ACTOR = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Chocolate.MOD_ID, "actor"),
            FabricBlockEntityTypeBuilder.create(ActorBlockEntity::new, ModBlocks.ACTOR).build());

    private ModBlockEntities() {
    }

    public static void initialize() {
        // Trigger registration during common initialization.
    }
}
