package com.ensarsarajcic.neovim.java.pluginhost.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NeovimCommand {
    String value();
    String nargs() default "0";
    String complete() default "";
    String description() default "";
    boolean enableRange() default false;
    String range() default "";
    String count() default "";
    String addr() default "";
    boolean force() default true;
    boolean bang() default false;
    boolean bar() default false;
    boolean register() default false;
    boolean keepScript() default false;
    boolean sync() default false;
}
