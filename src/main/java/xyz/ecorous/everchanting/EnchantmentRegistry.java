package xyz.ecorous.everchanting;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import xyz.ecorous.everchanting.enchantments.*;

public class EnchantmentRegistry {
	//flint and steel

	public static final HellfireEnchantment HELLFIRE = new HellfireEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR, new EquipmentSlot[]{EquipmentSlot.MAINHAND});

	//sword

	public static final StrippingEnchantment STRIPPING = new StrippingEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	public static void init() {
		Registry.register(Registry.ENCHANTMENT, EverchantingMod.ID("hellfire"), HELLFIRE);
		Registry.register(Registry.ENCHANTMENT, EverchantingMod.ID("stripping"), STRIPPING);
	}

}
