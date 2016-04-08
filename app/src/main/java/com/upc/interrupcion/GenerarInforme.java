package com.upc.interrupcion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.upc.ingreso.MainActivity;
import com.upc.ingreso.R;
import com.upc.model.InformeBean;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GenerarInforme extends AppCompatActivity {

    public List<InformeBean> informes;
    public ArrayList<String> lista = new ArrayList<>();
    public String seleccinado="";
    public String codigo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_informe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        informes = new ArrayList<>();
        new HttpRequestTask().execute();

        ListView listaViewInformes = (ListView) findViewById(R.id.listViewInformes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
        listaViewInformes.setAdapter(adapter);
        listaViewInformes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                seleccinado = ((TextView) view).getText().toString();
                Toast.makeText(getApplicationContext(),
                        seleccinado, Toast.LENGTH_SHORT).show();
                for (InformeBean obj : informes) {
                    if (obj.getDescripcion().contains(seleccinado)) {
                        codigo = obj.getCodigoInforme();
                        Intent i = new Intent(getApplicationContext(), informe_detalle.class);
                        i.putExtra("contenido", obj.toString());
                        startActivity(i);
                        ((TextView) findViewById(R.id.textViewInforme)).setText("SELECCIÓN: " + seleccinado);
                        break;
                    }
                }
            }
        });
    }

    public void enviarInforme(View v){
        if(!seleccinado.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "Informe Enviado", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),
                    "SELECCIONE UN INFORME", Toast.LENGTH_SHORT).show();
        }
    }
/*
    public void regresar(View v){
        finish();
    }
*/
    private class HttpRequestTask extends AsyncTask<Void, Void, List<InformeBean>> {
        @Override
        protected List<InformeBean> doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            try {
                String url = "http://104.214.71.24:8080/RESTService/api/WOrdenAtencionInterrupcion/"+ MainActivity.codigoUsuario;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA",objetos.toString());
                LinkedHashMap<String,Object> respuesta = (LinkedHashMap<String,Object>) objetos;
                Log.i("LinkedHashMap", respuesta.toString());
                ArrayList<LinkedHashMap<String,String>> lista =
                        (ArrayList<LinkedHashMap<String,String>>) respuesta.get("DTOInformeList");
                Log.i("INTERRUPCIONES", lista.toString());
                for (LinkedHashMap<String,String> inter : lista) {
                    Log.i("INTERRUPCION", inter.toString());
                    InformeBean nuevo = new InformeBean();
                    nuevo.setCodigoInforme(String.valueOf(inter.get("CodigoInforme")));
                    nuevo.setDescripcion(String.valueOf(inter.get("Descripcion")));
                    nuevo.setDetalle(String.valueOf(inter.get("Detalle")));
                    informes.add(nuevo);
                }
                return informes;
            } catch (Exception e) {
                Log.i("Exception", "ERROR");
                Log.e("HttpRequestTask", e.getMessage(), e);
            }
            Log.i("doInBackground", "fin");
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<InformeBean> informes) {
            Log.i("onPostExecute", "inicio");
            Log.i("LISTO",informes.toString());
            lista = new ArrayList<>();
            for (InformeBean inter:informes) {
                lista.add(inter.getDescripcion());
            }
            ListView listaViewInforme = (ListView) findViewById(R.id.listViewInformes);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lista);
            listaViewInforme.setAdapter(adapter);
            TextView tv = (TextView)findViewById(R.id.textViewInforme);
            if(lista.isEmpty()){
                tv.setText("NO SE ENCONTRARÓN INFORMES");
            }else{
                tv.setText("SELECCIONE");
            }
            Log.i("onPostExecute", "fin");
        }

    }
}
