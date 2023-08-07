package com.chromanyan.chromaticarsenal.items;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModItems;
import com.chromanyan.chromaticarsenal.init.ModStats;
import com.chromanyan.chromaticarsenal.init.ModTags;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.ISuperCurio;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChromaSalvager extends Item {

    private static final ModConfig.Common config = ModConfig.COMMON;

    public ChromaSalvager() {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(1)
                .defaultDurability(8)
                .rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.chroma_salvager.1"));
        list.add(Component.translatable("tooltip.chromaticarsenal.chroma_salvager.2"));
        list.add(Component.translatable("tooltip.chromaticarsenal.chroma_salvager.3"));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (player.level.isClientSide) {
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }

        ItemStack salvageTarget;

        if (hand == InteractionHand.MAIN_HAND)
            salvageTarget = player.getItemInHand(InteractionHand.OFF_HAND);
        else
            salvageTarget = player.getItemInHand(InteractionHand.MAIN_HAND);

        Item salvageReturn = null;

        if (!salvageTarget.isEmpty()) {
            if (!salvageTarget.is(ModTags.Items.NO_SALVAGE)) {
                Item salvageItem = salvageTarget.getItem();
                if (salvageItem instanceof ISuperCurio) {
                    if (config.returnInferiorVariant.get()) {
                        ItemStack inferiorReturn = new ItemStack(((ISuperCurio) salvageItem).getInferiorVariant().get());
                        if (!player.getInventory().add(inferiorReturn)) {
                            player.drop(inferiorReturn, false);
                        }
                    }

                    salvageReturn = ModItems.ASCENSION_ESSENCE.get();
                } else if (salvageItem instanceof IChromaCurio) {
                    salvageReturn = ModItems.CHROMA_SHARD.get();
                } else {
                    player.displayClientMessage(Component.translatable("message.chromaticarsenal.invalid_salvage"), true);
                }
            } else {
                player.displayClientMessage(Component.translatable("message.chromaticarsenal.salvage_blacklisted"), true);
            }
        } else {
            player.displayClientMessage(Component.translatable("message.chromaticarsenal.cannot_salvage_air"), true);
        }

        if (salvageReturn != null) {
            ItemStack returnedItem = new ItemStack(salvageReturn);
            if (!player.getInventory().add(returnedItem)) { // thanks farmer's delight
                player.drop(returnedItem, false);
            }
            salvageTarget.shrink(1);
            player.awardStat(Stats.ITEM_USED.get(this));
            player.awardStat(ModStats.CHROMA_SALVAGER_USES);
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.ENDER_EYE_DEATH, SoundSource.PLAYERS, 0.8f, 1f);
            if (config.canDamageSalvager.get())
                itemstack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(hand));
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
