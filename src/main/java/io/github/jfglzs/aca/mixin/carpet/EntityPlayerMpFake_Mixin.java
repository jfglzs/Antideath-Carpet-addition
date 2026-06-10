package io.github.jfglzs.aca.mixin.carpet;

import carpet.patches.EntityPlayerMPFake;
import io.github.jfglzs.aca.accessors.EntityPlayerMPFakeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityPlayerMPFake.class)
public class EntityPlayerMpFake_Mixin implements EntityPlayerMPFakeAccessor {
    @Unique
    private boolean isLocked = false;


    @Override
    public void aca$setLockState(boolean locked) {
        this.isLocked = locked;
    }

    @Override
    public boolean aca$getLockState() {
        return isLocked;
    }
}
