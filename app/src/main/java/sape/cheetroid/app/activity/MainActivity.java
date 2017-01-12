package sape.cheetroid.app.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import sape.cheetroid.R;
import sape.cheetroid.app.control.MunicipioController;
import sape.cheetroid.app.model.Municipio;
import sape.cheetroid.lib.util.CJSONUtil;
import sape.cheetroid.lib.webservice.ServiceHandler;

public class MainActivity extends Activity
{

    private TextView textView;
    private Context context;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        textView = (TextView) findViewById( R.id.textViewTest );

        context = getApplicationContext();

        new asyncTest().execute();

    }

    class asyncTest extends AsyncTask< Void, Void, Void >
    {

        @Override
        protected Void doInBackground(Void... voids)
        {

            ServiceHandler sh = new ServiceHandler();
            JSONObject webserviceReturn = sh.makeServiceCall( "http://ceres.rn.gov.br/v2/app/services/ceres_cidadao/MunicipioWebservice.class.php", ServiceHandler.GET );

            CJSONUtil jsonUtil = new CJSONUtil( webserviceReturn );
            JSONArray dados = jsonUtil.getJSONArray( "dados" );

            for( int i = 0; i < dados.length(); i++ )
            {

                Municipio municipio = new Municipio( CJSONUtil.getJSONObject( dados, i ) );
                municipio.storeWithId( context );

            }

            return null;

        }

        @Override
        protected void onPostExecute( Void result )
        {

            super.onPostExecute(result);

            MunicipioController municipioController = new MunicipioController( context );

            for( Municipio municipio : municipioController.getAll() )
                addText( municipio.id + " " + municipio.nome + "\n" );

        }

    }

    private void addText( String text )
    {

        textView.setText( textView.getText() + "\n" + text );

    }

}