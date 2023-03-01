package com.chromanyan.chromaticarsenal.util;

import com.chromanyan.chromaticarsenal.items.curios.interfaces.IChromaCurio;
import com.chromanyan.chromaticarsenal.items.curios.interfaces.ISuperCurio;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

@SuppressWarnings("unused")
public class ChromaCurioHelper {

    private ChromaCurioHelper() { // i also don't want people to create curio helpers

    }

    // thanks flux networks
    @Nonnull
    public static Iterable<ItemStack> getFlatStacks(ServerPlayer player) {
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

    public static boolean isSuperCurio(Item item) {
        return item instanceof ISuperCurio;
    }

    public static boolean isChromaCurio(Item item) {
        return item instanceof IChromaCurio;
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
