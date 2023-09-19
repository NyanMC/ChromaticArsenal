package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS_REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ChromaticArsenal.MODID);

    public static final RegistryObject<SoundEvent> DIAL_UP = SOUNDS_REGISTRY.register("dial_up", () -> new SoundEvent(new ResourceLocation(ChromaticArsenal.MODID, "dial_up")));
    public static final RegistryObject<SoundEvent> SPRING = SOUNDS_REGISTRY.register("spring", () -> new SoundEvent(new ResourceLocation(ChromaticArsenal.MODID, "spring")));
}
