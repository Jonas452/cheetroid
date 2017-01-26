package sape.cheetroid.lib.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

import sape.cheetroid.lib.database.cfield.CFieldAnno;
import sape.cheetroid.lib.database.cfield.CPrimaryKey;
import sape.cheetroid.lib.database.ctable.CTableAnno;
import sape.cheetroid.lib.database.DatabaseConnector;
import sape.cheetroid.lib.exception.CCustomException;

public class CModel
{

    private String tableName;
    private String primaryKeyName;

    private ArrayList<View> childsView;

    public CModel()
    {

        setTableName();
        setPrimaryKeyName();

    }

    public CModel( long keyValue, Context context )
    {

        setTableName();
        setPrimaryKeyName();

        DatabaseConnector databaseConnector = new DatabaseConnector( context );

        databaseConnector.open();

        Cursor cursor = databaseConnector.getDatabase().query(
                tableName,
                null,
                primaryKeyName + " = " + keyValue,
                null,
                null,
                null,
                null );

        if( cursor != null && cursor.moveToFirst() )
        {

            for( Field tempField : this.getClass().getFields() )
            {

                int fieldIndex = cursor.getColumnIndex( tempField.getName() );

                if( fieldIndex != - 1 )
                    setFieldValue( tempField, cursor.getString( fieldIndex ) );

            }

        }else
        {

            this.setPrimaryKeyToNegative();

        }

        databaseConnector.close();

    }

    public CModel( JSONObject jsonObject )
    {

        setTableName();
        setPrimaryKeyName();

        for( Field field : this.getClass().getFields() )
        {

            if( field.isAnnotationPresent( CFieldAnno.class ) || field.isAnnotationPresent( CPrimaryKey.class ) )
            {

                try
                {

                    if( jsonObject.has( field.getName() ) )
                        setFieldValue( field, jsonObject.getString( field.getName() ) );

                }catch( JSONException e )
                {

                    e.printStackTrace();

                }

            }

        }

    }

    public CModel( LinearLayout linearLayout, Context context )
    {

        childsView = new ArrayList<View>();

        setTableName();
        setPrimaryKeyName();

        final int childCount = linearLayout.getChildCount();

        for( int i = 0; i < childCount; i++ )
        {

            final View myChild = linearLayout.getChildAt( i );
            Field field = null;

            String fieldName = treatFieldName( myChild, context );

            if( fieldName != null  )
                field = getFieldByName( fieldName );

            Log.e( "FIELD", fieldName + "" );

            if( field != null )
            {

                String value = String.valueOf( ( ( EditText ) myChild ).getText() );

                Log.e( "VALUE", value + "" );

                if( value != null && value.length() > 0 )
                {

                    setFieldValue( field, value );

                }else
                {

                    CFieldAnno annoField = field.getAnnotation( CFieldAnno.class );

                    if( annoField.isNotNull() )
                        childsView.add( linearLayout.getChildAt( i - 1 ) );

                }

            }

        }

    }

    public JSONObject toJSON()
    {

        JSONObject json = new JSONObject();

        for( Field field : this.getClass().getFields() )
        {

            try
            {

                if( field.isAnnotationPresent( CFieldAnno.class ) || field.isAnnotationPresent( CPrimaryKey.class ) )
                    json.put( field.getName(), String.valueOf( field.get( this ) ) );

            }catch( IllegalAccessException e )
            {

                e.printStackTrace();

            }catch (JSONException e)
            {

                e.printStackTrace();

            }

        }

        return json;

    }

    public ContentValues toContentValues( boolean withId )
    {

        ContentValues contentValues = new ContentValues();

        for( Field field : this.getClass().getFields() )
        {

            try
            {

                boolean isCFieldAnno = field.isAnnotationPresent( CFieldAnno.class );
                boolean isCPrimartyKeyAnno = field.isAnnotationPresent( CPrimaryKey.class );

                if( isCFieldAnno || ( isCPrimartyKeyAnno && withId ) )
                    contentValues.put( field.getName(), String.valueOf( field.get( this ) ) );

            }catch( IllegalAccessException e )
            {

                e.printStackTrace();

            }

        }

        return contentValues;

    }

    //------------- BEGIN DATABASE METHODS -------------------

    public boolean store( Context context )
    {

        boolean wasStore;

        if( hasPrimaryKeyValue() )
            wasStore = update( context );
        else
            wasStore = insert( context, false );

        return wasStore;

    }

    public boolean storeWithId( Context context )
    {

        boolean wasStore;

        wasStore = insert( context, true );

        return wasStore;

    }

    private boolean insert( Context context, boolean withId )
    {

        long insertedSuccessfully;

        DatabaseConnector databaseConnector = new DatabaseConnector( context );

        ContentValues contentValues = this.toContentValues( withId );

        databaseConnector.open();

        insertedSuccessfully = databaseConnector.getDatabase().insert(
                tableName,
                null,
                contentValues );

        databaseConnector.close();

        this.setValueToPrimaryKey( insertedSuccessfully );

        if( insertedSuccessfully >= 0 )
            return true;
        else
            return false;

    }

