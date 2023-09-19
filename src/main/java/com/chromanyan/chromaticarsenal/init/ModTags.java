package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Items {
        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

        public static final TagKey<Item> GEMS_CHROMA = forgeTag("gems/chroma");
        public static final TagKey<Item> STORAGE_BLOCKS_CHROMA = forgeTag("storage_blocks/chroma");

        public static final TagKey<Item> DUSTS_ASCENSION = forgeTag("dusts/ascension");

        public static final TagKey<Item> NO_SALVAGE = ItemTags.create(new ResourceLocation(ChromaticArsenal.MODID, "no_salvage"));

        public static final TagKey<Item> CHROMATIC_CURIOS = ItemTags.create(new ResourceLocation(ChromaticArsenal.MODID, "chromatic_curios"));
        public static final TagKey<Item> SUPER_CURIOS = ItemTags.create(new ResourceLocation(ChromaticArsenal.MODID, "super_curios"));
    }

    public static class Blocks {
        @SuppressWarnings("SameParameterValue")
        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

        public static final TagKey<Block> STORAGE_BLOCKS_CHROMA = forgeTag("storage_blocks/chroma");
    }

}
