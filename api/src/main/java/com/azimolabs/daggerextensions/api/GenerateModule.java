package com.azimolabs.daggerextensions.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class marked with this annotation will have generated Dagger Module with binding methods (@Bind)
 * <p>
 * {@link #includes()} - which class should be included to generated module,
 * when NotSpecified, then nothing will be included
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GenerateModule {
    Class<?> includes() default NotSpecified.class;
}