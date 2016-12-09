package sape.cheetroid.lib.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.HashMap;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 02/10/2016 (m/d/y);

Description =
The class with many utilities.
*/
public class Util
{

    private Util() {}

    /*
	 METHOD = isNullOrWhiteSpace;

	 SUMMAY = verify is a String is null or white space.

     PARAMETERS
     text = the String to be tested;
	 */
    public static boolean isNullOrWhiteSpace( String text )
    {

        return text != null && text.trim().length() > 0 ? false : true;

    }

    /*
    METHOD = testConnection;
    SUMMAY = Test the connection of the device;

    PARAMETERS
    context = The context of the application;
    */
    public static boolean testConnection( Context context )
    {

        boolean connected = false;

        ConnectivityManager connectivityManager = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if( activeNetwork != null )
        {

            if( activeNetwork.getType() == ConnectivityManager.TYPE_WIFI )
            {

                connected = true;

            }

            if( activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE )
            {

                connected = true;

            }

        }

        return connected;

    }

    /*
    METHOD = cursorToArrayListOfHashMaps;
    SUMMAY = Transform a cursor into a arraylist of hashmaps;

    PARAMETERS
    cursor = The curso that is going to be transformed;
    */
    public static ArrayList< HashMap<String, String> > cursorToArrayListOfHashMaps(Cursor cursor )
    {

        if( cursor != null && cursor.moveToFirst() )
        {

            ArrayList< HashMap<String, String> > arrayListOfHashMaps = new ArrayList< HashMap<String, String> >();

            for( int i = 0; i < cursor.getCount(); i++ )
            {

                cursor.moveToPosition( i );
                arrayListOfHashMaps.add( Util.cursorToHashMap( cursor ) );

            }

            return arrayListOfHashMaps;

        }else
        {

            return null;

        }

    }

    /*
    METHOD = cursorToHashMap;
    SUMMAY = Transform a specific opsition of the cursor into a hashmap;

    PARAMETERS
    cursor = The curso that is going to be transformed;
    */
    public static HashMap< String, String> cursorToHashMap( Cursor cursor )
    {

        if( cursor != null )
        {

            HashMap<String, String> hashMap = new HashMap<String, String>();

            for (int i = 0; i < cursor.getColumnCount(); i++)
            {

                hashMap.put( cursor.getColumnName(i), cursor.getString(i) );

            }

            return hashMap;

        }else
        {

            return null;

        }

    }

}