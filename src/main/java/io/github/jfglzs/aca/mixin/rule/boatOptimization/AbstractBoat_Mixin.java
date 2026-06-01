package io.github.jfglzs.aca.mixin.rule.boatOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.IVehicleAccessor;
import net.minecraft.world.entity.Entity;
//? if < 1.21.11 && > 1.21.1 {
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.AbstractBoat;
//?} else if > 1.21.10 {
/*import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
*///?} else {
/*import net.minecraft.world.entity.vehicle.Boat;
*///?}

//? if > 1.21.5 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


//~ if <= 1.21.1 'AbstractBoat' -> 'Boat' {
@Mixin(AbstractBoat.class)
//~}
public class AbstractBoat_Mixin implements IVehicleAccessor {
    @Unique
    private int rideCount = 0;

    @Unique
    private static String RIDE_COUNT = "RideCount";

    @Unique
    private Boat.Status lastStatus;

    @WrapOperation(
            method = "tick",
            //? if < 1.21.4 {
            /*at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;checkInsideBlocks()V")
            *///?} else < 26.1 && != 1.21.11 {
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractBoat;applyEffectsFromBlocks()V")
            //?} else {
            /*at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;applyEffectsFromBlocks()V")
            *///?}
    )
    //? if < 1.21.4 {
    /*public void tick(Boat instance, Operation<Void> original) {
    *///?} else {
     public void tick(AbstractBoat instance, Operation<Void> original) {
    //?}
        if (!AcaSetting.boatOptimization && rideCount < 10) {
            original.call(instance);
        }
    }

    @Inject(
            method = "isUnderwater",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void isUnderwater_Cache(CallbackInfoReturnable<Boat.Status> cir) {
        if (AcaSetting.boatOptimization && rideCount > 10 && this.lastStatus != null) {
            cir.setReturnValue(lastStatus);
        }
    }

    @Inject(
            method = "isUnderwater",
            at = @At("RETURN")
    )
    private void isUnderwater(CallbackInfoReturnable<Boat.Status> cir) {
        if (AcaSetting.boatOptimization && lastStatus == null) {
            this.lastStatus = cir.getReturnValue();
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;startRiding(Lnet/minecraft/world/entity/Entity;)Z")
    )
    public boolean startRiding(Entity instance, Entity entity, Operation<Boolean> original) {
        var bl = original.call(instance, entity);
        if (bl) rideCount++;
        return bl;
    }

    @Override
    public int aca$getRideCount() {
        return rideCount;
    }

    //? if > 1.21.5 {
    @Inject(
            method = "addAdditionalSaveData",
            at = @At("HEAD")
    )
    protected void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        output.putInt(RIDE_COUNT, rideCount);
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("HEAD")
    )
    protected void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
        rideCount = input.getInt(RIDE_COUNT).orElse(0);
    }
    //?} else {
    /*@Inject(
            method = "addAdditionalSaveData",
            at = @At("HEAD")
    )
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putInt(RIDE_COUNT, rideCount);
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("HEAD")
    )
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        //? if > 1.21.4 {
        rideCount = compoundTag.getInt(RIDE_COUNT).orElse(0);
        //?} else {
        /^rideCount = compoundTag.getInt(RIDE_COUNT);
        ^///?}
    }
    *///?}
}
