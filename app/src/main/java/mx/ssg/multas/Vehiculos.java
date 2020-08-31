package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class Vehiculos extends AppCompatActivity implements VehiculoPublico.OnFragmentInteractionListener,VehiculoPrivado.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);
        //iniciar_fragmentVP();
        iniciar_fragmentTP();
    }

    public void iniciar_fragmentVP(){
        Fragment fragmento = new VehiculoPrivado();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorVehiculos, fragmento).commit();
    }

    public void iniciar_fragmentTP(){
        Fragment fragmento = new VehiculoPublico();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorVehiculos, fragmento).commit();
    }
}
