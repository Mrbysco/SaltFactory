package com.mrbysco.saltfactory;

import com.mrbysco.saltfactory.client.ClientHandler;
import com.mrbysco.saltfactory.registry.SaltRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SaltFactory.MOD_ID)
public class SaltFactory {
    public static final String MOD_ID = "saltfactory";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final CreativeModeTab TAB_FACTORY = new CreativeModeTab(MOD_ID) {
        public ItemStack makeIcon() {
            return new ItemStack(SaltRegistry.CRYING_BOWL.get());
        }
    };

    public SaltFactory() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SaltRegistry.ITEMS.register(eventBus);
        SaltRegistry.SOUND_EVENTS.register(eventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
        });
    }
}
