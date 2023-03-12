package com.chromanyan.chromaticarsenal.datagen;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.init.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CAAdvancements extends AdvancementProvider {
    public CAAdvancements(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    private Advancement simpleHasItemRecipe(ItemLike itemLike, Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        return Advancement.Builder.advancement()
                .addCriterion("has_item", hasItem(itemLike))
                .save(consumer, new ResourceLocation(Reference.MODID, itemLike.toString()), fileHelper);
    }

    @Override
    @SuppressWarnings("unused")
    protected void registerAdvancements(@NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper fileHelper) {
        Advancement chromaShard = simpleHasItemRecipe(ModItems.CHROMA_SHARD.get(), consumer, fileHelper);
        Advancement ascensionEssence = simpleHasItemRecipe(ModItems.ASCENSION_ESSENCE.get(), consumer, fileHelper);
        Advancement spicyCoal = simpleHasItemRecipe(ModItems.SPICY_COAL.get(), consumer, fileHelper);

        Advancement goldenHeart = simpleHasItemRecipe(ModItems.GOLDEN_HEART.get(), consumer, fileHelper);
        Advancement glassShield = simpleHasItemRecipe(ModItems.GLASS_SHIELD.get(), consumer, fileHelper);
        Advancement wardCrystal = simpleHasItemRecipe(ModItems.WARD_CRYSTAL.get(), consumer, fileHelper);
    }

    private CriterionTriggerInstance hasItem(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build());
    }
}
