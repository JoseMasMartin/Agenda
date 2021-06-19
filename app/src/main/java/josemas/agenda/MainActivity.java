package josemas.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner categoria;

    final String[] datos = new String[]{"Actividad Física","Trabajo","Compras", "Recreativo","Otros"};

    ArrayAdapter<String> adaptador;

    EditText etFecha, etHora;
    ImageButton ibObtenerFecha, ibObtenerHora;

    SQlite_OpenHelper sql;
    SQLiteDatabase db;


    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "/";
    public final Calendar c = Calendar.getInstance();
    EditText txtName, txtLast, txtPhone;
    Button btnGuardar, bt_consul;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sql= new SQlite_OpenHelper(getApplicationContext());
        etFecha = (EditText) findViewById(R.id.ed_fecha);
        etHora = (EditText) findViewById(R.id.ed_hora);
        txtName = findViewById(R.id.ed_nombre);
        txtLast = findViewById(R.id.ed_apellido);
        txtPhone = findViewById(R.id.ed_telefono);
        ibObtenerFecha = (ImageButton) findViewById(R.id.ImBtFecha);
        ibObtenerHora = (ImageButton) findViewById(R.id.ImBtHora);
        btnGuardar = findViewById(R.id.button2);
        bt_consul = findViewById(R.id.bt_consulta);
        bt_consul.setOnClickListener(this);
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
        if (v.getId()==R.id.button2)
        {
            crearCita(getDataUI());
            Intent i = new Intent(this, ListDatesActivity.class);
            i.putExtra("Cita", getDataUI());
            startActivity(i);

        }  if (v.getId()==R.id.bt_consulta)
        {
            Intent intent = new Intent(this, Editar.class);
            startActivity(intent);
        }
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