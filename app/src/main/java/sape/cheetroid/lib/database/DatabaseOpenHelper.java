package sape.cheetroid.lib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sape.cheetroid.app.database.DBCTables;
import sape.cheetroid.lib.database.ctable.CTable;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 26/10/2016 (m/d/y);

Description =
The local database tables register.
*/
public class DatabaseOpenHelper extends SQLiteOpenHelper
{

    public DatabaseOpenHelper( Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int version )
    {

        super( context, dataBaseName, factory, version );

    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {

        DBCTables tables = new DBCTables();

        for( String tableScript : tables.DATABASE_TABLES_SCRIPTS )
            db.execSQL( tableScript );

    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {


    }

}