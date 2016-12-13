package sape.cheetroid.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import sape.cheetroid.R;
import sape.cheetroid.app.control.UsuarioController;
import sape.cheetroid.app.model.Usuario;

public class MainActivity extends Activity
{

    public TextView textViewTest;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Usuario usuario = new Usuario();

        usuario.login = "user452";
        usuario.password = "123";

        usuario.store( getApplicationContext() );

        UsuarioController usuarioController = new UsuarioController( getApplicationContext() );

        for( Usuario usuarioTemp : usuarioController.getAll() )
            Log.e( "USUARIO: ", usuarioTemp.id + " " + usuarioTemp.login + " " + usuarioTemp.password );

        //Usuario usuario = new Usuario( 1, getApplicationContext() );
        //Log.e( "USUARIO", usuario.id + " " + usuario.login + " " + usuario.password );

    }

}