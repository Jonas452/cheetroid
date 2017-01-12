package sape.cheetroid.app.model;

import android.content.Context;

import org.json.JSONObject;

import sape.cheetroid.lib.database.cfield.CField;
import sape.cheetroid.lib.database.cfield.CFieldAnno;
import sape.cheetroid.lib.database.cfield.CPrimaryKey;
import sape.cheetroid.lib.database.ctable.CTableAnno;
import sape.cheetroid.lib.main.CModel;

@CTableAnno( tableName = "municipio", version = 2 )
public class Municipio extends CModel
{

    @CPrimaryKey()
    public long id;

    @CFieldAnno( fieldType = CField.TEXT, isNotNull =  true )
    public String nome;

    public Municipio() { }

    public Municipio( long id, Context context ) { super( id, context ); }

    public Municipio( JSONObject jsonObject ) { super( jsonObject ); }

}