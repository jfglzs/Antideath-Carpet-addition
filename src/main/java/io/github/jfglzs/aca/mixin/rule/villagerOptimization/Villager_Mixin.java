package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import io.github.jfglzs.aca.utils.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
//? if > 1.21.5 {
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public class Villager_Mixin implements VillagerAccessor {
    @Unique
    private static final String VILLAGER_COUNT = "VillagerCount";
    @Unique
    private static final String GOLEM_COUNT = "GolemCount";
    @Unique
    private int count = 0;
    @Unique
    private int golemCount = 0;

    @Inject(
            method = "customServerAiStep",
            at = @At("HEAD")
    )
    //? if > 1.21.1 {
    protected void mobTick_Inject(ServerLevel world, CallbackInfo ci) {
    //?} else {
    /*protected void mobTick_Inject(CallbackInfo ci) {
    *///?}
        if (AcaSetting.villagerOptimization) {
            Villager entity = (Villager) ((Object) this);

            if ((entity.tickCount + entity.getId() % 807) % 400 == 0 && !entity.isSleeping()) {

                AABB box = new AABB(
                        EntityUtils.getEntityPos(entity).add(0.5, 0.5, 0.5),
                        EntityUtils.getEntityPos(entity).add(-0.5, -0.5, -0.5)
                );

                count = entity.level().getEntities(EntityType.VILLAGER, box, e -> true).size();
            }
        }
    }

    @Override
    public boolean aca$canDisableAI() {
//        return count == 3 && golemCount > 1;
        return true;
    }

    @Inject(
            method = "spawnGolemIfNeeded",
            at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V")
    )
    private void foreach_Inject(ServerLevel world, long time, int requiredCount, CallbackInfo ci) {
        this.golemCount++;
    }

    //? if > 1.21.5 {
    @Inject(
            method = "addAdditionalSaveData",
            at = @At("HEAD")
    )
    protected void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        output.putInt(VILLAGER_COUNT, count);
        output.putInt(GOLEM_COUNT, golemCount);
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("HEAD")
    )
    protected void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
        count = input.getInt(VILLAGER_COUNT).orElse(0);
        golemCount = input.getInt(GOLEM_COUNT).orElse(0);
    }
    //?} else {
    /*@Inject(
            method = "addAdditionalSaveData",
            at = @At("HEAD")
    )
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putInt(GOLEM_COUNT, golemCount);
        compoundTag.putInt(VILLAGER_COUNT, count);
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("HEAD")
    )
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        //? if > 1.21.4 {
        count = compoundTag.getInt(VILLAGER_COUNT).orElse(0);
        golemCount = compoundTag.getInt(GOLEM_COUNT).orElse(0);
        //?} else {
        /^count = compoundTag.getInt(VILLAGER_COUNT);
        golemCount = compoundTag.getInt(GOLEM_COUNT);
        ^///?}
    }
    *///?}
}
