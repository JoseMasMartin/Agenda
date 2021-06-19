package josemas.agenda.Models;

public class Agenda
{
    String nombre;
    String apellido;
    int telefono;
    String fecha;
    String hora;
    String category;

    public Agenda(String nombre, String apellido, int telefono, String fecha, String hora, String category) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.fecha = fecha;
        this.hora = hora;
        this.category = category;
    }

}
