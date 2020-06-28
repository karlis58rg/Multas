package mx.ssg.multas.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {
    public static final String DataBase_Name = "AppMultas";
    public static final String Table_Tabulador = "CatTabulador";
    public static final int Database_Version = 1;
    public static final String Create_Tabulador = "CREATE TABLE IF NOT EXISTS " + Table_Tabulador +"(Clave INTEGER PRIMARY KEY, Descripcion TEXT NOT NULL UNIQUE, Articulos TEXT ,IdFraccion TEXT , SalMinimos INTEGER)";
    public static final String Delete_Tabulador = "DROP TABLE IF EXISTS "+Table_Tabulador;

    public DataHelper(Context context){
        super(context,DataBase_Name,null,Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Tabulador);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Delete_Tabulador);
        onCreate(db);
    }

    public void insertTabulador(int clave,String descripcion,String articulos,String idInfraccion,int salMinimos){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("Clave",clave);
            values.put("Descripcion",descripcion);
            values.put("Articulos",articulos);
            values.put("IdFraccion",idInfraccion);
            values.put("SalMinimos",salMinimos);
            dbSqLiteDatabase.insert(Table_Tabulador,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){e.printStackTrace();}
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }

    public ArrayList<String> getAllTabulador(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_Tabulador;
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String asehCargo = cursor.getString(cursor.getColumnIndex("Descripcion"));
                    list.add(asehCargo);
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }
}
