package sape.cheetroid.lib.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import sape.cheetroid.app.database.DatabaseConfig;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 26/10/2016 (m/d/y);

Description =
Helps to make connections to the local database.
*/
public class DatabaseConnector
{

    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;

    public DatabaseConnector( Context context )
    {

        databaseOpenHelper = new DatabaseOpenHelper( context, DatabaseConfig.NAME, null, DatabaseConfig.VERSION );

    }

    public void open() throws SQLException
    {

        database = databaseOpenHelper.getWritableDatabase();

    }

    public void close()
    {

        if( database != null )
        {

            database.close();

        }

    }

    public SQLiteDatabase getDatabase() { return this.database; }

}