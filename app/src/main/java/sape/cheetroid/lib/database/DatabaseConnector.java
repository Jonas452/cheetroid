package sape.cheetroid.lib.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import sape.cheetroid.app.database.DatabaseConfig;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 26/10/2016 (m/d/y);

Description =
Helps to make connections to the local DATABASE_INSTANCE.
*/
public class DatabaseConnector
{

    private static SQLiteDatabase DATABASE_INSTANCE;
    private static DatabaseOpenHelper DATABASE_OPEN_HELPER;

    public DatabaseConnector( Context context )
    {

        DATABASE_OPEN_HELPER = new DatabaseOpenHelper( context, DatabaseConfig.NAME, null, DatabaseConfig.VERSION );

    }

    public void open() throws SQLException
    {

        if( DATABASE_INSTANCE == null || !DATABASE_INSTANCE.isOpen() )
            DATABASE_INSTANCE = DATABASE_OPEN_HELPER.getWritableDatabase();

    }

    public void close()
    {

        if( DATABASE_INSTANCE != null && DATABASE_INSTANCE.isOpen() )
        {

            DATABASE_INSTANCE.close();

        }

    }

    public SQLiteDatabase getDatabase() { return this.DATABASE_INSTANCE; }

}