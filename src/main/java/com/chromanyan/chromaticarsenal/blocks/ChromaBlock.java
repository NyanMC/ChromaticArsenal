package com.chromanyan.chromaticarsenal.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class ChromaBlock extends RotatedPillarBlock {

	public ChromaBlock() {
		super(AbstractBlock.Properties.copy(Blocks.DIAMOND_BLOCK));
	}

	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return 15;
	}

}
