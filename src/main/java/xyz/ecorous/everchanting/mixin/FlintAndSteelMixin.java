package xyz.ecorous.everchanting.mixin;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.ecorous.everchanting.AbstractSoulFireBlock;
import xyz.ecorous.everchanting.EnchantmentRegistry;
import xyz.ecorous.everchanting.EverchantingMod;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.ecorous.everchanting.enchantments.HellfireEnchantment;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelMixin {

	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	public void onInit(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		cir.cancel();
		PlayerEntity playerEntity = context.getPlayer();
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
			BlockPos blockPos2 = blockPos.offset(context.getSide());
			AbstractFireBlock.getState(world, blockPos2);
			BlockState blockState2;
			if (AbstractSoulFireBlock.canPlaceAt(world, blockPos2, context.getPlayerFacing())) {
				world.playSound(playerEntity, blockPos2, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
				assert playerEntity != null;
				if (EnchantmentHelper.getLevel(EnchantmentRegistry.HELLFIRE, playerEntity.getMainHandStack()) != 0) {
					if (blockState.getBlock() instanceof SoulFireBlock || blockState.getBlock() instanceof AbstractSoulFireBlock) {
						System.out.println("SoulFireBlock detected");
						blockState2 = AbstractFireBlock.getState(world, blockPos);
					} else {
						blockState2 = AbstractSoulFireBlock.getState(world, blockPos2);
					}
				} else {
					blockState2 = AbstractFireBlock.getState(world, blockPos);
				}
				//BlockState blockState2 = AbstractSoulFireBlock.getState(world, blockPos2);
				world.setBlockState(blockPos2, blockState2);
				world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
				ItemStack itemStack = context.getStack();
				if (playerEntity instanceof ServerPlayerEntity) {
					Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos2, itemStack);
					itemStack.damage(1, playerEntity, (p) -> {
						p.sendToolBreakStatus(context.getHand());
					});
				}

				cir.setReturnValue(ActionResult.success(world.isClient()));
			} else {
				cir.setReturnValue(ActionResult.FAIL);
			}
		} else {
			world.playSound(playerEntity, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			world.setBlockState(blockPos, (BlockState) blockState.with(Properties.LIT, true), 11);
			world.emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
			if (playerEntity != null) {
				context.getStack().damage(1, playerEntity, (p) -> {
					p.sendToolBreakStatus(context.getHand());
				});
			}

			cir.setReturnValue(ActionResult.success(world.isClient()));
		}
	}
}
