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

        while (true) {
            String semester = null;

            switch (cmd){
                case Server.LOAD_COMMAND:
                    semester = semesterMenu();
                    objOs.writeObject(cmd + " " + semester);
                    objOs.flush();
                    courseMenu(semester);
                    commandMenu();
                    clientSocket = createSocket();
                    break;

                case Server.REGISTER_COMMAND:
                    objOs.writeObject(cmd);
                    objOs.writeObject(registrationMenu());
                    objOs.flush();
                    System.out.println((String) objIs.readObject());
                    break;
            }
        }
    }

    public static String semesterMenu() throws IOException {
        System.out.print("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:\n" +
                "1. Automne\n" +
                "2. Hiver\n" +
                "3. Ete\n" +
                "> Choix: ");

        int choice = scanner.nextInt();
        String semester = null;

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
        }

        return semester;
    }

    public static void commandMenu() throws IOException {
        System.out.print("1. Consulter les cours offerts pour une autre session\n" +
                        "2. Inscription à un cours\n" +
                        "> Choix: ");

        int choice = scanner.nextInt();

        if (choice == 2) {
            cmd = Server.REGISTER_COMMAND;
        }
    }

    public static void courseMenu(String semester) throws IOException, ClassNotFoundException {
        System.out.println("Les cours offerts pendant la session d'" + semester + " sont: ");

        ArrayList<Course> courses = (ArrayList<Course>) objIs.readObject();
        int i = 1;

        for (Course course: courses){
            System.out.println( i + ". " + course.getCode() + "\t" + course.getName());
            i++;
        }
    }

    public static RegistrationForm registrationMenu() throws IOException {
        scanner.nextLine();

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
        Course course = new Course(null, code, null);

        RegistrationForm inscriptionForm = new RegistrationForm(prenom, nom, email, matricule, course);
        System.out.println(inscriptionForm);

        return inscriptionForm;
    }

    public static Socket createSocket() throws IOException {
        Socket clientSocket = new Socket(LOCALHOST, ServerLauncher.PORT);
        objOs = new ObjectOutputStream(clientSocket.getOutputStream());
        objIs = new ObjectInputStream(clientSocket.getInputStream());
        return clientSocket;
    }
}
