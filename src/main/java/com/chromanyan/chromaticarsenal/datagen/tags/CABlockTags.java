package com.chromanyan.chromaticarsenal.datagen.tags;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModBlocks;
import com.chromanyan.chromaticarsenal.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CABlockTags extends BlockTagsProvider {

    public CABlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChromaticArsenal.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CHROMA_BLOCK.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.CHROMA_BLOCK.get());
        tag(ModTags.Blocks.STORAGE_BLOCKS_CHROMA).add(ModBlocks.CHROMA_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.CHROMA_BLOCK.get());

        tag(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(ModBlocks.BLAHAJ.get(), ModBlocks.CHROMANYAN.get());
    }
}
