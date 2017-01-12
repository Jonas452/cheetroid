package sape.cheetroid.lib.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CJSONUtil
{

    JSONObject jsonObject;

    public CJSONUtil(JSONObject jsonObject)
    {

        this.jsonObject = jsonObject;

    }

    public String getString( String tag )
    {

        try
        {

            return jsonObject.getString(tag);

        }catch( JSONException e )
        {

            e.printStackTrace();

        }

        return "";

    }

    public int getInt( String tag )
    {

        try
        {

            return jsonObject.getInt(tag);

        }catch( JSONException e )
        {

            e.printStackTrace();

        }

        return -1;

    }

    public JSONObject getJSONObject( String name )
    {

        try
        {

            return jsonObject.getJSONObject(name);

        }catch( JSONException e )
        {

            e.printStackTrace();

        }

        return null;

    }

    public JSONArray getJSONArray( String name )
    {

        try
        {

            return jsonObject.getJSONArray( name );

        }catch( JSONException e )
        {

            e.printStackTrace();

        }

        return null;

    }

    public static String getStringFromJSONObject( JSONObject tempJsonObject, String name )
    {

        try
        {

            return tempJsonObject.getString( name );

        }catch( JSONException e )
        {

            e.printStackTrace();

        }

        return "";

    }

    public static int getIntFromJSONObject( JSONObject tempJsonObject, String name )
    {

        try
        {

            return tempJsonObject.getInt( name );

        }catch( JSONException e )
        {

            e.printStackTrace();

        }

        return -1;

    }

    public static JSONObject getJSONObject(JSONArray jsonArray, int i )
    {

        try
        {

            return jsonArray.getJSONObject( i );

        } catch (JSONException e)
        {

            e.printStackTrace();

        }

        return null;

    }

}