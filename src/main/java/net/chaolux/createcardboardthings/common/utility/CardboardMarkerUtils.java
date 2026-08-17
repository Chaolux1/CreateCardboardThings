package net.chaolux.createcardboardthings.common.utility;

import net.chaolux.createcardboardthings.common.entity.CardboardMarkerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CardboardMarkerUtils {
    public static boolean isMark(BlockState blockState) {
        return blockState.getBlock() instanceof LeverBlock || blockState.getBlock() instanceof ButtonBlock;
    }

    @Nullable
    public static CardboardMarkerEntity findMark(Level level, BlockPos blockPos) {
        AABB aabb=new AABB(blockPos).inflate(0.25);
        List<CardboardMarkerEntity> list=level.getEntitiesOfClass(CardboardMarkerEntity.class,aabb,marker -> marker.blockPosition().equals(blockPos));
        return list.isEmpty() ? null : list.get(0);
    }
}
