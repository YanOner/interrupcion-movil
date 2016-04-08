package com.upc.ingreso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.upc.model.Usuario;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    String usuarioLogin = "";
    String password = "";
    public static String codigoUsuario = "";
    public static String nombreUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //probable
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void validarIngreso(View v){
        usuarioLogin = ((EditText)findViewById(R.id.editText)).getText().toString();
        password = ((EditText)findViewById(R.id.editText2)).getText().toString();
        if(usuarioLogin.equals("") || null == usuarioLogin){
            Toast.makeText(getApplicationContext(),"Ingresar un Usuario",Toast.LENGTH_SHORT).show();
        }else if(password.equals("") || null == password){
            Toast.makeText(getApplicationContext(),"Ingresar una Contraseña",Toast.LENGTH_SHORT).show();
        }else{
            ((Button)findViewById(R.id.button)).setVisibility(View.INVISIBLE);
            new InterrupcionWS().execute();
        }
    }

    public void recuperarPassword(View v){
        Intent i = new Intent(this,Password.class);
        startActivity(i);
    }

    protected class InterrupcionWS extends AsyncTask<Void,String,Usuario>{

        @Override
        protected void onPostExecute(Usuario usuario) {
            Log.e("onPostExecute", "Response from server: " + usuario.toString());
            //PRUEBA
            usuario.setValido(true);
            if(usuario.getValido()){
                codigoUsuario = usuario.getCodigo();
                nombreUsuario = usuario.getNombre();
                Toast.makeText(getApplicationContext(),usuario.getNombre(),Toast.LENGTH_LONG).show();
                //Intent i = new Intent(getApplicationContext(),com.upc.menu.Menu.class);
                Intent i = new Intent(getApplicationContext(),com.upc.menu.activity_menu1.class);
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(),usuario.getMensaje(),Toast.LENGTH_LONG).show();
            }
            ((Button)findViewById(R.id.button)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.editText)).setText("");
            ((EditText)findViewById(R.id.editText2)).setText("");
            super.onPostExecute(usuario);
        }

        @Override
        protected Usuario doInBackground(Void... params) {
            Usuario usuario = new Usuario();
            Log.i("doInBackground", "inicio");
            try {
                //String url = "http://104.214.71.24:8080/RESTService/api/WInterrupcionLista/"+ MainActivity.codigoUsuario +"/D";
                String url = "http://10.0.2.2:8080/ws-cxf-sgi/api/ws/WValidarUsuario/"+usuarioLogin+"/"+password;
                Log.i("URL", url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA", objetos.toString());
                LinkedHashMap<String,Object> respuesta = (LinkedHashMap<String,Object>) objetos;
                usuario.setCodigo(String.valueOf(respuesta.get("codigo")));
                usuario.setNombre(String.valueOf(respuesta.get("nombre")));
                String valido = String.valueOf(respuesta.get("valido"));
                if(valido.equals("true")){
                    usuario.setValido(true);
                }else{
                    usuario.setValido(false);
                }
                usuario.setMensaje(String.valueOf(respuesta.get("mensaje")));
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.getMessage());
                usuario.setValido(false);
                usuario.setMensaje("Ocurrió un error al procesar los datos.");
            }

            return usuario;
        }
    }

}
