package sape.cheetroid.lib.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import sape.cheetroid.lib.database.cfield.CFieldAnno;
import sape.cheetroid.lib.database.cfield.CPrimaryKey;

/*
Author = Jonas Jordão de Macêdo;
Creation Date = 21/12/2016 (m/d/y);
*/
public class CJSONHandler
{

    private Class myModel;

    public CJSONHandler( Class myModel )
    {

        this.myModel = myModel;

    }

    public <T> ArrayList<T> fromJSONArrayToMyModelArrayList( JSONArray jsonArray )
    {

        ArrayList<T> myModelArrayList = new ArrayList<T>();

        for( int i = 0; i < jsonArray.length(); i++ )
        {

            JSONObject jsonObject = getJSONObjectFromJSONArrayPosition( jsonArray, i );

            if( jsonObject != null )
            {

                Object object = fromJSONObjectToObject( jsonObject );

                if( object != null )
                {

                    myModelArrayList.add( (T) object );

                }

            }

        }

        return myModelArrayList;

    }

    public JSONArray fromMyModelArrayListToJSONArray( ArrayList myModelArrayList )
    {

        ArrayList<Object> arrayList = myModelArrayList;

        JSONArray jsonArray = new JSONArray();

        for( Object object : arrayList )
        {

            Field[] fields = object.getClass().getFields();

            JSONObject jsonObject = new JSONObject();

            for( Field field : fields )
            {

                if( field.isAnnotationPresent( CFieldAnno.class ) || field.isAnnotationPresent( CPrimaryKey.class ) )
                {

                    try
                    {

                        jsonObject.put( field.getName(), String.valueOf( field.get( object ) ) );

                    }catch( JSONException e )
                    {

                        e.printStackTrace();

                    }catch( IllegalAccessException e )
                    {

                        e.printStackTrace();

                    }

                }

            }

            jsonArray.put( jsonObject );

        }

        return  jsonArray;

    }

    private Object fromJSONObjectToObject( JSONObject jsonObject )
    {

        return newInstanceMyModel( jsonObject );

    }

    private Object newInstanceMyModel( JSONObject jsonObject )
    {

        try
        {

            Constructor constructor = myModel.getConstructor( JSONObject.class );

            return constructor.newInstance( jsonObject );

        }catch( InstantiationException e )
        {

            e.printStackTrace();

        }catch( IllegalAccessException e )
        {

            e.printStackTrace();

        }catch( NoSuchMethodException e )
        {

            e.printStackTrace();

        }catch( InvocationTargetException e )
        {

            e.printStackTrace();

        }

        return null;

    }

    private JSONObject getJSONObjectFromJSONArrayPosition( JSONArray jsonArray, int i )
    {

        try
        {

            return jsonArray.getJSONObject( i );

        }catch( JSONException e )
        {

            e.printStackTrace();

        }

        return null;

    }

}