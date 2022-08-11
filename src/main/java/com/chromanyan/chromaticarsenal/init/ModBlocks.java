package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.Reference;
import com.chromanyan.chromaticarsenal.blocks.ChromaBlock;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);
	
	public static final RegistryObject<Block> CHROMA_BLOCK = modBlock("chroma_block", new ChromaBlock());
	
	private static RegistryObject<Block> modBlock(String name, Block block) {
		return BLOCKS_REGISTRY.register(name, () -> block);
	}
}
