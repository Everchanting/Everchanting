package xyz.ecorous.everchanting.enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import xyz.ecorous.everchanting.BaseEnchantment;

public class StrippingEnchantment extends BaseEnchantment {
	public StrippingEnchantment(Rarity rarityIn, EnchantmentTarget enchantmentTarget, EquipmentSlot[] equipmentSlot) {
		super(rarityIn, enchantmentTarget, equipmentSlot);
	}

	@Override
	public boolean isApplicableTo(ItemStack stack) {
		boolean g;
		if(stack.getItem() instanceof SwordItem) {
			g = EnchantmentHelper.getLevel(this, stack) == 0;
		} else {
			g = false;
		}
		return g;
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		boolean g;
		if(stack.getItem() instanceof SwordItem) {
			g = EnchantmentHelper.getLevel(this, stack) == 0;
		} else {
			g = false;
		}
		return g;
	}
}
