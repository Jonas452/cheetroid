package sape.cheetroid.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import sape.cheetroid.R;
import sape.cheetroid.app.model.Usuario;

public class MainActivity extends Activity
{

    public TextView textViewTest;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        textViewTest = (TextView) findViewById( R.id.textViewTest );

        Usuario usuario = new Usuario();

        usuario.login = "user452";
        usuario.password = "123";

        usuario.store( getApplicationContext() );

        Log.e( "SAVED", usuario.id + " " + usuario.login + " " + usuario.password );

        usuario.login = "user";

        usuario.store( getApplicationContext() );

        long id = usuario.id;

        Usuario usuario2 = new Usuario( id, getApplicationContext() );

        Log.e( "UPDATED", usuario2.id + " " + usuario2.login + " " + usuario2.password );

        usuario2.delete( getApplicationContext() );

        Usuario usuarioBuscar = new Usuario( id, getApplicationContext() );

        if( !usuarioBuscar.hasData )
            Log.e( "DELETED", "NOT FOUND." );

    }

}