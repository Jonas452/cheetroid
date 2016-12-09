package sape.cheetroid.lib.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

import sape.cheetroid.lib.util.Util;
import sape.cheetroid.lib.database.DatabaseConnector;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 02/10/2016 (m/d/y);
*/
public class CController
{

    private DatabaseConnector databaseConnector;

    private String tableName;
    private String primaryKeyName;

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

        this.tableName = CModel.getTableName( myModel );

        this.primaryKeyName = CModel.getPrimartyKeyName( myModel );

    }

    /*
    METHOD = selectAll;
    SUMMAY = Select all the data from the table attached to the controller;

    PARAMETERS
    whereClause = The filters of the select (null to no filter);
    orderBy = The orderBy params (null to no order);
    */
    public ArrayList<HashMap<String, String>> selectAll( String whereClause, String orderBy )
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

        ArrayList<HashMap<String, String>> arrayListHashMap = Util.cursorToArrayListOfHashMaps( cursor );

        databaseConnector.close();

        return arrayListHashMap;

    }

}