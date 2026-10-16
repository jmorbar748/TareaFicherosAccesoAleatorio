package main.java.tarea;

public class Departamento {

    private short numero;
    private String nombre;
    private String localidad;
    private short numEmpleados;

    public Departamento(short numero, String nombre, String localidad, short numEmpleados) {
        this.numero = numero;
        this.nombre = nombre;
        this.localidad = localidad;
        this.numEmpleados = numEmpleados;
    }

    //Getters y Setters
    public short getNumero() {
        return numero;
    }
    public void setNumero(short numero) {
        this.numero = numero;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    public short getNumEmpleados() {
        return numEmpleados;
    }
    public void setNumEmpleados(short numEmpleados) {
        this.numEmpleados = numEmpleados;
    }


    // Valida que el texto no esté vacio ni supere los 15 caracteres.
    public static void validarTexto(String texto){
        texto = texto.trim();

        if (texto.isEmpty()) {
            throw new IllegalArgumentException("El texto no puede estar vacío.");
        }

        if (texto.length() > 15) {
            throw new IllegalArgumentException("El texto no puede superar los 15 caracteres.");
        }

    }

    //Quita los espacios de relleno del final para mostrar el texto limpio
    public static String limpiarTexto(String texto) {
        return texto.trim();
    }

}
