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

            //final String URL="http://10.0.2.2:8080/ws-cxf-sgi/ws/ws/interrupcionService?wsdl";
            final String URL="http://104.214.71.24:9090/ws-cxf-sgii/ws/ws/interrupcionService?wsdl";
            final String NAMESPACE = "http://ws.interrupciones.upc.com/";
            final String METHOD_NAME = "validarUsuario";
            final String SOAP_ACTION = "";//"http://ws.interrupciones.upc.com/validarUsuario";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("arg0",usuarioLogin);
            request.addProperty("arg1",password);

            SoapSerializationEnvelope envelope =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL,5000);

            try
            {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap =(SoapObject)envelope.getResponse();
                int total = resSoap.getPropertyCount();
                Log.i("RESPONSE",resSoap.toString());
                Log.i("TOTAL", "" + total);
                for (int i=0;i<total;i++){
                    Log.i("PROPIEDAD",""+resSoap.getProperty(i));
                }
                usuario.setCodigo(String.valueOf(resSoap.getPropertySafely("codigo")));
                usuario.setNombre(String.valueOf(resSoap.getPropertySafely("nombre")));
                String valido = String.valueOf(resSoap.getPropertySafely("valido"));
                if(valido.equals("true")){
                    usuario.setValido(true);
                }else{
                    usuario.setValido(false);
                }
                usuario.setMensaje(String.valueOf(resSoap.getPropertySafely("mensaje")));
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
