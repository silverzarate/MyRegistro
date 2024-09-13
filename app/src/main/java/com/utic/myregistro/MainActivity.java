package com.utic.myregistro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
;
import com.android.volley.toolbox.Volley;

import com.android.volley.toolbox.StringRequest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView messageTextView;
    private static final String IMAGE_URL = "https://utic2025.com/silvestre_zarate/recuperar.php?idcliente=";


    Button btnMostrar, btnGuardar, btnLimpiar;
    EditText etIdCliente, etNombreApellido, etCorreo, etTelefono, etCiudad;
    String idcliente, nombreapellido, correo, telefono, ciudad;
    FloatingActionButton  btnEliminar;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnMostrar = (Button) findViewById(R.id.btnMostrar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);

        btnEliminar = findViewById(R.id.btnEliminar);


        etIdCliente = (EditText) findViewById(R.id.etIdCliente);
        etNombreApellido = (EditText) findViewById(R.id.etNombreApellido);
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        etCiudad = (EditText) findViewById(R.id.etCiudad);









        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarTextos();
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idcliente = etIdCliente.getText().toString().trim();
                Toast.makeText(MainActivity.this, "ImageView clicked!", Toast.LENGTH_SHORT).show();

                String url = "https://utic2025.com/silvestre_zarate/recuperar.php?idcliente=" + idcliente ;
                recuperarData(url);
            }
        });





        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombreapellido = etNombreApellido.getText().toString().trim();
                correo = etCorreo.getText().toString().trim();
                telefono = etTelefono.getText().toString().trim();
                ciudad = etCiudad.getText().toString().trim();




                // Insertar datos en la base de datos


                String url = "https://utic2025.com/silvestre_zarate/insertar.php?nombreapellido="
                        + nombreapellido + "&correo="
                        + correo + "&telefono=" + telefono + "&ciudad=" + ciudad;

                guardarData(url);

            }

            private byte[] imageViewToByteArray(ImageView imageView) {
                return null;
            }
        });


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Desea Eliminar este Cliente?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                idcliente = etIdCliente.getText().toString().trim();
                String url = "https://utic2025.com/silvestre_zarate/eliminar.php?idcliente="
                        + idcliente;
                guardarData(url);

               }
            })

                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }
    });

    }












    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menuAgregar) {

            agregarRegistro();
            return true;

        } else if(item.getItemId()==R.id.menuServicio) {

            agregarServicio();
            return true;

        }  else if(item.getItemId()==R.id.menuContactos) {

        agregarContacto();
        return true;

       }else if(item.getItemId()==R.id.menuCalculadora) {

            agregarCalculadora();
            return true;

        } else if(item.getItemId()==R.id.menuCamara) {

        agregarCamara();
        return true;

      }else if(item.getItemId()==R.id.menuJuego) {

            agregarJuego();
            return true;

        }else if(item.getItemId()==R.id.menuSalir) {

            Salir();
            return true;

        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void agregarRegistro(){
        Intent intent = new Intent(this, AgregarActivity.class);
        startActivity(intent);
    }


    private void agregarServicio(){
        Intent intent = new Intent(this, AgregarServicioActivity.class);
        startActivity(intent);
    }

    private void agregarContacto(){
        Intent intent = new Intent(this, MainActivityAgenda.class);
        startActivity(intent);
    }
    private void agregarCalculadora(){
        Intent intent = new Intent(this, MainCalculadora.class);
        startActivity(intent);
    }

    private void agregarCamara(){
        Intent intent = new Intent(this, MainCamara.class);
        startActivity(intent);
    }

    private void agregarJuego(){
        Intent intent = new Intent(this, MainJuego.class);
        startActivity(intent);
    }

    private void Salir(){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }


    private void recuperarData(String url) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recuperando...");

        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("true")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                        etNombreApellido.setText(object.getString(
                                                "nombreapellido"));
                                        etCorreo.setText(object.getString("correo"));
                                        etTelefono.setText(object.getString("telefono"));
                                        etCiudad.setText(object.getString("ciudad"));



                                    }




                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "REGISTRO NO ENCONTRADO!",
                                        Toast.LENGTH_LONG).show();
                                limpiarTextos();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        limpiarCache(requestQueue);

    }



    private void lista(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    private void guardarData(String url) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando...");

        progressDialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("false")) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response,
                            Toast.LENGTH_LONG).show();

                } else {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ACCION REALIZADA!",
                            Toast.LENGTH_LONG).show();etIdCliente.setText(response);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        limpiarCache(requestQueue);
        limpiarTextos();

    }

    private void limpiarCache(RequestQueue requestQueue) {
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

    public void limpiarTextos() {
        etIdCliente.setText(null);
        etNombreApellido.setText(null);
        etCorreo.setText(null);
        etTelefono.setText(null);
        etCiudad.setText(null);

    }

}