package io.github.jfglzs.aca.mixin.rule.blockEntityIterationOptimization;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Level.class)
public abstract class Level_Mixin {
    @Shadow
    @Final
    protected List<TickingBlockEntity> blockEntityTickers;

    @Shadow
    public abstract boolean shouldTickBlocksAt(BlockPos pos);

    @Shadow
    public abstract TickRateManager tickRateManager();

    @Inject(
            method = "tickBlockEntities",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;tickingBlockEntities:Z",
                    opcode = Opcodes.PUTFIELD
            ),
            cancellable = true
    )
    private void tickBlockEntities(CallbackInfo ci) {
        if (! AcaSetting.blockEntityIterationOptimization)
            return;

        int write = 0;
        int size = blockEntityTickers.size();
        boolean tickBlockEntities = this.tickRateManager().runsNormally();

        for (int read = 0; read < size; read++) {
            TickingBlockEntity ticker = blockEntityTickers.get(read);

            if (ticker.isRemoved())
                continue;

            if (write != read)
                blockEntityTickers.set(write, ticker);

            write++;

            if (tickBlockEntities && this.shouldTickBlocksAt(ticker.getPos())) {
                ticker.tick();
            }
        }

        if (write != size) {
            blockEntityTickers.subList(write, size).clear();
        }

        ci.cancel();
    }
}
