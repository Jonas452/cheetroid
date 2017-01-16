package sape.cheetroid.lib.database.ctable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CTableAnno
{

    String tableName();
    int version() default 1;

}