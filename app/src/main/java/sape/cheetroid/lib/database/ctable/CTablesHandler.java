package sape.cheetroid.lib.database.ctable;

import java.util.ArrayList;

public class CTablesHandler
{

    public static ArrayList<String> DATABASE_TABLES_SCRIPTS = new ArrayList<String>();

    public void addTableScript( String tableScript )
    {

        DATABASE_TABLES_SCRIPTS.add( tableScript );

    }

}