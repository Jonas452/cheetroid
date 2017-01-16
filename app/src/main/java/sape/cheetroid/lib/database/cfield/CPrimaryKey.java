package sape.cheetroid.lib.database.cfield;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CPrimaryKey
{

    boolean isPrimaryKey() default true;

}