package io.github.jfglzs.aca.utils.validator;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import carpet.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

public class NonZeroValidator extends Validator<Integer> {
    @Override
    public Integer validate(@Nullable ServerCommandSource source, CarpetRule<Integer> rule, Integer integer, String s) {
        if (integer < 0) {
            Messenger.m(source, "r The value must be greater than 0");
            return null;
        }
        return integer;
    }
}
