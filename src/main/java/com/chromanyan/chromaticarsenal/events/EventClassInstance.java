package com.chromanyan.chromaticarsenal.events;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.config.ModConfig.Common;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModPotions;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.Random;

public class EventClassInstance {

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

            if (player instanceof ServerPlayer serverPlayer) {
                for (ItemStack stack : ChromaCurioHelper.getFlatStacks(serverPlayer)) {
                    if (stack.getItem() instanceof IChromaCurio chromaStack) {
                        chromaStack.onWearerHurt(event, stack, player);
                    }
                }
            }

            // attacker events
            Entity possibleAttacker = event.getSource().getEntity();
            if (possibleAttacker instanceof LivingEntity livingAttacker) { // you can never be too safe
                if (livingAttacker instanceof ServerPlayer serverPlayer) {
                    for (ItemStack stack : ChromaCurioHelper.getFlatStacks(serverPlayer)) {
                        if (stack.getItem() instanceof IChromaCurio chromaStack) {
                            chromaStack.onWearerAttack(event, stack, livingAttacker, player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void potionImmunityEvent(PotionApplicableEvent event) {
        LivingEntity player = event.getEntityLiving();
        if (!player.getCommandSenderWorld().isClientSide()) {
            if (event.getResult() == Result.DENY) {
                return;
            }
            if (player instanceof ServerPlayer serverPlayer) {
                for (ItemStack stack : ChromaCurioHelper.getFlatStacks(serverPlayer)) {
                    if (stack.getItem() instanceof IChromaCurio chromaStack) {
                        chromaStack.onGetImmunities(event, stack, event.getPotionEffect().getEffect());
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    // CA revives should take effect after most other revivals to avoid diamond heart anti-synergy, but shouldn't take effect last either
    public void playerDeathEvent(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        LivingEntity player = event.getEntityLiving();
        if (!player.getCommandSenderWorld().isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer) {
                for (ItemStack stack : ChromaCurioHelper.getFlatStacks(serverPlayer)) {
                    if (stack.getItem() instanceof IChromaCurio chromaStack) {
                        chromaStack.onWearerDied(event, stack, player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void potionAppliedEvent(PotionEvent.PotionAddedEvent event) {
        LivingEntity player = event.getEntityLiving();
        if (!player.getCommandSenderWorld().isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer) {
                for (ItemStack stack : ChromaCurioHelper.getFlatStacks(serverPlayer)) {
                    if (stack.getItem() instanceof IChromaCurio chromaStack) {
                        chromaStack.onPotionApplied(event);
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
        Entity entity = event.getCause();
        if (entity instanceof LivingEntity player) {
            if (!player.getCommandSenderWorld().isClientSide()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    for (ItemStack stack : ChromaCurioHelper.getFlatStacks(serverPlayer)) {
                        if (stack.getItem() instanceof IChromaCurio chromaStack) {
                            chromaStack.onVanillaEvent(event, stack, player);
                        }
                    }
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
        if (!config.lootTableInsertion.get()) {
            return;
        }

        switch (event.getName().getPath()) {
            case "chests/bastion_treasure", "gameplay/piglin_bartering" ->
                    injectInto(event, "main", LootItem.lootTableItem(ModItems.GOLDEN_HEART.get()).setWeight(4).build());
            case "chests/end_city_treasure" -> {
                injectInto(event, "main", LootItem.lootTableItem(ModItems.LUNAR_CRYSTAL.get()).setWeight(2).build());
                injectInto(event, "main", LootItem.lootTableItem(ModItems.COSMICOLA.get()).setWeight(5).build());
                injectInto(event, "main", LootItem.lootTableItem(ModItems.MAGIC_GARLIC_BREAD.get()).setWeight(10).build());
            }
            case "chests/ruined_portal" ->
                    injectInto(event, "main", LootItem.lootTableItem(ModItems.SPICY_COAL.get()).setWeight(8).build());
        }

        if (event.getName().getPath().contains("chests")) {
            if (event.getTable().getPool("main") != null) {
                injectInto(event, "main", LootItem.lootTableItem(ModItems.CHROMA_SHARD.get()).setWeight(2).build());
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
                1,
                3
        ));
    }

}
