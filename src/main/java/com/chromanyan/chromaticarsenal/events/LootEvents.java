package com.chromanyan.chromaticarsenal.events;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.items.curios.CurioBubbleAmulet;
import com.chromanyan.chromaticarsenal.items.curios.CurioLunarCrystal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.Random;

public class LootEvents {
    private final Random rand = new Random();
    private final ModConfig.Common config = ModConfig.COMMON;

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

    private static LootItemConditionalFunction.Builder<?> exactlyOne() {
        return SetItemCountFunction.setCount(ConstantValue.exactly(1));
    }

    @SubscribeEvent
    public void insertLoot(LootTableLoadEvent event) {
        if (!config.lootTableInsertion.get()) {
            return;
        }

        switch (event.getName().getPath()) {
            case "chests/bastion_treasure", "gameplay/piglin_bartering" ->
                    injectInto(event, "main", LootItem.lootTableItem(ModItems.GOLDEN_HEART.get()).setWeight(6)
                            .apply(exactlyOne()).build());
            case "chests/end_city_treasure" -> {
                injectInto(event, "main", LootItem.lootTableItem(ModItems.LUNAR_CRYSTAL.get()).setWeight(4)
                        .apply(exactlyOne()).build());
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
                            .apply(exactlyOne()).setWeight(10).build());
            case "chests/village/village_shepherd" ->
                    injectInto(event, "main", LootItem.lootTableItem(ModItems.BLAHAJ.get())
                            .apply(exactlyOne()).setWeight(4).build());
            case "chests/village/village_armorer" ->
                    injectInto(event, "main", LootItem.lootTableItem(ModItems.MOMENTUM_STONE.get())
                            .apply(exactlyOne()).setWeight(4).build());
        }

        // without the second check, chroma shards generate in jungle temple dispensers
        if (event.getName().getPath().contains("chests") && !event.getName().getPath().contains("dispenser")) {
            //noinspection ConstantConditions
            if (event.getTable().getPool("main") != null) {
                injectInto(event, "main", LootItem.lootTableItem(ModItems.CHROMA_SHARD.get())
                        .apply(exactlyOne()).setWeight(2).build());
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
