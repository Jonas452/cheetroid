package sape.cheetroid.app.control;

import android.content.Context;

import java.util.ArrayList;

import sape.cheetroid.app.model.Municipio;
import sape.cheetroid.lib.main.CController;

public class MunicipioController extends CController
{

    public MunicipioController( Context context )
    {

        super( context, Municipio.class );

    }

    public ArrayList<Municipio> getAll()
    {

        return selectAll( null, null );

    }

}