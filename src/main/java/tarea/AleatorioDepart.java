package main.java.tarea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

// Cada registro ocupa 64 bytes: 2 (num) + 30 (nombre) + 30 (localidad) + 2 (empleados)
public class AleatorioDepart {

    private static final int TAMANO_REGISTRO = 64;
    private File fichero = new File("AleatorioDepart.dat");
    private RandomAccessFile file;

    public AleatorioDepart() {

        try {
            // Modo lectura/escritura. Si no existe, lo crea
            file = new RandomAccessFile(fichero, "rw");
        } catch (FileNotFoundException e){
            System.out.println("El fichero " + fichero.getName() + " no ha sido encontrado.");
        } catch (IllegalArgumentException e) {
            System.out.println("El modo lectura/escritura del fichero no es correcto.");
        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }
    }

    public void cerrar() {
        try {
            file.close();
        } catch (IOException e) {
            System.out.println("Se ha producido un error al cerrar el fichero.");
        }
    }

    // Calcula el byte  donde empieza el registro del departamento indicado
    // (numero - 1) * 64
    private long calcularOffset(short numero) {
        return (long) (numero - 1) * TAMANO_REGISTRO;
    }


    // Comprueba si la posición del departamento esta dentro del fichero
    private boolean posicionDentroDelFichero(short numero) {
        try {
            return calcularOffset(numero) < file.length();
        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
            return false;
        }
    }

    // Devuelve true si en la posición del número indicado hay un registro activo
    // cuyo identificador coincide. En cualquier otro caso
    // (fuera del fichero, nunca utilizada, eliminada o anómala), devuelve false.
    public boolean existeDepartamento(short numero) {

        try {

            if (numero < 1 || numero > 32767) {
                System.out.println("Número de departamento inválido");
                return false;
            }

            long offset = calcularOffset(numero);
            if (offset >= file.length()) {
                return false;
            }

            file.seek(offset);

            short idAlmacenado= file.readShort();

            return idAlmacenado == numero;

        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
            return false;
        }
    }

    // Muestra los datos del departamento indicado por pantalla.
    public void consultaDepartamento(short numero){

        try {

            if (!existeDepartamento(numero)) {
                System.out.println("El departamento " + numero + " no existe.");
                return;
            }

            file.seek(calcularOffset(numero));

            short num = file.readShort();

            // Leer los 15 caracteres del nombre
            char nombreChars[] = new char[15];
            for (int i = 0; i < 15; i++ ){
                nombreChars[i] = file.readChar();
            }
            String nombre = new String(nombreChars);

            // Leer los 15 caracteres de la localidad
            char localidadChars[] = new char[15];
            for (int i = 0; i < 15; i++) {
                localidadChars[i] = file.readChar();
            }
            String localidad = new String(localidadChars);

            short empleados = file.readShort();

            System.out.println("Número: " + num);
            System.out.println("Nombre: " + Departamento.limpiarTexto(nombre));
            System.out.println("Localidad: " + Departamento.limpiarTexto(localidad));
            System.out.println("Empleados: " + empleados);

        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }
    }

    // Actualiza solo el campo de empleados. El campo ocupa 2 bytes y esta al final del registro (offset + 62)
    public void actualizaNumeroEmpleados(short numero, short nuevoNumEmpleados) {
        try {

            if (!existeDepartamento(numero)) {
                System.out.println("El departamento " + numero + " no existe.");
                return;
            }

            file.seek(calcularOffset(numero) + 62);
            file.writeShort(nuevoNumEmpleados);

            System.out.println("Número de empleados actualizado.");

        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }
    }

    // Actualiza el nombre y la localidad. Se validad ambos textos
    public void actualizaNombreLocalidad(short numero, String nuevoNombre, String nuevaLocalidad) {
        try {

            if (!existeDepartamento(numero)) {
                System.out.println("El departamento " + numero + " no existe.");
                return;
            }

            if (nuevoNombre.trim().isEmpty() || nuevoNombre.trim().length() > 15) {
                System.out.println("Nombre inválido.");
                return;
            }

            if (nuevaLocalidad.trim().isEmpty() || nuevaLocalidad.trim().length() > 15) {
                System.out.println("Localidad inválida");
                return;
            }


            StringBuffer bufferNombre = new StringBuffer(nuevoNombre.trim());
            bufferNombre.setLength(15);
            String nombreFormateado = bufferNombre.toString();

            StringBuffer bufferLocalidad = new StringBuffer(nuevaLocalidad.trim());
            bufferLocalidad.setLength(15);
            String localidadFormateada = bufferLocalidad.toString();

            file.seek(calcularOffset(numero) + 2);
            for (int i = 0; i < 15; i++) {
                file.writeChar(nombreFormateado.charAt(i));
            }

            file.seek(calcularOffset(numero) + 32);
            for (int i = 0; i < 15; i++) {
                file.writeChar(localidadFormateada.charAt(i));
            }

            System.out.println("Nombre y localidad actualizados.");

        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }
    }

