package sape.cheetroid.lib.database.ctable;

import java.util.ArrayList;

public class CTablesHandler
{

    public static ArrayList<CTable> DATABASE_TABLES = new ArrayList<CTable>();

    public void addTable( CTable table )
    {

        DATABASE_TABLES.add( table );

    }

    public ArrayList<CTable> getAllTablesFromVersion( int version )
    {

        ArrayList<CTable> tables = new ArrayList<CTable>();

        for( CTable cTable : DATABASE_TABLES )
        {

            if( cTable.getVersion() == version )
            {

                tables.add( cTable );

            }

        }

        return tables;

    }

}