package sape.cheetroid.app.model;

import android.content.Context;

import sape.cheetroid.lib.database.cfield.CField;
import sape.cheetroid.lib.main.CModel;

import sape.cheetroid.lib.database.cfield.CFieldAnno;
import sape.cheetroid.lib.database.cfield.CPrimaryKey;
import sape.cheetroid.lib.database.ctable.CTableAnno;

@CTableAnno( tableName = "usuario" )
public class Usuario extends CModel
{

    @CPrimaryKey()
    public long id;

    @CFieldAnno( fieldType = CField.TEXT, isNotNull =  true )
    public String login;

    @CFieldAnno( fieldType = CField.TEXT, isNotNull =  true )
    public String password;

    public Usuario() { }

    public Usuario( long id, Context context ) { super( id, context ); }

}