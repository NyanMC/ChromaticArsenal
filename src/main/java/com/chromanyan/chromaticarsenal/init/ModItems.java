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

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("all")
public class ModItems {
	public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);
	
	public static final RegistryObject<Item> CHROMA_SHARD = genericItem("chroma_shard", new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> ASCENSION_ESSENCE = genericItem("ascension_essence", new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.RARE));
	public static final RegistryObject<Item> SPICY_COAL = modItem("spicy_coal", new SpicyCoal());
	
	public static final RegistryObject<Item> GOLDEN_HEART = modItem("golden_heart", new CurioGoldenHeart());
	public static final RegistryObject<Item> GLASS_SHIELD = modItem("glass_shield", new CurioGlassShield());
	public static final RegistryObject<Item> WARD_CRYSTAL = modItem("ward_crystal", new BaseCurioItem());
	public static final RegistryObject<Item> SHADOW_TREADS = modItem("shadow_treads", new CurioShadowTreads());
	public static final RegistryObject<Item> DUALITY_RINGS = modItem("duality_rings", new BaseCurioItem());
	public static final RegistryObject<Item> FRIENDLY_FIRE_FLOWER = modItem("friendly_fire_flower", new CurioFriendlyFireFlower());
	public static final RegistryObject<Item> LUNAR_CRYSTAL = modItem("lunar_crystal", new CurioLunarCrystal());
	
	public static final RegistryObject<Item> SUPER_GOLDEN_HEART = modItem("super_golden_heart", new CurioDiamondHeart());
	public static final RegistryObject<Item> SUPER_GLASS_SHIELD = modItem("super_glass_shield", new CurioShieldOfUndying());
	public static final RegistryObject<Item> SUPER_WARD_CRYSTAL = modItem("super_ward_crystal", new BaseSuperCurio(ModItems.WARD_CRYSTAL));
	
	public static final RegistryObject<Item> MAGIC_GARLIC_BREAD = modItem("magic_garlic_bread", new MagicGarlicBread());
	public static final RegistryObject<Item> COSMICOLA = modItem("cosmicola", new Cosmicola());
	
	public static final RegistryObject<BlockItem> CHROMA_BLOCK_ITEM = blockItem("chroma_block", ModBlocks.CHROMA_BLOCK, new Item.Properties().tab(ChromaticArsenal.GROUP).rarity(Rarity.UNCOMMON));

    private static RegistryObject<Item> genericItem(String name, Item.Properties prop) {
        return ITEMS_REGISTRY.register(name, () -> new Item(prop));
    }

    private static RegistryObject<Item> modItem(String name, Item item) {
        return ITEMS_REGISTRY.register(name, () -> item);
    }
    
    private static RegistryObject<BlockItem> blockItem(String name, RegistryObject<Block> block, Item.Properties prop) {
    	return ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(), prop));
    }
}
