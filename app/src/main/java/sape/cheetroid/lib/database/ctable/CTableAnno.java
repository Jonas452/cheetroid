package sape.cheetroid.lib.database.ctable;

public @interface CTableAnno
{

    String tableName();
    int version() default 1;

}