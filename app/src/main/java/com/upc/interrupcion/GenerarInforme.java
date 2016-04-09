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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.upc.ingreso.MainActivity;
import com.upc.ingreso.R;
import com.upc.model.InformeBean;
import com.upc.model.OrdenAtencionBean;
import com.upc.utility.services;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class GenerarInforme extends AppCompatActivity {

    public List<InformeBean> informes;
    public ArrayList<String> lista = new ArrayList<>();
    public String seleccinado="";
    public String codigo="";
    public String codigoInterrupcion="";
    public String codigoCuadrilla="";
    public String observacion="";

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
                        codigoInterrupcion = obj.getCodigoInterrupcion();
                        codigoCuadrilla = obj.getCodigoCuadrilla();
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
        observacion = ((EditText)findViewById(R.id.editTextObservacion)).getText().toString();
        if(!seleccinado.equalsIgnoreCase("") || !observacion.equalsIgnoreCase("")){
            new HttpRequestTaskEnviar().execute();
        }else{
            Toast.makeText(getApplicationContext(),
                    "SELECCIONE UN INFORME E INGRESE UNA OBSERVACIÓN", Toast.LENGTH_SHORT).show();
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<InformeBean>> {
    @Override
    protected List<InformeBean> doInBackground(Void... params) {
        Log.i("doInBackground", "inicio");
        List<InformeBean> lista = new ArrayList<>();
        try {
            String url = services.WOrdenAtencionInterrupcion + MainActivity.codigoUsuario;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Object objetos = restTemplate.getForObject(url, Object.class);
            Log.i("RESPUESTA",objetos.toString());
            List<HashMap<String,Object>> respuesta = (List<HashMap<String, Object>>) objetos;
            for (HashMap<String,Object> inter : respuesta) {
                Log.i("InformeBean", inter.toString());
                InformeBean nuevo = new InformeBean();
                nuevo.setCodigoInforme(String.valueOf(inter.get("codigo")));
                nuevo.setDescripcion(String.valueOf(inter.get("descripcion"))+" - "+String.valueOf(inter.get("comentario")));
                nuevo.setCodigoInterrupcion(String.valueOf(inter.get("codigoInterrupcion")));
                nuevo.setCodigoCuadrilla(String.valueOf(inter.get("codigoCuadrilla")));
                lista.add(nuevo);
            }
            return lista;
        } catch (Exception e) {
            Log.i("Exception", "ERROR");
            Log.e("HttpRequestTask", e.getMessage(), e);
        }
        Log.i("doInBackground", "fin");
        return lista;
    }

        @Override
        protected void onPostExecute(List<InformeBean> informesTask) {
            Log.i("onPostExecute", "inicio");
            Log.i("LISTO",informesTask.toString());
            lista = new ArrayList<>();
            for (InformeBean inter:informesTask) {
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
            informes = informesTask;
            Log.i("onPostExecute", "fin");
        }

    }
    //ENVIAR
    private class HttpRequestTaskEnviar extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            boolean exito = false;
            try {
                String url = services.WEnviarOrden + codigo + "/" + codigoInterrupcion + "/" +codigoCuadrilla + "/" +observacion;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA",objetos.toString());
                LinkedHashMap<String,Object> respuesta = (LinkedHashMap<String,Object>) objetos;
                String valor = String.valueOf(respuesta.get("exito"));
                if(valor.equals("true")){
                    exito = true;
                }
            } catch (Exception e) {
                Log.i("Exception", "ERROR");
                Log.e("HttpRequestTask", e.getMessage(), e);
            }
            Log.i("doInBackground", "fin");
            return exito;
        }

        @Override
        protected void onPostExecute(Boolean exito) {
            Log.i("onPostExecute", "inicio");
            if(exito){
                Toast.makeText(getApplicationContext(), "Proceso terminado exitosamente.", Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            }else{
                Toast.makeText(getApplicationContext(), "Ocurrió un error al procesar la información.", Toast.LENGTH_LONG).show();
            }
            Log.i("onPostExecute", "fin");
        }

    }
}
