package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.CreateCardboardThings;
import net.chaolux.createcardboardthings.client.render.CardboardTridentItemRenderer;
import net.chaolux.createcardboardthings.common.entity.CardboardTridentEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;


public class CardboardTridentItem extends TridentItem {
    private static final double MAX_Y= -5;
    private static final int TICK=10;
    public CardboardTridentItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack=player.getItemInHand(interactionHand);
        if(!Config.cardboardTrident()) return InteractionResultHolder.pass(itemStack);
        if(isDestroyFluid(player)) {
            destroyTrident(level,player,itemStack);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemStack,level.isClientSide);
        }
        if(!canUseCarboardTrident(level,player)) return InteractionResultHolder.fail(itemStack);
        if(itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) return InteractionResultHolder.fail(itemStack);
        player.startUsingItem(interactionHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity,int tick) {
        if(!Config.cardboardTrident()) return;
        if(!(livingEntity instanceof Player player)) return;
        int usedTick=this.getUseDuration(itemStack,livingEntity) - tick;
        if(usedTick < TICK) return;
        if(isDestroyFluid(player)) {
            destroyTrident(level,player,itemStack);
            return;
        }
        if(!canUseCarboardTrident(level,player)) return;
        int riptideLevel=enchantment(player,itemStack,Enchantments.RIPTIDE);
        if(riptideLevel > 0) {
            damage(level,player,itemStack,player.getUsedItemHand());
            useCarboardReptide(level,player,itemStack,riptideLevel);
            return;
        }
        if(level instanceof ServerLevel serverLevel) throwTrident(serverLevel,player,itemStack);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> componentList, TooltipFlag tooltipFlag) {
        componentList.add(Component.translatable("tooltip.createcardboardthings.cardboard_trident").withStyle(ChatFormatting.GRAY));
    }

    private static void throwTrident(ServerLevel serverLevel,Player player,ItemStack itemStack) {
        ItemStack stack=itemStack.copyWithCount(1);
        CardboardTridentEntity cardboardTridentEntity=new CardboardTridentEntity(serverLevel,player,stack);
        cardboardTridentEntity.pickup=player.getAbilities().instabuild ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;
        cardboardTridentEntity.shootFromRotation(player,player.getXRot(),player.getYRot(),0.0f,2.5f,1.0f);
        if(!serverLevel.addFreshEntity(cardboardTridentEntity)) return;
        damage(serverLevel,player,itemStack,player.getUsedItemHand());
        serverLevel.playSound(null,cardboardTridentEntity,SoundEvents.TRIDENT_THROW.value(),SoundSource.PLAYERS,1.0f,1.0f);
        if(!player.getAbilities().instabuild) itemStack.shrink(1);
    }

    private static boolean canUseCarboardTrident(Level level,Player player) {
        return level.dimension().equals(Level.END) && player.getY() <= MAX_Y;
    }

    private static boolean isDestroyFluid(Player player) {
        return player.isInWater() || player.isInLava();
    }

    private static void destroyTrident(Level level,Player player,ItemStack itemStack) {
        if(level.isClientSide) return;
        level.playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.ITEM_BREAK,SoundSource.PLAYERS,0.9f,0.85f + level.random.nextFloat() * 0.3f);
        if(!player.getAbilities().instabuild) itemStack.shrink(1);

    }

    private static void useCarboardReptide(Level level,Player player,ItemStack itemStack, int riptideLevel) {
        float xRot=player.getXRot();
        float yRot=player.getYRot();
        float x= -Mth.sin(yRot * ((float) Math.PI / 180.0f)) * Mth.cos(xRot * ((float) Math.PI / 180.0f));
        float y= -Mth.sin(xRot * ((float) Math.PI / 180.0f));
        float z= Mth.cos(yRot * ((float) Math.PI / 180.0f)) * Mth.cos(xRot * ((float) Math.PI / 180.0f));
        float length=Mth.sqrt(x * x + y * y + z * z);
        float strength= 3.0f * ((1.0f + riptideLevel) / 4.0f);
        x *= strength / length;
        y *= strength / length;
        z *= strength / length;
        player.push(x,y,z);
        player.startAutoSpinAttack(20,1.0f,itemStack);
        if(player.onGround()) player.move(MoverType.SELF,new Vec3(0.0,1.1999,0.0));
        SoundEvent soundEvent=SoundEvents.TRIDENT_RIPTIDE_1.value();
        if(riptideLevel >= 3) {
            soundEvent=SoundEvents.TRIDENT_RIPTIDE_3.value();
        } else if (riptideLevel == 2) {
            soundEvent = SoundEvents.TRIDENT_RIPTIDE_2.value();
        }
        level.playSound(null,player,soundEvent,SoundSource.PLAYERS,1.0f,1.0f);
    }

    public static ItemAttributeModifiers createItemAttribute() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE,new AttributeModifier(ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"cardboard_trident_attack_damage"),0.0,AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED,new AttributeModifier(ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"cardboard_trident_attack_speed"),-2.9,AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    private int enchantment(LivingEntity livingEntity,ItemStack itemStack,ResourceKey<Enchantment> resourceKey) {
        HolderLookup.RegistryLookup<Enchantment> holderLookup=livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> enchantmentHolder=holderLookup.getOrThrow(resourceKey);
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder,itemStack);
    }

    private static void damage(Level level,Player player,ItemStack itemStack,InteractionHand interactionHand) {
        if(!(level instanceof ServerLevel serverLevel) || player.getAbilities().instabuild) return;
        EquipmentSlot equipmentSlot=interactionHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        itemStack.hurtAndBreak(1,serverLevel,player,item -> player.onEquippedItemBroken(item,equipmentSlot));
    }
}
