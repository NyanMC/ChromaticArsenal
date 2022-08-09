package com.chromanyan.chromaticarsenal;

import com.chromanyan.chromaticarsenal.init.ModItems;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CAGroup extends ItemGroup {

	public CAGroup(String label) {
		super(label);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ItemStack makeIcon() {
		// TODO Auto-generated method stub
		return new ItemStack(ModItems.CHROMA_SHARD.get());
	}

}
