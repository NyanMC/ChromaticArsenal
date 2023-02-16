package com.chromanyan.chromaticarsenal.events;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import com.chromanyan.chromaticarsenal.util.CooldownHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

import static com.chromanyan.chromaticarsenal.util.ChromaCurioHelper.getCurio;

public class EventClassInstance {
	// clean up this mess
	
	private final Random rand = new Random();
	private final Common config = ModConfig.COMMON;
	
	@SubscribeEvent
	public void playerAttackedEvent(LivingHurtEvent event) {
		LivingEntity player = event.getEntityLiving();
		if (!player.getCommandSenderWorld().isClientSide()) {
			// spatial: block fall damage
			if (player.hasEffect(ModPotions.SPATIAL.get()) && event.getSource() == DamageSource.FALL) {
				event.setAmount(0); // just in case, you know?
				event.setCanceled(true);
			}
			if (event.isCanceled()) {
				return; // the rest of the effects should only fire if they're even applicable
			}
			// glass shield: block hits
			Optional<SlotResult> shield = getCurio(player, ModItems.GLASS_SHIELD.get());
			if (shield.isPresent() && event.getAmount() != 0 && !event.getSource().isBypassInvul()) {
				ItemStack stack = shield.get().stack();
				CompoundTag nbt = stack.getOrCreateTag();
				int savedTicks = 0;
				int freeBlockChance = 0;
				if (getItemEnchantmentLevel(Enchantments.MENDING, stack) > 0) {
					savedTicks = getItemEnchantmentLevel(Enchantments.MENDING, stack) * config.enchantmentCooldownReduction.get(); // i doubt this will ever be > 1, but maybe some mod uses mixins to increase it. i want to be ready for that.
				}
				if (getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) > 0) {
					freeBlockChance = (int) Math.ceil(getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) * config.enchantmentFreeBlockChance.get());
				}
				if (CooldownHelper.isCooldownFinished(nbt)) {
					int randBlock = rand.nextInt(99);
					if (randBlock < freeBlockChance) { // not <= because rand.nextInt is always one less than i want it to be
						player.getCommandSenderWorld().playSound((Player)null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
						player.getCommandSenderWorld().playSound((Player)null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
					} else {
						CooldownHelper.updateCounter(nbt, config.cooldownDuration.get() - savedTicks);
						player.getCommandSenderWorld().playSound((Player)null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
					}
					event.setAmount(0);
					event.setCanceled(true);
				}
			}
			
			// (super) dispel gel: reduce incoming damage
			Optional<SlotResult> crystal = getCurio(player, ModItems.WARD_CRYSTAL.get());
			Optional<SlotResult> superCrystal = getCurio(player, ModItems.SUPER_WARD_CRYSTAL.get());
			if (event.getSource().isMagic() && (crystal.isPresent() || superCrystal.isPresent()) && !event.getSource().isBypassInvul()) {
				event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierIncoming.get()));
			}

			Optional<SlotResult> lCrystal = getCurio(player, ModItems.LUNAR_CRYSTAL.get());
			if (event.getSource() == DamageSource.FALL && lCrystal.isPresent()) {
				int fallEnchantLevel = getItemEnchantmentLevel(Enchantments.FALL_PROTECTION, lCrystal.get().stack());
				if (fallEnchantLevel > 0) {
					float percentage = (float) (1 - (fallEnchantLevel * config.fallDamageReduction.get()));
					if (percentage < 0) percentage = 0;
					event.setAmount(event.getAmount() * percentage);
				}
			}
			
			// attacker events
			Entity possibleAttacker = event.getSource().getEntity();
			if (possibleAttacker instanceof LivingEntity livingAttacker) { // you can never be too safe
				// (super) dispel gel: reduce outgoing damage
				Optional<SlotResult> attackerCrystal = getCurio(livingAttacker, ModItems.WARD_CRYSTAL.get());
				Optional<SlotResult> attackerSuperCrystal = getCurio(livingAttacker, ModItems.SUPER_WARD_CRYSTAL.get());
				if (event.getSource().isMagic() && (attackerCrystal.isPresent() || attackerSuperCrystal.isPresent())) {
					event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierOutgoing.get()));
				}
				
				// duality rings: increase projectile damage
				Optional<SlotResult> attackerRings = getCurio(livingAttacker, ModItems.DUALITY_RINGS.get());
				if (event.getSource().isProjectile() && attackerRings.isPresent()) {
					event.setAmount((float) (event.getAmount() * config.aroOfClubsMultiplier.get()));
				}

				// friendly fire flower: negate self-damage
				Optional<SlotResult> friendlyFlower = getCurio(livingAttacker, ModItems.FRIENDLY_FIRE_FLOWER.get());
				if (possibleAttacker == player && friendlyFlower.isPresent()) {
					event.setAmount(0);
				}
				
