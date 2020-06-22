package mx.ssg.multas.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import mx.ssg.multas.Contactos;
import mx.ssg.multas.LugaresDePago;
import mx.ssg.multas.R;
import mx.ssg.multas.Tabulador;
import mx.ssg.multas.TarjetasConductor;
import mx.ssg.multas.ViewPDFController;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageView btnTerminal;
    private LinearLayout btnReglamento,btnLugaresPago,btnContacto,btnTabulador;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        /*************************** EVENTO DE LOS BOTONES *******************************/
        btnTerminal = root.findViewById(R.id.imgTerminal);
        btnReglamento = root.findViewById(R.id.lyInicio);
        btnLugaresPago = root.findViewById(R.id.lyCategoria);
        btnContacto = root.findViewById(R.id.lyContacto);
        btnTabulador = root.findViewById(R.id.lyFavoritos);

        btnTerminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TarjetasConductor.class);
                startActivity(i);
            }
        });
        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewPDFController.class);
                startActivity(i);
            }
        });

        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LugaresDePago.class);
                startActivity(i);
            }
        });

        btnContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Contactos.class);
                startActivity(i);
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Tabulador.class);
                startActivity(i);
            }
        });


        /*************************************************************************/

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
}