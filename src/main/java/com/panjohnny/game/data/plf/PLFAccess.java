package com.panjohnny.game.data.plf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used only for constructors. To determine which data should be exported. (which constructor should be used)
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface PLFAccess {
    AccessMode mode() default AccessMode.GETTERS;

    /**
     * Getter prefix, if the first letter should be uppercase use {@code ⌃} (e. g. getSomething -> get⌃).
     * Use {@code ↑} if everything should be uppercase after prefix (e. g. getRGB -> get↑).
     */
    String getterPrefix() default "get⌃";

    /**
     * Used with CUSTOM_GETTERS, use empty string ({@code ""}) to use the default {@link #getterPrefix()}
     *
     * @see #getterPrefix() for the variants
     */
    String[] specialGetterPrefixes() default {};

    /**
     * Used to provide parameter names
     */
    String[] paramNames();

    enum AccessMode {
        GETTERS,
        FIELDS,
        CUSTOM_GETTERS
    }
}
