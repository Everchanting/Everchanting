package xyz.ecorous.everchanting;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public abstract class BaseEnchantment extends Enchantment {
	public BaseEnchantment(Rarity rarityIn, EnchantmentTarget enchantmentTarget, EquipmentSlot[] equipmentSlot) {
		super(rarityIn, enchantmentTarget, equipmentSlot);
	}

	public abstract boolean isApplicableTo(ItemStack stack);

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return true;
	}
}
