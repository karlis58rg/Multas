package mx.ssg.multas.SqLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelperOffLine extends SQLiteOpenHelper {
    public static final String DataBase_Name = "AppMultas";
    public static final String Table_Tabulador = "CatTabulador";
    public static final int Database_Version = 1;
    public static final String Create_Tabulador = "CREATE TABLE IF NOT EXISTS " + Table_Tabulador +"(Clave INTEGER PRIMARY KEY, Descripcion TEXT NOT NULL UNIQUE, Articulos TEXT ,IdFraccion TEXT , SalMinimos INTEGER)";
    public static final String Delete_Tabulador = "DROP TABLE IF EXISTS "+Table_Tabulador;

    public DataHelperOffLine(Context context){
        super(context,DataBase_Name,null,Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
