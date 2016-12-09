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

    public boolean hasData = false;

    public CModel() {}

    public CModel( long keyValue, Context context )
    {

        DatabaseConnector databaseConnector = new DatabaseConnector( context );

        databaseConnector.open();

        Cursor cursor = databaseConnector.getDatabase().query(
                CModel.getTableName( this.getClass() ),
                null,
                CModel.getPrimartyKeyName( this.getClass() ) + " = " + keyValue,
                null,
                null,
                null,
                null );

        if( cursor != null && cursor.moveToFirst() )
        {

            HashMap<String, String> hashMap = Util.cursorToHashMap( cursor );

            this.fromHashMap( hashMap );

        }

        databaseConnector.close();

    }

    public CModel( HashMap<String, String> hashMap )
    {

        this.fromHashMap( hashMap );

    }

    public CModel( JSONObject jsonObject )
    {

        for( Field field : this.getClass().getFields() )
        {

            try
            {

                if( jsonObject.has( field.getName() ) )
                {

                    hasData = true;

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
                CModel.getTableName( this.getClass() ),
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
                CModel.getTableName( this.getClass() ),
                this.toContentValues( true ),
                CModel.getPrimartyKeyName( this.getClass() ) + " = " + this.getPrimaryKeyValue(),
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
                CModel.getTableName( this.getClass() ),
                 CModel.getPrimartyKeyName( this.getClass() ) + " = " + this.getPrimaryKeyValue(),
                null);

        databaseConnector.close();

        if( deletedSuccessfully > 0 )
            return true;
        else
            return false;

    }

    private boolean hasPrimaryKeyValue()
    {

        if( getPrimaryKeyValue() == 0 )
            return false;
        else
            return true;

    }

    public static String getPrimartyKeyName( Class myModel )
    {

        Field[] fields = myModel.getFields();

        for( Field field : fields )
        {

            if( field.isAnnotationPresent( CPrimaryKey.class ) )
                return field.getName();

        }

        return null;

    }

    public static String getTableName( Class myModel )
    {

        if( myModel.isAnnotationPresent( CTableAnno.class ) )
        {

            CTableAnno annoTable = (CTableAnno) myModel.getAnnotation( CTableAnno.class );
            return annoTable.tableName();

        }

        return null;

    }

    private void setValueToPrimaryKey( long value )
    {

        try
        {

            Field field = this.getClass().getDeclaredField( CModel.getPrimartyKeyName( this.getClass() ) );

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

    private long getPrimaryKeyValue()
    {

        try
        {

            Field field = this.getClass().getDeclaredField( CModel.getPrimartyKeyName( this.getClass() ) );

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

    private void fromHashMap( HashMap<String, String> hashMap )
    {

        for( Field field : this.getClass().getFields() )
        {

            if( hashMap.containsKey( field.getName() ) )
            {

                hasData = true;

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

}