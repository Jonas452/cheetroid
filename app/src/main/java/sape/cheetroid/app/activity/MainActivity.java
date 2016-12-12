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

        UsuarioController usuarioController = new UsuarioController( getApplicationContext() );

        for( Usuario usuarioTemp : usuarioController.getAll() )
            Log.e( "USUARIO: ", usuarioTemp.id + " " + usuarioTemp.login + " " + usuarioTemp.password );

    }

}