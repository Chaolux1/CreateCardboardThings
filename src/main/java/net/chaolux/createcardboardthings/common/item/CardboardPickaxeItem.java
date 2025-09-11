package net.chaolux.createcardboardthings.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CardboardPickaxeItem extends PickaxeItem {
    public CardboardPickaxeItem(Tier tier, Properties properties) {
        super(tier,properties.attributes(attributeModifiers(tier,1.0f,-2.8f)));
    }

    public CardboardPickaxeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier,properties.attributes(attributeModifiers(tier,attackDamage,attackSpeed)));
    }

    private static ItemAttributeModifiers attributeModifiers(Tier tier, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE,new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,attackDamage + tier.getAttackDamageBonus(),AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,attackSpeed,AttributeModifier.Operation.ADD_VALUE),EquipmentSlotGroup.MAINHAND)
                .add(Attributes.MOVEMENT_SPEED,new AttributeModifier(ResourceLocation.fromNamespaceAndPath("createcardboardthings","cardboard_pickaxe_speed"),0.15,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),EquipmentSlotGroup.MAINHAND).build();

    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.createcardboardthings.cardboard_pickaxe").withStyle(ChatFormatting.GRAY));
    }
}