				// lunar crystal: apply levitation
				Optional<SlotResult> attackerLCrystal = getCurio(livingAttacker, ModItems.LUNAR_CRYSTAL.get());
				int randresult = rand.nextInt(config.levitationChance.get() - 1);
				if (attackerLCrystal.isPresent() && randresult == 0) {
					player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, config.levitationDuration.get(), config.levitationPotency.get()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void potionImmunityEvent(PotionApplicableEvent event) {
		LivingEntity player = event.getEntityLiving();
		if (!player.getCommandSenderWorld().isClientSide()) {
			
			Optional<SlotResult> treads = getCurio(player, ModItems.SHADOW_TREADS.get());
			if (treads.isPresent() && event.getPotionEffect().getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
				event.setResult(Result.DENY);
			}
			
			Optional<SlotResult> lcrystal = getCurio(player, ModItems.LUNAR_CRYSTAL.get());
			if (lcrystal.isPresent() && event.getPotionEffect().getEffect() == MobEffects.LEVITATION) {
				event.setResult(Result.DENY);
			}
			
			Optional<SlotResult> superCrystal = getCurio(player, ModItems.SUPER_WARD_CRYSTAL.get());
			if (superCrystal.isPresent()) {
				event.setResult(Result.DENY);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW) // CA revives should take effect after most other revivals to avoid diamond heart anti-synergy, but shouldn't take effect last either
	public void playerDeathEvent(LivingDeathEvent event) {
		if (event.isCanceled()) {
			return;
		}
		LivingEntity player = event.getEntityLiving();
		Optional<SlotResult> diamondHeart = getCurio(player, ModItems.SUPER_GOLDEN_HEART.get());
		if (diamondHeart.isPresent() && !player.hasEffect(ModPotions.FRACTURED.get())) {
			if (!event.getSource().isBypassInvul()) {
				ItemStack stack = diamondHeart.get().stack();
				CompoundTag nbt = stack.getOrCreateTag();
				if (CooldownHelper.isCooldownFinished(nbt)) {
					CooldownHelper.updateCounter(nbt, config.revivalCooldown.get());
					event.setCanceled(true);
					player.setHealth(player.getMaxHealth());
					player.addEffect(new MobEffectInstance(ModPotions.FRACTURED.get(), config.fracturedDuration.get(), config.fracturedPotency.get()));
					player.setHealth(player.getMaxHealth()); // lazy max health correction, just set the value twice lol
					player.getCommandSenderWorld().playSound((Player)null, player.blockPosition(), SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 0.5F, 1.0F);
				}
			}
		}
		Optional<SlotResult> undyingShield = getCurio(player, ModItems.SUPER_GLASS_SHIELD.get());
		if (undyingShield.isPresent()) {
			if (!event.getSource().isBypassInvul()) {
				ItemStack stack = undyingShield.get().stack();
				CompoundTag nbt = stack.getOrCreateTag();
				if (CooldownHelper.getCounter(nbt) < config.revivalLimit.get()) {
					CooldownHelper.addToCounter(nbt, config.shatterRevivalCooldown.get());
					event.setCanceled(true);
					player.setHealth(player.getMaxHealth());
					player.getCommandSenderWorld().playSound((Player)null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.5F, 1.0F);
					if (CooldownHelper.getCounter(nbt) < config.revivalLimit.get()) {
						player.getCommandSenderWorld().playSound((Player)null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F); // player has minimum one more revive left, let them know that
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void vanillaEvent(VanillaGameEvent event) {
		if (event.isCanceled()) {
			return;
		}
		if (event.getVanillaEvent() == GameEvent.STEP || event.getVanillaEvent() == GameEvent.HIT_GROUND) {
			Entity cause = event.getCause();
			if (cause instanceof LivingEntity entity) {
				Optional<SlotResult> treads = getCurio(entity, ModItems.SHADOW_TREADS.get());
				if (treads.isPresent() && entity.fallDistance < 3.0F && !entity.isSprinting()) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SuppressWarnings("all")
	private static void injectInto(LootTableLoadEvent event, String poolName, LootPoolEntryContainer... entries) {
		LootPool pool = event.getTable().getPool(poolName);
		//noinspection ConstantConditions method is annotated wrongly
		if (pool != null) {
			int oldLength = pool.entries.length;
			pool.entries = Arrays.copyOf(pool.entries, oldLength + entries.length);
			System.arraycopy(entries, 0, pool.entries, oldLength, entries.length);
		}
	}
	@SuppressWarnings("all")
	@SubscribeEvent
	public void insertLoot(LootTableLoadEvent event) {
		if(!config.lootTableInsertion.get()) {
			return;
		}

		switch (event.getName().getPath()) {
			case "chests/bastion_treasure", "gameplay/piglin_bartering" -> injectInto(event, "main", LootItem.lootTableItem(ModItems.GOLDEN_HEART.get()).setWeight(4).build());
			case "chests/end_city_treasure" -> {
				injectInto(event, "main", LootItem.lootTableItem(ModItems.LUNAR_CRYSTAL.get()).setWeight(2).build());
				injectInto(event, "main", LootItem.lootTableItem(ModItems.COSMICOLA.get()).setWeight(5).build());
				injectInto(event, "main", LootItem.lootTableItem(ModItems.MAGIC_GARLIC_BREAD.get()).setWeight(10).build());
			}
			case "chests/ruined_portal" -> injectInto(event, "main", LootItem.lootTableItem(ModItems.SPICY_COAL.get()).setWeight(8).build());
		}

		if(event.getName().getPath().contains("chests")) {
			if (event.getTable().getPool("main") != null) {
				injectInto(event, "main", LootItem.lootTableItem(ModItems.CHROMA_SHARD.get()).setWeight(4).build());
			}
		}
	}

	@SubscribeEvent
	public void onWandererTrades(WandererTradesEvent event) {
		if(!config.lootTableInsertion.get()) {
			return;
		}
		event.getRareTrades().add(new BasicItemListing(
				rand.nextInt(16, 24),
				new ItemStack(ModItems.CHROMA_SHARD.get()),
				1,
				3
		));
	}
	
}
