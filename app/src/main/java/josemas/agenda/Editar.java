package josemas.agenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import josemas.agenda.SQlite_OpenHelper.SQlite_OpenHelper;

public class Editar extends AppCompatActivity implements View.OnClickListener{
    Spinner categoria;

    final String[] datos = new String[]{"Actividad Física","Trabajo","Compras", "Recreativo","Otros"};

    ArrayAdapter<String> adaptador;

    EditText etFecha, etHora, etID;
    ImageButton ibObtenerFecha, ibObtenerHora;

    SQlite_OpenHelper sql;
    SQLiteDatabase db;


    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "/";
    public final Calendar c = Calendar.getInstance();
    EditText txtName, txtLast, txtPhone;
    Button btnGuardar, btn_busca, eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        sql= new SQlite_OpenHelper(getApplicationContext());
        etFecha = (EditText) findViewById(R.id.ed_fecha);
        etHora = (EditText) findViewById(R.id.ed_hora);
        txtName = findViewById(R.id.ed_nombre);
        txtLast = findViewById(R.id.ed_apellido);
        txtPhone = findViewById(R.id.ed_telefono);
        ibObtenerFecha = (ImageButton) findViewById(R.id.ImBtFecha);
        ibObtenerHora = (ImageButton) findViewById(R.id.ImBtHora);
        etID = findViewById(R.id.ed_id);
        btnGuardar = findViewById(R.id.bt_actualizar);
        btn_busca= findViewById(R.id.bt_buscar);
        eliminar = findViewById(R.id.bt_eliminar);
        eliminar.setOnClickListener(this);

        btn_busca.setOnClickListener(this);
        ibObtenerFecha.setOnClickListener(this);

        ibObtenerHora.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos);

        categoria = (Spinner)findViewById(R.id.spinner);
        categoria.setAdapter(adaptador);

    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.ImBtFecha)
        {
            final int mes = c.get(Calendar.MONTH);
            final int dia = c.get(Calendar.DAY_OF_MONTH);
            final int anio = c.get(Calendar.YEAR);
            etFecha.setText("Fecha: "+dia+BARRA+(mes+1)+BARRA+anio);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            {
                DatePickerDialog obtenerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        etFecha.setText(dayOfMonth + BARRA + (month+1) + BARRA + year);
                    }
                },anio,mes,dia);
                obtenerFecha.show();
            }
        }
        if (v.getId()==R.id.ImBtHora)
        {
            final int hora = c.get(Calendar.HOUR_OF_DAY);
            final int minuto = c.get(Calendar.MINUTE);
            TimePickerDialog obtenerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    etHora.setText(hourOfDay+ DOS_PUNTOS+minute);
                }
            },hora,minuto,false);
            obtenerHora.show();
        }
        if (v.getId()==R.id.bt_buscar)
        {
            consultar();

        }
        if (v.getId()==R.id.bt_actualizar)
        {
            actualizar();

        }
        if (v.getId()==R.id.bt_eliminar)
        {
            eliminar();

        }
    }

    private void eliminar()
    {
        SQLiteDatabase db = sql.getWritableDatabase();
        String[] parametros= {etID.getText().toString()};
        db.delete("citas","id=?",parametros);
        Toast.makeText(getApplicationContext(), "Se elimino", Toast.LENGTH_LONG).show();

        etID.setText("");
        clear();
        db.close();
    }

    private void actualizar()
    {
        SQLiteDatabase db = sql.getWritableDatabase();
        String[] parametros= {etID.getText().toString()};
        ContentValues values = new ContentValues();
        values.put("nombre", txtName.getText().toString());
        values.put("apellido", txtLast.getText().toString());
        values.put("telefono", txtPhone.getText().toString());
        values.put("fecha", etFecha.getText().toString());
        values.put("hora", etHora.getText().toString());
        values.put("category", categoria.getSelectedItem().toString());

        db.update("citas",values,"id=?",parametros);
        Toast.makeText(getApplicationContext(), "Se actualizo los datos", Toast.LENGTH_LONG).show();
        etID.setText("");
        clear();
        db.close();

    }

    private void consultar()
    {
        SQLiteDatabase db = sql.getReadableDatabase();
        String[] parametros = {txtName.getText().toString()};
        String [] campos = { "id" ,"nombre", "apellido","telefono", "fecha","hora","category"};

           try {

             Cursor cursor = db.query("citas",campos,"nombre=?",parametros,null,null,null);
               if (cursor.moveToFirst())
               {
                   do {
                     etID.setText(cursor.getString(0));
                     txtName.setText(cursor.getString(1));
                     txtLast.setText(cursor.getString(2));
                     txtPhone.setText(cursor.getString(3));

                     etFecha.setText(cursor.getString(4));
                     etHora.setText(cursor.getString(5));
                     switch (cursor.getString(6)){
                         case "Actividad Física":
                                categoria.setSelection(0);
                             break;
                         case "Trabajo":
                             categoria.setSelection(1);
                             break;
                         case "Compras":
                             categoria.setSelection(2);
                             break;
                         case "Recreativo":
                             categoria.setSelection(3);
                             break;
                         case "Otros":
                             categoria.setSelection(4);
                             break;

                     }
                  //   cursor.close();
                   }while(cursor.moveToNext());


               }else{ Toast.makeText(this,"Nose encontraron resultados", Toast.LENGTH_SHORT).show();

               }
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "No existe", Toast.LENGTH_LONG).show();
            clear();
        }
    }
    private void clear()
    {
        txtName.setText("");
        txtLast.setText("");
        txtPhone.setText("");
        categoria.setSelection(0);
        etFecha.setText("");
        etHora.setText("");
    }

    public Cita getDataUI() {

        return new Cita(txtName.getText().toString(), txtLast.getText().toString(), Integer.parseInt(txtPhone.getText().toString()), etFecha.getText().toString(),etHora.getText().toString(),categoria.getSelectedItem().toString());
    }
    public void crearCita(Cita c)
    {
        db= sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre",c.getNombre());
        values.put("apellido", c.getApellido());
        values.put("telefono", c.getTelefono());
        values.put("fecha", c.getFecha());
        values.put("hora", c.getHora());
        values.put("category", c.getCategory());
        long id = db.insert("citas",null,values);
    }
    public void consultarUsuario()
    {
        // User usuarios = new User();
        String cita="";
        // String clausulas[] = new String[]{ids.getText().toString()};
        String campos[] = new String[] {"id" ,"nombre", "apellido","telefono", "fecha","hora,","category"};
        db= sql.getReadableDatabase();
        Cursor c = db.query("citas",campos, "id=?",null,null,null,null);
        if (c.moveToFirst())
        {
            do {


                String nombre= c.getString(1);
                String apellido= c.getString(2);
                String telefono= c.getString(3);
                String fecha= c.getString(4);
                String hora= c.getString(5);;
                String category= c.getString(6);
                cita+= nombre+" "+apellido+" "+telefono+" "+fecha+" "+hora+" "+category+"\n";
                //    usuarios.setNombre(c.getString(1));
                //  usuarios.setEdad(c.getInt(2));

            }while(c.moveToNext());

            //   nombre.setText(usuarios.getNombre());
            //   edad.setText(usuarios.getEdad() +" ");
            //resultado.setText(usuarios);
        }else{ Toast.makeText(this,"Nose encontraron resultados", Toast.LENGTH_SHORT).show();

        }
    }
}