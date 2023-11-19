package com.chromanyan.chromaticarsenal.datagen.tags;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CAItemTags extends ItemTagsProvider {

    public CAItemTags(DataGenerator p_126530_, BlockTagsProvider p_126531_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126530_, p_126531_, ChromaticArsenal.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        copy(ModTags.Blocks.STORAGE_BLOCKS_CHROMA, ModTags.Items.STORAGE_BLOCKS_CHROMA);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

        tag(ModTags.Items.GEMS_CHROMA).add(ModItems.CHROMA_SHARD.get());
        tag(Tags.Items.GEMS).add(ModItems.CHROMA_SHARD.get());
        tag(ItemTags.BEACON_PAYMENT_ITEMS).add(ModItems.CHROMA_SHARD.get());

        tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(ModItems.CRYO_RING.get());

        tag(ModTags.Items.DUSTS_ASCENSION).add(ModItems.ASCENSION_ESSENCE.get());
        tag(Tags.Items.DUSTS).add(ModItems.ASCENSION_ESSENCE.get());

        tag(ModTags.Items.STANDARD_CURIOS).add(
                ModItems.GOLDEN_HEART.get(),
                ModItems.GLASS_SHIELD.get(),
                ModItems.WARD_CRYSTAL.get(),
                ModItems.SHADOW_TREADS.get(),
                ModItems.DUALITY_RINGS.get(),
                ModItems.FRIENDLY_FIRE_FLOWER.get(),
                ModItems.LUNAR_CRYSTAL.get(),
                ModItems.CRYO_RING.get(),
                ModItems.BUBBLE_AMULET.get()
        );

        tag(ModTags.Items.SUPER_CURIOS).add(
                ModItems.SUPER_GOLDEN_HEART.get(),
                ModItems.SUPER_GLASS_SHIELD.get(),
                ModItems.SUPER_WARD_CRYSTAL.get(),
                ModItems.SUPER_SHADOW_TREADS.get(),
                ModItems.SUPER_LUNAR_CRYSTAL.get(),
                ModItems.SUPER_HARPY_FEATHER.get(),
                ModItems.SUPER_GLOW_RING.get()
        );

        tag(ModTags.Items.CHALLENGE_CURIOS).add(
                ModItems.ASCENDED_STAR.get(),
                ModItems.WORLD_ANCHOR.get(),
                ModItems.CURSED_TOTEM.get()
        );

        tag(ModTags.Items.UTILITY_CURIOS).add(
                ModItems.GRAVITY_STONE.get(),
                ModItems.VERTICAL_STASIS.get(),
                ModItems.HARPY_FEATHER.get(),
                ModItems.SUPER_HARPY_FEATHER.get()
        );

        tag(ModTags.Items.CHROMATIC_CURIOS)
                .addTag(ModTags.Items.STANDARD_CURIOS)
                .addTag(ModTags.Items.SUPER_CURIOS)
                .addTag(ModTags.Items.CHALLENGE_CURIOS)
                .addTag(ModTags.Items.UTILITY_CURIOS);

        tag(ModTags.Items.NO_SALVAGE).add(ModItems.ASCENDED_STAR.get());
    }
}
