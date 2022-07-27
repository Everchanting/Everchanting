package xyz.ecorous.everchanting.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import xyz.ecorous.everchanting.BaseEnchantment;

public class HellfireEnchantment extends BaseEnchantment {
	public HellfireEnchantment(Rarity rarityIn, EnchantmentTarget enchantmentTarget, EquipmentSlot[] equipmentSlot) {
		super(rarityIn, enchantmentTarget, equipmentSlot);
	}

	@Override
	public boolean canAccept(Enchantment enchantment) {
		return true;
	}

	@Override
	public boolean isApplicableTo(ItemStack stack) {
		boolean g;
		if(stack.getItem() instanceof FlintAndSteelItem) {
			g = EnchantmentHelper.getLevel(this, stack) == 0;
		} else {
			g = false;
		}
		return g;
	}
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		boolean g;
		if(stack.getItem() instanceof FlintAndSteelItem) {
			g = EnchantmentHelper.getLevel(this, stack) == 0;
		} else {
			g = false;
		}
		return g;
	}
}
