package sape.cheetroid.lib.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import sape.cheetroid.app.database.DBCTables;
import sape.cheetroid.lib.database.cfield.CField;
import sape.cheetroid.lib.database.ctable.CTable;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 26/10/2016 (m/d/y);

Description =
The local database tables register.
*/
public class DatabaseOpenHelper extends SQLiteOpenHelper
{

    private final String ALTER_TABLE = "ALTER TABLE ";
    private final String ADD_COLUMN = " ADD COLUMN ";

    private Context context;

    public DatabaseOpenHelper( Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int version )
    {

        super( context, dataBaseName, factory, version );

        this.context = context;

    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {

        DBCTables tables = DBCTables.getInstance();

        for( CTable table : tables.DATABASE_TABLES )
            db.execSQL( table.getCreateTableScript() );

    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {

        DBCTables tables = DBCTables.getInstance();

        int versionExecuteUpdate = oldVersion + 1;

        do
        {

            for( CTable table : tables.getAllTablesFromVersion( versionExecuteUpdate ) )
            {

                String tableName = table.getTableName();

                if( doesTableExist( db, tableName ) )
                {

                    for( CField field : table.getTableFields() )
                    {

                        if( !doesFieldExistInTable( db, tableName, field.getName() ) )
                        {

                            db.execSQL( ALTER_TABLE + tableName + ADD_COLUMN + field.getFieldScript() + ";" );

                        }

                    }

                }else
                {

                    db.execSQL( table.getCreateTableScript() );

                }

            }

            versionExecuteUpdate++;

        }while( versionExecuteUpdate <= newVersion );

    }

    private boolean doesTableExist( SQLiteDatabase db, String tableName )
    {

        boolean tableExist = false;

        Cursor cursor = db.rawQuery(
                "SELECT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'", null );

        if( cursor != null && cursor.getCount() > 0 )
            tableExist = true;

        return tableExist;

    }

    private boolean doesFieldExistInTable( SQLiteDatabase db, String tableName, String fieldName )
    {

        boolean fieldExist = false;

        Cursor cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 1", null );

        if( cursor != null &&
            cursor.moveToFirst() &&
            cursor.getColumnIndex( fieldName ) != -1 )
            fieldExist = true;

        return fieldExist;

    }

}