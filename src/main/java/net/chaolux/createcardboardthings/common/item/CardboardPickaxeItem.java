package net.chaolux.createcardboardthings.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

import java.util.UUID;

public class CardboardPickaxeItem extends PickaxeItem {
    public CardboardPickaxeItem(Tier p_42961_, int p_42962_, float p_42963_, Properties p_42964_) {
        super(p_42961_, p_42962_, p_42963_, p_42964_);
    }

    private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("67a7831f-6c80-4310-954f-ab01f2550a86");

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getDefaultAttributeModifiers(slot);

        if(slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                    MOVEMENT_SPEED_UUID,
                    "",
                    0.15,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));
            return builder.build();
        }
        return modifiers;
    }
}
