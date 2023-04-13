package client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import server.*;
import server.models.*;

/**
 * Cree des connexions qui sont envoyees au serveur et lui envoie des demandes de commande
 * @author Julie Yang (20239909)
 * @author Celina Zhang (20207461)
 */
public class Client {

    private final static String LOCALHOST = "127.0.0.1";        // Adresse du Localhost
    private static ObjectOutputStream objOs;                    // Stream d'entree
    private static ObjectInputStream objIs;                     // Stream de sortie
    private static Scanner scanner = new Scanner(System.in);    // Scanner
    private static String cmd;                                  // La commande à envoyer

    /**
     * Methode generale qui initilise le client et le reinitialise tant qu'il y a des
     * envvoies de commandes
     * @param args Argument inutilise
     * @throws IOException Si une erreur d'entree/sortie est survenue
     * @throws ClassNotFoundException Si une erreur de stream d'entree/sortie est survenue
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");

        Socket clientSocket = createSocket();
        cmd = Server.LOAD_COMMAND;
        String semester = null;
        boolean bool = true;

        while (bool) {
            switch (cmd){
                case Server.LOAD_COMMAND:
                    semester = semesterMenu();
                    courseMenu(semester);
                    commandMenu();
                    clientSocket = createSocket();
                    break;

                case Server.REGISTER_COMMAND:
                    registrationMenu(semester);
                    System.out.println((String) objIs.readObject());
                    bool = false;
                    break;
            }
        }
    }

    /**
     * Saisit le choix de session par l'utilisateur
     * @return La session à consulter
     * @throws IOException Si une erreur d'entree ou de sortie est survenue
     */
    public static String semesterMenu() throws IOException {
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

        objOs.writeObject(Server.LOAD_COMMAND + " " + semester);
        objOs.flush();

        return semester;
    }

    /**
     * Saisit le choix d'action par l'utilisateur
     */
    public static void commandMenu() {
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
     * Affiche la liste de cours offerts à une session voulue
     * @param semester La session desiree par l'utilisateur
     * @return La liste de cours offert à la session desiree
     * @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     * @throws ClassNotFoundException Si une classe d'un objet serialise est corrompu
     */
    public static ArrayList<Course> courseMenu(String semester) throws IOException, ClassNotFoundException {
        System.out.println("Les cours offerts pendant la session d'" + semester + " sont: ");

        ArrayList<Course> courses = (ArrayList<Course>) objIs.readObject();
        int i = 1;

        for (Course course: courses){
            System.out.println( i + ". " + course.getCode() + "\t" + course.getName());
            i++;
        }

        System.out.print("\n");
        return courses;
    }

    /**
     * Saisit les informations de l'utilisateur et gère la validation de ces informations
     * @param semester La session du cours desire par l'utilisateur
     * @throws IOException Si une erreur est survenue au stream d'entree/sortie
     * @return Le formulaire rempli valide
     */
    public static void registrationMenu(String semester) throws IOException {
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
                createForm(prenom, nom, email, matricule, course);
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
     * Creer le formulaire d'inscription de l'utilisateur
     * @param prenom Le prenom de l'utilisateur
     * @param nom Le nom de l'utilisateur
     * @param email L'adresse courriel de l'utilisateur
     * @param matricule La matricule de l'utilisateur
     * @throws IOException Si une erreur est survenue au stream d'entree/sortie
     * @return Le formulaire d'inscription
     */
    public static RegistrationForm createForm(String prenom, String nom, String email, String matricule, Course course) throws IOException {

        RegistrationForm form = new RegistrationForm(prenom, nom, email, matricule, course);

        objOs.writeObject(Server.REGISTER_COMMAND);
        objOs.writeObject(form);
        objOs.flush();

        return form;
    }

    /**
     * Cree un socket à l'adresse du Localhost sur le même port que le serveur
     * @return  Le socket cree
     * @throws IOException Si une erreur d'entree/sortie est survenue
     */
    public static Socket createSocket() throws IOException {
        Socket clientSocket = new Socket(LOCALHOST, ServerLauncher.PORT);
        objOs = new ObjectOutputStream(clientSocket.getOutputStream());
        objIs = new ObjectInputStream(clientSocket.getInputStream());
        return clientSocket;
    }

    /**
     * Validation du type d'entree de l'utilisateur
     * @param msg Message à afficher à l'utilisateur
     * @return Le chiffre saisi par l'utilisateur
     */
    public static int choiceValidation(String msg) {
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
}