    public void insertaDepartamento(short numero, String nombre, String localidad, short numEmpleados) {

        try {
            if (numero < 1 || numero > 32767) {
                System.out.println("Número de departamento inválido.");
                return ;
            }

            if (numEmpleados < 0 || numEmpleados > 32767) {
                System.out.println("Número de empleados inválido (debe estar entre 0 y 32.767).");
                return ;
            }

            if (existeDepartamento(numero)) {
                System.out.println("El departamento " + numero + " ya existe.");
                return;
            }

            if (nombre.trim().isEmpty() || nombre.trim().length() > 15) {
                System.out.println("Nombre inválido.");
                return;
            }

            if (localidad.trim().isEmpty() || localidad.trim().length() > 15) {
                System.out.println("Localidad inválida");
                return;
            }

            StringBuffer bufferNombre = new StringBuffer(nombre.trim());
            bufferNombre.setLength(15);
            String nombreFormateado = bufferNombre.toString();

            StringBuffer bufferLocalidad = new StringBuffer(localidad.trim());
            bufferLocalidad.setLength(15);
            String localidadFormateada = bufferLocalidad.toString();

            long posicionesActuales = file.length() / TAMANO_REGISTRO;
            for (int i = (int) posicionesActuales + 1; i < numero; i++) {
                long offsetHueco = (long)(i - 1) * TAMANO_REGISTRO;
                file.seek(offsetHueco);
                file.writeShort(0);
                for (int j = 0; j < 15; j++) {
                    file.writeChar('-');
                }
                for (int j = 0; j < 15; j++) {
                    file.writeChar('-');
                }
                file.writeShort(0);
            }

            file.seek(calcularOffset(numero));
            file.writeShort(numero);
            for (int j = 0; j < 15; j++) {
                file.writeChar(nombreFormateado.charAt(j));
            }
            for (int j = 0; j < 15; j++) {
                file.writeChar(localidadFormateada.charAt(j));
            }
            file.writeShort(numEmpleados);
        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }
    }

    public void eliminaDepartamento(short numero) {

        try {

            if (!existeDepartamento(numero)) {
                System.out.println("El departamento " + numero + " no existe.");
                return;
            }

            file.seek(calcularOffset(numero));
            file.writeShort(-1);
            for (int i = 0; i < 15; i++) {
                file.writeChar('*');
            }
            for (int i = 0; i < 15; i++) {
                file.writeChar('*');
            }
            file.writeShort(-1);

            System.out.println("Departamento " + numero + " eliminado.");

        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }
    }



    public void listadoDepartamentos() {

        try {

            long totalPosiciones = file.length() / TAMANO_REGISTRO;
            int contador = 0;

            for (int i = 1; i <= totalPosiciones; i++){
                file.seek((long)(i - 1) * TAMANO_REGISTRO);

                short num = file.readShort();

                // Leer los 15 caracteres del nombre
                char nombreChars[] = new char[15];
                for (int j = 0; j < 15; j++ ){
                    nombreChars[j] = file.readChar();
                }
                String nombre = new String(nombreChars);

                // Leer los 15 caracteres de la localidad
                char localidadChars[] = new char[15];
                for (int j = 0; j < 15; j++) {
                    localidadChars[j] = file.readChar();
                }
                String localidad = new String(localidadChars);

                short empleados = file.readShort();

                if (num > 0) {
                    System.out.println("Número: " + num);
                    System.out.println("Nombre: " + Departamento.limpiarTexto(nombre));
                    System.out.println("Localidad: " + Departamento.limpiarTexto(localidad));
                    System.out.println("Empleados: " + empleados);
                    System.out.println("---------------------");
                    contador++;
                }

            }

            if (contador == 0) {
                System.out.println("No hay departamentos activos.");
            } else {
                System.out.println("Total de departamentos mostrados: " + contador);
            }

        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }

    }

    public void inspeccionFisica() {

        try {
            long totalPosiciones = file.length() / TAMANO_REGISTRO;

            if (totalPosiciones == 0) {
                System.out.println("El fichero está vacío.");
                return;
            }

            for (int i = 1; i <= totalPosiciones; i++) {
                long offset = (long)(i - 1) * TAMANO_REGISTRO;
                file.seek(offset);

                short idAlmacenado = file.readShort();
                for (int j = 0; j < 15; j++) {
                    file.readChar();
                }
                for (int j = 0; j < 15; j++) {
                    file.readChar();
                }
                file.readShort();

                String estado;
                if (idAlmacenado == i) {
                    estado = "ACTIVA";
                } else if (idAlmacenado == 0) {
                    estado = "NUNCA UTILIZADA";
                } else if (idAlmacenado == -1) {
                    estado = "ELIMINADA";
                } else {
                    estado = "ANÓMALA";
                }

                System.out.println("Posición: " + i);
                System.out.println("Offset: " + offset);
                System.out.println("ID esperado: " + i);
                System.out.println("ID almacenado: " +idAlmacenado);
                System.out.println("Estado: " + estado);
                System.out.println("-------------");
            }
        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }
    }

