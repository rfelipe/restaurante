package br.ftec.prova.feliperodrigues.restaurante;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class cadastro extends AppCompatActivity {
    EditText rest,end,tipo,preco;
    BancoDeDados crud;
    String codigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Button cadastro = (Button) findViewById(R.id.cad);
        Button pesquisa = (Button) findViewById(R.id.alt);
        Button deletar = (Button) findViewById(R.id.del);
        rest=(EditText) findViewById(R.id.rest);
        end=(EditText) findViewById(R.id.end);
        tipo=(EditText) findViewById(R.id.tipo);
        preco=(EditText) findViewById(R.id.preco);

        crud = new BancoDeDados(getBaseContext());



        codigo = this.getIntent().getStringExtra("codigo");
        if (codigo == null) {
            //está abrindo a tela direto do menu principal para cadastro

            //ajusta botões
            cadastro.setEnabled(true);
            pesquisa.setEnabled(false);
            deletar.setEnabled(false);

        } else {
            //está abrindo a tela depois da lista, então vem com um código

            //executa o método que devolve od registros de um CODIGO
            Cursor cursor = crud.carregarDadoById(Integer.parseInt(codigo));
            rest.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDeDados.Nome)));
            end.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDeDados.Endereco)));
            tipo.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDeDados.Comida)));
            preco.setText(cursor.getString(cursor.getColumnIndexOrThrow(BancoDeDados.Preco)));


            //ajusta botões
            cadastro.setEnabled(false);
            pesquisa.setEnabled(true);
            deletar.setEnabled(true);
        }


        //tratamento do botão CADASTRAR
        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sNome = rest.getText().toString();
                String sEnd = end.getText().toString();
                String sTipo = tipo.getText().toString();
                String sPreco = preco.getText().toString();


                //executa método para inserir no banco
                boolean resultado = crud.inserirRegistro(sNome, sEnd, sTipo, sPreco);

                if (resultado) {
                    //funcionou
                    Toast.makeText(getApplicationContext(), "Registro inserido com sucesso", Toast.LENGTH_LONG).show();
                    //limpar campos
                    rest.setText("");
                    end.setText("");
                    tipo.setText("");
                    preco.setText("");

                } else {
                    //ocorreu algum erro
                    Toast.makeText(getApplicationContext(), "Erro ao inserir dados", Toast.LENGTH_LONG).show();
                }
            }
        });

        deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //executa método para deletar o registro selecionado
                int count = crud.deletarRegistro(Integer.parseInt(codigo));
                if (count > 0) {
                    Toast.makeText(getApplicationContext(), "Registro deletado com sucesso", Toast.LENGTH_LONG).show();

                    //encerra esta activity e volta para a lista
                    Intent intent = new Intent(cadastro.this, consulta.class);
                    startActivity(intent);
                    finish();
                } else {
                    //ocorreu algum erro
                    Toast.makeText(getApplicationContext(), "Erro ao deletar registro", Toast.LENGTH_LONG).show();
                }


            }
        });

        pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtem valores dos EditText
                String sNome = rest.getText().toString();
                String sEnd = end.getText().toString();
                String sTipo = tipo.getText().toString();
                String sPreco = preco.getText().toString();

                //executa método de alteração
                int count = crud.alterarRegistro(Integer.parseInt(codigo), sNome, sEnd, sTipo, sPreco);

                if (count > 0) {
                    Toast.makeText(getApplicationContext(), "Registro alterado com sucesso", Toast.LENGTH_LONG).show();

                    //encerra esta activity e volta para a lista
                    Intent intent = new Intent(cadastro.this, consulta.class);
                    startActivity(intent);
                    finish();
                } else {
                    //ocorreu algum erro
                    Toast.makeText(getApplicationContext(), "Erro ao alterar registro", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}