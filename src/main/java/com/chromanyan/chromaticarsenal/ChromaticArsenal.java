package com.chromanyan.chromaticarsenal;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.datagen.CAAdvancements;
import com.chromanyan.chromaticarsenal.datagen.CAModels;
import com.chromanyan.chromaticarsenal.datagen.CARecipes;
import com.chromanyan.chromaticarsenal.events.EventClassInstance;
import com.chromanyan.chromaticarsenal.init.ModBlocks;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MODID)
public class ChromaticArsenal {
    // Directly reference a log4j logger.
    @SuppressWarnings("all")
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab GROUP = new CAGroup(Reference.MODID);

    public ChromaticArsenal() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::gatherData);

        ModBlocks.BLOCKS_REGISTRY.register(bus);
        ModItems.ITEMS_REGISTRY.register(bus);
        ModPotions.EFFECTS_REGISTRY.register(bus);
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.commonSpec);
        bus.register(ModConfig.class);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
            gen.addProvider(new CAModels(gen, event.getExistingFileHelper()));
        }
        if (event.includeServer()) {
            gen.addProvider(new CARecipes(gen));
            gen.addProvider(new CAAdvancements(gen, event.getExistingFileHelper()));
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventClassInstance());
        // some preinit code
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("body").size(1).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").size(1).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").size(1).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("super_curio").size(1).build());
    }


}
