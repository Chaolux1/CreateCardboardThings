package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.common.entity.CardboardArrowEntity;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class CardboardArrowItem extends ArrowItem {
    public CardboardArrowItem(Properties p_40512_) {
        super(p_40512_);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        return new CardboardArrowEntity(level, shooter);
    }
}
