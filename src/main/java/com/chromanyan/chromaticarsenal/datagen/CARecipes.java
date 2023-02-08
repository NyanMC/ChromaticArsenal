package com.chromanyan.chromaticarsenal.datagen;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CARecipes extends RecipeProvider {

    private final TagKey<Item> CHROMA_SHARD = ModTags.Items.GEMS_CHROMA;
    private final TagKey<Item> ASCENSION_ESSENCE = ModTags.Items.DUSTS_ASCENSION;

    public CARecipes(DataGenerator gen) {
        super(gen);
    }

    @Override
    public @NotNull String getName() {
        return "Chromatic Arsenal Recipes";
    }

    // center is only ever a chroma shard or ascension essence in these
    public void nyanPatternRecipe(@NotNull Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike corners, ItemLike edges, TagKey<Item> center, String name) {
        ShapedRecipeBuilder.shaped(output, 1)
                .pattern("aba")
                .pattern("bcb")
                .pattern("aba")
                .define('a', corners)
                .define('b', edges)
                .define('c', center)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(Reference.MODID, name));
    }

    @Override
    public void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModItems.GLASS_SHIELD.get(), 1)
                .pattern("pcp")
                .pattern("ggg")
                .pattern(" g ")
                .define('p', Items.POPPED_CHORUS_FRUIT)
                .define('g', Tags.Items.GLASS)
                .define('c', CHROMA_SHARD)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(Reference.MODID, "glass_shield"));

        nyanPatternRecipe(consumer, ModItems.WARD_CRYSTAL.get(), Items.FERMENTED_SPIDER_EYE, Items.AMETHYST_SHARD, CHROMA_SHARD, "ward_crystal");
        nyanPatternRecipe(consumer, ModItems.SHADOW_TREADS.get(), Items.BLACK_WOOL, Items.ENDER_PEARL, CHROMA_SHARD, "shadow_treads");

        ShapedRecipeBuilder.shaped(ModItems.DUALITY_RINGS.get(), 1)
                .pattern("awm")
                .pattern("wcb")
                .pattern("mba")
                .define('a', Items.ARROW)
                .define('w', Items.WHITE_WOOL)
                .define('m', ModItems.MAGIC_GARLIC_BREAD.get())
                .define('c', CHROMA_SHARD)
                .define('b', Items.BLACK_WOOL)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(Reference.MODID, "duality_rings"));

        nyanPatternRecipe(consumer, ModItems.FRIENDLY_FIRE_FLOWER.get(), ModItems.MAGMATIC_SCRAP.get(), Items.ORANGE_TULIP, CHROMA_SHARD, "friendly_fire_flower");
        nyanPatternRecipe(consumer, ModItems.SUPER_GOLDEN_HEART.get(), Items.DIAMOND, ModItems.GOLDEN_HEART.get(), ASCENSION_ESSENCE, "super_golden_heart");

        ShapedRecipeBuilder.shaped(ModItems.SUPER_GLASS_SHIELD.get(), 1)
                .pattern("gcg")
                .pattern("gag")
                .pattern(" g ")
                .define('g', Items.TINTED_GLASS)
                .define('a', ASCENSION_ESSENCE)
                .define('c', ModItems.GLASS_SHIELD.get())
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(Reference.MODID, "super_glass_shield"));

        nyanPatternRecipe(consumer, ModItems.SUPER_WARD_CRYSTAL.get(), Items.FERMENTED_SPIDER_EYE, ModItems.WARD_CRYSTAL.get(), ASCENSION_ESSENCE, "super_ward_crystal");
    }
}
