package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.items.SpicyCoal;
import com.chromanyan.chromaticarsenal.items.curios.*;
import com.chromanyan.chromaticarsenal.items.curios.advanced.*;
import com.chromanyan.chromaticarsenal.items.food.Cosmicola;
import com.chromanyan.chromaticarsenal.items.food.MagicGarlicBread;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused") // intellij doesn't recognize that things like ASCENSION_ESSENCE are, in fact, used
public class ModItems {
	public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);
	
	public static final RegistryObject<Item> CHROMA_SHARD = ITEMS_REGISTRY.register("chroma_shard", () -> new Item(new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ASCENSION_ESSENCE = ITEMS_REGISTRY.register("ascension_essence", () -> new Item(new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SPICY_COAL = ITEMS_REGISTRY.register("spicy_coal", SpicyCoal::new);
	public static final RegistryObject<Item> MAGMATIC_SCRAP = ITEMS_REGISTRY.register("magmatic_scrap", () -> new Item(new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.UNCOMMON).fireResistant()));
	
	public static final RegistryObject<Item> GOLDEN_HEART = ITEMS_REGISTRY.register("golden_heart", CurioGoldenHeart::new);
	public static final RegistryObject<Item> GLASS_SHIELD = ITEMS_REGISTRY.register("glass_shield", CurioGlassShield::new);
	public static final RegistryObject<Item> WARD_CRYSTAL = ITEMS_REGISTRY.register("ward_crystal", CurioWardCrystal::new);
	public static final RegistryObject<Item> SHADOW_TREADS = ITEMS_REGISTRY.register("shadow_treads", CurioShadowTreads::new);
	public static final RegistryObject<Item> DUALITY_RINGS = ITEMS_REGISTRY.register("duality_rings", CurioDualityRings::new);
	public static final RegistryObject<Item> FRIENDLY_FIRE_FLOWER = ITEMS_REGISTRY.register("friendly_fire_flower", CurioFriendlyFireFlower::new);
	public static final RegistryObject<Item> LUNAR_CRYSTAL = ITEMS_REGISTRY.register("lunar_crystal", CurioLunarCrystal::new);
	public static final RegistryObject<Item> HARPY_FEATHER = ITEMS_REGISTRY.register("harpy_feather", CurioHarpyFeather::new);
	
	public static final RegistryObject<Item> SUPER_GOLDEN_HEART = ITEMS_REGISTRY.register("super_golden_heart", CurioDiamondHeart::new);
	public static final RegistryObject<Item> SUPER_GLASS_SHIELD = ITEMS_REGISTRY.register("super_glass_shield", CurioShieldOfUndying::new);
	public static final RegistryObject<Item> SUPER_WARD_CRYSTAL = ITEMS_REGISTRY.register("super_ward_crystal", CurioDispellingCrystal::new);
	public static final RegistryObject<Item> SUPER_SHADOW_TREADS = ITEMS_REGISTRY.register("super_shadow_treads", CurioCelestialCharm::new);

	public static final RegistryObject<Item> ASCENDED_STAR = ITEMS_REGISTRY.register("ascended_star", CurioLimitBreak::new);
	
	public static final RegistryObject<Item> MAGIC_GARLIC_BREAD = ITEMS_REGISTRY.register("magic_garlic_bread", MagicGarlicBread::new);
	public static final RegistryObject<Item> COSMICOLA = ITEMS_REGISTRY.register("cosmicola", Cosmicola::new);
	
	public static final RegistryObject<BlockItem> CHROMA_BLOCK_ITEM = ITEMS_REGISTRY.register("chroma_block", () -> new BlockItem (ModBlocks.CHROMA_BLOCK.get(), new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.UNCOMMON)));

}
