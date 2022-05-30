package com.ensarsarajcic.neovim.java.pluginhost.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NeovimAutocommand {
    String[] value();
    String group() default "";
    String pattern() default "*";
    String description() default "";
    boolean once() default false;
    boolean nested() default false;
    boolean sync() default false;
}
