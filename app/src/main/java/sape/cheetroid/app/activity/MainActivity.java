package sape.cheetroid.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import sape.cheetroid.R;
import sape.cheetroid.app.control.UsuarioController;
import sape.cheetroid.app.model.Usuario;

public class MainActivity extends Activity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //----------------------------------------------------------------------------------------------------
        /*
        Usuario usuario1 = new Usuario();

        usuario1.login = "user452";
        usuario1.password = "1";

        Usuario usuario2 = new Usuario();

        usuario2.login = "user452";
        usuario2.password = "2";

        Usuario usuario3 = new Usuario();

        usuario3.login = "user452";
        usuario3.password = "3";

        usuario1.store( getApplicationContext() );
        usuario2.store( getApplicationContext() );
        usuario3.store( getApplicationContext() );
        */
        //----------------------------------------------------------------------------------------------------

        UsuarioController usuarioController = new UsuarioController( getApplicationContext() );

        for( Usuario usuarioTemp : usuarioController.getAllByLogin( "user452" ) )
            Log.e( "USUARIO LOGIN", usuarioTemp.id + " " + usuarioTemp.login + " " + usuarioTemp.password );

        for( Usuario usuarioTemp : usuarioController.getAll() )
            Log.e( "USUARIO ALL", usuarioTemp.id + " " + usuarioTemp.login + " " + usuarioTemp.password );

        //----------------------------------------------------------------------------------------------------

    }

}