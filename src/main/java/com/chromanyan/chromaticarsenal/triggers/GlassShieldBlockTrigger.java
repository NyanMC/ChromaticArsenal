package com.chromanyan.chromaticarsenal.triggers;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GlassShieldBlockTrigger extends SimpleCriterionTrigger<GlassShieldBlockTrigger.TriggerInstance> {
    public static final ResourceLocation ID = new ResourceLocation(ChromaticArsenal.MODID, "glass_shield_block");
    public static final  GlassShieldBlockTrigger INSTANCE = new GlassShieldBlockTrigger();

    @Override
    protected @NotNull TriggerInstance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.Composite playerPred, @NotNull DeserializationContext conditions) {
        return new TriggerInstance(playerPred, GsonHelper.getAsInt(json, "required_amount"));
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, int amount) {
        this.trigger(player, triggerInstance -> triggerInstance.test(amount));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final int requiredAmount;

        public TriggerInstance(EntityPredicate.Composite playerPred, int requiredAmount) {
            super(ID, playerPred);

            this.requiredAmount = requiredAmount;
        }

        @NotNull
        @Override
        public ResourceLocation getCriterion() {
            return ID;
        }

        boolean test(int amount) {
            return amount >= this.requiredAmount;
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext pConditions) {
            JsonObject jsonobject = super.serializeToJson(pConditions);
            jsonobject.addProperty("required_amount", this.requiredAmount);
            return jsonobject;
        }
    }
}
