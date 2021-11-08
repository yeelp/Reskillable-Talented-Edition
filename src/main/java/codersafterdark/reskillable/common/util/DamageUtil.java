package codersafterdark.reskillable.common.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class DamageUtil {

    public static boolean attackEntityFrom(@Nonnull Entity attacked, @Nonnull DamageSource type, float amount) {
        return attacked.attackEntityFrom(type, amount);
    }

    public static boolean attackEntityFrom(@Nonnull Entity attacked, @Nonnull DamageSource type, float amount, @Nullable Entity newSource) {
        DamageSource newType = DamageSourceUtil.withEntityDirect(type, newSource);
        return attackEntityFrom(attacked, newType != null ? newType : type, amount);
    }
}
