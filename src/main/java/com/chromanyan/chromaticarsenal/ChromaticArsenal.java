package com.chromanyan.chromaticarsenal;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModBlocks;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import com.chromanyan.chromaticarsenal.util.EventClassInstance;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MODID)
public class ChromaticArsenal
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup GROUP = new CAGroup(Reference.MODID);

    public ChromaticArsenal() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        bus.addListener(this::doClientStuff);

        ModBlocks.BLOCKS_REGISTRY.register(bus);
        ModItems.ITEMS_REGISTRY.register(bus);
        ModPotions.EFFECTS_REGISTRY.register(bus);
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.commonSpec);
        bus.register(ModConfig.class);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new EventClassInstance());
        // some preinit code
        //LOGGER.info("HELLO FROM PREINIT");
        //LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
    	InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("body").size(1).build());
    	InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").size(1).build());
    	InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").size(1).build());
    	InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("super_curio").size(1).build());
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("chromaticarsenal", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        //LOGGER.info("Got IMC {}", event.getIMCStream().
        //        map(m->m.getMessageSupplier().get()).
        //        collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            //LOGGER.info("HELLO from Register Block");
        }
    }
}
