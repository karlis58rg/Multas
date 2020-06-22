package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Tabulador extends AppCompatActivity {
    private LinearLayout btnReglamento,btnLugaresPago,btnTabulador;
    private ImageView btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabulador);

        btnReglamento = findViewById(R.id.lyInicioTab);
        btnLugaresPago = findViewById(R.id.lyCategoriaTab);
        btnTabulador = findViewById(R.id.lyFavoritosTab);
        btnList = findViewById(R.id.btnListTab);

        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tabulador.this, ViewPDFController.class);
                startActivity(i);
            }
        });
        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tabulador.this, LugaresDePago.class);
                startActivity(i);
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tabulador.this, Tabulador.class);
                startActivity(i);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tabulador.this, Reglamento.class);
                startActivity(i);
            }
        });
    }
}
