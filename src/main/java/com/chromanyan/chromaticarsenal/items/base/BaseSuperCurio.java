package com.chromanyan.chromaticarsenal.items.base;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.ISuperCurio;
import com.chromanyan.chromaticarsenal.util.ChromaCurioHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Optional;

public class BaseSuperCurio extends BaseCurioItem implements ISuperCurio {

    private final RegistryObject<Item> inferiorVariant;

    public BaseSuperCurio(@Nullable RegistryObject<Item> upgradeTo) {
        super(new Item.Properties()
                .tab(ChromaticArsenal.GROUP)
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .defaultDurability(0));
        this.inferiorVariant = upgradeTo;
    }

    @Nullable
    public RegistryObject<Item> getInferiorVariant() {
        return inferiorVariant;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.chromaticarsenal.super"));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {

        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), this).isEmpty() && ChromaCurioHelper.isValidSuperCurioSlot(slotContext);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment != ModEnchantments.CHROMATIC_TWISTING.get() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity livingEntity = context.entity();
        if (livingEntity.level.isClientSide || inferiorVariant == null) {
            return;
        }
        Optional<SlotResult> inferiorInstance = CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, inferiorVariant.get());
        if (inferiorInstance.isPresent()) {
            ItemStack s = inferiorInstance.get().stack();
            if (livingEntity instanceof Player player) {
                player.drop(s.copy(), true);
                s.setCount(0);
            }
        }
    }
}
