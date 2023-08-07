package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused") // just because i don't use some of these doesn't mean i shouldn't include them
public class ModTags {

    public static class Items {
        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

        public static final TagKey<Item> GEMS_CHROMA = forgeTag("gems/chroma");
        public static final TagKey<Item> STORAGE_BLOCKS_CHROMA = forgeTag("storage_blocks/chroma");

        public static final TagKey<Item> DUSTS_ASCENSION = forgeTag("dusts/ascension");

        public static final TagKey<Item> NO_SALVAGE = ItemTags.create(new ResourceLocation(Reference.MODID, "no_salvage"));
    }

    public static class Blocks {
        @SuppressWarnings("all")
        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

        public static final TagKey<Block> STORAGE_BLOCKS_CHROMA = forgeTag("storage_blocks/chroma");
    }

}
