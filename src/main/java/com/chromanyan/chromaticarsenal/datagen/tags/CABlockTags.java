package com.chromanyan.chromaticarsenal.datagen.tags;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModBlocks;
import com.chromanyan.chromaticarsenal.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CABlockTags extends BlockTagsProvider {

    public CABlockTags(DataGenerator p_126511_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126511_, ChromaticArsenal.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CHROMA_BLOCK.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.CHROMA_BLOCK.get());
        tag(ModTags.Blocks.STORAGE_BLOCKS_CHROMA).add(ModBlocks.CHROMA_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.CHROMA_BLOCK.get());
    }
}
