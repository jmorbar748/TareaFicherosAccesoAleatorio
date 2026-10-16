package main.java.tarea;

import org.iesalandalus.programacion.utilidades.Entrada;

public class Main {

    public static void main(String[] args) {
        AleatorioDepart gestor = new AleatorioDepart();

        boolean modoDiagnostico = gestor.integridad();

        if (modoDiagnostico) {
            System.out.println("    ATENCIÓN EL FICHERO PRESENTA ANOMALÍAS  ");
            System.out.println("    Modo diagnóstico activado. Solo se permiten las opciones 7, 8, 9 y 0");
        }

        int opcion;

        do {
            System.out.println("1. Insertar departamento");
            System.out.println("2. Consultar departamento");
            System.out.println("3. Eliminar departamento");
            System.out.println("4. Actualizar número de empleados");
            System.out.println("5. Actualizar nombre y localidad");
            System.out.println("6. Listar departamentos");
            System.out.println("7. Inspección física");
            System.out.println("8. Estadísticas");
            System.out.println("9. Integridad");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = Entrada.entero();

            if (modoDiagnostico && opcion != 7 && opcion != 8 && opcion != 9 && opcion != 0) {
                System.out.println("En modo diagnóstico solo se permiten las opciones 7,8,9 y 0");
                continue;
            }

            switch (opcion) {

                case 1:
                    System.out.print("Número: ");
                    short num = (short) Entrada.entero();
                    System.out.print("Nombre: ");
                    String nombre = Entrada.cadena();
                    System.out.print("Localidad: ");
                    String localidad = Entrada.cadena();
                    System.out.print("Empleados: ");
                    short empleados = (short) Entrada.entero();
                    gestor.insertaDepartamento(num, nombre, localidad, empleados);
                    break;

                case 2:
                    System.out.print("Número: ");
                    gestor.consultaDepartamento((short) Entrada.entero());
                    break;

                case 3:
                    System.out.print("Número: ");
                    short numEliminar = (short) Entrada.entero();
                    System.out.print("¿Seguro que quieres eliminar? (s/n): ");
                    String confirmacion = Entrada.cadena();
                    if (confirmacion.equalsIgnoreCase("s")) {
                        gestor.eliminaDepartamento(numEliminar);
                    } else {
                        System.out.println("Operación cancelada.");
                    }
                    break;

                case 4:
                    System.out.print("Número: ");
                    short numAct = (short) Entrada.entero();
                    System.out.print("Nuevo número de empleados: ");
                    short nuevosEmp = (short) Entrada.entero();
                    gestor.actualizaNumeroEmpleados(numAct, nuevosEmp);
                    break;

                case 5:
                    System.out.print("Número: ");
                    short numAct2 = (short) Entrada.entero();
                    System.out.print("Nuevo nombre: ");
                    String nuevoNom = Entrada.cadena();
                    System.out.print("Nueva localidad: ");
                    String nuevaLoc = Entrada.cadena();
                    gestor.actualizaNombreLocalidad(numAct2, nuevoNom, nuevaLoc);
                    break;

                case 6:
                    gestor.listadoDepartamentos();
                    break;

                case 7:
                    gestor.inspeccionFisica();
                    break;

                case 8:
                    gestor.estadisticas();
                    break;

                case 9:
                    gestor.integridad();
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);

        gestor.cerrar();

    }
}
