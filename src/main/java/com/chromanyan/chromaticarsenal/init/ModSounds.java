package com.chromanyan.chromaticarsenal.init;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS_REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ChromaticArsenal.MODID);

    public static final SoundEvent DIAL_UP = SoundEvent.createVariableRangeEvent(new ResourceLocation(ChromaticArsenal.MODID, "dial_up"));
    public static final SoundEvent SPRING = SoundEvent.createVariableRangeEvent(new ResourceLocation(ChromaticArsenal.MODID, "spring"));
}
