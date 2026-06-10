package io.github.jfglzs.aca.mixin.rule.fixNbtFold;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.nbt.TextComponentTagVisitor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TextComponentTagVisitor.class)
public class TextComponentTagVisitor_Mixin {
    @ModifyConstant(
            method = "visitCompound",
            constant = @Constant(intValue = 64)
    )
    public int visitCompound(int constant) {
        return AcaSetting.fixNbtFold ? 10240 : constant;
    }

    @ModifyConstant(
            method = "visitList",
            constant = @Constant(intValue = 128)
    )
    public int visitList_1(int constant) {
        return AcaSetting.fixNbtFold ? 10240 : constant;
    }
}
