package com.chromanyan.chromaticarsenal.util;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CompletionHelper {

    private static final ModConfig.Common config = ModConfig.COMMON;

    private CompletionHelper() {

    }

    /**
     * Gets the number of advancements which the provided {@link ServerPlayer} has completed, and the total number of
     * advancements which can be earned. If the {@link ServerPlayer}'s {@link MinecraftServer} is null, this method will also return null.
     * Ignores advancements containing a blacklisted keyword.
     * @param serverPlayer A {@link ServerPlayer} to check advancements for.
     * @return A {@link Tuple} in which A is the number of completed advancements and B is the number of total advancements.
     */
    public static @Nullable Tuple<Integer, Integer> getCompletedAndTotalAdvancements(@NotNull ServerPlayer serverPlayer) {
        MinecraftServer server = serverPlayer.level.getServer();
        if (server == null) return null;

        PlayerAdvancements playerAdvancements = server.getPlayerList().getPlayerAdvancements(serverPlayer);
        int completedAdvancements = 0;
        int totalAdvancements = 0;

        for (Map.Entry<Advancement, AdvancementProgress> advancements : playerAdvancements.advancements.entrySet()) {
            Advancement advancement = advancements.getKey();

            if (isAdvancementIgnored(advancement)) continue;

            totalAdvancements++;
            if (playerAdvancements.getOrStartProgress(advancement).isDone()) {
                completedAdvancements++;
            }
        }

        return new Tuple<>(completedAdvancements, totalAdvancements);
    }

    /**
     * Returns true if the given {@link Advancement} should be ignored due to containing a blacklisted keyword.
     * @param advancement The {@link Advancement} to check.
     * @return Whether the advancement should be ignored.
     */
    public static boolean isAdvancementIgnored(Advancement advancement) {
        for (String keyword : config.advancementBlacklist.get()) {
            if (advancement.getId().toString().contains(keyword)) {
                return true;
            }
        }
        return false;
    }

}
