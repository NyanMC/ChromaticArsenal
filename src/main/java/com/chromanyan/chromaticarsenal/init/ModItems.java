package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.items.*;
import com.chromanyan.chromaticarsenal.items.compat.*;
import com.chromanyan.chromaticarsenal.items.curios.*;
import com.chromanyan.chromaticarsenal.items.curios.advanced.*;
import com.chromanyan.chromaticarsenal.items.curios.basic.CurioAmethystRing;
import com.chromanyan.chromaticarsenal.items.curios.basic.CurioCopperRing;
import com.chromanyan.chromaticarsenal.items.curios.basic.CurioVitalStone;
import com.chromanyan.chromaticarsenal.items.curios.challenge.*;
import com.chromanyan.chromaticarsenal.items.curios.utility.*;
import com.chromanyan.chromaticarsenal.items.food.*;
import com.google.common.collect.Sets;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

@SuppressWarnings("unused") // intellij doesn't recognize that things like ASCENSION_ESSENCE are, in fact, used
public class ModItems {
    public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ChromaticArsenal.MODID);
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    public static RegistryObject<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        RegistryObject<Item> item = ITEMS_REGISTRY.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

    // materials
    public static final RegistryObject<Item> CHROMA_SHARD = registerWithTab("chroma_shard", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ASCENSION_ESSENCE = registerWithTab("ascension_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> CHAMPION_CATALYST = registerWithTab("champion_catalyst", ChampionCatalyst::new);

    // standard curios
    public static final RegistryObject<Item> GOLDEN_HEART = registerWithTab("golden_heart", CurioGoldenHeart::new);
    public static final RegistryObject<Item> GLASS_SHIELD = registerWithTab("glass_shield", CurioGlassShield::new);
    public static final RegistryObject<Item> WARD_CRYSTAL = registerWithTab("ward_crystal", CurioWardCrystal::new);
    public static final RegistryObject<Item> SHADOW_TREADS = registerWithTab("shadow_treads", CurioShadowTreads::new);
    public static final RegistryObject<Item> DUALITY_RINGS = registerWithTab("duality_rings", CurioDualityRings::new);
    public static final RegistryObject<Item> FRIENDLY_FIRE_FLOWER = registerWithTab("friendly_fire_flower", CurioFriendlyFireFlower::new);
    public static final RegistryObject<Item> LUNAR_CRYSTAL = registerWithTab("lunar_crystal", CurioLunarCrystal::new);
    public static final RegistryObject<Item> CRYO_RING = registerWithTab("cryo_ring", CurioCryoRing::new);
    public static final RegistryObject<Item> BUBBLE_AMULET = registerWithTab("bubble_amulet", CurioBubbleAmulet::new);
    public static final RegistryObject<Item> MOMENTUM_STONE = registerWithTab("momentum_stone", CurioMomentumStone::new);
    public static final RegistryObject<Item> ADVANCING_HEART = registerWithTab("advancing_heart", CurioAdvancingHeart::new);
    public static final RegistryObject<Item> THUNDERGUARD = registerWithTab("thunderguard", CurioThunderguard::new);

    // super curios
    public static final RegistryObject<Item> SUPER_GOLDEN_HEART = registerWithTab("super_golden_heart", CurioDiamondHeart::new);
    public static final RegistryObject<Item> SUPER_GLASS_SHIELD = registerWithTab("super_glass_shield", CurioShieldOfUndying::new);
    public static final RegistryObject<Item> SUPER_WARD_CRYSTAL = registerWithTab("super_ward_crystal", CurioDispellingCrystal::new);
    public static final RegistryObject<Item> SUPER_SHADOW_TREADS = registerWithTab("super_shadow_treads", CurioCelestialCharm::new);
    public static final RegistryObject<Item> SUPER_FRIENDLY_FIRE_FLOWER = registerWithTab("super_friendly_fire_flower", CurioInfernoFlower::new);
    public static final RegistryObject<Item> SUPER_LUNAR_CRYSTAL = registerWithTab("super_lunar_crystal", CurioPrismaticCrystal::new);
    public static final RegistryObject<Item> SUPER_HARPY_FEATHER = registerWithTab("super_harpy_feather", CurioPolychromaticFeather::new);
    public static final RegistryObject<Item> SUPER_GLOW_RING = registerWithTab("super_glow_ring", CurioIlluminatedSoul::new);

    // challenge curios
    public static final RegistryObject<Item> ASCENDED_STAR = registerWithTab("ascended_star", CurioLimitBreak::new);
    public static final RegistryObject<Item> WORLD_ANCHOR = registerWithTab("world_anchor", CurioWorldAnchor::new);
    public static final RegistryObject<Item> CURSED_TOTEM = registerWithTab("cursed_totem", CurioCursedTotem::new);

    // utility curios
    public static final RegistryObject<Item> GRAVITY_STONE = registerWithTab("gravity_stone", CurioGravityStone::new);
    public static final RegistryObject<Item> VERTICAL_STASIS = registerWithTab("vertical_stasis_stone", CurioVerticalStasis::new);
    public static final RegistryObject<Item> HARPY_FEATHER = registerWithTab("harpy_feather", CurioHarpyFeather::new);
    public static final RegistryObject<Item> BLAHAJ = registerWithTab("blahaj", CurioBlahaj::new);
    public static final RegistryObject<Item> ANONYMITY_UMBRELLA = registerWithTab("anonymity_umbrella", CurioAnonymityUmbrella::new);

    // basic curios
    public static final RegistryObject<Item> AMETHYST_RING = registerWithTab("amethyst_ring", CurioAmethystRing::new);
    public static final RegistryObject<Item> COPPER_RING = registerWithTab("copper_ring", CurioCopperRing::new);
    public static final RegistryObject<Item> VITAL_STONE = registerWithTab("vital_stone", CurioVitalStone::new);

    // misc. items
    public static final RegistryObject<Item> MAGIC_GARLIC_BREAD = registerWithTab("magic_garlic_bread", MagicGarlicBread::new);
    public static final RegistryObject<Item> COSMICOLA = registerWithTab("cosmicola", Cosmicola::new);
    public static final RegistryObject<Item> CHROMA_SALVAGER = registerWithTab("chroma_salvager", ChromaSalvager::new);
    public static final RegistryObject<Item> CHROMANYAN = ITEMS_REGISTRY.register("chromanyan_plush", CurioChromaNyan::new);
    public static final RegistryObject<Item> DEBUG = registerWithTab("viewer_item", CurioDebug::new);

    // bog compatibility
    public static RegistryObject<Item> MARK_TWISTED = null;

    // enigmaticlegacy compatibility
    public static RegistryObject<Item> OMNI_RING = null;

    // block items
    public static final RegistryObject<BlockItem> CHROMA_BLOCK_ITEM = ITEMS_REGISTRY.register("chroma_block", () -> new BlockItem(ModBlocks.CHROMA_BLOCK.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static void tryBOGCompat() {
        //if (!ModList.get().isLoaded("band_of_gigantism")) return;
        //MARK_TWISTED = registerWithTab("mark_twisted", MarkTwisted::new);
    }

    public static void tryEnigmaticLegacyCompat() {
        if (!ModList.get().isLoaded("enigmaticlegacy")) return;
        OMNI_RING = registerWithTab("omni_ring", CurioOmniRing::new);
    }

}
