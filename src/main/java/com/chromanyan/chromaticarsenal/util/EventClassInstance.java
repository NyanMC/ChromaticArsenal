package com.chromanyan.chromaticarsenal.util;

import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModPotions;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class EventClassInstance {
	// this is a mess
	
	Random rand = new Random();
	Common config = ModConfig.COMMON;
	
	@SubscribeEvent
	public void playerAttackedEvent(LivingHurtEvent event) {
		LivingEntity player = event.getEntityLiving();
		if (!player.getCommandSenderWorld().isClientSide()) {
			// glass shield: block hits
			Optional<ImmutableTriple<String, Integer, ItemStack>> shield = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.GLASS_SHIELD.get(), player);
			if (shield.isPresent() && event.getAmount() != 0 && !event.getSource().isBypassInvul()) {
				ItemStack stack = shield.get().getRight();
				CompoundNBT nbt = stack.getOrCreateTag();
				int savedTicks = 0;
				if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) > 0) {
					savedTicks = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) * config.enchantmentCooldownReduction.get();
				}
				if (!(nbt.contains("counter") && nbt.getInt("counter") > 0)) {
					nbt.putInt("counter", config.cooldownDuration.get() - savedTicks);
					event.setAmount(0);
					player.getCommandSenderWorld().playSound((PlayerEntity)null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundCategory.PLAYERS, 0.5F, 1.0F);
					
				} else {
					// ChromaticArsenal.LOGGER.info("DEBUG: Attempted to block damage, but player had " + nbt.getInt("counter") + "ticks of cooldown remaining");
				}
			}
			
			// (super) dispel gel: reduce incoming damage
			Optional<ImmutableTriple<String, Integer, ItemStack>> crystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.WARD_CRYSTAL.get(), player);
			Optional<ImmutableTriple<String, Integer, ItemStack>> superCrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.SUPER_WARD_CRYSTAL.get(), player);
			if (event.getSource().isMagic() && (crystal.isPresent() || superCrystal.isPresent()) && !event.getSource().isBypassInvul()) {
				event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierIncoming.get()));
			}
			
			// attacker events
			Entity possibleAttacker = event.getSource().getEntity();
			if (possibleAttacker != null && possibleAttacker instanceof LivingEntity) { // you can never be too safe
				// (super) dispel gel: reduce outgoing damage
				Optional<ImmutableTriple<String, Integer, ItemStack>> attackerCrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.WARD_CRYSTAL.get(), (LivingEntity) possibleAttacker);
				Optional<ImmutableTriple<String, Integer, ItemStack>> attackerSuperCrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.SUPER_WARD_CRYSTAL.get(), (LivingEntity) possibleAttacker);
				if (event.getSource().isMagic() && (attackerCrystal.isPresent() || attackerSuperCrystal.isPresent())) {
					event.setAmount((float) (event.getAmount() * config.antiMagicMultiplierOutgoing.get()));
				}
				
				// duality rings: increase projectile damage
				Optional<ImmutableTriple<String, Integer, ItemStack>> attackerRings = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.DUALITY_RINGS.get(), (LivingEntity) possibleAttacker);
				if (event.getSource().isProjectile() && attackerRings.isPresent()) {
					event.setAmount((float) (event.getAmount() * config.aroOfClubsMultiplier.get()));
				}
				
				// lunar crystal: apply levitation
				Optional<ImmutableTriple<String, Integer, ItemStack>> attackerLCrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.LUNAR_CRYSTAL.get(), (LivingEntity) possibleAttacker);
				int randresult = rand.nextInt(config.levitationChance.get() - 1);
				if (attackerLCrystal.isPresent() && randresult == 0) {
					player.addEffect(new EffectInstance(Effects.LEVITATION, config.levitationDuration.get(), config.levitationPotency.get()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void potionImmunityEvent(PotionApplicableEvent event) {
		LivingEntity player = event.getEntityLiving();
		if (!player.getCommandSenderWorld().isClientSide()) {
			
			Optional<ImmutableTriple<String, Integer, ItemStack>> treads = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.SHADOW_TREADS.get(), player);
			if (treads.isPresent() && event.getPotionEffect().getEffect() == Effects.MOVEMENT_SLOWDOWN) {
				event.setResult(Result.DENY);
			}
			
			Optional<ImmutableTriple<String, Integer, ItemStack>> lcrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.LUNAR_CRYSTAL.get(), player);
			if (lcrystal.isPresent() && event.getPotionEffect().getEffect() == Effects.LEVITATION) {
				event.setResult(Result.DENY);
			}
			
			Optional<ImmutableTriple<String, Integer, ItemStack>> superCrystal = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.SUPER_WARD_CRYSTAL.get(), player);
			if (superCrystal.isPresent()) {
				event.setResult(Result.DENY);
			}
		}
	}
	
	@SubscribeEvent
	public void playerDeathEvent(LivingDeathEvent event) {
		if (event.isCanceled()) {
			return;
		}
		LivingEntity player = event.getEntityLiving();
		Optional<ImmutableTriple<String, Integer, ItemStack>> diamondHeart = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.SUPER_GOLDEN_HEART.get(), player);
		if (diamondHeart.isPresent() && !player.hasEffect(ModPotions.FRACTURED.get())) {
			if (!event.getSource().isBypassInvul()) {
				ItemStack stack = diamondHeart.get().getRight();
				CompoundNBT nbt = stack.getOrCreateTag();
				if (!(nbt.contains("counter") && nbt.getInt("counter") > 0)) {
					nbt.putInt("counter", config.revivalCooldown.get());
					event.setCanceled(true);
					player.setHealth(player.getMaxHealth());
					player.addEffect(new EffectInstance(ModPotions.FRACTURED.get(), config.fracturedDuration.get(), config.fracturedPotency.get()));
					player.setHealth(player.getMaxHealth()); // lazy max health correction, just set the value twice lol
					player.getCommandSenderWorld().playSound((PlayerEntity)null, player.blockPosition(), SoundEvents.IRON_GOLEM_DAMAGE, SoundCategory.PLAYERS, 0.5F, 1.0F);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void insertLoot(LootTableLoadEvent event) {
		if(!config.lootTableInsertion.get()) {
			return;
		}
		
		LootPool.Builder builder = LootPool.lootPool().setRolls(RandomValueRange.between(-5, 1)).name("chromaticarsenal_rare_loot");
		LootPool.Builder builder2 = LootPool.lootPool().setRolls(RandomValueRange.between(-1, 2)).name("chromaticarsenal_common_loot");
		boolean pool1HasLoot = false;
		boolean pool2HasLoot = false;
		if(event.getName().getPath().contains("chests/bastion_treasure")) { // so == doesn't work, but .contains() does. sure, i guess.
			builder.add(ItemLootEntry.lootTableItem(() -> ModItems.GOLDEN_HEART.get()));
			pool1HasLoot = true;
		}
		
		if(event.getName().getPath().contains("chests/end_city_treasure")) {
			builder.add(ItemLootEntry.lootTableItem(() -> ModItems.LUNAR_CRYSTAL.get()));
			builder2.add(ItemLootEntry.lootTableItem(() -> ModItems.MAGIC_GARLIC_BREAD.get()));
			pool1HasLoot = true;
			pool2HasLoot = true;
		}
		if(event.getName().getPath().contains("chests")) {
			builder2.add(ItemLootEntry.lootTableItem(() -> ModItems.CHROMA_SHARD.get()));
			pool2HasLoot = true;
		}
		
		if(pool1HasLoot) {
			event.getTable().addPool(builder.build());
		}
		if(pool2HasLoot) {
			event.getTable().addPool(builder2.build());
		}
	}
	
}
