package sape.cheetroid.lib.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

import sape.cheetroid.lib.database.DatabaseConnector;
import sape.cheetroid.lib.database.cfield.CFieldAnno;
import sape.cheetroid.lib.database.cfield.CPrimaryKey;
import sape.cheetroid.lib.database.ctable.CTableAnno;
import sape.cheetroid.lib.util.Util;

public class CModel
{

    private String tableName;
    private String primaryKeyName;

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

            HashMap<String, String> hashMap = Util.cursorToHashMap( cursor );

            this.fromHashMap( hashMap );

        }else
        {

            this.setPrimaryKeyToNegative();

        }

        databaseConnector.close();

    }

    public CModel( HashMap<String, String> hashMap )
    {

        setTableName();
        setPrimaryKeyName();

        this.fromHashMap( hashMap );

    }

    public CModel( JSONObject jsonObject )
    {

        setTableName();
        setPrimaryKeyName();

        for( Field field : this.getClass().getFields() )
        {

            try
            {

                if( jsonObject.has( field.getName() ) )
                {

                    if( int.class == field.getType() )
                    {

                        field.setInt( this, jsonObject.getInt( field.getName() ) );

                    }else if( long.class == field.getType() )
                    {

                        field.setLong( this, jsonObject.getLong( field.getName() ) );

                    }else if( double.class == field.getType() )
                    {

                        field.setDouble( this, jsonObject.getDouble( field.getName() ) );

                    }else if( boolean.class == field.getType() )
                    {

                        field.setBoolean( this, jsonObject.getBoolean( field.getName() ) );

                    }else if( String.class == field.getType() )
                    {

                        field.set( this, jsonObject.getString( field.getName() ) );

                    }

                }

            }catch( IllegalAccessException e )
            {

                e.printStackTrace();

            }catch( JSONException e )
            {

                e.printStackTrace();

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

    private void fromHashMap( HashMap<String, String> hashMap )
    {

        for( Field field : this.getClass().getFields() )
        {

            if( hashMap.containsKey( field.getName() ) )
            {

                try
                {

                    String value = hashMap.get( field.getName() );

                    if( int.class == field.getType() )
                    {

                        field.setInt( this, Integer.valueOf( value ) );

                    }else if( long.class == field.getType() )
                    {

                        field.setLong( this, Long.valueOf( value ) );

                    }else if( double.class == field.getType() )
                    {

                        field.setDouble( this, Double.valueOf( value ) );

                    }else if( String.class == field.getType() )
                    {

                        field.set( this, value );

                    }else if( boolean.class == field.getType() )
                    {

                        field.set( this, Boolean.valueOf( value ) );

                    }

                } catch( IllegalAccessException e )
                {

                    e.printStackTrace();

                }

            }

        }

    }

    //------------- BEGIN DATABASE METHODS -------------------

    public boolean store( Context context )
    {

        boolean wasStore;

        if( hasPrimaryKeyValue() )
            wasStore = update( context );
        else
            wasStore = insert( context );

        return wasStore;

    }

    private boolean insert( Context context )
    {

        long insertedSuccessfully;

        DatabaseConnector databaseConnector = new DatabaseConnector( context );

        ContentValues contentValues = this.toContentValues( false );

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

    public boolean delete(  Context context  )
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

    public static String getPrimaryKeyName(Class myModel )
    {

        Field[] fields = myModel.getFields();

        for( Field field : fields )
        {

            if( field.isAnnotationPresent( CPrimaryKey.class ) )
                return field.getName();

        }

        return null;

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

}