package mx.ssg.multas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomAdapterTemp extends ArrayAdapter<String> {

    private ArrayList<String> Descripcion;
    private ArrayList<String> SalMinimos;
    private Context context;
    int count = 0;

    public CustomAdapterTemp(@NonNull Context context, ArrayList<String> Descripcion, ArrayList<String> SalMinimos) {
        super(context, R.layout.activity_net_pay);
        this.context = context;
        this.Descripcion = Descripcion;
        this.SalMinimos = SalMinimos;
    }

    @Override
    public int getCount() {
        return Descripcion.size();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.activity_tabulador_2, parent, false);
        }else {

            final TextView descripcion = view.findViewById(R.id.descripcion);
            final TextView salMinimos = view.findViewById(R.id.salminimos);

            descripcion.setText(Descripcion.get(position));
            salMinimos.setText(SalMinimos.get(position));
        }
        return view;
    }
}


