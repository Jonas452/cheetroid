package sape.cheetroid.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

import sape.cheetroid.R;
import sape.cheetroid.app.model.Usuario;
import sape.cheetroid.lib.util.CJSONHandler;

public class MainActivity extends Activity
{

    private TextView textView;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        textView = (TextView) findViewById( R.id.textViewTest );

        //------------------------------------------

        Usuario usuario1 = new Usuario( 1, getApplicationContext() );
        Usuario usuario2 = new Usuario( 2, getApplicationContext() );
        Usuario usuario3 = new Usuario( 3, getApplicationContext() );

        //------------------------------------------

        JSONArray jsonArray = new JSONArray();

        jsonArray.put( usuario1.toJSON() );
        jsonArray.put( usuario2.toJSON() );
        jsonArray.put( usuario3.toJSON() );

        addText( "JSON ARRAY FROM MODELS:" );
        addText( jsonArray.toString() );

        addText( "" );

        //------------------------------------------

        CJSONHandler cjsonHandler = new CJSONHandler( Usuario.class );

        ArrayList<Usuario> usuarioArrayList = cjsonHandler.fromJSONArrayToMyModelArrayList( jsonArray );

        addText( "MY MODELS FROM JSON ARRAY:" );

        for( Usuario usuario : usuarioArrayList )
            addText( usuario.id + " " + usuario.login + " " + usuario.password );

        //------------------------------------------

        JSONArray jsonArray2 = cjsonHandler.fromMyModelArrayListToJSONArray( usuarioArrayList );

        addText( "" );
        addText( "JSON ARRAY FROM MODELS ARRAY LIST:" );
        addText( jsonArray2.toString() );

        addText( "" );

        //------------------------------------------

    }

    private void addText( String text )
    {

        textView.setText( textView.getText() + "\n" + text );

    }

}