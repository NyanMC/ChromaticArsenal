package com.chromanyan.chromaticarsenal.datagen;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.triggers.GlassShieldBlockTrigger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CAAdvancements extends AdvancementProvider {
    // this class is so fucked

    private static final Item[] CURIOS = new Item[] {
            ModItems.GOLDEN_HEART.get(), ModItems.GLASS_SHIELD.get(), ModItems.WARD_CRYSTAL.get(), ModItems.SHADOW_TREADS.get(), ModItems.DUALITY_RINGS.get(), ModItems.FRIENDLY_FIRE_FLOWER.get(), ModItems.LUNAR_CRYSTAL.get(), ModItems.CRYO_RING.get(), ModItems.BUBBLE_AMULET.get(), ModItems.MOMENTUM_STONE.get(),
            ModItems.SUPER_GOLDEN_HEART.get(), ModItems.SUPER_GLASS_SHIELD.get(), ModItems.SUPER_WARD_CRYSTAL.get(), ModItems.SUPER_SHADOW_TREADS.get(), ModItems.SUPER_FRIENDLY_FIRE_FLOWER.get(), ModItems.SUPER_LUNAR_CRYSTAL.get(), ModItems.SUPER_GLOW_RING.get(),
            ModItems.ASCENDED_STAR.get(), ModItems.WORLD_ANCHOR.get(), ModItems.CURSED_TOTEM.get(), ModItems.GRAVITY_STONE.get(), ModItems.VERTICAL_STASIS.get(), ModItems.HARPY_FEATHER.get(), ModItems.SUPER_HARPY_FEATHER.get(), ModItems.BLAHAJ.get()
    };

    public CAAdvancements(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    private Advancement.Builder simpleHasItemBase(ItemLike itemLike) {
        return Advancement.Builder.advancement()
                .addCriterion("has_item", hasItem(itemLike));
    }

    private Advancement.Builder displayedHasItemBase(ItemLike itemLike, FrameType frameType) {
        return simpleHasItemBase(itemLike)
                .display(
                        itemLike,
                        Component.translatable("advancement.chromaticarsenal." + itemLike + ".title"),
                        Component.translatable("advancement.chromaticarsenal." + itemLike + ".description"),
                        null,
                        frameType,
                        true,
                        true,
                        false
                );
    }

    private Advancement simpleHasItemRecipe(ItemLike itemLike, Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        return simpleHasItemBase(itemLike).save(consumer, new ResourceLocation(ChromaticArsenal.MODID, itemLike.toString()), fileHelper);
    }

    private Advancement displayedHasItem(ItemLike itemLike, Consumer<Advancement> consumer, ExistingFileHelper fileHelper, FrameType frameType, ResourceLocation parent) {
        return displayedHasItemBase(itemLike, frameType)
                .parent(parent)
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, itemLike.toString()), fileHelper);
    }

    @SuppressWarnings("SameParameterValue")
    private Advancement displayedHasItem(ItemLike itemLike, Consumer<Advancement> consumer, ExistingFileHelper fileHelper, FrameType frameType, Advancement parent) {
        return displayedHasItemBase(itemLike, frameType)
                .parent(parent)
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, itemLike.toString()), fileHelper);
    }

    @Override
    @SuppressWarnings("unused") // most of these are considered unused by intellij
    protected void registerAdvancements(@NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper fileHelper) {
        Advancement chromaShard = displayedHasItem(ModItems.CHROMA_SHARD.get(), consumer, fileHelper, FrameType.TASK, new ResourceLocation("adventure/root"));
        Advancement ascensionEssence = displayedHasItem(ModItems.ASCENSION_ESSENCE.get(), consumer, fileHelper, FrameType.TASK, chromaShard);
        Advancement spicyCoal = simpleHasItemRecipe(ModItems.SPICY_COAL.get(), consumer, fileHelper);
        Advancement magicGarlicBread = simpleHasItemRecipe(ModItems.MAGIC_GARLIC_BREAD.get(), consumer, fileHelper);
        Advancement cosmicola = simpleHasItemRecipe(ModItems.COSMICOLA.get(), consumer, fileHelper);
        Advancement championCatalyst = simpleHasItemRecipe(ModItems.CHAMPION_CATALYST.get(), consumer, fileHelper);
        Advancement chromaSalvager = displayedHasItem(ModItems.CHROMA_SALVAGER.get(), consumer, fileHelper, FrameType.GOAL, chromaShard);
        Advancement chromanyanPlush = simpleHasItemRecipe(ModItems.CHROMANYAN.get(), consumer, fileHelper);

        Advancement goldenHeart = displayedHasItem(ModItems.GOLDEN_HEART.get(), consumer, fileHelper, FrameType.TASK, new ResourceLocation("nether/loot_bastion"));
        Advancement glassShield = simpleHasItemRecipe(ModItems.GLASS_SHIELD.get(), consumer, fileHelper);
        Advancement wardCrystal = simpleHasItemRecipe(ModItems.WARD_CRYSTAL.get(), consumer, fileHelper);
        Advancement shadowTreads = simpleHasItemRecipe(ModItems.SHADOW_TREADS.get(), consumer, fileHelper);
        Advancement dualityRings = simpleHasItemRecipe(ModItems.DUALITY_RINGS.get(), consumer, fileHelper);
        Advancement friendlyFireFlower = simpleHasItemRecipe(ModItems.FRIENDLY_FIRE_FLOWER.get(), consumer, fileHelper);
        Advancement lunarCrystal = displayedHasItem(ModItems.LUNAR_CRYSTAL.get(), consumer, fileHelper, FrameType.GOAL, new ResourceLocation("end/find_end_city"));
        Advancement cryoRing = displayedHasItem(ModItems.CRYO_RING.get(), consumer, fileHelper, FrameType.TASK, new ResourceLocation("adventure/walk_on_powder_snow_with_leather_boots"));
        Advancement bubbleAmulet = displayedHasItem(ModItems.BUBBLE_AMULET.get(), consumer, fileHelper, FrameType.TASK, new ResourceLocation("adventure/kill_a_mob"));
        Advancement momentumStone = displayedHasItem(ModItems.MOMENTUM_STONE.get(), consumer, fileHelper, FrameType.TASK, new ResourceLocation("adventure/trade"));

        Advancement ascendedStar = displayedHasItem(ModItems.ASCENDED_STAR.get(), consumer, fileHelper, FrameType.GOAL, ascensionEssence);
        Advancement worldAnchor = simpleHasItemRecipe(ModItems.WORLD_ANCHOR.get(), consumer, fileHelper);
        Advancement cursedTotem = simpleHasItemRecipe(ModItems.CURSED_TOTEM.get(), consumer, fileHelper);

        Advancement gravityStone = simpleHasItemRecipe(ModItems.GRAVITY_STONE.get(), consumer, fileHelper);
        Advancement verticalStasis = simpleHasItemRecipe(ModItems.VERTICAL_STASIS.get(), consumer, fileHelper);
        Advancement harpyFeather = simpleHasItemRecipe(ModItems.HARPY_FEATHER.get(), consumer, fileHelper);
        Advancement blahaj = displayedHasItem(ModItems.BLAHAJ.get(), consumer, fileHelper, FrameType.TASK, new ResourceLocation("adventure/trade"));

        Advancement superGoldenHeart = simpleHasItemRecipe(ModItems.SUPER_GOLDEN_HEART.get(), consumer, fileHelper);
        Advancement superGlassShield = simpleHasItemRecipe(ModItems.SUPER_GLASS_SHIELD.get(), consumer, fileHelper);
        Advancement superWardCrystal = simpleHasItemRecipe(ModItems.SUPER_WARD_CRYSTAL.get(), consumer, fileHelper);
        Advancement superShadowTreads = simpleHasItemRecipe(ModItems.SUPER_SHADOW_TREADS.get(), consumer, fileHelper);
        Advancement superFriendlyFireFlower = simpleHasItemRecipe(ModItems.SUPER_FRIENDLY_FIRE_FLOWER.get(), consumer, fileHelper);
        Advancement superLunarCrystal = simpleHasItemRecipe(ModItems.SUPER_LUNAR_CRYSTAL.get(), consumer, fileHelper);
        Advancement superHarpyFeather = simpleHasItemRecipe(ModItems.SUPER_HARPY_FEATHER.get(), consumer, fileHelper);

        Advancement superGlowRing = simpleHasItemRecipe(ModItems.SUPER_GLOW_RING.get(), consumer, fileHelper);

        Advancement curioCollectathon = addCurios(Advancement.Builder.advancement())
                .display(
                        ModItems.ASCENDED_STAR.get(),
                        Component.translatable("advancement.chromaticarsenal.arsenal_accumulated.title"),
                        Component.translatable("advancement.chromaticarsenal.arsenal_accumulated.description"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .rewards(AdvancementRewards.Builder.experience(100))
                .parent(ascendedStar)
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "arsenal_accumulated"), fileHelper);

        Advancement blockOneHundred = Advancement.Builder.advancement()
                .addCriterion("blocked_100", new GlassShieldBlockTrigger.TriggerInstance(EntityPredicate.Composite.ANY, 100))
                .display(
                        ModItems.GLASS_SHIELD.get(),
                        Component.translatable("advancement.chromaticarsenal.block_100.title"),
                        Component.translatable("advancement.chromaticarsenal.block_100.description"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .rewards(AdvancementRewards.Builder.experience(100))
                .parent(chromaShard)
                .save(consumer, new ResourceLocation(ChromaticArsenal.MODID, "block_100"), fileHelper);
    }

    private Advancement.Builder addCurios(Advancement.Builder advancement) {
        for (Item item : CURIOS) {
            advancement.addCriterion("has_" + item, hasItem(item));
        }
        return advancement;
    }

    private CriterionTriggerInstance hasItem(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build());
    }
}
