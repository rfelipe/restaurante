package br.ftec.prova.feliperodrigues.restaurante;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDeDados extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String NOME_BANCO = "banco.db";
    public static final String TABELA = "restaurante";
    public static final String ID = "_id";
    public  static final String Nome = "Nome";
    public  static final String  Endereco= "Endereco";
    public static final String Comida = "Comida";
    public static final String Preco = "Preco";

    private static final int VERSAO = 2;

    public BancoDeDados(Context context) {
        super(context, NOME_BANCO,null,VERSAO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //esse código é usado para criar o banco de dados
        String sql = "CREATE TABLE if not exists "+TABELA+"(" + ID + " integer primary key autoincrement," + Nome + " text," + Endereco + " text," + Comida +" text,"+ Preco +" text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //caso a versão do banco de dados mude, podemos executar um SQL aqui
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }

    public boolean inserirRegistro(String sNome, String sEndereço, String sComida, String sPreço) {
        ContentValues valores;

        db = getWritableDatabase();
        valores = new ContentValues();
        valores.put(Nome, sNome);
        valores.put(Endereco, sEndereço);
        valores.put(Comida, sComida);
        valores.put(Preco, sPreço);


        long resultado = db.insert(TABELA, null, valores);
        db.close();

        if (resultado == -1)
            return false;
        else
            return true;
    }

    public int deletarRegistro(int id){
        //deleta o registro e retorna o contador de linhas deletadas
        String where = ID + "=" + id;
        db = getReadableDatabase();
        int count = db.delete(TABELA, where, null);
        db.close();
        return  count;
    }

    public int alterarRegistro(int id, String sNome, String sEndereço, String sComida, String sPreço){
        ContentValues valores;
        String where;

        db = getWritableDatabase();

        where = ID + "=" + id;

        valores = new ContentValues();
        valores.put(Nome, sNome);
        valores.put(Endereco, sEndereço);
        valores.put(Comida, sComida);
        valores.put(Preco, sPreço);


        int count = db.update(TABELA, valores, where, null);
        db.close();
        return count;
    }

    public Cursor carregarDados(){
        Cursor cursor;
        String[] campos =  {ID,Nome,Preco};
        db = getReadableDatabase();
        cursor = db.query(TABELA, campos, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor carregarDadoById(int id){
        Cursor cursor;
        String[] campos =  {ID,Nome,Endereco,Comida,Preco};
        String where = ID + "=" + id;
        db = getReadableDatabase();
        cursor = db.query(TABELA,campos,where, null, null, null, null,null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor consultaPorColuna(String sColuna, String sValor){
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA + " WHERE " + sColuna + " = ? ", new String[]{sValor});
        cursor.moveToFirst();
        db.close();

        return cursor;
    }
}


