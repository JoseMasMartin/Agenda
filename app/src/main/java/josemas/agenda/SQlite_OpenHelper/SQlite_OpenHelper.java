package josemas.agenda.SQlite_OpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQlite_OpenHelper extends SQLiteOpenHelper {

    public static final String CREAR_TABLA_USUARIO= "CREATE TABLE citas(id INTEGER PRIMARY KEY, nombre TEXT, apellido TEXT, telefono INTEGER, fecha TEXT, hora TEXT, category TEXT)";
    public static final String DB_NAME ="demo";
    public static final int DB_version=1;

    public SQlite_OpenHelper(Context context) {
        super(context, DB_NAME, null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_USUARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
