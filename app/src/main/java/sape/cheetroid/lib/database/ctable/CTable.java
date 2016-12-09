package sape.cheetroid.lib.database.ctable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import sape.cheetroid.lib.database.cfield.CField;
import sape.cheetroid.lib.database.cfield.CFieldAnno;
import sape.cheetroid.lib.database.cfield.CPrimaryKey;
import sape.cheetroid.lib.main.CModel;

public class CTable
{

    //------------- Static values -----------------
    private static final String CREATE_TABLE = "CREATE TABLE";
    //----------------------------------------------

    private static ArrayList<CField> tableFields = new ArrayList<CField>();

    private CTable() {}

    public static String getTableScript( Class myClass )
    {

        prepareFields( myClass.getFields() );

        String tableScript = "";

        tableScript += CREATE_TABLE + " " + CModel.getTableName( myClass ) + " ( ";

        for( int i = 0; i < tableFields.size(); i++ )
        {

            tableScript += tableFields.get( i ).getFieldScript();

            if( i == ( tableFields.size() - 1 ) )
                tableScript += ");";
            else
                tableScript += ", ";

        }

        tableFields.clear();

        return tableScript;

    }

    private static void prepareFields( Field[] fields )
    {

        for( Field field : fields )
        {

            if( field.isAnnotationPresent( CPrimaryKey.class ) )
            {

                addPrimaryKey( field.getName() );

            }else if( field.isAnnotationPresent( CFieldAnno.class ) )
            {

                CFieldAnno annoField = field.getAnnotation( CFieldAnno.class );

                addField( field.getName(), annoField.fieldType(), annoField.isNotNull() );

            }

        }

    }

    private static void addPrimaryKey( String fieldName )
    {

        tableFields.add( new CField( fieldName ) );

    }

    private static void addField( String fieldName, String fieldType, boolean isNotNull )
    {

        tableFields.add( new CField( fieldName, fieldType, isNotNull ) );

    }

}