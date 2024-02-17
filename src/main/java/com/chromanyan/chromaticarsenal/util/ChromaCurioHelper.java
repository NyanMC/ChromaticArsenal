package com.chromanyan.chromaticarsenal.util;

import com.chromanyan.chromaticarsenal.config.ModConfig;
import com.chromanyan.chromaticarsenal.init.ModEnchantments;
import com.chromanyan.chromaticarsenal.init.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

public class ChromaCurioHelper {

    private static final ModConfig.Common config = ModConfig.COMMON;

    private ChromaCurioHelper() { // i also don't want people to create curio helpers

    }

    public static boolean isValidSuperCurioSlot(SlotContext context) {
        if (context.identifier().equals("super_curio")) {
            return true;
        }
        return !config.superCuriosOnlyInRespectiveSlot.get();
    }

    public static boolean isChromaticTwisted(ItemStack stack, @Nullable LivingEntity player) {
        if (player != null && ModList.get().isLoaded("band_of_gigantism"))
            return ChromaCurioHelper.getCurio(player, ModItems.MARK_TWISTED.get()).isPresent() || stack.getEnchantmentLevel(ModEnchantments.CHROMATIC_TWISTING.get()) > 0;
        else
            return stack.getEnchantmentLevel(ModEnchantments.CHROMATIC_TWISTING.get()) > 0;
    }

    public static boolean shouldIgnoreDamageEvent(LivingHurtEvent event) {
        return event.getAmount() == 0 || event.isCanceled() || event.getSource().isBypassInvul();
    }

    // thanks flux networks
    @Nonnull
    public static Iterable<ItemStack> getFlatStacks(LivingEntity player) {
        final LazyOptional<ICuriosItemHandler> curios = CuriosApi.getCuriosHelper().getCuriosHandler(player);
        if (curios.isPresent()) {
            // we can cache the ICuriosItemHandler atm, but cannot for getEquippedCurios()
            final ICuriosItemHandler peek = curios.orElseThrow(RuntimeException::new);
            return () -> new FlatIterator(peek);
        }
        return Collections.emptyList();
    }

    public static Optional<SlotResult> getCurio(LivingEntity livingEntity, Item item) {
        return CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, item);
    }

    // again, thanks flux networks
    private static class FlatIterator implements Iterator<ItemStack> {

        private final Iterator<ICurioStacksHandler> mIterator;

        private IItemHandler mHandler;
        private int mIndex;

        FlatIterator(ICuriosItemHandler curios) {
            mIterator = curios.getCurios().values().iterator();
        }

        @Override
        public boolean hasNext() {
            forward();
            return mHandler != null && mIndex < mHandler.getSlots();
        }

        @Override
        public ItemStack next() {
            forward();
            return mHandler.getStackInSlot(mIndex++);
        }

        private void forward() {
            while ((mHandler == null || mIndex == mHandler.getSlots()) && mIterator.hasNext()) {
                mHandler = mIterator.next().getStacks();
                mIndex = 0;
            }
        }
    }
}
