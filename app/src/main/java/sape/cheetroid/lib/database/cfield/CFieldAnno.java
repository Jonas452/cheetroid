package sape.cheetroid.lib.database.cfield;

public @interface CFieldAnno
{

    String fieldType();
    boolean isNotNull();
    int version() default 1;

}