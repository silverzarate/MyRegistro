package com.utic.myregistro;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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



import android.content.DialogInterface;

public class AgregarActivity extends AppCompatActivity {

    Button btnLimpiar,btnMostrar, btnAgregar, btnModificar;
    EditText etIdProducto, etNombre, etPrecio, etCantidad;
    String idproducto, nombre, precio, cantidad;
    FloatingActionButton btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        EdgeToEdge.enable(this);



        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);
        btnMostrar = (Button) findViewById(R.id.btnMostrar);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        etIdProducto  = (EditText) findViewById(R.id.etIdProducto);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        etCantidad = (EditText) findViewById(R.id.etCantidad);

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarTextos();
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idproducto = etIdProducto.getText().toString().trim();

                String url = "https://utic2025.com/silvestre_zarate/recuperar_producto.php?idproducto=" + idproducto;
                recuperarData(url);
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = etNombre.getText().toString().trim();
                precio = etPrecio.getText().toString().trim();
                cantidad = etCantidad.getText().toString().trim();

                String url = "https://utic2025.com/silvestre_zarate/insertar_producto.php?nombre="
                        + nombre + "&precio="
                        + precio + "&cantidad=" + cantidad;

                guardarData(url);

            }
        });


        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idproducto = etIdProducto.getText().toString().trim();
                nombre = etNombre.getText().toString().trim();
                precio = etPrecio.getText().toString().trim();
                cantidad = etCantidad.getText().toString().trim();

                String url = "https://utic2025.com/silvestre_zarate/modificar_producto.php?nombre="
                        + nombre + "&precio="
                        + precio + "&cantidad=" + cantidad
                        + "&idproducto=" + idproducto;

                guardarData(url);
            }
        });



        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AgregarActivity.this);
                builder.setMessage("¿Desea Eliminar este Producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                idproducto = etIdProducto.getText().toString().trim();
                                String url = "https://utic2025.com/silvestre_zarate/eliminar_producto.php?idproducto="
                                        + idproducto;
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
                                    etNombre.setText(object.getString(
                                            "nombre"));
                                    etPrecio.setText(object.getString("precio"));
                                    etCantidad.setText(object.getString("cantidad"));

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
        etIdProducto.setText(null);
        etNombre.setText(null);
        etPrecio.setText(null);
        etCantidad.setText(null);
    }
}