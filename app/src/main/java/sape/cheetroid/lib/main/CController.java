package sape.cheetroid.lib.main;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import sape.cheetroid.lib.database.DatabaseConnector;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 02/10/2016 (m/d/y);
*/
public class CController
{

    private DatabaseConnector databaseConnector;

    private String tableName;

    private Class myModel;

    /*
    METHOD = CController;
    SUMMAY = The constructor of the class;

    PARAMETERS

    context = The context used to create the connection with the database;
    tableName = The table name that actions will occur;
    primaryKeyName = The primary key name that is going to be used in delete, update and select fucntions;
    */
    public CController(Context context, Class myModel )
    {

        databaseConnector = new DatabaseConnector( context );

        this.myModel = myModel;

        this.tableName = CModel.getTableName( this.myModel );

    }

    /*
    METHOD = selectAll;
    SUMMAY = Select all the data from the table attached to the controller;

    PARAMETERS
    whereClause = The filters of the select (null to no filter);
    orderBy = The orderBy params (null to no order);
    */
    public ArrayList<Object> selectAll( String whereClause, String orderByClause )
    {

        databaseConnector.open();

        Cursor cursor = databaseConnector.getDatabase().query(
                tableName,
                null,
                whereClause,
                null,
                null,
                null,
                orderByClause );

        ArrayList<Object> arrayListMyModel= cursorToArrayListMyModel( cursor );

        databaseConnector.close();

        return arrayListMyModel;

    }

    private ArrayList<Object> cursorToArrayListMyModel( Cursor cursor )
    {

        ArrayList<Object> myModelArrayList = null;

        if( cursor != null && cursor.moveToFirst() )
        {

            myModelArrayList = new ArrayList<Object>();

            do
            {

                myModelArrayList.add( cursorToMyModel( cursor ) );

            }while( cursor.moveToNext() );

        }

        return myModelArrayList;

    }

    private Object cursorToMyModel( Cursor cursor )
    {

        Object myObject = newInstanceMyModel();

        Field[] myObjectFields = myObject.getClass().getFields();

        for( Field tempField : myObjectFields )
        {

            int fieldIndex = cursor.getColumnIndex( tempField.getName() );

            if( fieldIndex != - 1 )
            {

                try
                {

                    if( int.class == tempField.getType() )
                    {

                        tempField.setInt( myObject, cursor.getInt( fieldIndex ) );

                    }else if( long.class == tempField.getType() )
                    {

                        tempField.setLong( myObject, cursor.getLong( fieldIndex ) );

                    }else if( double.class == tempField.getType() )
                    {

                        tempField.setDouble( myObject, cursor.getDouble( fieldIndex ) );

                    }else if( String.class == tempField.getType() )
                    {

                        tempField.set( myObject, cursor.getString( fieldIndex ) );

                    }

                }catch( IllegalAccessException e )
                {

                    e.printStackTrace();

                }

            }

        }

        return myObject;

    }

    private Object newInstanceMyModel()
    {

        try
        {

            Constructor constructor = myModel.getConstructor();

            return constructor.newInstance();

        }catch( InstantiationException e )
        {

            e.printStackTrace();

        }catch( IllegalAccessException e )
        {

            e.printStackTrace();

        }catch( NoSuchMethodException e )
        {

            e.printStackTrace();

        }catch( InvocationTargetException e )
        {

            e.printStackTrace();

        }

        return null;

    }

}