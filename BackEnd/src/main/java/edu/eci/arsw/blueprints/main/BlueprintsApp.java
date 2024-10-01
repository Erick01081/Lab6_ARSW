package edu.eci.arsw.blueprints.main;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

@SpringBootApplication
@ComponentScan(basePackages = { "edu.eci.arsw.blueprints" })
public class BlueprintsApp {

    public static void main(String[] args) {
        SpringApplication.run(BlueprintsApp.class, args);
    }

    @Bean
    public CommandLineRunner run(BlueprintsServices bbpServices) {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("=====Aplicación de Gestor de Planos=====");
                System.out.println("");
                System.out.println(
                        "Digita una opción:\n1.Buscar plano por autor y nombre\n2.Consultar planos de un autor\n3.Registrar un nuevo plano\n4.Consultar todos los planos\n5.Salir");
                System.out.println("");
                String selection0 = scanner.nextLine();

                try {
                    switch (selection0) {
                        case "1":
                            System.out.println("Digite el nombre del autor");
                            String name = scanner.nextLine();
                            System.out.println("Digite el nombre del plano");
                            String author = scanner.nextLine();
                            System.out.println(bbpServices.getBlueprint(name, author));
                            break;
                        case "2":
                            System.out.println("Digite el nombre del autor");
                            String authorName = scanner.nextLine();
                            System.out.println(bbpServices.getBlueprintsByAuthor(authorName));
                            break;
                        case "3":
                            System.out.println("Digite el nombre del autor");
                            String newAuthor = scanner.nextLine();
                            System.out.println("Digite el nombre del plano");
                            String newName = scanner.nextLine();
                            System.out.println("Digite el numero de puntos que tendrá el plano");
                            int numberOfPoints = Integer.parseInt(scanner.nextLine());
                            bbpServices.addNewBlueprint(
                                    new Blueprint(newAuthor, newName, getPointsFromUser(scanner, numberOfPoints)));
                            System.out.println("Nuevo plano registrado con éxito.");
                            break;
                        case "4":
                            System.out.println("Planos existentes: ");
                            System.out.println(bbpServices.getAllBlueprints());
                            break;
                        case "5":
                            running = false;
                            System.out.println("Saliendo de la aplicación...");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    }
                } catch (BlueprintNotFoundException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Entrada no válida. Por favor, ingrese un número.");
                } catch (Exception e) {
                    System.out.println("Error inesperado: " + e.getMessage());
                }
                System.out.println(); // Add a blank line for readability
            }
            scanner.close();
        };
    }

    private Point[] getPointsFromUser(Scanner scanner, int numberOfPoints) {
        Point[] points = new Point[numberOfPoints];

        for (int i = 0; i < numberOfPoints; i++) {
            System.out.println("Digite las coordenadas del punto " + (i + 1) + " en el formato 'x y':");
            String[] coordinates = scanner.nextLine().split(" ");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            points[i] = new Point(x, y);
        }

        return points;
    }
}
