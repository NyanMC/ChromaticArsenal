package com.chromanyan.chromaticarsenal.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ModConfig {

    public static class Common {

        public final DoubleValue maxHealthBoost;
        public final IntValue maxHealthBoostOperation;
        public final IntValue absorptionLevel;
        public final IntValue absorptionDuration;
        public final DoubleValue enchantmentMaxHealthIncrease;
        public final IntValue enchantmentAbsorptionReduction;
        public final IntValue twistedWitherDuration;

        public final IntValue cooldownDuration;
        public final IntValue enchantmentCooldownReduction;
        public final DoubleValue enchantmentFreeBlockChance;
        public final DoubleValue twistedShatterDamageMultiplier;

        public final DoubleValue antiMagicMultiplierIncoming;
        public final DoubleValue antiMagicMultiplierOutgoing;
        public final IntValue twistedWeaknessDuration;
        public final DoubleValue antiMagicProtectionModifier;
        public final ConfigValue<List<? extends String>> damageSourceBlacklist;

        public final IntValue darkspeedPotency;
        public final DoubleValue enchantmentSpeedMultiplier;
        public final IntValue maxLightLevel;
        public final DoubleValue swiftSneakDetectionReduction;
        public final DoubleValue twistedShadowDodgeChance;

        public final DoubleValue aroOfClubsMultiplier;
        public final IntValue strengthLevel;
        public final IntValue strengthDuration;
        public final IntValue healthBoostLevel;
        public final IntValue healthBoostDuration;
        public final IntValue twistedSaturationDuration;
        public final IntValue twistedHungerLevel;

        public final IntValue fireResistanceDuration;
        public final IntValue fireResistanceProtectionDuration;
        public final BooleanValue canBeDamaged;
        public final IntValue twistedFireDamageTicks;
        public final DoubleValue twistedFireDamageValue;
        public final DoubleValue twistedUnbreakingChance;

        public final IntValue levitationChance;
        public final IntValue levitationDuration;
        public final IntValue levitationDurationEnchantmentModifier;
        public final IntValue twistedLevitationDurationModifier;
        public final IntValue levitationPotency;
        public final DoubleValue gravityModifier;
        public final BooleanValue everyoneIsLuna;
        public final DoubleValue fallDamageReduction;
        public final IntValue lunarCrystalDropChance;
        public final IntValue lunarCrystalDropLootingModifier;

        public final IntValue chilledTicks;
        public final IntValue chilledTicksVulnerable;
        public final DoubleValue cryoDamage;
        public final BooleanValue cryoHealsGolems;
        public final DoubleValue twistedCryoSpeedPenalty;
        public final DoubleValue twistedCryoDamagePenalty;
        public final DoubleValue twistedCryoFireDamageMultiplier;

        public final DoubleValue amuletSwimSpeed;
        public final DoubleValue depthStriderAdditionalSpeed;
        public final IntValue bubblePanicDuration;
        public final IntValue baseBubbleCooldown;
        public final IntValue respirationCooldownReduction;
        public final DoubleValue twistedBubbleSlowness;
        public final IntValue amuletDropChance;
        public final IntValue amuletDropLootingModifier;

        public final DoubleValue momentumStoneFriction;
        public final DoubleValue momentumStoneProtectionToughness;
        public final DoubleValue twistedMomentumStoneExhaustion;

        public final IntValue advancingHealthModifier;
        public final ConfigValue<List<? extends String>> advancementBlacklist;

        public final DoubleValue anchorGravityMultiplier;
        public final DoubleValue anchorSpeedMultiplier;
        public final DoubleValue anchorKnockbackResistanceMultiplier;
        public final DoubleValue anchorArmor;
        public final BooleanValue anchorSoulbound;
        public final DoubleValue twistedAnchorGravityMultiplier;

        public final IntValue cursedTotemFracturedLevel;
        public final IntValue cursedTotemBonusLooting;

        public final DoubleValue jumpForce;
        public final DoubleValue featherFallDamageReduction;
        public final DoubleValue twistedFeatherGravityModifier;

        public final IntValue fracturedDuration;
        public final IntValue fracturedPotency;
        public final IntValue revivalCooldown;

        public final IntValue revivalLimit;
        public final IntValue shatterRevivalCooldown;
        public final DoubleValue healthTradeoff;

        public final DoubleValue potionDurationMultiplier;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> effectBlacklist;

        public final DoubleValue damageModifierMax;
        public final DoubleValue speedModifierMax;

        public final IntValue infernoDuration;
        public final DoubleValue viciousFireDamageMultplier;

        public final DoubleValue superGravityModifier;
        public final DoubleValue voidBounceMultiplier;
        public final DoubleValue voidBounceDamage;

        public final DoubleValue superJumpForce;
        public final DoubleValue jumpBonusForce;

        public final IntValue glowingDuration;
        public final DoubleValue illuminatedUndeadMultiplier;

        public final IntValue bonusSlots;
        public final DoubleValue damageMultiplier;
        public final IntValue bonusLooting;
        public final IntValue bonusFortune;

        public final DoubleValue amethystRingReachModifier;
        public final DoubleValue copperRingUnbreakingChance;

        public final BooleanValue canDamageSalvager;
        public final BooleanValue returnInferiorVariant;

        public final BooleanValue potionImmunitySideCheck;

        public final BooleanValue lootTableInsertion;
        public final BooleanValue superCuriosOnlyInRespectiveSlot;

        Common(Builder builder) {
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
                    twistedWitherDuration = builder
                            .comment("The duration of the Wither effect, in ticks, when the Golden Heart's Chromatic Twisting effect procs.")
                            .defineInRange("twistedWitherDuration", 200, 0, Integer.MAX_VALUE);
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
                    twistedShatterDamageMultiplier = builder
                            .comment("The multiplier for damage taken when a Glass Shield enchanted with Chromatic Twisting procs.")
                            .defineInRange("twistedShatterDamageMultiplier", 1.5D, 1, Double.MAX_VALUE);
                builder.pop();

                builder.push("WardCrystalSettings");
                    antiMagicMultiplierIncoming = builder
                            .comment("The multiplier for incoming magic damage when wearing a Ward Crystal. 0.0 completely nullifies the attack, and 1.0 does not change the attack strength at all.")
                            .defineInRange("antiMagicMultiplierIncoming", 0.25, 0.0, Double.MAX_VALUE);
                    antiMagicMultiplierOutgoing = builder
                            .comment("The multiplier for outgoing magic damage when wearing a Ward Crystal. 0.0 completely nullifies the attack, and 1.0 does not change the attack strength at all.")
                            .defineInRange("antiMagicMultiplierOutgoing", 0.25, 0.0, Double.MAX_VALUE);
                    twistedWeaknessDuration = builder
                            .comment("The duration of the Weakness effect, in ticks, when the Ward Crystal's Chromatic Twisting effect procs.")
                            .defineInRange("twistedWeaknessDuration", 200, 0, Integer.MAX_VALUE);
                    antiMagicProtectionModifier = builder
                            .comment("The additional percentage reduction per level of Protection. Can be negative to increase damage instead, if you want that for whatever reason. If using a mod which changes how many levels of Protection exist, this number should be decreased.")
                            .defineInRange("antiMagicProtectionModifier", 0.05, -1, 1);
                    damageSourceBlacklist = builder
                            .comment("A list of damage sources the Ward Crystal should NOT protect against, if they normally would be. To add new blacklisted sources, provide their internal name used for death message lang keys, also known as their msgId. Note that these damage sources still won't trigger the weakness effect from Chromatic Twisting.")
                            .defineList("damageSourceBlacklist", new ArrayList<>(List.of("sonic_boom")), o -> o instanceof String);
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
                    swiftSneakDetectionReduction = builder
                            .comment("The reduction in mob detection range per level of Swift Sneak. Stacks multiplicatively with other factors such as invisibility. Setting this to 0 effectively disables the mixin injection, if that is causing issues.")
                            .defineInRange("swiftSneakDetectionReduction", 0.05, 0D, 1D);
                    twistedShadowDodgeChance = builder
                            .comment("The chance to dodge an attack while in darkness with Shadow Treads enchanted with Chromatic Twisting. Beware that a value of 1 will make every hit guaranteed to be dodged.")
                            .defineInRange("twistedShadowDodgeChance", 0.2, 0, 1);
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
                    twistedSaturationDuration = builder
                            .comment("The duration of the saturation effect with the Chromatic Twisting enchantment present, in ticks.")
                            .defineInRange("twistedSaturationDuration", 300, 1, Integer.MAX_VALUE);
                    twistedHungerLevel = builder
                            .comment("The level of the hunger effect with the Chromatic Twisting enchantment when the player doesn't have the saturation effect.")
                            .defineInRange("twistedHungerLevel", 1, 0, 255);
                builder.pop();

                builder.push("FriendlyFireFlowerSettings");
                    fireResistanceDuration = builder
                            .comment("The base duration of Fire Resistance granted after taking fire damage, in ticks.")
                            .defineInRange("fireResistanceDuration", 600, 1, Integer.MAX_VALUE);
                    fireResistanceProtectionDuration = builder
                            .comment("The additional duration of Fire Resistance per level of Fire Protection on the item.")
                            .defineInRange("fireResistanceProtectionDuration", 100, 1, Integer.MAX_VALUE);
                    canBeDamaged = builder
                            .comment("If set to false, prevents the Friendly Fire Flower from being damaged when granting Fire Resistance to the wearer. Note that this will not remove durability values from flowers, and existing damaged flowers will still show as damaged.")
                            .define("canBeDamaged", true);
                    twistedFireDamageTicks = builder
                            .comment("The amount of ticks between each attempt to inflict fire damage to the player with Chromatic Twisting. Respects immunity frames.")
                            .defineInRange("twistedFireDamageTicks", 40, 1, Integer.MAX_VALUE);
                    twistedFireDamageValue = builder
                            .comment("The amount of fire damage inflicted with Chromatic Twisting. This counts as fire damage, and will be scaled by anything which scales fire damage.")
                            .defineInRange("twistedFireDamageValue", 2, 0, Double.MAX_VALUE);
                    twistedUnbreakingChance = builder
                            .comment("The chance for the Friendly Fire Flower to not take durability damage with Chromatic Twisting. Stacks with Unbreaking.")
                            .defineInRange("twistedUnbreakingChance", 0.5, 0, 1);
                builder.pop();

                builder.push("LunarCrystalSettings");
                    levitationChance = builder
                            .comment("A 1 in X chance for an attacked entity to levitate upon being attacked while a Lunar Crystal is equipped.")
                            .defineInRange("levitationChance", 10, 2, Integer.MAX_VALUE);
                    levitationDuration = builder
                            .comment("The duration of the Levitation effect, in ticks.")
                            .defineInRange("levitationDuration", 60, 1, Integer.MAX_VALUE);
                    levitationDurationEnchantmentModifier = builder
                            .comment("The duration added to the Levitation effect, in ticks, per level of Power on the curio.")
                            .defineInRange("levitationDurationEnchantmentModifier", 5, 1, Integer.MAX_VALUE);
                    twistedLevitationDurationModifier = builder
                            .comment("The duration added to the Levitation effect, in ticks, when the crystal has Chromatic Twisting.")
                            .defineInRange("twistedLevitationDurationModifier", 40, 1, Integer.MAX_VALUE);
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
                    lunarCrystalDropChance = builder
                            .comment("There is a 1 in (X - (Looting Level * Y)) chance for a Lunar Crystal to drop when killing an Enderman in the End. This is X.")
                            .defineInRange("lunarCrystalDropChance", 200, 1, Integer.MAX_VALUE);
                    lunarCrystalDropLootingModifier = builder
                            .comment("There is a 1 in (X - (Looting Level * Y)) chance for a Lunar Crystal to drop when killing an Enderman in the End. This is Y.")
                            .defineInRange("lunarCrystalDropLootingModifier", 5, 1, Integer.MAX_VALUE);
                builder.pop();

                builder.push("CryoRingSettings");
                    chilledTicks = builder
                            .comment("The amount of ticks Chilled should be applied for when hitting an entity with a snowball.")
                            .defineInRange("chilledTicks", 100, 0, Integer.MAX_VALUE);
                    chilledTicksVulnerable = builder
                            .comment("Same as above, but for enemies which are extra vulnerable to freezing damage.")
                            .defineInRange("chilledTicksVulnerable", 200, 0, Integer.MAX_VALUE);
                    cryoDamage = builder
                            .comment("The amount of damage added when a snowball hits a mob with this curio equipped.")
                            .defineInRange("cryoDamage", 3D, 0D, Float.MAX_VALUE);
                    cryoHealsGolems = builder
                            .comment("Should Snow Golems be healed for the damage amount upon being hit with a snowball?")
                            .define("cryoHealsGolems", true);
                    twistedCryoSpeedPenalty = builder
                            .comment("The speed penalty when in hot locations with a Chromatic Twisting cryo ring.")
                            .defineInRange("twistedCryoSpeedPenalty", -0.2, -1, 0);
                    twistedCryoDamagePenalty = builder
                            .comment("The damage penalty when in hot locations with a Chromatic Twisting cryo ring.")
                            .defineInRange("twistedCryoDamagePenalty", -0.25, -1, 0);
                    twistedCryoFireDamageMultiplier = builder
                            .comment("The multiplier for fire damage taken with a Chromatic Twisting cryo ring.")
                            .defineInRange("twistedCryoFireDamageMultiplier", 2, 1, Float.MAX_VALUE);
                builder.pop();

                builder.push("BubbleAmuletSettings");
                    amuletSwimSpeed = builder
                            .comment("The operation 1 attribute modifier for swim speed on the Bubble Amulet.")
                            .defineInRange("amuletSwimSpeed", 0.3D, 0, Double.MAX_VALUE);
                    depthStriderAdditionalSpeed = builder
                            .comment("The additional swim speed added per level of Depth Strider. Still operation 1.")
                            .defineInRange("depthStriderAdditionalSpeed", 0.1D, 0, Double.MAX_VALUE);
                    bubblePanicDuration = builder
                            .comment("The duration of the Bubble Panic! effect when the wearer runs out of breath.")
                            .defineInRange("bubblePanicDuration", 1200, 0, Integer.MAX_VALUE);
                    baseBubbleCooldown = builder
                            .comment("The base duration of the cooldown after the Bubble Amulet takes effect.")
                            .defineInRange("baseBubbleCooldown", 2400, 0, Integer.MAX_VALUE);
                    respirationCooldownReduction = builder
                            .comment("The amount of ticks removed from the cooldown per level of Respiration.")
                            .defineInRange("respirationCooldownReduction", 300, 0, Integer.MAX_VALUE);
                    twistedBubbleSlowness = builder
                            .comment("The operation 2 attribute modifier for movement speed when the player is outside of water or rain with a chromatically twisted Bubble Amulet.")
                            .defineInRange("twistedBubbleSlowness", -0.5, -1, 0);
                    amuletDropChance = builder
                            .comment("There is a 1 in (X - (Looting Level * Y)) chance for a Bubble Amulet to drop when killing a Drowned. This is X.")
                            .defineInRange("amuletDropChance", 40, 1, Integer.MAX_VALUE);
                    amuletDropLootingModifier = builder
                            .comment("There is a 1 in (X - (Looting Level * Y)) chance for a Bubble Amulet to drop when killing a Drowned. This is Y.")
                            .defineInRange("amuletDropLootingModifier", 2, 0, Integer.MAX_VALUE);
                builder.pop();

                builder.push("MomentumStoneSettings");
                    momentumStoneFriction = builder
                            .comment("The amount of friction added to the player while not sprinting and wearing a Momentum Stone. Stacks with block friction, caps at 0.989.")
                            .defineInRange("momentumStoneFriction", 0.3, 0, 0.989);
                    momentumStoneProtectionToughness = builder
                            .comment("The amount of armor toughness added per level of Protection.")
                            .defineInRange("momentumStoneProtectionToughness", 0.5, 1, Double.MAX_VALUE);
                    twistedMomentumStoneExhaustion = builder
                            .comment("The multiplier given to exhaustion when sprinting with a chromatically twisted Momentum Stone.")
                            .defineInRange("twistedMomentumStoneExhaustion", 1.5, 1, Double.MAX_VALUE);
                builder.pop();

                builder.push("AdvancingHeartSettings");
                    advancingHealthModifier = builder
                            .comment("The amount of bonus health obtainable with every advancement completed. The health bonus is floored to the nearest even number.")
                            .defineInRange("advancingHealthModifier", 20, 2, Integer.MAX_VALUE);
                    advancementBlacklist = builder
                            .comment("The Advancing Heart will ignore advancements which contain any of these keywords in their name. By default, ignores all recipe advancements.")
                            .defineList("advancementBlacklist", new ArrayList<>(List.of("recipes")), o -> o instanceof String);
                builder.pop();

                builder.push("WorldAnchorSettings");
                    anchorGravityMultiplier = builder
                            .comment("The multiplier for the gravity modifier. (e.g. at 0.5, gravity will be increased by +50% at world height)")
                            .defineInRange("anchorGravityMultiplier", 1D, 0D, 1D);
                    anchorSpeedMultiplier = builder
                            .comment("The multiplier for the speed modifier.")
                            .defineInRange("anchorSpeedMultiplier", -0.5, -1D, 0D);
                    anchorKnockbackResistanceMultiplier = builder
                            .comment("The multiplier for the knockback resistance modifier.")
                            .defineInRange("anchorKnockbackResistanceMultiplier", 1D, 0D, 1D);
                    anchorArmor = builder
                            .comment("The bonus armor points given for having the World Anchor equipped.")
                            .defineInRange("anchorArmor", 4D, 0D, 2048D);
                    anchorSoulbound = builder
                            .comment("Should the World Anchor persist in the player's inventory after death? This setting exists to help provide consistency with other challenge-like items such as the Ring of the Seven Curses from Enigmatic Legacy or the marks from Band of Gigantism.")
                            .define("anchorSoulbound", false);
                    twistedAnchorGravityMultiplier = builder
                            .comment("The additional flat multiplier for the World Anchor's stat modifiers if the anchor is cursed with Chromatic Twisting.")
                            .defineInRange("twistedAnchorGravityMultiplier", 2, 1, Double.MAX_VALUE);
                builder.pop();

                builder.push("CursedTotemSettings");
                    cursedTotemFracturedLevel = builder
                            .comment("The level of Fractured applied to enemies that revive from Cursed Revival. Every level is an additional 10% max health lost.")
                            .defineInRange("cursedTotemFracturedLevel", 4, 0, 8);
                    cursedTotemBonusLooting = builder
                            .comment("The amount of looting levels added when killing an enemy which has revived from Cursed Revival.")
                                    .defineInRange("cursedTotemBonusLooting", 2, 0, Integer.MAX_VALUE);
                builder.pop();

                builder.push("HarpyFeatherSettings");
                    jumpForce = builder
                            .comment("The force of the Harpy Feather jump. This goes by internal values, so tweaking may be required.")
                            .defineInRange("jumpForce", 0.42D, 0, Double.MAX_VALUE);
                    featherFallDamageReduction = builder
                            .comment("The multiplier to fall damage taken when a Harpy Feather is equipped.")
                            .defineInRange("fallDamageMultiplier", 0.8, 0, 1);
                    twistedFeatherGravityModifier = builder
                            .comment("The operation 2 modifier to gravity when a Twisted Harpy Feather is equipped.")
                            .defineInRange("twistedFeatherGravityModifier", 0.25, 0, Double.MAX_VALUE);
                builder.pop();
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

                builder.push("SuperWardCrystalSettings");
                    potionDurationMultiplier = builder
                            .comment("The duration of all applied potion effects will be multiplied by this value.")
                            .defineInRange("potionDurationMultiplier", 0.2D, 0D, 2D);
                    effectBlacklist = builder
                            .comment("A list of effects which will NOT have their duration changed. Defined using the effect resource location.")
                            .defineList("effectBlacklist", new ArrayList<>(List.of("minecraft:night_vision")), o -> o instanceof String);
                builder.pop();

                builder.push("SuperShadowTreadsSettings");
                    damageModifierMax = builder
                            .comment("The damage boost applied at noon (time 6000). This is always operation 2.")
                            .defineInRange("damageModifierMax", 0.2D, 0D, Double.MAX_VALUE);
                    speedModifierMax = builder
                            .comment("The speed boost applied at midnight (time 18000). This is always operation 2.")
                            .defineInRange("speedModifierMax", 0.2D, 0D, Double.MAX_VALUE);
                builder.pop();

                builder.push("SuperFriendlyFireFlowerSettings");
                    infernoDuration = builder
                            .comment("The duration of the Inferno potion effect applied when dealing damage, in ticks.")
                            .defineInRange("infernoDuration", 100, 0, Integer.MAX_VALUE);
                    viciousFireDamageMultplier = builder
                            .comment("The damage multiplier against enemies which have been set on fire.")
                            .defineInRange("viciousFireDamageMultiplier", 1.1, 0, Double.MAX_VALUE);
                builder.pop();

                builder.push("SuperLunarCrystalSettings");
                    superGravityModifier = builder
                            .comment("The percentage in which the player's gravity is changed while the Super Lunar Crystal is equipped. Negative values reduce gravity, positive values increase it. -1 is not recommended, unless your planet needs you.")
                            .defineInRange("superGravityModifier", -0.5D, -1, 0);
                    voidBounceMultiplier = builder
                            .comment("The multiplier for the force applied upon falling below the world's minimum building height. The base force is dependent on the speed in which the player fell into the void.")
                            .defineInRange("voidBounceMultiplier", 2D, 0D, Double.MAX_VALUE);
                    voidBounceDamage = builder
                            .comment("The amount of damage taken when bouncing out of the void. This is void damage, meaning it bypasses most forms of protection, even creative mode.")
                            .defineInRange("voidBounceDamage", 4D, 0D, Float.MAX_VALUE);
                builder.pop();

                builder.push("SuperHarpyFeatherSettings");
                    superJumpForce = builder
                            .comment("The force of the Polychromatic Feather jump.")
                            .defineInRange("superJumpForce", 0.42D, 0, Double.MAX_VALUE);
                    jumpBonusForce = builder
                            .comment("The additional force per tick while holding the jump button with the Polychromatic Feather equipped. Multiplied by entity gravity divided by 0.08.")
                            .defineInRange("jumpBonusForce", 0.05, 0, Double.MAX_VALUE);
                builder.pop();

                builder.push("SuperGlowRingSettings");
                    glowingDuration = builder
                            .comment("The duration of the Glowing effect applied when attacking a mob. Set to 0 to disable.")
                            .defineInRange("glowingDuration", 1200, 0, Integer.MAX_VALUE);
                    illuminatedUndeadMultiplier = builder
                            .comment("The damage multiplier when attacking undead mobs with a Ring of Illuminated Soul.")
                            .defineInRange("illuminatedUndeadMultiplier", 1.2, 1, Float.MAX_VALUE);
                builder.pop();

                builder.push("AscendedStarSettings");
                    bonusSlots = builder
                            .comment("The amount of additional slots granted upon equipping the star.")
                            .defineInRange("bonusSlots", 1, 0, 3);
                    damageMultiplier = builder
                            .comment("The multiplier to all incoming damage the player receives.")
                            .defineInRange("damageMultiplier", 3, 1, Double.MAX_VALUE);
                    bonusFortune = builder
                            .comment("The amount of additional fortune levels granted upon equipping the star.")
                            .defineInRange("bonusFortune", 1, 0, Integer.MAX_VALUE);
                    bonusLooting = builder
                            .comment("The amount of additional looting levels granted upon equipping the star.")
                            .defineInRange("bonusLooting", 1, 0, Integer.MAX_VALUE);
                builder.pop();

            builder.comment("Because basic curios are simple and typically only have one or two config values, they are all tacked on here.");
                builder.push("BasicCurioSettings");
                    amethystRingReachModifier = builder
                            .comment("The amount of additional reach granted by the Amethyst Ring. Additive.")
                            .defineInRange("amethystRingReachModifier", 1F, 0F, Double.MAX_VALUE);
                    copperRingUnbreakingChance = builder
                            .comment("The chance for an item to not use durability with the Copper Ring equipped. Stacks multiplicatively with Unbreaking.")
                            .defineInRange("copperRingUnbreakingChance", 0.1F, 0F, 1F);
                builder.pop();
            builder.pop();

            builder.push("ChromaSalvagerSettings");
                canDamageSalvager = builder
                        .comment("Should the Chroma Salvager take damage when used? Doesn't remove durability from the tool entirely, merely prevents it from taking damage.")
                        .define("canDamageSalvager", true);
                returnInferiorVariant = builder
                        .comment("When salvaging a Super Curio, should the standard variant of the curio be returned as well?")
                        .define("returnInferiorVaruant", true);
            builder.pop();

            builder.comment("WARNING: These settings could have unintended side effects. You are less likely to receive help with issues if any of these settings are on.");
            builder.push("ExperimentalSettings");
                potionImmunitySideCheck = builder
                        .comment("Whether curios that hook into MobEffectEvent.Applicable should impose a serverside check before running. May fix compatibility issues with mods such as Oh the Biomes You'll Go, but could lead to other issues in the process.")
                        .define("potionImmunitySideCheck", true);
            builder.pop();

            lootTableInsertion = builder
                    .comment("Set to false to prevent Chromatic Arsenal from injecting its own items into loot tables. This will cause items only found as loot (such as the Golden Heart) to become unobtainable, and it will be up to the modpack to add a method to obtain them. This setting also affects the Wandering Trader trade for Chroma Shards.")
                    .worldRestart()
                    .define("lootTableInsertion", true);
            superCuriosOnlyInRespectiveSlot = builder
                    .comment("Whether super curios are allowed in slots other than super curio slots. Prevents equipping super curios in general purpose curio slots.")
                    .define("superCuriosOnlyInRespectiveSlot", true);
        }

    }

    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Client {

        public final BooleanValue suppressEnchantedSuperCurioWarning;
        public final IntValue tooltipDecimalThreshold;
        public final BooleanValue anonymityOptOut;

        Client(Builder builder) {
            builder.push("TooltipSettings");
                suppressEnchantedSuperCurioWarning = builder
                        .comment("Disables the extra tooltip lines which are displayed when a super curio is enchanted with a non-curse enchantment.")
                        .define("suppressEnchantedSuperCurioWarning", false);
                tooltipDecimalThreshold = builder
                        .comment("Whenever ticks are converted to seconds in a tooltip, the decimal is kept if the resulting amount of seconds is under this value.")
                        .defineInRange("tooltipDecimalThreshold", 10, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("AnonymityUmbrellaSettings");
                anonymityOptOut = builder
                        .comment("The Anonymity Umbrella is meant to be a fun item to cause a bit of mischief in small servers, similar to the Invisibility effect. If it is being misused, you can disable it here. While disabled, you will see the true skins of anonymous players, however nametags will still be hidden.")
                        .define("anonymityOptOut", false);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

}
