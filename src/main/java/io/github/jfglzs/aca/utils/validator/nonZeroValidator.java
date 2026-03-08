package io.github.jfglzs.aca.utils.validator;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import carpet.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

public class nonZeroValidator extends Validator<Integer> {
    @Override
    public Integer validate(@Nullable ServerCommandSource serverCommandSource, CarpetRule<Integer> carpetRule, Integer integer, String s) {
        if (integer < 0) {
            Messenger.m(serverCommandSource, "r The value must be bigger than 0");
            return 0;
        }
        return integer;
    }
}
