package dev.chocolate.block;

import dev.chocolate.Chocolate;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {
    public static final ActorBlock ACTOR = registerActor();

    private ModBlocks() {
    }

    private static ActorBlock registerActor() {
        Identifier id = Identifier.fromNamespaceAndPath(Chocolate.MOD_ID, "actor");
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);

        ActorBlock actor = new ActorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DISPENSER)
                .setId(blockKey));
        Registry.register(BuiltInRegistries.BLOCK, blockKey, actor);
        Registry.register(BuiltInRegistries.ITEM, itemKey,
                new BlockItem(actor, new Item.Properties().useBlockDescriptionPrefix().setId(itemKey)));
        return actor;
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS)
                .register(output -> output.accept(ACTOR));
    }
}
