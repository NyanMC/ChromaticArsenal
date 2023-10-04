package com.chromanyan.chromaticarsenal;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.datagen.CAAdvancements;
import com.chromanyan.chromaticarsenal.datagen.CAModels;
import com.chromanyan.chromaticarsenal.datagen.CARecipes;
import com.chromanyan.chromaticarsenal.datagen.tags.CABlockTags;
import com.chromanyan.chromaticarsenal.datagen.tags.CAItemTags;
import com.chromanyan.chromaticarsenal.events.EventClassInstance;
import com.chromanyan.chromaticarsenal.init.*;
import com.chromanyan.chromaticarsenal.items.curios.CurioGoldenHeart;
import com.chromanyan.chromaticarsenal.items.curios.advanced.CurioIlluminatedSoul;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ChromaticArsenal.MODID)
public class ChromaticArsenal {
    // Directly reference a log4j logger.

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "chromaticarsenal";
    public static final CreativeModeTab GROUP = new CAGroup(MODID);
    private static final ResourceLocation SUPER_CURIO_ICON = new ResourceLocation("curios", "slot/empty_super_curio_slot"); // 1.19.2 curios is stupid and requires slot textures to be registered under its own namespace

    public ChromaticArsenal() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::gatherData);

        ModSounds.SOUNDS_REGISTRY.register(bus);
        ModBlocks.BLOCKS_REGISTRY.register(bus);
        ModItems.ITEMS_REGISTRY.register(bus);
        ModPotions.EFFECTS_REGISTRY.register(bus);
        ModEnchantments.ENCHANTMENTS_REGISTRY.register(bus);
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.commonSpec);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ModConfig.clientSpec);
        bus.register(ModConfig.class);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();
        if (event.includeClient()) {
            gen.addProvider(true, new CAModels(gen, efh));
        }
        if (event.includeServer()) {
            gen.addProvider(true, new CARecipes(gen));
            gen.addProvider(true, new CAAdvancements(gen, efh));
            CABlockTags blockTags = new CABlockTags(gen, event.getExistingFileHelper());
            gen.addProvider(true, blockTags);
            gen.addProvider(true, new CAItemTags(gen, blockTags, efh));
    }
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventClassInstance());
        Registry.register(Registry.CUSTOM_STAT, ModStats.GSHIELD_TOTAL_BLOCK_LOCATION, ModStats.GSHIELD_TOTAL_BLOCK_LOCATION);
        Registry.register(Registry.CUSTOM_STAT, ModStats.CHROMA_SALVAGER_USES_LOCATION, ModStats.CHROMA_SALVAGER_USES_LOCATION);
        Stats.CUSTOM.get(ModStats.GSHIELD_TOTAL_BLOCK_LOCATION, StatFormatter.DIVIDE_BY_TEN);
        ModStats.load();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        CurioGoldenHeart.registerVariants();
        CurioIlluminatedSoul.registerVariants();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("body").size(1).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").size(2).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").size(1).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("super_curio").icon(SUPER_CURIO_ICON).size(1).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("curio").size(0).build()); // for mark of the twisted
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        event.addSprite(SUPER_CURIO_ICON);
    }


}
