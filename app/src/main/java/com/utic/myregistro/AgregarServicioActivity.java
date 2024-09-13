package com.utic.myregistro;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AgregarServicioActivity extends AppCompatActivity {

    Button btnLimpiar,btnMostrar, btnAgregar, btnModificar;
    EditText etIdServicio, etNombreServicio, etCosto, etPersonal;
    String idservicio, NombreServicio, costo, personal;
    FloatingActionButton btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_servicio);

        EdgeToEdge.enable(this);



        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);
        btnMostrar = (Button) findViewById(R.id.btnMostrar);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        etIdServicio  = (EditText) findViewById(R.id.etIdServicio);
        etNombreServicio = (EditText) findViewById(R.id.etNombreServicio);
        etCosto = (EditText) findViewById(R.id.etCosto);
        etPersonal = (EditText) findViewById(R.id.etPersonal);


        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarTextos();
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idservicio = etIdServicio.getText().toString().trim();

                String url = "https://utic2025.com/silvestre_zarate/recuperar_servicio.php?idservicio=" + idservicio;
                recuperarData(url);
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NombreServicio = etNombreServicio.getText().toString().trim();
                costo = etCosto.getText().toString().trim();
                personal = etPersonal.getText().toString().trim();


                String url = "https://utic2025.com/silvestre_zarate/insertar_servicio.php?NombreServicio="
                        + NombreServicio + "&costo="
                        + costo + "&personal="
                        + personal;

                guardarData(url);

            }
        });


        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idservicio = etIdServicio.getText().toString().trim();
                NombreServicio = etNombreServicio.getText().toString().trim();
                costo = etCosto.getText().toString().trim();
                personal = etPersonal.getText().toString().trim();


                String url = "https://utic2025.com/silvestre_zarate/modificar_servicio.php?NombreServicio="
                        + NombreServicio + "&costo="
                        + costo + "&personal="
                        + personal
                        + "&idservicio=" + idservicio;

                guardarData(url);
            }
        });



        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AgregarServicioActivity.this);
                builder.setMessage("¿Desea Eliminar este Servicio?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                idservicio = etIdServicio.getText().toString().trim();
                                String url = "https://utic2025.com/silvestre_zarate/eliminar_servicio.php?idservicio="
                                        + idservicio;
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
                                    etNombreServicio.setText(object.getString(
                                            "NombreServicio"));
                                    etCosto.setText(object.getString("costo"));
                                    etPersonal.setText(object.getString("personal"));


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
                            Toast.LENGTH_LONG).show();
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
        etIdServicio.setText(null);
        etNombreServicio.setText(null);
        etCosto.setText(null);
        etPersonal.setText(null);

    }
}