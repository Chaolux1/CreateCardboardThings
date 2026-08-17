package net.chaolux.createcardboardthings.common.block;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.common.entity.CardboardTntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class CardboardTntBlock extends TntBlock {
    public  CardboardTntBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onCaughtFire(BlockState blockState, Level level, BlockPos blockPos, @Nullable Direction direction, @Nullable LivingEntity livingEntity) {
        if(!Config.cardboardTnt()) return;
        fresh(level,blockPos,livingEntity);
    }

    @Override
    public void wasExploded(Level level, BlockPos blockPos, Explosion explosion) {
        if(!Config.cardboardTnt()) return;
        if(level.isClientSide) return;
        CardboardTntEntity cardboardTntEntity=new CardboardTntEntity(level,blockPos.getX() + 0.5,blockPos.getY(),blockPos.getZ() + 0.5,explosion.getIndirectSourceEntity());
        int fuse=cardboardTntEntity.getFuse();
        cardboardTntEntity.setFuse(level.random.nextInt(fuse / 4) + fuse / 8);
        level.addFreshEntity(cardboardTntEntity);
    }

    private static void fresh(Level level,BlockPos blockPos,@Nullable LivingEntity livingEntity) {
        if(level.isClientSide) return;
        CardboardTntEntity cardboardTntEntity=new CardboardTntEntity(level,blockPos.getX() + 0.5,blockPos.getY(),blockPos.getZ() + 0.5,livingEntity);
        level.addFreshEntity(cardboardTntEntity);
        level.playSound(null,cardboardTntEntity.getX(),cardboardTntEntity.getY(),cardboardTntEntity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS,1.0f,1.0f);
        level.gameEvent(livingEntity, GameEvent.PRIME_FUSE,blockPos);
    }
}
