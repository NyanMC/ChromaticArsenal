package com.chromanyan.chromaticarsenal.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class CooldownHelper {

    private CooldownHelper() { // i don't want people to create cooldown helpers

    }

    /**
     * Gets the value of the "counter" tag on an item.
     * If this tag does not exist, it will be created with a default value of zero.
     *
     * @param nbt the CompoundTag of the item to get the cooldown for
     * @return the value of counter
     */
    public static int getCounter(CompoundTag nbt) {
        if (!nbt.contains("counter")) {
            nbt.putInt("counter", 0);
        }
        return nbt.getInt("counter");
    }

    public static boolean isCooldownFinished(CompoundTag nbt) {
        return getCounter(nbt) <= 0; // should never be less than 0 unless another mod touches it, but better safe than sorry
    }

    /**
     * Decrements the "counter" tag by 1.
     * If this causes the counter to reach zero, this method returns true.
     * Otherwise, it will return false.
     *
     * @param nbt the CompoundTag of the item to be ticked
     * @return a boolean for if the counter was finished this tick
     */
    public static boolean tickCounter(CompoundTag nbt) {
        if (!isCooldownFinished(nbt)) {
            nbt.putInt("counter", nbt.getInt("counter") - 1);
            return getCounter(nbt) == 0;
        }
        return false;
    }

    /**
     * Identical to tickCounter(CompoundTag), but also plays a sound effect at the entity if the cooldown reached zero this tick.
     *
     * @param nbt          the CompoundTag of the item to be ticked
     * @param sound        the SoundEvent to play if the cooldown finishes
     * @param livingEntity the LivingEntity to play the sound at if the cooldown finishes
     * @return a boolean for if the counter was finished this tick
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean tickCounter(CompoundTag nbt, SoundEvent sound, LivingEntity livingEntity) {
        if (tickCounter(nbt)) {
            livingEntity.getCommandSenderWorld().playSound(null, livingEntity.blockPosition(), sound, SoundSource.PLAYERS, 0.5F, 1.0F);
            return true;
        }
        return false;
    }

    public static void updateCounter(CompoundTag nbt, int amount) {
        nbt.putInt("counter", amount);
    }

    public static void addToCounter(CompoundTag nbt, int amount) {
        nbt.putInt("counter", getCounter(nbt) + amount);
    }

}
