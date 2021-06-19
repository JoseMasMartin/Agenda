package josemas.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListDatesActivity extends AppCompatActivity {
    ArrayList<Cita> arrayListc = new ArrayList<Cita>();
    ListView opciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dates2);
        Cita c = (Cita)getIntent().getSerializableExtra("Cita");
        Toast.makeText(this,c.getCategory(),Toast.LENGTH_LONG).show();

        opciones = findViewById(R.id.lv_1);
        CitasSingleton.singleton.agregarCita(c);
        CitasAdapter adapter = new CitasAdapter(this,CitasSingleton.singleton.getCita());
        opciones.setAdapter(adapter);


    }
}