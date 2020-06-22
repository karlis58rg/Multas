package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Contactos extends AppCompatActivity {

    private LinearLayout btnReglamento,btnLugaresPago,btnTabulador;
    private ImageView btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        btnReglamento = findViewById(R.id.lyInicio);
        btnLugaresPago = findViewById(R.id.lyCategoria);
        btnTabulador = findViewById(R.id.lyFavoritos);
        btnList = findViewById(R.id.btnList);

        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contactos.this, ViewPDFController.class);
                startActivity(i);
            }
        });
        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contactos.this, LugaresDePago.class);
                startActivity(i);
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contactos.this, Tabulador.class);
                startActivity(i);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contactos.this, Reglamento.class);
                startActivity(i);
            }
        });
    }
}
