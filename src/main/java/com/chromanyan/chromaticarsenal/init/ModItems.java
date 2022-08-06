package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.items.base.BaseCurioItem;
import com.chromanyan.chromaticarsenal.items.curios.CurioGlassShield;
import com.chromanyan.chromaticarsenal.items.curios.CurioGoldenHeart;
import com.chromanyan.chromaticarsenal.items.curios.CurioLunarCrystal;
import com.chromanyan.chromaticarsenal.items.curios.CurioShadowTreads;
import com.chromanyan.chromaticarsenal.items.food.MagicGarlicBread;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);
	
	public static final RegistryObject<Item> CHROMA_SHARD = genericItem("chroma_shard");
	
	public static final RegistryObject<Item> GOLDEN_HEART = modItem("golden_heart", new CurioGoldenHeart());
	public static final RegistryObject<Item> GLASS_SHIELD = modItem("glass_shield", new CurioGlassShield());
	public static final RegistryObject<Item> WARD_CRYSTAL = modItem("ward_crystal", new BaseCurioItem());
	public static final RegistryObject<Item> SHADOW_TREADS = modItem("shadow_treads", new CurioShadowTreads());
	public static final RegistryObject<Item> DUALITY_RINGS = modItem("duality_rings", new BaseCurioItem());
	public static final RegistryObject<Item> LUNAR_CRYSTAL = modItem("lunar_crystal", new CurioLunarCrystal());
	
	public static final RegistryObject<Item> MAGIC_GARLIC_BREAD = modItem("magic_garlic_bread", new MagicGarlicBread());

    private static RegistryObject<Item> genericItem(String name) {
        return ITEMS_REGISTRY.register(name, () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
    }

    private static RegistryObject<Item> modItem(String name, Item item) {
        return ITEMS_REGISTRY.register(name, () -> item);
    }
}
