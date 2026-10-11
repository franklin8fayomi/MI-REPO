package upc.edu.calculadora;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView pantalla;
    private String expresion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pantalla = findViewById(R.id.pantalla);

        // Conecta todos los botones del teclado con el método click()
        ViewGroup teclado = findViewById(R.id.teclado);
        for (int i = 0; i < teclado.getChildCount(); i++) {
            ViewGroup fila = (ViewGroup) teclado.getChildAt(i);
            for (int j = 0; j < fila.getChildCount(); j++) {
                fila.getChildAt(j).setOnClickListener(this::click);
            }
        }
    }

    public void click(View v) {
        String tecla = v.getTag().toString();

        if (tecla.equals("C")) {
            expresion = "";                       // borrar todo
        } else if (tecla.equals("=")) {
            try {
                double resultado = Evaluador.evaluar(expresion, true);   // true = grados
                expresion = Evaluador.formatear(resultado);
            } catch (ArithmeticException e) {
                pantalla.setText("Error");
                expresion = "";
                return;
            }
        } else {
            expresion = expresion + tecla;        // añadir número, operador o función
        }

        pantalla.setText(expresion.isEmpty() ? "0" : expresion);
    }
}
