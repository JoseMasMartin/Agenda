package josemas.agenda;

import java.util.ArrayList;

public class CitasSingleton
{
    public static final CitasSingleton singleton= new CitasSingleton();
    private ArrayList<Cita> citas;
        private CitasSingleton()
        {
            citas = new ArrayList<Cita>();
        }

        public void agregarCita(Cita cita)
        {
            this.citas.add(cita);
        }
        public ArrayList<Cita> getCita()
        {
            return citas;
        }
}
