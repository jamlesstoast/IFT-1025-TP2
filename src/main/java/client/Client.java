package client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import server.*;
import server.models.*;


public class Client {

    private final static String LOCALHOST = "127.0.0.1";
    private static ObjectOutputStream objOs;
    private static ObjectInputStream objIs;
    private static Scanner scanner = new Scanner(System.in);
    private static String cmd;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");

        Socket clientSocket = createSocket();
        cmd = Server.LOAD_COMMAND;
        String semester = null;

        while (true) {

            switch (cmd){
                case Server.LOAD_COMMAND:
                    semester = semesterMenu();
                    objOs.writeObject(cmd + " " + semester);
                    objOs.flush();
                    ArrayList<Course> courses = new ArrayList(courseMenu(semester));
                    commandMenu();
                    clientSocket = createSocket();
                    break;

                case Server.REGISTER_COMMAND:
                    objOs.writeObject(cmd);
                    objOs.writeObject(registrationMenu(semester));
                    objOs.flush();
                    System.out.println((String) objIs.readObject());
                    break;
            }
        }
    }

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

        return semester;
    }

    public static void commandMenu() throws IOException {
        int choice = choiceValidation("\n1. Consulter les cours offerts pour une autre session\n" +
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

    public static ArrayList<Course> courseMenu(String semester) throws IOException, ClassNotFoundException {
        System.out.println("Les cours offerts pendant la session d'" + semester + " sont: ");

        ArrayList<Course> courses = (ArrayList<Course>) objIs.readObject();
        int i = 1;

        for (Course course: courses){
            System.out.println( i + ". " + course.getCode() + "\t" + course.getName());
            i++;
        }

        return courses;
    }

    public static RegistrationForm registrationMenu(String semester) throws IOException {
        boolean validate = true;
        ArrayList<String> errors = new ArrayList<>();
        RegistrationForm inscriptionForm = null;

        while (validate) {
            System.out.print("Veuillez saisir votre prénom: ");
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
                System.out.println(prenom);
            }
            if (nom.equals("")) {
                errors.add("Nom est invalide");
                System.out.println(nom);
            }
            if (!email.matches("(.+)@(.+)")) {
                errors.add("Email est invalide");
                System.out.println(email);
            }
            if (!matricule.matches("([0-9]{8})")) {
                errors.add("Matricule est invalide");
                System.out.println(matricule);
            }
            if (!code.matches("[A-Z]{3}[0-9]{4}")) {
                errors.add("Code du cours est invalide");
                System.out.println(code);
            }

            if (errors.isEmpty()) {
                inscriptionForm = new RegistrationForm(prenom, nom, email, matricule, course);
                validate = false;
            }
            else {
                for (String e : errors){
                    System.out.println(e);
                }
                errors.clear();
            }
        }

        return inscriptionForm;
    }

    public static Socket createSocket() throws IOException {
        Socket clientSocket = new Socket(LOCALHOST, ServerLauncher.PORT);
        objOs = new ObjectOutputStream(clientSocket.getOutputStream());
        objIs = new ObjectInputStream(clientSocket.getInputStream());
        return clientSocket;
    }

    public static int choiceValidation(String msg){
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
