package xyz.ecorous.everchanting;

import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ecorous.everchanting.enchantments.HellfireEnchantment;

public class EverchantingMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Everchanting");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Loading {}!", mod.metadata().name());
		EnchantmentRegistry.init();
	}
	public static Identifier ID(String id) {
		return new Identifier("everchanting", id);
	}
}
