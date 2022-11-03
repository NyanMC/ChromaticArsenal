package com.chromanyan.chromaticarsenal.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("unused")
public class CooldownHelper {

    public static int getCounter(CompoundTag nbt) {
        if (!nbt.contains("counter")) {
            nbt.putInt("counter", 0);
        }
        return nbt.getInt("counter");
    }

    public static boolean isCooldownFinished(CompoundTag nbt) {
        return getCounter(nbt) <= 0; // should never be less than 0 unless another mod touches it, but better safe than sorry
    }

    public static void tickCounter(CompoundTag nbt) {
        if (isCooldownFinished(nbt)) {
            nbt.putInt("counter", nbt.getInt("counter") - 1);
        }
    }

    public static void tickCounter(CompoundTag nbt, SoundEvent sound, LivingEntity livingEntity) {
        if (!isCooldownFinished(nbt)) {
            nbt.putInt("counter", nbt.getInt("counter") - 1);
            if (nbt.getInt("counter") == 0)
                livingEntity.getCommandSenderWorld().playSound((Player) null, livingEntity.blockPosition(), sound, SoundSource.PLAYERS, 0.5F, 1.0F);
        }
    }

    public static void updateCounter(CompoundTag nbt, int amount) {
        nbt.putInt("counter", amount);
    }

    public static void addToCounter(CompoundTag nbt, int amount) {
        nbt.putInt("counter", getCounter(nbt) + amount);
    }

}
