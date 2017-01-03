package sape.cheetroid.lib.database.ctable;

import java.lang.reflect.Field;
import java.util.ArrayList;

import sape.cheetroid.lib.database.cfield.CField;
import sape.cheetroid.lib.database.cfield.CFieldAnno;
import sape.cheetroid.lib.database.cfield.CPrimaryKey;
import sape.cheetroid.lib.main.CModel;
import sape.cheetroid.lib.exception.CCustomException;

public class CTable
{

    //------------- Static values -----------------
    private static final String CREATE_TABLE = "CREATE TABLE";
    //----------------------------------------------

    private ArrayList<CField> tableFields = new ArrayList<CField>();

    private Class myClass;
    private int version;

    private String tableName;

    private String tableScript;

    public CTable( Class myClass )
    {

        this.myClass = myClass;
        this.version = CModel.getTableVersion( this.myClass );
        this.tableName = CModel.getTableName( this.myClass );

        this.createTableScript();

    }

    public String getCreateTableScript()
    {

        return tableScript;

    }

    private void createTableScript()
    {

        prepareFields( myClass.getFields() );

        tableScript = "";

        if( tableName == null )
            throw new CCustomException( "There is no table name annotation for the Class " + myClass.getName() + "." );

        tableScript += CREATE_TABLE + " " + tableName + " ( ";

        for( int i = 0; i < tableFields.size(); i++ )
        {

            tableScript += tableFields.get( i ).getFieldScript();

            if( i == ( tableFields.size() - 1 ) )
                tableScript += ");";
            else
                tableScript += ", ";

        }

    }

    private void prepareFields( Field[] fields )
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

    private void addPrimaryKey( String fieldName )
    {

        tableFields.add( new CField( fieldName ) );

    }

    private void addField( String fieldName, String fieldType, boolean isNotNull )
    {

        tableFields.add( new CField( fieldName, fieldType, isNotNull ) );

    }

    public int getVersion(){ return this.version; }

    public String getTableName() { return this.tableName; }

    public ArrayList<CField> getTableFields() { return this.tableFields; }

}