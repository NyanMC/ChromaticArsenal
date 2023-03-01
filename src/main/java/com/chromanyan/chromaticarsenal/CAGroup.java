package com.chromanyan.chromaticarsenal;

import com.chromanyan.chromaticarsenal.init.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CAGroup extends CreativeModeTab {

    public CAGroup(String label) {
        super(label);
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(ModItems.CHROMA_SHARD.get());
    }

}
