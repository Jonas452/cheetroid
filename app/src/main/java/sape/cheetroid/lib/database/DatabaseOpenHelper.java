package sape.cheetroid.lib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

        DBCTables tables = DBCTables.getInstance();

        for( CTable table : tables.DATABASE_TABLES )
            db.execSQL( table.getTableScript() );

    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {

        DBCTables tables = DBCTables.getInstance();

        int versionExecuteUpdate = oldVersion + 1;

        do
        {

            for( CTable table : tables.getAllTablesFromVersion( versionExecuteUpdate ) )
                db.execSQL(table.getTableScript());

            versionExecuteUpdate++;

        }while( versionExecuteUpdate <= newVersion );

    }

}