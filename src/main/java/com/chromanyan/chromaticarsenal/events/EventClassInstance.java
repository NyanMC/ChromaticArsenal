package com.chromanyan.chromaticarsenal.events;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModEffects;
import com.chromanyan.chromaticarsenal.items.curios.CurioAdvancingHeart;
import com.chromanyan.chromaticarsenal.items.curios.CurioBubbleAmulet;
import com.chromanyan.chromaticarsenal.items.curios.CurioLunarCrystal;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AdvancementEvent.AdvancementEarnEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class EventClassInstance {

    private final Random rand = new Random();
    private final Common config = ModConfig.COMMON;

    @SubscribeEvent
    public void playerAttackedEvent(LivingHurtEvent event) {
        LivingEntity player = event.getEntity();
        if (!player.getCommandSenderWorld().isClientSide()) {
            // spatial: block fall damage
            if (player.hasEffect(ModEffects.SPATIAL.get()) && event.getSource() == DamageSource.FALL) {
                event.setAmount(0); // just in case, you know?
                event.setCanceled(true);
            }
            if (event.isCanceled()) {
                return; // the rest of the effects should only fire if they're even applicable
            }

            for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
                if (stack.getItem() instanceof IChromaCurio chromaStack) {
                    chromaStack.onWearerHurt(event, stack, player);
                }
            }

            // attacker events
            Entity possibleAttacker = event.getSource().getEntity();
            if (possibleAttacker instanceof LivingEntity livingAttacker) {
                for (ItemStack stack : ChromaCurioHelper.getFlatStacks(livingAttacker)) {
                    if (stack.getItem() instanceof IChromaCurio chromaStack) {
                        chromaStack.onWearerAttack(event, stack, livingAttacker, player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void potionImmunityEvent(MobEffectEvent.Applicable event) {
        LivingEntity player = event.getEntity();
        if (player.getCommandSenderWorld().isClientSide() || !config.potionImmunitySideCheck.get()) return;
        if (event.getResult() == Result.DENY) return;

        for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
            if (stack.getItem() instanceof IChromaCurio chromaStack) {
                chromaStack.onGetImmunities(event, stack, event.getEffectInstance().getEffect());
            }
        }
    }

    @SubscribeEvent
    public void playerGotAdvancement(AdvancementEarnEvent event) {
        Player player = event.getEntity();

        Optional<SlotResult> slotResult = ChromaCurioHelper.getCurio(player, ModItems.ADVANCING_HEART.get());
        if (slotResult.isEmpty()) return;

        CurioAdvancingHeart.updateNBTForStack(slotResult.get().slotContext(), slotResult.get().stack());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    // CA revives should take effect after most other revivals to avoid diamond heart anti-synergy, but shouldn't take effect last either
    public void playerDeathEvent(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if (entity.getCommandSenderWorld().isClientSide()) {
            return;
        }

        // those damn creepers, allowing a mob to have both cursed revival and fractured
        if (entity.hasEffect(ModEffects.CURSED_REVIVAL.get())
                && !entity.hasEffect(ModEffects.FRACTURED.get())
                && !(entity instanceof Player)) {
            event.setCanceled(true);
            entity.removeEffect(ModEffects.CURSED_REVIVAL.get());
            entity.addEffect(new MobEffectInstance(ModEffects.FRACTURED.get(), 72000, config.cursedTotemFracturedLevel.get()));
            entity.setHealth(entity.getMaxHealth());
            entity.getCommandSenderWorld().playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.HOSTILE, 0.5F, 1.0F);
        }

        for (ItemStack stack : ChromaCurioHelper.getFlatStacks(entity)) {
            if (stack.getItem() instanceof IChromaCurio chromaStack) {
                chromaStack.onWearerDied(event, stack, entity);
            }
        }
    }

    @SubscribeEvent
    public void entityVisibilityEvent(LivingEvent.LivingVisibilityEvent event) {
        LivingEntity player = event.getEntity();

        if (!player.getCommandSenderWorld().isClientSide()) {
            for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
                if (stack.getItem() instanceof IChromaCurio chromaStack) {
                    chromaStack.onGetVisibility(event, stack);
                }
            }
        }
    }

    @SubscribeEvent
    public void potionAppliedEvent(MobEffectEvent.Added event) {
        LivingEntity player = event.getEntity();

        if (!player.getCommandSenderWorld().isClientSide()) {
            for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
                if (stack.getItem() instanceof IChromaCurio chromaStack) {
                    chromaStack.onPotionApplied(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void vanillaEvent(VanillaGameEvent event) {
        if (event.isCanceled()) {
            return;
        }
        Entity entity = event.getCause();
        if (entity instanceof LivingEntity player) {
            if (!player.getCommandSenderWorld().isClientSide()) {
                for (ItemStack stack : ChromaCurioHelper.getFlatStacks(player)) {
                    if (stack.getItem() instanceof IChromaCurio chromaStack) {
                        chromaStack.onVanillaEvent(event, stack, player);
                    }
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static void injectInto(LootTableLoadEvent event, String poolName, LootPoolEntryContainer... entries) {
        LootPool pool = event.getTable().getPool(poolName);
        //noinspection ConstantConditions method is annotated wrongly
        if (pool != null) {
            int oldLength = pool.entries.length;
            pool.entries = Arrays.copyOf(pool.entries, oldLength + entries.length);
            System.arraycopy(entries, 0, pool.entries, oldLength, entries.length);
        }
    }

    @SubscribeEvent
    public void insertLoot(LootTableLoadEvent event) {
        if (!config.lootTableInsertion.get()) {
            return;
        }

        switch (event.getName().getPath()) {
            case "chests/bastion_treasure", "gameplay/piglin_bartering" ->
                injectInto(event, "main", LootItem.lootTableItem(ModItems.GOLDEN_HEART.get()).setWeight(6)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).build());
            case "chests/end_city_treasure" -> {
                injectInto(event, "main", LootItem.lootTableItem(ModItems.LUNAR_CRYSTAL.get()).setWeight(4)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).build());
                injectInto(event, "main", LootItem.lootTableItem(ModItems.COSMICOLA.get()).setWeight(4)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).build());
                injectInto(event, "main", LootItem.lootTableItem(ModItems.MAGIC_GARLIC_BREAD.get()).setWeight(5)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).build());
            }
            case "chests/ruined_portal", "chests/nether_bridge" ->
                injectInto(event, "main", LootItem.lootTableItem(ModItems.SPICY_COAL.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))).setWeight(6).build());
            case "chests/igloo_chest" ->
                injectInto(event, "main", LootItem.lootTableItem(ModItems.CRYO_RING.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).setWeight(10).build());
            case "chests/village/village_shepherd" ->
                injectInto(event, "main", LootItem.lootTableItem(ModItems.BLAHAJ.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).setWeight(4).build());
            case "chests/village/village_armorer" ->
                    injectInto(event, "main", LootItem.lootTableItem(ModItems.MOMENTUM_STONE.get())
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).setWeight(4).build());
        }

        // without the second check, chroma shards generate in jungle temple dispensers
        if (event.getName().getPath().contains("chests") && !event.getName().getPath().contains("dispenser")) {
            //noinspection ConstantConditions
            if (event.getTable().getPool("main") != null) {
                injectInto(event, "main", LootItem.lootTableItem(ModItems.CHROMA_SHARD.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).setWeight(2).build());
            }
        }
    }

    @SubscribeEvent
    public void onWandererTrades(WandererTradesEvent event) {
        if (!config.lootTableInsertion.get()) {
            return;
        }
        event.getRareTrades().add(new BasicItemListing(
                rand.nextInt(16, 24),
                new ItemStack(ModItems.CHROMA_SHARD.get()),
                3,
                3
        ));
    }

    @SubscribeEvent
    public void villagerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.SHEPHERD) {
            event.getTrades().get(5).add(new BasicItemListing(
                    new ItemStack(Items.EMERALD, 24),
                    new ItemStack(ModItems.CHROMA_SHARD.get()),
                    new ItemStack(ModItems.BLAHAJ.get()),
                    2,
                    0,
                    0.2F
            ));
        }
        if (event.getType() == VillagerProfession.ARMORER) {
            event.getTrades().get(3).add(new BasicItemListing(
                    new ItemStack(Items.EMERALD, 8),
                    new ItemStack(ModItems.CHROMA_SHARD.get()),
                    new ItemStack(ModItems.MOMENTUM_STONE.get()),
                    2,
                    16,
                    0.2F
            ));
        }
    }

    @SubscribeEvent
    public void insertDrops(LivingDropsEvent event) {
        if (!config.lootTableInsertion.get()) {
            return;
        }

        LivingEntity dying = event.getEntity();

        if (dying instanceof Drowned) {
            CurioBubbleAmulet.handleDrop(event, dying);
        }

        if (dying instanceof EnderMan && dying.level.dimension() == Level.END) {
            CurioLunarCrystal.handleDrop(event, dying);
        }
    }

}
