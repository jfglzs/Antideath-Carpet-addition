package io.github.jfglzs.aca.mixin.rule.disableSystemReportRecordHardware;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.SystemReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SystemReport.class)
public class SystemReport_Mixin {
    @Inject(
            method = "putHardware",
            at = @At("HEAD"),
            cancellable = true
    )
    private void putHardware(CallbackInfo ci) {
        if (AcaSetting.disableSystemReportRecordHardware)
            ci.cancel();
    }
}
