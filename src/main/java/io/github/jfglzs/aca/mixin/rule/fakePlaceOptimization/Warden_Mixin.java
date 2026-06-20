package io.github.jfglzs.aca.mixin.rule.fakePlaceOptimization;
import net.minecraft.world.entity.EntityType;
//? if >= 26.2 {
/*import net.minecraft.world.entity.EntityTypes;
*///?}
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.EntityAccessor;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Warden.class)
public class Warden_Mixin extends Monster implements EntityAccessor {
    @Unique
    private int count = -1;

    protected Warden_Mixin(EntityType<? extends Monster> entityType, ServerLevel world) {
        super(entityType, world);
    }

    @Inject(
            method = "customServerAiStep",
            at = @At("HEAD")
    )
    //? if > 1.21.1 {
    protected void mobTick_Inject(ServerLevel world, CallbackInfo ci) {
     //?} else {
    /*protected void mobTick_Inject(CallbackInfo ci) {
    *///?}
        if (!AcaSetting.fakePeaceOptimization) return;

        if ((this.tickCount + this.getId() % 13) % 200 == 0 || count == -1) {
            Warden entity = (Warden) ((Object) this);

            AABB box = new AABB(
                    entity.position().add(0.5, 0.5, 0.5),
                    entity.position().add(-0.5, -0.5, -0.5)
            );

            //~ if < 26.2 'EntityTypes' -> 'EntityType' {
            count = entity.level().getEntities(EntityType.WARDEN, box, e -> true).size();
            //~}
        }
    }

    @Override
    public int aca$getCount() {
        return count;
    }
}
