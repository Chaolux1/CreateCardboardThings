package net.chaolux.createcardboardthings.registry.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENT;
    public static final Supplier<DataComponentType<Boolean>> FLIGHT_START ;


    static {
        COMPONENT=DeferredRegister.create(Registries.DATA_COMPONENT_TYPE,"createcardboardthings");
        FLIGHT_START =COMPONENT.register("flight_start",() -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    }
}