    private boolean update( Context context )
    {

        DatabaseConnector databaseConnector = new DatabaseConnector( context );

        long updatedSuccessfully;

        databaseConnector.open();

        updatedSuccessfully = databaseConnector.getDatabase().update(
                tableName,
                this.toContentValues( true ),
                primaryKeyName + " = " + this.getPrimaryKeyValue(),
                null);

        databaseConnector.close();

        if( updatedSuccessfully >= 0 )
            return true;
        else
            return false;

    }

    public boolean delete( Context context )
    {

        DatabaseConnector databaseConnector = new DatabaseConnector( context );

        long deletedSuccessfully;

        databaseConnector.open();

        deletedSuccessfully = databaseConnector.getDatabase().delete(
               tableName,
               primaryKeyName + " = " + this.getPrimaryKeyValue(),
               null);

        databaseConnector.close();

        if( deletedSuccessfully > 0 )
            return true;
        else
            return false;

    }

    //------------- END DATABASE METHODS ----------------------

    //------------- BEGIN TABLE HANDLING ----------------------

    public static String getTableName( Class myModel )
    {

        if( myModel.isAnnotationPresent( CTableAnno.class ) )
        {

            CTableAnno annoTable = (CTableAnno) myModel.getAnnotation( CTableAnno.class );
            return annoTable.tableName();

        }

        return null;

    }

    public static int getTableVersion( Class myModel )
    {

        if( myModel.isAnnotationPresent( CTableAnno.class ) )
        {

            CTableAnno annoTable = (CTableAnno) myModel.getAnnotation( CTableAnno.class );
            return annoTable.version();

        }

        return -1;

    }

    private long getPrimaryKeyValue()
    {

        try
        {

            Field field = this.getClass().getDeclaredField( primaryKeyName );

            return field.getLong( this );

        }catch ( NoSuchFieldException e )
        {

            e.printStackTrace();

        } catch (IllegalAccessException e)
        {

            e.printStackTrace();

        }

        return 0;

    }

    public static String getPrimaryKeyName( Class myModel )
    {

        Field[] fields = myModel.getFields();

        for( Field field : fields )
        {

            if( field.isAnnotationPresent( CPrimaryKey.class ) )
                return field.getName();

        }

        throw new CCustomException( "There is no primary key annotation for the Class " + myModel.getName() + "." );

    }

    private boolean hasPrimaryKeyValue()
    {

        if( getPrimaryKeyValue() > 0 )
            return true;
        else
            return false;

    }

    private void setValueToPrimaryKey( long value )
    {

        try
        {

            Field field = this.getClass().getDeclaredField( CModel.getPrimaryKeyName( this.getClass() ) );

            if( int.class == field.getType() )
            {

                field.setInt( this, (int) value );

            }else if( long.class == field.getType() ) {

                field.setLong( this, value );

            }

        }catch ( NoSuchFieldException e )
        {

            e.printStackTrace();

        } catch (IllegalAccessException e)
        {

            e.printStackTrace();

        }

    }

    private void setPrimaryKeyToNegative()
    {

        try
        {

            Field field = this.getClass().getDeclaredField( primaryKeyName );

            if( int.class == field.getType() )
            {

                field.setInt( this, (int) -1 );

            }else if( long.class == field.getType() )
            {

                field.setLong( this, -1 );

            }

        }catch( NoSuchFieldException e )
        {

            e.printStackTrace();

        }catch( IllegalAccessException e )
        {

            e.printStackTrace();

        }

    }

    private void setTableName()
    {

        this.tableName = getTableName( this.getClass() );

    }

    private void setPrimaryKeyName()
    {

        this.primaryKeyName = getPrimaryKeyName( this.getClass() );

    }

    //--------------- END TABLE HANDLING ---------------------

    //------------- BEGIN FORM HANDLING ----------------------

    private Field getFieldByName( String fieldName )
    {

        try
        {

            return this.getClass().getField( fieldName );

        }catch( NoSuchFieldException e )
        {

            e.printStackTrace();

        }

        return null;

    }

    private String treatFieldName( View view, Context context )
    {

        String fieldName = context.getResources().getResourceEntryName( view.getId() );

        fieldName = fieldName.toLowerCase();

        if( view instanceof EditText )
            return  fieldName.replace( ( tableName + "edittext" ).toLowerCase(), "" );
        else if( view instanceof TextView )
            ( ( TextView ) view ).setTextColor( Color.BLACK );

        return null;

    }

    private void setFieldValue( Field field, String value )
    {

        try
        {

            if( int.class == field.getType() )
            {

                field.setInt( this, Integer.parseInt( value ) );

            }else if( long.class == field.getType() )
            {

                field.setLong( this, Long.parseLong( value) );

            }else if( double.class == field.getType() )
            {

                field.setDouble( this, Double.parseDouble( value ) );

            }else if( boolean.class == field.getType() )
            {

                field.setBoolean( this, Boolean.parseBoolean( value ) );

            }else if( String.class == field.getType() )
            {

                field.set( this, value );

            }

        }catch( IllegalAccessException e )
        {

            e.printStackTrace();

        }

    }

    public boolean validate()
    {

        if( childsView.size() > 0 )
        {

            for( View view : childsView )
            {

                if( view instanceof TextView )
                    ( ( TextView ) view ).setTextColor( Color.RED );

            }

            return false;

        }

        return true;

    }

    //------------- END FORM HANDLING ----------------------

}