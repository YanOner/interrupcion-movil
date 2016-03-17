package com.upc.interrupcion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.upc.model.CuadrillaBean;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Cuadrillas extends AppCompatActivity {

    public ArrayList<CuadrillaBean> cuadrillas;
    public ArrayList<String> lista = new ArrayList<>();
    public String seleccinado="";
    public String codigo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadrillas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cuadrillas = new ArrayList<>();
        new HttpRequestTask().execute();

        ListView listaViewCuadrillas = (ListView) findViewById(R.id.listViewCuadrillas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
        listaViewCuadrillas.setAdapter(adapter);
        listaViewCuadrillas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                seleccinado = ((TextView) view).getText().toString();
                Toast.makeText(getApplicationContext(),
                        seleccinado, Toast.LENGTH_SHORT).show();
                for (CuadrillaBean obj:cuadrillas) {
                    if(obj.getDescripcion().contains(seleccinado)){
                        codigo = obj.getCodigoCuadrilla();
                        Intent i = new Intent(getApplicationContext(), CuadrillaDetalle.class);
                        i.putExtra("contenido", obj.toString());
                        startActivity(i);
                        ((TextView)findViewById(R.id.textViewCuadrillas)).setText("SELECCIÓN: " + seleccinado);
                        break;
                    }
                }
            }
        });
    }

    public void asignarCuadrilla(View v){
        if(!seleccinado.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "LA CUADRILLA FUE ASIGNADA", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),
                    "SELECCIONE UNA CUADRILLA", Toast.LENGTH_SHORT).show();
        }
    }

    public void regresar(View v){
        finish();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<CuadrillaBean>> {
        @Override
        protected List<CuadrillaBean> doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            try {
                String url = "http://104.214.71.24:8080/RESTService/api/WCuadrillaLista/"+ MainActivity.codigoUsuario +"/D";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA",objetos.toString());
                LinkedHashMap<String,Object> respuesta = (LinkedHashMap<String,Object>) objetos;
                Log.i("LinkedHashMap", respuesta.toString());
                ArrayList<LinkedHashMap<String,String>> lista =
                        (ArrayList<LinkedHashMap<String,String>>) respuesta.get("DTOCuadrillaList");
                Log.i("CUADRILLAS", lista.toString());
                for (LinkedHashMap<String,String> lhm : lista) {
                    Log.i("CUADRILLA", lhm.toString());
                    CuadrillaBean nuevo = new CuadrillaBean();
                    nuevo.setCodigoCuadrilla(String.valueOf(lhm.get("CodigoCuadrilla")));
                    nuevo.setDescripcion(String.valueOf(lhm.get("Descripcion")));
                    nuevo.setEstado(String.valueOf(lhm.get("Estado")));
                    cuadrillas.add(nuevo);
                }
                return cuadrillas;
            } catch (Exception e) {
                Log.i("Exception", "ERROR");
                Log.e("HttpRequestTask", e.getMessage(), e);
            }
            Log.i("doInBackground", "fin");
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<CuadrillaBean> cuadrillas) {
            Log.i("onPostExecute", "inicio");
            Log.i("LISTO",cuadrillas.toString());
            lista = new ArrayList<>();
            for (CuadrillaBean obj:cuadrillas) {
                lista.add(obj.getDescripcion());
            }
            ListView listaViewCuadrillas = (ListView) findViewById(R.id.listViewCuadrillas);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lista);
            listaViewCuadrillas.setAdapter(adapter);
            TextView tv = (TextView)findViewById(R.id.textViewCuadrillas);
            if(lista.isEmpty()){
                tv.setText("NO SE ENCONTRARÓN CUADRILLAS");
            }else{
                tv.setText("SELECCIONE");
            }
            Log.i("onPostExecute", "fin");
        }

    }
}
