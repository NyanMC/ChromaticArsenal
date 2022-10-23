package com.chromanyan.chromaticarsenal;

import com.chromanyan.chromaticarsenal.init.ModItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CAGroup extends CreativeModeTab {

	public CAGroup(String label) {
		super(label);
	}
	
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.CHROMA_SHARD.get());
	}

}
