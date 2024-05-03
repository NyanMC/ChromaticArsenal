package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChromaticArsenal.MODID);

    public static final RegistryObject<CreativeModeTab> TAB_CHROMATIC_ARSENAL = CREATIVE_TABS.register(ChromaticArsenal.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.chromaticarsenal"))
                    .icon(() -> new ItemStack(ModItems.CHROMA_SHARD.get()))
                    .displayItems(((itemDisplayParameters, output) -> ModItems.CREATIVE_TAB_ITEMS.forEach((item) -> output.accept(item.get()))))
                    .build());
}
