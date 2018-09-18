package todolist.cursoandroid.com.todolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText textoTarefa;
    private Button botaoAdicionar;
    private ListView listaTarefas;
    private SQLiteDatabase bancoDados;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            textoTarefa = (EditText) findViewById(R.id.textoTarefaId);
            botaoAdicionar = (Button) findViewById(R.id.botaoAdicionarId);
            listaTarefas = (ListView) findViewById(R.id.listViewId);

            bancoDados = openOrCreateDatabase("apptarefas",MODE_PRIVATE,null);

            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT,tarefa VARCHAR)");

            botaoAdicionar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String textoDigitado = textoTarefa.getText().toString();
                    salvarTarefa(textoDigitado);
                }
            });

            listaTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    removerTarefa(ids.get(position));
                }
            });

            recuperarTarefas();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void salvarTarefa(String texto){

        try{
            if(texto.equals("")) {
                Toast.makeText(MainActivity.this,"Digite uma Tarefa",Toast.LENGTH_SHORT).show();
            }else{
                bancoDados.execSQL("INSERT INTO tarefas (tarefa) VALUES ('" + texto + "')");
                Toast.makeText(MainActivity.this,"Tarefa salva com sucesso",Toast.LENGTH_SHORT).show();
                recuperarTarefas();
                textoTarefa.setText("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void recuperarTarefas(){

        try{

            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC",null);

            if(cursor != null){
                Log.i("cursor", "vazio");
            }else{
                Log.i("cursor2","não vzzazio");
            }

            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTafera = cursor.getColumnIndex("tarefa");

            itens = new ArrayList<String>();
            ids = new ArrayList<Integer>();

            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    itens);

            listaTarefas.setAdapter(itensAdaptador);

            cursor.moveToFirst();

            while( cursor != null){
                Log.i("bbb","ccceeee333");
                itens.add(cursor.getString(indiceColunaTafera));
                ids.add(Integer.parseInt(cursor.getString(indiceColunaId)));
                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removerTarefa(Integer id){

        try{

            bancoDados.execSQL("DELETE FROM tarefas WHERE id="+id);
            Toast.makeText(MainActivity.this,"Tarefa removida com sucesso",Toast.LENGTH_SHORT).show();
            recuperarTarefas();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
