package com.mrbysco.saltfactory.registry;

import com.mrbysco.saltfactory.SaltFactory;
import com.mrbysco.saltfactory.item.CryingbowlItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SaltRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SaltFactory.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SaltFactory.MOD_ID);

	public static final RegistryObject<SoundEvent> CRYING = SOUND_EVENTS.register("crying", () -> new SoundEvent(new ResourceLocation(SaltFactory.MOD_ID, "crying")));

	public static final RegistryObject<Item> CRYING_BOWL = ITEMS.register("crying_bowl" , () -> new CryingbowlItem(new Item.Properties().tab(SaltFactory.TAB_FACTORY).stacksTo(1)));
	public static final RegistryObject<Item> TEARY_BOWL = ITEMS.register("teary_bowl" , () -> new Item(new Item.Properties().tab(SaltFactory.TAB_FACTORY).stacksTo(1)));
	public static final RegistryObject<Item> DRIED_TEARS = ITEMS.register("dried_tears" , () -> new Item(new Item.Properties().tab(SaltFactory.TAB_FACTORY)));

}
