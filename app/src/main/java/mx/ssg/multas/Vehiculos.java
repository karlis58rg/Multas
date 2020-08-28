package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class Vehiculos extends AppCompatActivity implements VehiculoPrivado.OnFragmentInteractionListener, VehiculoPublico.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);
        iniciar_fragment();
    }

    public void iniciar_fragment(){
        Fragment fragmento = new VehiculoPrivado();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorVehiculos, fragmento).commit();
    }
}
