package sape.cheetroid.app.database;

import sape.cheetroid.app.model.Casa;
import sape.cheetroid.app.model.Cidade;
import sape.cheetroid.app.model.Estado;
import sape.cheetroid.app.model.Usuario;
import sape.cheetroid.lib.database.ctable.CTable;
import sape.cheetroid.lib.database.ctable.CTablesHandler;

public class DBCTables extends CTablesHandler
{

    private static DBCTables DBCTABLES_INSTANCE = null;

    //TODO Here you must add all yours tables.
    private DBCTables()
    {

        addTable( new CTable( Usuario.class ) );
        addTable( new CTable( Cidade.class ) );
        addTable( new CTable( Estado.class ) );
        addTable( new CTable( Casa.class ) );

    }

    public static DBCTables getInstance()
    {

        if( DBCTABLES_INSTANCE == null )
        {

            DBCTABLES_INSTANCE = new DBCTables();

        }

        return DBCTABLES_INSTANCE;

    }

}