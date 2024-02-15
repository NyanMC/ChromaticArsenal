package com.chromanyan.chromaticarsenal.util;

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

    private CompletionHelper() {

    }

    /**
     * Gets the number of advancements which the provided ServerPlayer has completed, and the total number of
     * advancements which can be earned. If the ServerPlayer's server is null, this method will also return null.
     * Ignores advancements containing the string "recipes" in them.
     * @param serverPlayer A ServerPlayer to check advancements for.
     * @return A Tuple in which A is the number of completed advancements and B is the number of total advancements.
     */
    public static @Nullable Tuple<Integer, Integer> getCompletedAndTotalAdvancements(@NotNull ServerPlayer serverPlayer) {
        MinecraftServer server = serverPlayer.level.getServer();
        if (server == null) return null;

        PlayerAdvancements playerAdvancements = server.getPlayerList().getPlayerAdvancements(serverPlayer);
        int completedAdvancements = 0;
        int totalAdvancements = 0;

        for (Map.Entry<Advancement, AdvancementProgress> advancements : playerAdvancements.advancements.entrySet()) {
            Advancement advancement = advancements.getKey();
            if (advancement.getId().toString().contains("recipes")) continue; //TODO make this configurable too

            totalAdvancements++;
            if (playerAdvancements.getOrStartProgress(advancement).isDone()) {
                completedAdvancements++;
            }
        }

        return new Tuple<>(completedAdvancements, totalAdvancements);
    }

}
