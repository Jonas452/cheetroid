package sape.cheetroid.app.database;

import sape.cheetroid.app.model.Usuario;
import sape.cheetroid.lib.database.ctable.CTable;
import sape.cheetroid.lib.database.ctable.CTablesHandler;

public class DBCTables extends CTablesHandler
{

    /*
     * Here you must add
     * all yours tables.
     */
    public DBCTables()
    {

        addTableScript( CTable.getTableScript( Usuario.class ) );

    }

}