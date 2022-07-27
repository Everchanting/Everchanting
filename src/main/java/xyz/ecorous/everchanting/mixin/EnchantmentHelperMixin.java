package xyz.ecorous.everchanting.mixin;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.ecorous.everchanting.EverchantingMod;
import xyz.ecorous.everchanting.enchantments.HellfireEnchantment;

import java.util.List;
import java.util.Objects;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	private static final List<String> EVERCHANTING_ENCHANTMENTS = Lists.newArrayList("Hellfire", "Stripping");
	@Inject(method = "getPossibleEntries", at = @At("HEAD"), cancellable = true)
	private static void mixin1(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		cir.cancel();
		List<EnchantmentLevelEntry> list = Lists.<EnchantmentLevelEntry>newArrayList();
		Item item = stack.getItem();
		boolean bl = stack.isOf(Items.BOOK);

		for(Enchantment enchantment : Registry.ENCHANTMENT) {
			if ((!enchantment.isTreasure() || treasureAllowed) && enchantment.isAvailableForRandomSelection() && (enchantment.isAcceptableItem(item.getDefaultStack()) || bl)) {
				for(int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
					//EverchantingMod.LOGGER.info("Reached Goal 2");
					list.add(new EnchantmentLevelEntry(enchantment, i));
				}
			}
		}

		cir.setReturnValue(list);
	}
}