    public void estadisticas() {

        try {

            long longitud = file.length();
            long totalPosiciones = longitud / TAMANO_REGISTRO;

            int activos = 0;
            int eliminados = 0;
            int nuncaUtilizadas = 0;
            int sumaEmpleados = 0;

            for (int i = 1; i <= totalPosiciones; i++) {
                file.seek((long) (i - 1) * TAMANO_REGISTRO);

                short id = file.readShort();
                for (int j = 0; j < 15; j++) {
                    file.readChar();
                }
                for (int j = 0; j < 15; j++) {
                    file.readChar();
                }
                short empleados = file.readShort();

                if (id == i) {
                    activos++;
                    sumaEmpleados += empleados;
                } else if (id == -1) {
                    eliminados++;
                } else if (id == 0) {
                    nuncaUtilizadas++;
                }
            }

            double ocupacion = 0;
            if (eliminados + nuncaUtilizadas > 0) {
                ocupacion = (double) activos / (eliminados + nuncaUtilizadas) * 100;
            }

            System.out.println("Longitud del fichero: " + longitud + " bytes");
            System.out.println("Departamentos activos: " + activos);
            System.out.println("Posiciones eliminadas: " + eliminados);
            System.out.println("Posiciones nunca utilizadas: " + nuncaUtilizadas);
            System.out.println("Suma de empleados activos: " + sumaEmpleados);
            System.out.printf("Porcentaje de ocupación: %.2f%%%n", ocupacion);

        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
        }
    }

    public boolean integridad() {

        try {

            long longitud = file.length();

            if (longitud % TAMANO_REGISTRO != 0) {
                System.out.println("ERROR: La longitud del fichero (" + longitud + " bytes) no es múltiplo de 64" );
                return true;
            }

            long totalPosiciones = longitud / TAMANO_REGISTRO;
            boolean hayErrores = false;

            for (int i = 1; i <= totalPosiciones; i++) {
                long offset = (long)(i - 1) * TAMANO_REGISTRO;
                file.seek(offset);

                short id = file.readShort();

                // Leer los 15 caracteres del nombre
                char nombreChars[] = new char[15];
                for (int j = 0; j < 15; j++ ){
                    nombreChars[j] = file.readChar();
                }
                String nombre = new String(nombreChars);

                // Leer los 15 caracteres de la localidad
                char localidadChars[] = new char[15];
                for (int j = 0; j < 15; j++) {
                    localidadChars[j] = file.readChar();
                }
                String localidad = new String(localidadChars);

                short empleados = file.readShort();

                if (id < -1) {
                    System.out.println("ERROR en offset " + offset + ": ID negativo inválido (" + id + ").");
                    hayErrores = true;
                }

                if (id > 0 && id != i) {
                    System.out.println("ERROR en offset " + offset + ": ID " + id + " no corresponde a la posición " + i + ".");
                    hayErrores = true;
                }

                if (id > 0 && empleados < 0) {
                    System.out.println("ERROR en offset " + offset + ": empleados negativos en registro activo (" + empleados + ").");
                    hayErrores = true;
                }

                if (id > 0) {
                    if (Departamento.limpiarTexto(nombre).isEmpty()) {
                        System.out.println("ERROR en offset " + offset + ": nombre vacío en registro activo.");
                        hayErrores = true;
                    }

                    if (Departamento.limpiarTexto(localidad).isEmpty()) {
                        System.out.println("ERROR en offset " + offset + ": localidad vacía en registro activo.");
                        hayErrores = true;
                    }
                }

                if (id == 0) {
                    if (!nombre.equals("---------------") || !localidad.equals("---------------") || empleados !=0) {
                        System.out.println("ERROR en offset " + offset + ": posición nunca utilizada con formato incorrecto.");
                        hayErrores = true;
                    }
                }

                if (id == -1) {
                    if (!nombre.equals("***************") || !localidad.equals("***************") || empleados != -1) {
                        System.out.println("ERROR en offset " + offset + ": registro eliminado sin marcas de borrado.");
                        hayErrores = true;
                    }
                }
            }

            if (!hayErrores) {
                System.out.println("El fichero no presenta errores de integridad.");
            }
            return hayErrores;

        } catch (IOException e) {
            System.out.println("Se ha producido un error de E/S en el fichero.");
            return true;
        }

    }
}