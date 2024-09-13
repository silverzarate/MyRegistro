package com.utic.myregistro;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainPassword extends AppCompatActivity {

    private EditText etPass;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etPass=findViewById(R.id.etPass);
    }
    public void verificar(View view){
        password=etPass.getText().toString();
        if(password.equals("pass777")) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ATENCION");
            builder.setMessage("El password es incorrecto");
            builder.setPositiveButton("Aceptar", null);
            builder.create();
            builder.show();
        }

    }
}