package com.chromanyan.chromaticarsenal.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ModConfig {
	
	public static class Common {
		
		public final DoubleValue maxHealthBoost;
		public final IntValue maxHealthBoostOperation;
		public final IntValue absorptionLevel;
		public final IntValue absorptionDuration;
		
		public final IntValue cooldownDuration;
		
		public final DoubleValue antiMagicMultiplierIncoming;
		public final DoubleValue antiMagicMultiplierOutgoing;
		
		public final IntValue darkspeedPotency;
		
		public final DoubleValue aroOfClubsMultiplier;
		public final IntValue saturationLevel;
		public final IntValue saturationDuration;
		public final IntValue healthBoostLevel;
		public final IntValue healthBoostDuration;
		
		public final IntValue levitationChance;
		public final IntValue levitationDuration;
		public final IntValue levitationPotency;
		
		Common(ForgeConfigSpec.Builder builder) {
			builder.push("GoldenHeartSettings");
			maxHealthBoost = builder
					.comment("The max health granted when the player equips a golden heart.")
					.defineInRange("maxHealthBoost", 4.0, Double.MIN_VALUE, Double.MAX_VALUE);
			
			maxHealthBoostOperation = builder
					.comment("The operation used for the health bonus. 0 for additive, 1 for multiply base, 2 for multiply total.")
					.defineInRange("maxHealthBoostOperation", 0, 0, 2);
			
			absorptionLevel = builder
					.comment("The level of the absorption effect applied to the player while wearing the trinket. 0 is equivalent to effect level 1.")
					.defineInRange("absorptionLevel", 0, 0, 255);
			
			absorptionDuration = builder
					.comment("The duration of the absorption effect, in ticks. When this hits zero, the effect is re-applied.")
					.defineInRange("absorptionDuration", 400, 1, Integer.MAX_VALUE);
			builder.pop();
			
			builder.push("GlassShieldSettings");
			cooldownDuration = builder
					.comment("The duration, in ticks, for the Glass Shield to recharge after it blocks a hit. Extremely low values effectively make you invincible.")
					.defineInRange("cooldownDuration", 400, 0, Integer.MAX_VALUE);
			builder.pop();
			
			builder.push("WardCrystalSettings");
			antiMagicMultiplierIncoming = builder
					.comment("The multiplier for incoming magic damage when wearing a Ward Crystal. 0.0 completely nullifies the attack, and 1.0 does not change the attack strength at all.")
					.defineInRange("antiMagicMultiplierIncoming", 0.25, 0.0, Double.MAX_VALUE);
			
			antiMagicMultiplierOutgoing = builder
					.comment("The multiplier for outgoing magic damage when wearing a Ward Crystal. 0.0 completely nullifies the attack, and 1.0 does not change the attack strength at all.")
					.defineInRange("antiMagicMultiplierOutgoing", 0.25, 0.0, Double.MAX_VALUE);
			builder.pop();
			
			builder.push("ShadowTreadsSettings");
			darkspeedPotency = builder
					.comment("The level of the speed effect applied to the player while in darkness. 0 is equivalent to effect level 1.")
					.defineInRange("darkspeedPotency", 0, 0, 255);
			builder.pop();
			
			builder.push("DualityRingsSettings");
			aroOfClubsMultiplier = builder
					.comment("The damage multiplier of arrow projectiles while the Duality Rings are equipped. Values above 1.0 increase the damage, while values below 1.0 decrease the damage.")
					.defineInRange("aroOfClubsMultiplier", 1.25, 0.0, Double.MAX_VALUE);
			saturationLevel = builder
					.comment("The level of the saturation effect applied to the player when consuming Magic Garlic Bread while the Duality Rings are equipped.")
					.defineInRange("saturationLevel", 0, 0, 255);
			saturationDuration = builder
					.comment("The duration of the saturation effect, in ticks.")
					.defineInRange("saturationDuration", 200, 1, Integer.MAX_VALUE);
			healthBoostLevel = builder
					.comment("The level of the health boost effect applied to the player when consuming Magic Garlic Bread while the Duality Rings are equipped.")
					.defineInRange("healthBoostLevel", 2, 0, 255);
			healthBoostDuration = builder
					.comment("The duration of the health boost effect, in ticks.")
					.defineInRange("healthBoostDuration", 2400, 1, Integer.MAX_VALUE);
			builder.pop();
			
			builder.push("LunarCrystalSettings");
			levitationChance = builder
					.comment("A 1 in X chance for an attacked entity to levitate upon being attacked while a Lunar Crystal is equipped.")
					.defineInRange("levitationChance", 10, 2, Integer.MAX_VALUE);
			levitationDuration = builder
					.comment("The duration of the Levitation effect, in ticks.")
					.defineInRange("levitationDuration", 60, 1, Integer.MAX_VALUE);
			levitationPotency = builder
					.comment("The level of the levitation effect. 0 is equivalent to effect level 1.")
					.defineInRange("levitationPotency", 2, 0, 255);
			builder.pop();
		}
		
	}
	
	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;
	
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
	
}
