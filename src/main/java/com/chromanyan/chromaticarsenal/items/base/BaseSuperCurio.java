package com.chromanyan.chromaticarsenal.items.base;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Optional;
@SuppressWarnings("unused")
public class BaseSuperCurio extends Item implements ICurioItem {
	
	private final RegistryObject<Item> inferiorVariant;
	
	public BaseSuperCurio(RegistryObject<Item> upgradeTo) {
		super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(1).rarity(Rarity.EPIC).defaultDurability(0));
		this.inferiorVariant = upgradeTo;
	}

	public RegistryObject<Item> getInferiorVariant() {
		return inferiorVariant;
	}
	
	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack) {
		return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), this).isEmpty();
	}
	
	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		LivingEntity livingEntity = context.entity();
		if (livingEntity.level.isClientSide) {
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
	
	@Override
	public boolean isEnchantable(@NotNull ItemStack p_77616_1_) {
		return true;
	}
	
	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 1;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.BINDING_CURSE || enchantment == Enchantments.VANISHING_CURSE) {
			return true;
		} else {
			return super.canApplyAtEnchantingTable(stack, enchantment);
		}
	}
}
