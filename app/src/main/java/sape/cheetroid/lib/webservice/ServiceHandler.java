package sape.cheetroid.lib.webservice;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ServiceHandler
{
    public final static String GET = "GET";
    public final static String POST = "POST";

    private static final char DELIMITER_PARAMETER = '&';
    private static final char EQUALS_PARAMETER = '=';

    private JSONObject response;

    private URL url;
    private HttpURLConnection urlConnection;
    private InputStream inputStream;

    public JSONObject makeServiceCall( String url, String method )
    {

        return this.makeServiceCall( url, method, null );

    }

    public JSONObject makeServiceCall( String url, String method, HashMap<String, String> parameters )
    {

        prepareURL( url );

        openConnection( method );

        if( parameters != null )
            sendOutputStream( parameters );

        readInputStream();

        prepareJSON( InputStreamToString() );

        urlConnection.disconnect();

        return response;

    }

    private void prepareURL( String url )
    {

        Log.e( "URL", url );

        try
        {

            this.url = new URL( url );

        }catch(  MalformedURLException e )
        {

            e.printStackTrace();

        }

    }

    private void openConnection( String method )
    {

        try
        {

            urlConnection = ( HttpURLConnection ) url.openConnection();

            urlConnection.setRequestMethod(method);
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);

        }catch( IOException e )
        {

            e.printStackTrace();

        }

    }

    private void sendOutputStream( HashMap<String, String> parameters )
    {

        Log.e( "OUTPUT", parameters.toString() );

        urlConnection.setDoOutput( true );

        DataOutputStream wr = null;

        try
        {

            wr = new DataOutputStream( urlConnection.getOutputStream() );
            wr.writeBytes( createURLParameters( parameters ) );
            wr.flush ();
            wr.close();

        }catch( IOException e )
        {

            e.printStackTrace();

        }


    }

    private void readInputStream()
    {

        try
        {

            inputStream = new BufferedInputStream( urlConnection.getInputStream() );



        }catch( IOException e )
        {

            e.printStackTrace();

        }

    }

    private void prepareJSON( String jsonString )
    {

        try
        {

            Log.e( "INPUT", jsonString );

            response = new JSONObject( jsonString );

        }catch( JSONException e )
        {

            e.printStackTrace();

        }

    }

    private String InputStreamToString()
    {

        return bufferedReaderToStringBuilder( new BufferedReader( new InputStreamReader(inputStream) ) ).toString();

    }

    private StringBuilder bufferedReaderToStringBuilder( BufferedReader bufferedReader )
    {

        StringBuilder result = new StringBuilder();

        String line;

        try
        {

            while( ( line = bufferedReader.readLine() ) != null )
                result.append( line );

        }catch( IOException e )
        {

            e.printStackTrace();

        }

        return result;

    }

    private String createURLParameters( HashMap<String, String> parameters )
    {

        StringBuilder URLParameters = new StringBuilder();

        Set< Map.Entry< String, String > > mapSet = parameters.entrySet();
        Iterator< Map.Entry< String, String > > mapIterator = mapSet.iterator();

        while( mapIterator.hasNext() )
        {

            Map.Entry< String, String > mapEntry = mapIterator.next();

            try
            {

                URLParameters
                        .append( mapEntry.getKey() )
                        .append( EQUALS_PARAMETER )
                        .append( URLEncoder.encode( mapEntry.getValue(), "UTF-8" ) );

                if( mapIterator.hasNext() )
                    URLParameters.append( DELIMITER_PARAMETER );

            } catch (UnsupportedEncodingException e)
            {

                e.printStackTrace();

            }

        }

        return URLParameters.toString();

    }

}