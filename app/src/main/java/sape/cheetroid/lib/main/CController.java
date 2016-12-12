package sape.cheetroid.lib.main;

import android.content.Context;
import android.database.Cursor;

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

    }

    /*
    METHOD = selectAll;
    SUMMAY = Select all the data from the table attached to the controller;

    PARAMETERS
    whereClause = The filters of the select (null to no filter);
    orderBy = The orderBy params (null to no order);
    */
    public ArrayList<Object> selectAll( String whereClause, String orderBy )
    {

        databaseConnector.open();

        Cursor cursor = databaseConnector.getDatabase().query(
                tableName,
                null,
                whereClause,
                null,
                null,
                null,
                orderBy );

        ArrayList<Object> arrayListMyModel= cursorToArrayListMyModel( cursor );

        databaseConnector.close();

        return arrayListMyModel;

    }

    private ArrayList<Object> cursorToArrayListMyModel( Cursor cursor )
    {

        ArrayList<Object> myModel = new ArrayList<Object>();

        return null;

    }

}