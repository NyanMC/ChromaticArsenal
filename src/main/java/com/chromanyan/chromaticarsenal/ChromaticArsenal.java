package com.chromanyan.chromaticarsenal;

import com.chromanyan.chromaticarsenal.client.renderer.CuriosRenderers;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.datagen.*;
import com.chromanyan.chromaticarsenal.datagen.tags.*;
import com.chromanyan.chromaticarsenal.events.*;
import com.chromanyan.chromaticarsenal.init.*;
import com.chromanyan.chromaticarsenal.items.curios.CurioGoldenHeart;
import com.chromanyan.chromaticarsenal.items.curios.advanced.CurioIlluminatedSoul;
import com.chromanyan.chromaticarsenal.triggers.GlassShieldBlockTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ChromaticArsenal.MODID)
public class ChromaticArsenal {
    // Directly reference a log4j logger.

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "chromaticarsenal";

    public ChromaticArsenal() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::gatherData);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> bus.addListener(CuriosRenderers::onLayerRegister));

        ModSounds.SOUNDS_REGISTRY.register(bus);
        ModBlocks.BLOCKS_REGISTRY.register(bus);
        ModItems.ITEMS_REGISTRY.register(bus);
        ModEffects.EFFECTS_REGISTRY.register(bus);
        ModPotions.POTIONS_REGISTRY.register(bus);
        ModEnchantments.ENCHANTMENTS_REGISTRY.register(bus);
        ModCreativeTabs.CREATIVE_TABS.register(bus);
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.commonSpec);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ModConfig.clientSpec);
        bus.register(ModConfig.class);

        ModItems.tryBOGCompat();
        ModItems.tryEnigmaticLegacyCompat();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput output = gen.getPackOutput();
        if (event.includeClient()) {
            gen.addProvider(true, new CAModels(output, efh));
        }
        if (event.includeServer()) {
            gen.addProvider(true, new CARecipes(output));
            gen.addProvider(true, new CAAdvancements(output, lookupProvider, efh));
            CABlockTags blockTags = new CABlockTags(output, lookupProvider, efh);
            gen.addProvider(true, blockTags);
            gen.addProvider(true, new CAItemTags(output, lookupProvider, blockTags.contentsGetter(), efh));
    }
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new MiscEvents());
        MinecraftForge.EVENT_BUS.register(new CurioEvents());
        MinecraftForge.EVENT_BUS.register(new LootEvents());

        Registry.register(Registry.CUSTOM_STAT, ModStats.GSHIELD_TOTAL_BLOCK_LOCATION, ModStats.GSHIELD_TOTAL_BLOCK_LOCATION);
        Registry.register(Registry.CUSTOM_STAT, ModStats.CHROMA_SALVAGER_USES_LOCATION, ModStats.CHROMA_SALVAGER_USES_LOCATION);
        Stats.CUSTOM.get(ModStats.GSHIELD_TOTAL_BLOCK_LOCATION, StatFormatter.DIVIDE_BY_TEN);
        ModStats.load();

        CriteriaTriggers.register(GlassShieldBlockTrigger.INSTANCE);

        ModPotions.doRecipes();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        CurioGoldenHeart.registerVariants();
        CurioIlluminatedSoul.registerVariants();

        CuriosRenderers.register();
    }
}
