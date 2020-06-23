package mx.ssg.multas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<String> {



    private ArrayList<String> Clave;
    private ArrayList<String> Descripcion;
    private ArrayList<String> Articulos;
    private ArrayList<String> IdFraccion;
    private ArrayList<String> SalMinimos;
    private Context context;



    public CustomAdapter(@NonNull Context context, ArrayList<String> Clave, ArrayList<String> Descripcion, ArrayList<String> Articulos, ArrayList<String> IdFraccion, ArrayList<String> SalMinimos) {
        super(context, R.layout.activity_tabulador);
        this.context = context;
        this.Clave = Clave;
        this.Descripcion = Descripcion;
        this.Articulos = Articulos;
        this.IdFraccion = IdFraccion;
        this.SalMinimos = SalMinimos;


    }

    @Override
    public int getCount() {
        return Clave.size();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.activity_tabulador_2, parent, false);


        }else {

            final TextView clave = view.findViewById(R.id.clave);
            final TextView descripcion = view.findViewById(R.id.descripcion);
            final TextView articulos = view.findViewById(R.id.articulos);
            final TextView idFraccion = view.findViewById(R.id.idfraccion);
            final TextView salMinimos = view.findViewById(R.id.salminimos);


            clave.setText(Clave.get(position));
            descripcion.setText(Descripcion.get(position));
            articulos.setText(Articulos.get(position));
            idFraccion.setText(IdFraccion.get(position));
            salMinimos.setText(SalMinimos.get(position));
        }
        return view;
    }
}
