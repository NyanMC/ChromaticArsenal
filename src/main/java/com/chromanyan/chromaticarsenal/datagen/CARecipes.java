package com.chromanyan.chromaticarsenal.datagen;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CARecipes extends RecipeProvider {

    private final TagKey<Item> CHROMA_SHARD = ModTags.Items.GEMS_CHROMA;
    private final TagKey<Item> ASCENSION_ESSENCE = ModTags.Items.DUSTS_ASCENSION;

    public CARecipes(PackOutput output) {
        super(output);
    }

    // center is only ever a chromatic arsenal ingredient
    private void nyanPatternRecipe(@NotNull Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike corners, ItemLike edges, TagKey<Item> center, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern("aba")
                .pattern("bcb")
                .pattern("aba")
                .define('a', corners)
                .define('b', edges)
                .define('c', center)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, name));
    }

    @SuppressWarnings("SameParameterValue")
    private void chromaUpgrade(@NotNull Consumer<FinishedRecipe> consumer, ItemLike item1, ItemLike item2, ItemLike item3, Item output, String name) {
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(item1),
                Ingredient.of(item2),
                Ingredient.of(item3),
                RecipeCategory.COMBAT,
                output
        )
                .unlocks("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, name));
    }

    @SuppressWarnings("SameParameterValue")
    private void packAndUnpack(@NotNull Consumer<FinishedRecipe> consumer, ItemLike unpacked, ItemLike packed, String name, String name2) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, unpacked, 9)
                .requires(packed)
                .unlockedBy("has_unpackable", has(unpacked))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, name + "_unpack"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, packed, 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', unpacked)
                .unlockedBy("has_unpackable", has(unpacked))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, name2));
    }

    @Override
    public void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CHAMPION_CATALYST.get(), 1)
                .requires(ModItems.LUNAR_CRYSTAL.get())
                .requires(Tags.Items.NETHER_STARS)
                .unlockedBy("has_lunar_crystal", has(ModItems.LUNAR_CRYSTAL.get()))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "champion_catalyst"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ASCENSION_ESSENCE.get(), 1)
                .requires(ModItems.CHAMPION_CATALYST.get())
                .requires(CHROMA_SHARD)
                .requires(CHROMA_SHARD)
                .requires(CHROMA_SHARD)
                .requires(CHROMA_SHARD)
                .unlockedBy("has_lunar_crystal", has(ModItems.LUNAR_CRYSTAL.get()))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "ascension_essence"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MAGIC_GARLIC_BREAD.get(), 4)
                .requires(CHROMA_SHARD)
                .requires(Items.BREAD)
                .requires(Items.BREAD)
                .requires(Items.BREAD)
                .requires(Items.BREAD)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "magic_garlic_bread"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GLASS_SHIELD.get(), 1)
                .pattern("pcp")
                .pattern("ggg")
                .pattern(" g ")
                .define('p', Items.POPPED_CHORUS_FRUIT)
                .define('g', Tags.Items.GLASS)
                .define('c', CHROMA_SHARD)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "glass_shield"));

        nyanPatternRecipe(consumer, ModItems.WARD_CRYSTAL.get(), Items.FERMENTED_SPIDER_EYE, Items.AMETHYST_SHARD, CHROMA_SHARD, "ward_crystal");
        nyanPatternRecipe(consumer, ModItems.SHADOW_TREADS.get(), Items.ECHO_SHARD, Items.ENDER_PEARL, CHROMA_SHARD, "shadow_treads");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DUALITY_RINGS.get(), 1)
                .pattern("awm")
                .pattern("rcr")
                .pattern("mba")
                .define('a', Items.ARROW)
                .define('w', Items.WHITE_WOOL)
                .define('m', ModItems.MAGIC_GARLIC_BREAD.get())
                .define('r', ModItems.AMETHYST_RING.get())
                .define('c', CHROMA_SHARD)
                .define('b', Items.BLACK_WOOL)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "duality_rings"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.VERTICAL_STASIS.get(), 1)
                .pattern("pop")
                .pattern("scs")
                .pattern("pop")
                .define('c', CHROMA_SHARD)
                .define('s', Items.SHULKER_SHELL)
                .define('o', ModItems.COSMICOLA.get())
                .define('p', Items.POPPED_CHORUS_FRUIT)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "vertical_stasis_stone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ANONYMITY_UMBRELLA.get(), 1)
                .pattern("ece")
                .pattern(" s ")
                .pattern(" s ")
                .define('c', CHROMA_SHARD)
                .define('s', Tags.Items.RODS_WOODEN)
                .define('e', Items.ECHO_SHARD)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "anonymity_umbrella"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SUPER_GOLDEN_HEART.get(), 1)
                .pattern("d d")
                .pattern("bhb")
                .pattern(" a ")
                .define('d', Tags.Items.GEMS_DIAMOND)
                .define('b', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('a', ASCENSION_ESSENCE)
                .define('h', ModItems.GOLDEN_HEART.get())
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "super_golden_heart"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SUPER_GLASS_SHIELD.get(), 1)
                .pattern("gcg")
                .pattern("gag")
                .pattern(" g ")
                .define('g', Items.TINTED_GLASS)
                .define('a', ASCENSION_ESSENCE)
                .define('c', ModItems.GLASS_SHIELD.get())
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "super_glass_shield"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SUPER_WARD_CRYSTAL.get(), 1)
                .pattern("mam")
                .pattern("bwb")
                .pattern("mbm")
                .define('w', ModItems.WARD_CRYSTAL.get())
                .define('a', ASCENSION_ESSENCE)
                .define('b', Tags.Items.STORAGE_BLOCKS_AMETHYST)
                .define('m', Items.MILK_BUCKET)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "super_ward_crystal"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SUPER_SHADOW_TREADS.get(), 1)
                .pattern(" c ")
                .pattern("cas")
                .pattern(" s ")
                .define('c', Items.FIRE_CHARGE)
                .define('a', ASCENSION_ESSENCE)
                .define('s', ModItems.SHADOW_TREADS.get())
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "super_shadow_treads"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SUPER_LUNAR_CRYSTAL.get(), 1)
                .pattern("a e")
                .pattern(" le")
                .pattern("ee ")
                .define('e', Items.END_STONE)
                .define('a', ASCENSION_ESSENCE)
                .define('l', ModItems.LUNAR_CRYSTAL.get())
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "super_lunar_crystal"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ASCENDED_STAR.get(), 1)
                .pattern("g u")
                .pattern("dac")
                .pattern("f p")
                .define('g', ModItems.SUPER_GOLDEN_HEART.get())
                .define('a', ASCENSION_ESSENCE)
                .define('u', ModItems.SUPER_GLASS_SHIELD.get())
                .define('d', ModItems.SUPER_WARD_CRYSTAL.get())
                .define('c', ModItems.SUPER_SHADOW_TREADS.get())
                .define('f', ModItems.SUPER_FRIENDLY_FIRE_FLOWER.get())
                .define('p', ModItems.SUPER_LUNAR_CRYSTAL.get())
                .unlockedBy("has_chroma_shard", has(ASCENSION_ESSENCE))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "ascended_star"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CHROMA_SALVAGER.get(), 1)
                .pattern("ggg")
                .pattern("scs")
                .pattern("ggg")
                .define('s', Items.FIRE_CHARGE)
                .define('c', ModItems.CHROMA_SHARD.get())
                .define('g', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "chroma_salvager"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SUPER_GLOW_RING.get(), 1)
                .pattern("ai ")
                .pattern("igi")
                .pattern(" i ")
                .define('g', Items.GOLDEN_CARROT)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('a', ASCENSION_ESSENCE)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "super_glow_ring"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CURSED_TOTEM.get(), 1)
                .pattern("rtr")
                .pattern("ece")
                .pattern("rer")
                .define('c', CHROMA_SHARD)
                .define('t', Items.TOTEM_OF_UNDYING)
                .define('r', Items.END_CRYSTAL)
                .define('e', Tags.Items.GEMS_EMERALD)
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "cursed_totem"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_RING.get(), 1)
                .pattern(" c ")
                .pattern("c c")
                .pattern(" c ")
                .define('c', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "copper_ring"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AMETHYST_RING.get(), 1)
                .pattern(" a ")
                .pattern("aca")
                .pattern(" a ")
                .define('a', Tags.Items.GEMS_AMETHYST)
                .define('c', ModItems.COPPER_RING.get())
                .unlockedBy("has_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "amethyst_ring"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CHROMATIC_UPGRADE_SMITHING_TEMPLATE.get(), 1)
                .pattern("dud")
                .pattern("dcd")
                .pattern("ddd")
                .define('d', Tags.Items.GEMS_DIAMOND)
                .define('c', ModItems.CHROMA_SHARD.get())
                .define('u', ModItems.CHROMATIC_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy("has_chroma_shard", has(CHROMA_SHARD))
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "chromatic_upgrade_smithing_template"));

        packAndUnpack(consumer, ModItems.CHROMA_SHARD.get(), ModItems.CHROMA_BLOCK_ITEM.get(), "chroma_shard", "chroma_block");

        chromaUpgrade(consumer, ModItems.CHROMATIC_UPGRADE_SMITHING_TEMPLATE.get(), ModItems.GOLDEN_HEART.get(), Items.DRAGON_BREATH, ModItems.ADVANCING_HEART.get(), "advancing_heart");
        chromaUpgrade(consumer, ModItems.CHROMATIC_UPGRADE_SMITHING_TEMPLATE.get(), Items.TORCHFLOWER, ModItems.CHROMA_SHARD.get(), ModItems.FRIENDLY_FIRE_FLOWER.get(), "friendly_fire_flower");
        chromaUpgrade(consumer, ModItems.CHROMATIC_UPGRADE_SMITHING_TEMPLATE.get(), Items.FEATHER, ModItems.CHROMA_SHARD.get(), ModItems.HARPY_FEATHER.get(), "harpy_feather");
        chromaUpgrade(consumer, ModItems.CHROMATIC_UPGRADE_SMITHING_TEMPLATE.get(), Items.ANVIL, ModItems.CHROMA_SHARD.get(), ModItems.WORLD_ANCHOR.get(), "world_anchor");
        chromaUpgrade(consumer, ModItems.CHROMATIC_UPGRADE_SMITHING_TEMPLATE.get(), Items.END_STONE, ModItems.CHROMA_SHARD.get(), ModItems.GRAVITY_STONE.get(), "gravity_stone");
        chromaUpgrade(consumer, ModItems.CHROMATIC_UPGRADE_SMITHING_TEMPLATE.get(), ModItems.HARPY_FEATHER.get(), ModItems.ASCENSION_ESSENCE.get(), ModItems.SUPER_HARPY_FEATHER.get(), "super_harpy_feather");
    }
}
