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

        return selectAll( null, null );

    }

    public ArrayList<Usuario> getAllByMunicipioId( long municipioId )
    {

        return  selectAll( "municipio_id = '" + municipioId + "'", "nome" );

    }

}