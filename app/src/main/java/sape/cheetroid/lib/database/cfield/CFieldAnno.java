package sape.cheetroid.lib.database.cfield;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CFieldAnno
{

    String fieldType();
    boolean isNotNull();
    int version() default 1;

}