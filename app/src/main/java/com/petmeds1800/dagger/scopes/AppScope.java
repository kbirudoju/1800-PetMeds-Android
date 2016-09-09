package com.petmeds1800.dagger.scopes;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by arthur on 04/22/16.
 */
@Scope
@Retention(RUNTIME)
public @interface AppScope {
}
