package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.blocks.ChromaBlock;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("all")
public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);
	
	public static final RegistryObject<Block> CHROMA_BLOCK = BLOCKS_REGISTRY.register("chroma_block", () -> new ChromaBlock());
}
