package mx.ssg.multas.SqLite.Insert;

import android.content.Context;

import mx.ssg.multas.SqLite.DataHelper;

public class CatTabulador {
    Context context;

    public void spinner(){
        DataHelper dataHelper = new DataHelper(context);
        dataHelper.insertTabulador(1,"MANEJAR SIN LICENCIA","51 / 76","",15);
    }

}
