package sape.cheetroid.lib.database.cfield;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 27/10/2016 (m/d/y);

Description =
Class to generate field scripts for a table.
*/
public class CField
{

    //------------- Static values -----------------
    public static final String TEXT = "TEXT";
    public static final String INTEGER = "INTEGER";
    public static final String REAL = "REAL";
    public static final String BOLB = "BOLB";

    public static final String NOT_NULL = "NOT NULL";

    private static final String PRIMARY_KEY_AUTOINCREMENT = "PRIMARY KEY AUTOINCREMENT";
   //------------------------------------------------

    private String fieldName;
    private String fieldType;

    private boolean isPrimaryKey;
    private boolean isNotNull;

    /*
    METHOD = CField;
    SUMMAY = The constructor of the class, only for commum fields.;

    PARAMETERS

    fieldName = The name of the field;
    fieldType = The type of the field;
    isNotNull = If is not null;
    */
    public CField( String fieldName, String fieldType, boolean isNotNull )
    {

        this.fieldName = fieldName;
        this.fieldType = fieldType;

        this.isNotNull = isNotNull;

        this.isPrimaryKey = false;

    }

    /*
    METHOD = CField;
    SUMMAY = The constructor of the class, only for creating the primary key.;

    PARAMETERS

    fieldName = The name of the field;
    */
    public CField( String fieldName )
    {

        this.fieldName = fieldName;
        this.fieldType = INTEGER;

        this.isNotNull = false;

        this.isPrimaryKey = true;

    }

    /*
    METHOD = getFieldScript;
    SUMMAY = Generate the script from the fields informed;
    */
    public String getFieldScript()
    {

        String fieldScript = "";

        fieldScript += this.fieldName + " ";
        fieldScript += this.fieldType + " ";

        if( isPrimaryKey )
            fieldScript += PRIMARY_KEY_AUTOINCREMENT + " ";

        if( isNotNull )
            fieldScript += NOT_NULL + " ";

        return fieldScript;

    }

    private boolean isFieldTypeValid( String fieldType )
    {

        boolean isValid = false;

        if( fieldType.equals( TEXT ) )
            isValid = true;
        else if( fieldType.equals( INTEGER ) )
            isValid = true;
        else if( fieldType.equals( REAL ) )
            isValid = true;
        else if( fieldType.equals( BOLB ) )
            isValid = true;

        return isValid;

    }

}