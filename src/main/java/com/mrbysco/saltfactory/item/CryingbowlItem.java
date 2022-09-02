package com.mrbysco.saltfactory.item;

import com.mrbysco.saltfactory.registry.SaltRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class CryingbowlItem extends Item {

	public CryingbowlItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		ItemStack itemstack = player.getItemInHand(interactionHand);
		int tears = itemstack.getTag() != null ? itemstack.getTag().getInt("Tears") : 0;
		if (tears < 10) {
			player.startUsingItem(interactionHand);
		}
		return InteractionResultHolder.consume(itemstack);
	}

	@Override
	public SoundEvent getDrinkingSound() {
		return null;
	}

	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int useTime) {
		if (useTime <= 440) {
			if (level.getGameTime() % 40 == 0) {
				level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SaltRegistry.CRYING.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

				if (!level.isClientSide) {
					CompoundTag tag = stack.getOrCreateTag();
					int tears = tag.getInt("Tears");
					if (tears < 10) {
						tag.putInt("Tears", tears + 1);
						stack.setTag(tag);
					}
				}
			}
		}

		super.onUseTick(level, entity, stack, useTime);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int index, boolean p_41408_) {
		if (!level.isClientSide) {
			int tears = stack.getTag() != null ? stack.getTag().getInt("Tears") : 0;
			if (tears == 10) {
				ItemStack tearyStack = new ItemStack(SaltRegistry.TEARY_BOWL.get());
				stack.shrink(1);
				if (entity instanceof Player player) {
					if (!player.addItem(tearyStack)) {
						ItemEntity itemEntity = new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), tearyStack);
						level.addFreshEntity(itemEntity);
					}
				} else {
					ItemEntity itemEntity = new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), tearyStack);
					level.addFreshEntity(itemEntity);
				}
			}
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		ItemStack itemstack = super.finishUsingItem(stack, level, entity);
		int tears = stack.getTag() != null ? stack.getTag().getInt("Tears") : 0;
		if (tears == 10) {
			return new ItemStack(SaltRegistry.TEARY_BOWL.get());
		} else {
			return itemstack;
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 500;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}
}
