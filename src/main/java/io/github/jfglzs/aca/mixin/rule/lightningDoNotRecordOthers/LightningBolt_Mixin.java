package io.github.jfglzs.aca.mixin.rule.lightningDoNotRecordOthers;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningBolt.class)
public class LightningBolt_Mixin {
    @WrapMethod(
            method = "lambda$tick$0"
    )
    public boolean getEntities(Entity entity, Operation<Boolean> original) {
        if (entity instanceof LightningBolt)
            return false;
        return original.call(entity);
    }
}
