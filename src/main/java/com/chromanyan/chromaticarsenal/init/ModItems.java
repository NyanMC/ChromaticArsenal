package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.items.SpicyCoal;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.items.base.BaseSuperCurio;
import com.chromanyan.chromaticarsenal.items.curios.*;
import com.chromanyan.chromaticarsenal.items.curios.advanced.CurioDiamondHeart;
import com.chromanyan.chromaticarsenal.items.curios.advanced.CurioShieldOfUndying;
import com.chromanyan.chromaticarsenal.items.food.Cosmicola;
import com.chromanyan.chromaticarsenal.items.food.MagicGarlicBread;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("all")
public class ModItems {
	public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);
	
	public static final RegistryObject<Item> CHROMA_SHARD = ITEMS_REGISTRY.register("chroma_shard", () -> new Item(new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ASCENSION_ESSENCE = ITEMS_REGISTRY.register("ascension_essence", () -> new Item(new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SPICY_COAL = ITEMS_REGISTRY.register("spicy_coal", () -> new SpicyCoal());
	public static final RegistryObject<Item> MAGMATIC_SCRAP = ITEMS_REGISTRY.register("magmatic_scrap", () -> new Item(new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.UNCOMMON).fireResistant()));
	
	public static final RegistryObject<Item> GOLDEN_HEART = ITEMS_REGISTRY.register("golden_heart", () -> new CurioGoldenHeart());
	public static final RegistryObject<Item> GLASS_SHIELD = ITEMS_REGISTRY.register("glass_shield", () -> new CurioGlassShield());
	public static final RegistryObject<Item> WARD_CRYSTAL = ITEMS_REGISTRY.register("ward_crystal", () -> new BaseCurioItem());
	public static final RegistryObject<Item> SHADOW_TREADS = ITEMS_REGISTRY.register("shadow_treads", () -> new CurioShadowTreads());
	public static final RegistryObject<Item> DUALITY_RINGS = ITEMS_REGISTRY.register("duality_rings", () -> new BaseCurioItem());
	public static final RegistryObject<Item> FRIENDLY_FIRE_FLOWER = ITEMS_REGISTRY.register("friendly_fire_flower", () -> new CurioFriendlyFireFlower());
	public static final RegistryObject<Item> LUNAR_CRYSTAL = ITEMS_REGISTRY.register("lunar_crystal", () -> new CurioLunarCrystal());
	public static final RegistryObject<Item> HARPY_FEATHER = ITEMS_REGISTRY.register("harpy_feather", () -> new CurioHarpyFeather());
	
	public static final RegistryObject<Item> SUPER_GOLDEN_HEART = ITEMS_REGISTRY.register("super_golden_heart", () -> new CurioDiamondHeart());
	public static final RegistryObject<Item> SUPER_GLASS_SHIELD = ITEMS_REGISTRY.register("super_glass_shield", () -> new CurioShieldOfUndying());
	public static final RegistryObject<Item> SUPER_WARD_CRYSTAL = ITEMS_REGISTRY.register("super_ward_crystal", () -> new BaseSuperCurio(ModItems.WARD_CRYSTAL));
	
	public static final RegistryObject<Item> MAGIC_GARLIC_BREAD = ITEMS_REGISTRY.register("magic_garlic_bread", () -> new MagicGarlicBread());
	public static final RegistryObject<Item> COSMICOLA = ITEMS_REGISTRY.register("cosmicola", () -> new Cosmicola());
	
	public static final RegistryObject<BlockItem> CHROMA_BLOCK_ITEM = ITEMS_REGISTRY.register("chroma_block", () -> new BlockItem (ModBlocks.CHROMA_BLOCK.get(), new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.UNCOMMON)));

}
