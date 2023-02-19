package com.chromanyan.chromaticarsenal.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
	
	public static class Common {
		
		public final DoubleValue maxHealthBoost;
		public final IntValue maxHealthBoostOperation;
		public final IntValue absorptionLevel;
		public final IntValue absorptionDuration;
		public final DoubleValue enchantmentMaxHealthIncrease;
		public final IntValue enchantmentAbsorptionReduction;
		
		public final IntValue cooldownDuration;
		public final IntValue enchantmentCooldownReduction;
		public final DoubleValue enchantmentFreeBlockChance;
		
		public final DoubleValue antiMagicMultiplierIncoming;
		public final DoubleValue antiMagicMultiplierOutgoing;
		
		public final IntValue darkspeedPotency;
		public final DoubleValue enchantmentSpeedMultiplier;
		public final IntValue maxLightLevel;
		
		public final DoubleValue aroOfClubsMultiplier;
		public final IntValue strengthLevel;
		public final IntValue strengthDuration;
		public final IntValue healthBoostLevel;
		public final IntValue healthBoostDuration;

		public final IntValue fireResistanceDuration;
		public final BooleanValue canBeDamaged;
		
		public final IntValue levitationChance;
		public final IntValue levitationDuration;
		public final IntValue levitationPotency;
		public final DoubleValue gravityModifier;
		public final BooleanValue everyoneIsLuna;
		public final DoubleValue fallDamageReduction;

		public final IntValue jumpCooldown;
		public final DoubleValue jumpForce;
		
		public final IntValue fracturedDuration;
		public final IntValue fracturedPotency;
		public final IntValue revivalCooldown;
		
		public final IntValue revivalLimit;
		public final IntValue shatterRevivalCooldown;
		public final DoubleValue healthTradeoff;

		public final DoubleValue damageModifierMax;
		public final DoubleValue speedModifierMax;
		
		public final BooleanValue lootTableInsertion;
		
		Common(ForgeConfigSpec.Builder builder) {
			builder.push("CurioSettings");
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
			
			enchantmentMaxHealthIncrease = builder
					.comment("The extra max health gained per level of Protection on the Golden Heart. This obeys the operation, so if you set maxHealthBoostOperation to either 1 or 2, it's recommended to keep this at 0.1 at most.")
					.defineInRange("enchantmentMaxHealthIncrease", 1.0, 0.0, Double.MAX_VALUE);
			enchantmentAbsorptionReduction = builder
					.comment("The amount in which the duration of the absorption effect is reduced, in ticks. This allows for faster reapplication of the effect.")
					.defineInRange("enchantmentAbsorptionReduction", 20, 0, Integer.MAX_VALUE);
			builder.pop();
			
			builder.push("GlassShieldSettings");
			cooldownDuration = builder
					.comment("The duration, in ticks, for the Glass Shield to recharge after it blocks a hit. Extremely low values effectively make you invincible. It is recommended to make this greater than enchantmentCooldownReduction, unless mending is unobtainable in your modpack.")
					.defineInRange("cooldownDuration", 400, 0, Integer.MAX_VALUE);
			enchantmentCooldownReduction = builder
					.comment("The amount of ticks to remove from the Glass Shield cooldown if Mending is applied. Note that if another mod changes Mending to have multiple levels, this will be multiplied by the level of the enchant.")
					.defineInRange("enchantmentCooldownReduction", 100, 0, Integer.MAX_VALUE);
			enchantmentFreeBlockChance = builder
					.comment("The percent chance per level of Unbreaking to prevent the Glass Shield from shattering after blocking a hit. This is rounded up to the nearest whole number in-game. If your modpack has the ability to grant you outrageous levels of unbreaking (looking at you, Apotheosis), this should be set to a low value or kept at the default.")
					.defineInRange("enchantmentFreeBlockChance", 2D, 0D, 100D);
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
			enchantmentSpeedMultiplier = builder
					.comment("The amount of passive speed granted by each level of Soul Speed.")
					.defineInRange("enchantmentSpeedMultiplier", 0.05, 0.0, Double.MAX_VALUE);
			maxLightLevel = builder
					.comment("The maximum light level in which the Shadow Charm takes effect. Values below 4 are not recommended, as sky light causes the minimum light value to always be 4 on the surface, even at midnight.")
					.defineInRange("maxLightLevel", 7, 0, 15);
			builder.pop();
			
			builder.push("DualityRingsSettings");
			aroOfClubsMultiplier = builder
					.comment("The damage multiplier of arrow projectiles while the Duality Rings are equipped. Values above 1.0 increase the damage, while values below 1.0 decrease the damage.")
					.defineInRange("aroOfClubsMultiplier", 1.25, 0.0, Double.MAX_VALUE);
			strengthLevel = builder
					.comment("The level of the strength effect applied to the player when consuming Magic Garlic Bread while the Duality Rings are equipped.")
					.defineInRange("strengthLevel", 0, 0, 255);
			strengthDuration = builder
					.comment("The duration of the strength effect, in ticks.")
					.defineInRange("strengthDuration", 2400, 1, Integer.MAX_VALUE);
			healthBoostLevel = builder
					.comment("The level of the health boost effect applied to the player when consuming Magic Garlic Bread while the Duality Rings are equipped.")
					.defineInRange("healthBoostLevel", 2, 0, 255);
			healthBoostDuration = builder
					.comment("The duration of the health boost effect, in ticks.")
					.defineInRange("healthBoostDuration", 2400, 1, Integer.MAX_VALUE);
			builder.pop();

			builder.push("FriendlyFireFlowerSettings");
			fireResistanceDuration = builder
					.comment("The duration of Fire Resistance granted after taking fire damage, in ticks. This is multiplied by the level of Fire Protection on the item, +1.")
					.defineInRange("fireResistanceDuration", 200, 1, Integer.MAX_VALUE);
			canBeDamaged = builder
					.comment("If set to false, prevents the Friendly Fire Flower from being damaged when granting Fire Resistance to the wearer. Note that this will not remove durability values from flowers, and existing damaged flowers will still show as damaged.")
					.define("canBeDamaged", true);
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
			gravityModifier = builder
					.comment("The percentage in which the player's gravity is changed while the Lunar Crystal is equipped. Negative values reduce gravity, positive values increase it. -1 is not recommended, unless your planet needs you.")
					.defineInRange("gravityModifier", -0.25, -1, Double.MAX_VALUE);
			everyoneIsLuna = builder
					.comment("Set this to true to make my UUID exclusive easter egg apply to all players of the current modpack. Recommended for people named Luna playing singleplayer on a private modpack. Not so recommended in most other cases.")
					.define("everyoneIsLuna", false);
			fallDamageReduction = builder
					.comment("The percentage of fall damage reduced per level of feather falling on this item. Set to 0 to effectively disable this feature.")
					.defineInRange("fallDamageReduction", 0.05, 0, 1);
			builder.pop();
			builder.push("HarpyFeatherSettings");

			builder.pop();
			jumpCooldown = builder
					.comment("The cooldown between Harpy Feather jumps, in ticks.")
					.defineInRange("jumpCooldown", 60, 0, Integer.MAX_VALUE);
			jumpForce = builder
					.comment("The force of the Harpy Feather jump. This goes by internal values, so tweaking may be required.")
					.defineInRange("jumpForce", 0.42D, 0, Double.MAX_VALUE);
			builder.pop();
			
			builder.push("SuperCurioSettings");
			builder.push("SuperGoldenHeartSettings");
			fracturedDuration = builder
					.comment("The duration of the Fractured effect, in ticks.")
					.defineInRange("fracturedDuration", 6000, 1, Integer.MAX_VALUE);
			fracturedPotency = builder
					.comment("The level of the Fractured effect. Each level is 10% of max health lost. 0 is equal to level 1.")
					.defineInRange("fracturedPotency", 2, 0, 8);
			revivalCooldown = builder
					.comment("The cooldown of the diamond heart before you are allowed to revive again. This is in ticks. Extremely low values effectively make you unable to die.")
					.defineInRange("revivalCooldown", 6000, 0, Integer.MAX_VALUE);
			builder.pop();
			builder.push("SuperGlassShieldSettings");
			revivalLimit = builder
					.comment("Once this amount of ticks is exceeded, the next fatal blow will kill the wearer. At the default value, this will allow for four revives, unless somehow the wearer miraculously gets killed three times in a single tick.")
					.defineInRange("revivalLimit", 3600, 0, Integer.MAX_VALUE);
			shatterRevivalCooldown = builder
					.comment("The amount of ticks added each time the Shield of Undying sustains a fatal blow. This stacks until it reaches the value defined in revivalLimit, in which the next fatal blow will kill the wearer.")
					.defineInRange("shatterRevivalCooldown", 1200, 0, Integer.MAX_VALUE);
			healthTradeoff = builder
					.comment("The operation 2 modifier given to the player wearing this shield. This should be negative or zero.")
					.defineInRange("healthTradeoff", -0.8D, -1.0D, 0.0D);
			builder.pop();
			builder.push("SuperShadowTreadsSettings");
			damageModifierMax = builder
					.comment("The damage boost applied at noon (time 6000). This is always operation 2.")
					.defineInRange("damageModifierMax", 0.2D, 0D, Double.MAX_VALUE);
			speedModifierMax = builder
					.comment("The speed boost applied at midnight (time 18000). This is always operation 2.")
					.defineInRange("speedModifierMax", 0.2D, 0D, Double.MAX_VALUE);
			builder.pop();
			builder.pop();
			
			lootTableInsertion = builder
					.comment("Set to false to prevent Chromatic Arsenal from injecting its own items into loot tables. This will cause items only found as loot (such as the Golden Heart) to become unobtainable, and it will be up to the modpack to add a method to obtain them. This setting also affects the Wandering Trader trade for Chroma Shards.")
					.define("lootTableInsertion", true);
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
