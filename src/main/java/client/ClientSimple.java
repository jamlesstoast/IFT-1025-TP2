package client;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import server.Server;
import server.models.*;

/**
 * L'application client_simple
 * @author Julie Yang (20239909)
 * @author Celina Zhang (20207461)
 */
public class ClientSimple {
    private final Client client;
    private final static String LOCALHOST = "127.0.0.1";    // Adresse localhost
    private final static int PORT = 1337;                   // Meme port que serveur
    private final static Scanner scanner = new Scanner(System.in);
    private String cmd = Server.LOAD_COMMAND;

    // Simple client constructor
    public ClientSimple(String host, int port) throws IOException {
        this.client = new Client(host, port);
    }

    /**
     * Creer des demandes de commande
     * @throws IOException Erreur d'entree/sortie
     * @throws ClassNotFoundException Erreur de lecture du type d'objet serialise
     */
    public void runClient() throws IOException, ClassNotFoundException {
        String semester = null;
        boolean bool = true;

        while (bool) {
            switch (cmd){
                case Server.LOAD_COMMAND:
                    semester = semesterMenu();
                    courseMenu(semester);
                    commandMenu();
                    break;

                case Server.REGISTER_COMMAND:
                    registrationMenu(semester);
                    System.out.println(client.message());
                    bool = false;
                    break;
            }
        }
    }

    /**
     * Permet a l'utilisateur de faire un choix d'action
     */
    public void commandMenu() throws IOException, ClassNotFoundException {
        int choice = choiceValidation("1. Consulter les cours offerts pour une autre session\n" +
                "2. Inscription à un cours\n" +
                "> Choix: ");

        switch (choice) {
            case 1:
                cmd = Server.LOAD_COMMAND;
                break;
            case 2:
                cmd = Server.REGISTER_COMMAND;
                break;
            default: {
                System.out.println("Choix invalide");
                commandMenu();
            }
        }
    }

    /**
     * Permet a l'utilisateur de choisir la session a consulter
     * @return La session choisie par l'utilisateur
     */
    public String semesterMenu() {
        String semester = null;

        int choice = choiceValidation(
                "Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:\n" +
                        "1. Automne\n" +
                        "2. Hiver\n" +
                        "3. Ete\n" +
                        "> Choix: ");

        switch (choice) {
            case 1:
                semester = "Automne";
                break;
            case 2:
                semester = "Hiver";
                break;
            case 3:
                semester = "Ete";
                break;
            default: {
                System.out.println("Choix invalide");
                semesterMenu();
            }
        }

        return semester;
    }

    /**
     * Affiche la liste de cours offerts à une session voulue
     * @param semester La session desiree par l'utilisateur
     * @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     * @throws ClassNotFoundException Si une classe d'un objet serialise est corrompu
     */
    public void courseMenu(String semester) throws IOException, ClassNotFoundException {
        System.out.println("Les cours offerts pendant la session d'" + semester + " sont: ");

        ArrayList<Course> courses = client.loadCourse(semester);
        int i = 1;

        for (Course course: courses){
            System.out.println( i + ". " + course.getCode() + "\t" + course.getName());
            i++;
        }

        System.out.print("\n");
    }

    /**
     * Saisit les informations de l'utilisateur et gère la validation de ces informations
     * @param semester La session du cours desire par l'utilisateur
     * @throws IOException Si une erreur est survenue au stream d'entree/sortie
     */
    public void registrationMenu(String semester) throws IOException {
        boolean validate = true;
        ArrayList<String> errors = new ArrayList<>();

        while (validate) {
            System.out.print("Veuillez saisir votre prenom: ");
            String prenom = scanner.nextLine();

            System.out.print("Veuillez saisir votre nom: ");
            String nom = scanner.nextLine();

            System.out.print("Veuillez saisir votre email: ");
            String email = scanner.nextLine();

            System.out.print("Veuillez saisir votre matricule: ");
            String matricule = scanner.nextLine();

            System.out.print("Veuillez saisir le code du cours: ");
            String code = scanner.nextLine();
            Course course = new Course(null, code, semester);

            if (prenom.equals("")) {
                errors.add("Prenom est invalide");
            }
            if (nom.equals("")) {
                errors.add("Nom est invalide");
            }
            if (!email.matches("(.+)@(.+)")) {
                errors.add("Email est invalide");
            }
            if (!matricule.matches("([0-9]{8})")) {
                errors.add("Matricule est invalide");
            }
            if (!code.matches("[A-Z]{3}[0-9]{4}")) {
                errors.add("Code du cours est invalide");
            }

            if (errors.isEmpty()) {
                client.createForm(prenom, nom, email, matricule, course);
                validate = false;
            }
            else {
                for (String e : errors){
                    System.out.println(e);
                }
                errors.clear();
            }
        }
    }

    /**
     * Validation du type d'entree de l'utilisateur
     * @param msg Message à afficher à l'utilisateur
     * @return Le chiffre saisi par l'utilisateur
     */
    public int choiceValidation(String msg) {
        int choice = 0;
        do {
            try {
                System.out.print(msg);
                choice = Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException nfe) {
                System.out.println("Choix invalide");
                choice = -1;
            }
        } while (choice == -1);

        return choice;
    }

    /**
     * Lance l'application client_simple
     * @param args Argument inutilise
     */
    public static void main(String[] args) {
        ClientSimple client;
        try{
            client = new ClientSimple(LOCALHOST, PORT);
            System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
            client.runClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}