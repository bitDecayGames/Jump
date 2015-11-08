package com.bitdecay.jump.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Monday on 11/8/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueRange {
    int min();
    int max();
}
