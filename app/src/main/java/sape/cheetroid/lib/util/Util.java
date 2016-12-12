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

}