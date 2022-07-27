package xyz.ecorous.everchanting.mixin;

import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.ecorous.everchanting.EverchantingMod;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Shadow
	public abstract Item asItem();

	@Inject(method = "isEnchantable", at = @At("TAIL"), cancellable = true)
	public void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}
	@Inject(method = "getEnchantability", at = @At("TAIL"), cancellable = true)
	public void getEnchantability(CallbackInfoReturnable<Integer> cir) {
		if (this.asItem() instanceof FlintAndSteelItem) {
			//EverchantingMod.LOGGER.info("Setting FlintAndSteel enchantability to 20 using ItemMixin");
			cir.setReturnValue(20);
		}
	}
}
