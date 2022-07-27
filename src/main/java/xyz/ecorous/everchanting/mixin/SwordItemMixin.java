package xyz.ecorous.everchanting.mixin;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import xyz.ecorous.everchanting.EnchantmentRegistry;
import xyz.ecorous.everchanting.enchantments.StrippingEnchantment;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Mixin(SwordItem.class)
public class SwordItemMixin extends ToolItem {
	private static final Map<Block, Block> STRIPPED_BLOCKS = new ImmutableMap.Builder<Block, Block>()
			.put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
			.put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
			.put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
			.put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
			.put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
			.put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
			.put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
			.put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
			.put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
			.put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
			.put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
			.put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
			.put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
			.put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE)
			.put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
			.put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE)
			.put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD)
			.put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG)
			.build();
	public SwordItemMixin(ToolMaterial toolMaterial, Settings settings) {
		super(toolMaterial, settings);
	}
	private Optional<BlockState> getStrippedState(BlockState state) {
		return Optional.ofNullable((Block)STRIPPED_BLOCKS.get(state.getBlock()))
				.map(block -> block.getDefaultState().with(PillarBlock.AXIS, (Direction.Axis)state.get(PillarBlock.AXIS)));
	}
	// Override useOnBlock to add stripping enchantment
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (EnchantmentHelper.getLevel(EnchantmentRegistry.STRIPPING, Objects.requireNonNull(context.getPlayer()).getMainHandStack()) != 0) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		PlayerEntity playerEntity = context.getPlayer();
		BlockState blockState = world.getBlockState(blockPos);
		Optional<BlockState> optional = this.getStrippedState(blockState);
		Optional<BlockState> optional2 = Oxidizable.getDecreasedOxidationState(blockState);
		Optional<BlockState> optional3 = Optional.ofNullable((Block)((BiMap)HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()).get(blockState.getBlock()))
				.map(block -> block.getStateWithProperties(blockState));
		ItemStack itemStack = context.getStack();
		Optional<BlockState> optional4 = Optional.empty();
		if (optional.isPresent()) {
			world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			optional4 = optional;
		} else if (optional2.isPresent()) {
			world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
			optional4 = optional2;
		} else if (optional3.isPresent()) {
			world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.syncWorldEvent(playerEntity, 3004, blockPos, 0);
			optional4 = optional3;
		}

		if (optional4.isPresent()) {
			if (playerEntity instanceof ServerPlayerEntity) {
				Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
			}

			world.setBlockState(blockPos, (BlockState)optional4.get(), 11);
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.create(playerEntity, (BlockState)optional4.get()));
			if (playerEntity != null) {
				itemStack.damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
			}

			return ActionResult.success(world.isClient);
		} else {
			return ActionResult.PASS;
		}
	} else {
		return super.useOnBlock(context);
	}
	}
}
