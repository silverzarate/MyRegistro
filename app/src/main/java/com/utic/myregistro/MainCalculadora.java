package com.utic.myregistro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainCalculadora extends AppCompatActivity {

    private EditText display;
    private String input = "";
    private double operand1 = 0;
    private double operand2 = 0;
    private String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        display = findViewById(R.id.display);

        // Números
        setupNumberButton(R.id.button0, "0");
        setupNumberButton(R.id.button1, "1");
        setupNumberButton(R.id.button2, "2");
        setupNumberButton(R.id.button3, "3");
        setupNumberButton(R.id.button4, "4");
        setupNumberButton(R.id.button5, "5");
        setupNumberButton(R.id.button6, "6");
        setupNumberButton(R.id.button7, "7");
        setupNumberButton(R.id.button8, "8");
        setupNumberButton(R.id.button9, "9");

        // Operadores
        setupOperatorButton(R.id.buttonAdd, "+");
        setupOperatorButton(R.id.buttonSubtract, "-");
        setupOperatorButton(R.id.buttonMultiply, "*");
        setupOperatorButton(R.id.buttonDivide, "/");

        // Botón de igual
        Button buttonEquals = findViewById(R.id.buttonEquals);
        buttonEquals.setOnClickListener(v -> calculate());

        // Botón de limpiar
        Button buttonClear = findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(v -> clear());
    }

    private void setupNumberButton(int buttonId, String number) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            input += number;
            display.setText(input);
        });
    }

    private void setupOperatorButton(int buttonId, String op) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            if (!input.isEmpty()) {
                operand1 = Double.parseDouble(input);
                operator = op;
                input = "";
            }
        });
    }

    private void calculate() {
        if (!input.isEmpty() && !operator.isEmpty()) {
            operand2 = Double.parseDouble(input);
            double result = 0;
            switch (operator) {
                case "+":
                    result = operand1 + operand2;
                    break;
                case "-":
                    result = operand1 - operand2;
                    break;
                case "*":
                    result = operand1 * operand2;
                    break;
                case "/":
                    if (operand2 != 0) {
                        result = operand1 / operand2;
                    } else {
                        display.setText("Error");
                        return;
                    }
                    break;
            }
            input = String.valueOf(result);
            display.setText(input);
            operator = "";
        }
    }

    private void clear() {
        input = "";
        operand1 = 0;
        operand2 = 0;
        operator = "";
        display.setText("");
    }
}