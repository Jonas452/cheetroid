package sape.cheetroid.app.control;

import android.content.Context;

import java.util.ArrayList;

import sape.cheetroid.app.model.Usuario;
import sape.cheetroid.lib.main.CController;

public class UsuarioController extends CController
{

    public UsuarioController( Context context )
    {

        super( context, Usuario.class );

    }

    public ArrayList<Usuario> getAll()
    {

        ArrayList<Usuario> usuarioArrayList = new ArrayList<Usuario>();

        for( Object object : selectAll( null, null ) )
            usuarioArrayList.add( (Usuario) object );

        return usuarioArrayList;

    }

}