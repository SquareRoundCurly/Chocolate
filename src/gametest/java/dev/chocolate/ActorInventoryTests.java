package dev.chocolate;

import dev.chocolate.block.ActorBlockEntity;
import dev.chocolate.block.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

public class ActorInventoryTests {
    private static final BlockPos ACTOR_POS = new BlockPos(3, 3, 3);

    @GameTest(maxTicks = 100)
    public void hopperInsertsFromAbove(GameTestHelper helper) {
        ActorBlockEntity actor = placeActor(helper);
        BlockPos hopperPos = ACTOR_POS.above();
        helper.setBlock(hopperPos, Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, Direction.DOWN));
        HopperBlockEntity hopper = helper.getBlockEntity(hopperPos, HopperBlockEntity.class);
        hopper.setItem(0, new ItemStack(Items.IRON_PICKAXE));
        helper.succeedWhen(() -> {
            helper.assertTrue(actor.getItem(0).is(Items.IRON_PICKAXE), "Hopper must insert a tool into the Actor");
            helper.assertTrue(hopper.isEmpty(), "Inserted tool must leave the hopper");
            helper.assertTrue(actor.getContainerSize() == 1, "Actor must have exactly one slot");
        });
    }

    @GameTest(maxTicks = 100)
    public void hopperInsertsFromSide(GameTestHelper helper) {
        ActorBlockEntity actor = placeActor(helper);
        BlockPos hopperPos = ACTOR_POS.west();
        helper.setBlock(hopperPos, Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, Direction.EAST));
        HopperBlockEntity hopper = helper.getBlockEntity(hopperPos, HopperBlockEntity.class);
        hopper.setItem(0, new ItemStack(Items.WHEAT_SEEDS, 3));
        helper.succeedWhen(() -> {
            helper.assertTrue(actor.getItem(0).is(Items.WHEAT_SEEDS) && actor.getItem(0).getCount() == 3,
                    "Side hopper must insert and stack seeds");
            helper.assertTrue(hopper.isEmpty(), "All transferred seeds must leave the hopper");
        });
    }

    @GameTest(maxTicks = 100)
    public void hopperExtractsFromBelow(GameTestHelper helper) {
        ActorBlockEntity actor = placeActor(helper);
        actor.setItem(0, new ItemStack(Items.IRON_AXE));
        BlockPos hopperPos = ACTOR_POS.below();
        helper.setBlock(hopperPos, Blocks.HOPPER);
        HopperBlockEntity hopper = helper.getBlockEntity(hopperPos, HopperBlockEntity.class);
        helper.succeedWhen(() -> {
            helper.assertTrue(actor.isEmpty(), "Hopper must empty the Actor");
            helper.assertTrue(hopper.getItem(0).is(Items.IRON_AXE), "Extracted tool must arrive in hopper");
        });
    }

    @GameTest(maxTicks = 40)
    public void fullSlotDoesNotLoseItems(GameTestHelper helper) {
        ActorBlockEntity actor = placeActor(helper);
        actor.setItem(0, new ItemStack(Items.WHEAT_SEEDS, 64));
        BlockPos hopperPos = ACTOR_POS.above();
        helper.setBlock(hopperPos, Blocks.HOPPER);
        HopperBlockEntity hopper = helper.getBlockEntity(hopperPos, HopperBlockEntity.class);
        hopper.setItem(0, new ItemStack(Items.WHEAT_SEEDS, 7));
        helper.runAfterDelay(20, () -> {
            helper.assertTrue(actor.getItem(0).getCount() == 64, "Actor stack must not overflow");
            helper.assertTrue(hopper.getItem(0).getCount() == 7, "Uninserted items must remain in hopper");
            helper.succeed();
        });
    }

    @GameTest
    public void inventorySurvivesSerialization(GameTestHelper helper) {
        ActorBlockEntity actor = placeActor(helper);
        ItemStack tool = new ItemStack(Items.IRON_PICKAXE);
        tool.setDamageValue(12);
        actor.setItem(0, tool);
        var registry = helper.getLevel().registryAccess();
        var saved = actor.saveWithFullMetadata(registry);
        BlockEntity restored = BlockEntity.loadStatic(actor.getBlockPos(), actor.getBlockState(), saved, registry);
        helper.assertTrue(restored instanceof ActorBlockEntity, "Saved inventory must recreate an Actor");
        ItemStack restoredTool = ((ActorBlockEntity) restored).getItem(0);
        helper.assertTrue(ItemStack.matches(tool, restoredTool), "Count, item type, and tool damage must survive saving");
        helper.succeed();
    }

    @GameTest
    public void breakingActorDropsContentsOnce(GameTestHelper helper) {
        ActorBlockEntity actor = placeActor(helper);
        actor.setItem(0, new ItemStack(Items.WHEAT_SEEDS, 13));
        helper.destroyBlock(ACTOR_POS);
        helper.assertItemEntityCountIs(Items.WHEAT_SEEDS, ACTOR_POS, 2, 13);
        helper.succeed();
    }

    private ActorBlockEntity placeActor(GameTestHelper helper) {
        helper.setBlock(ACTOR_POS, ModBlocks.ACTOR);
        return helper.getBlockEntity(ACTOR_POS, ActorBlockEntity.class);
    }
}
