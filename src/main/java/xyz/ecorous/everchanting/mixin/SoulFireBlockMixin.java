package xyz.ecorous.everchanting.mixin;


import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoulFireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoulFireBlock.class)
public class SoulFireBlockMixin {
	@Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
	public void onInit(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
		if (!(state.getBlock() instanceof AirBlock)) {
			cir.setReturnValue(true);
		}
	}
}
