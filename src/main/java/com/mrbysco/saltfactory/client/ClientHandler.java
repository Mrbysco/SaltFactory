package com.mrbysco.saltfactory.client;

import com.mrbysco.saltfactory.registry.SaltRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {

	public static void onClientSetup(final FMLClientSetupEvent event) {
		ItemProperties.register(SaltRegistry.CRYING_BOWL.get(), new ResourceLocation("filled"), (stack, level, entity, tintIndex) -> {
			int tears = stack.getTag() != null ? stack.getTag().getInt("Tears") : 0;
			if (tears < 3) {
				return 0.0F;
			} else if (tears < 5) {
				return 0.25F;
			} else if (tears < 8) {
				return 0.5F;
			} else if (tears < 9) {
				return 0.75F;
			} else {
				return 1.0F;
			}
		});
	}
}
