package com.chromanyan.chromaticarsenal.items.base;

import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BaseSuperCurio extends Item implements ICurioItem {
	
	private RegistryObject<Item> inferiorVariant;
	
	public BaseSuperCurio(RegistryObject<Item> upgradeTo) {
		super(new Item.Properties().tab(ChromaticArsenal.GROUP).stacksTo(1).rarity(Rarity.EPIC).defaultDurability(0));
		this.inferiorVariant = upgradeTo;
	}
	
	@Override
	public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack) {
		return !CuriosApi.getCuriosHelper().findEquippedCurio(this, livingEntity).isPresent();
	}
	
	@Override
	public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
		if (livingEntity.level.isClientSide) {
			return;
		}
		Optional<ImmutableTriple<String, Integer, ItemStack>> inferiorInstance = CuriosApi.getCuriosHelper().findEquippedCurio(inferiorVariant.get(), livingEntity);
		if (inferiorInstance.isPresent()) {
			ItemStack s = inferiorInstance.get().getRight();
			if (livingEntity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) livingEntity;
				player.drop(s.copy(), true);
				s.setCount(0);
			}
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack p_77616_1_) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public int getItemEnchantability(ItemStack stack) {
		// TODO Auto-generated method stub
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
