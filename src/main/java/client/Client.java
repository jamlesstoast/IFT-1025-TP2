package client;

import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws IOException {

        ObjectOutputStream objOs;
        ObjectInputStream objIs;

        // Obtenir le stream d'input (données reçues du serveur)
        // clientSocket.getInputStream();
        // Obtenir le stream d'output (pour envoyer des données au serveur)
        // clientSocket.getOutputStream();

        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***\n");
        String semester = semesterMenu();

        while (true) {
            Socket clientSocket = new Socket();
            objOs = new ObjectOutputStream(clientSocket.getOutputStream());
            objIs = new ObjectInputStream(clientSocket.getInputStream());

            Scanner scanner = new Scanner(System.in);

            objOs.writeString(semester);
            objOs.flush();

            courseMenu(semester);
            commandMenu();
        }
    }

    public static String semesterMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:\n" +
                "1. Automne\n" +
                "2. Hiver\n" +
                "3. Ete\n" +
                "> Choix: ");

        int choice = scanner.nextInt();
        String semester = null;

        switch (choice) {
            case 1: semester = "Automne";
            case 2: semester = "Hiver";
            case 3: semester = "Ete";
        }

        return semester;
    }

    public void commandMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Consulter les cours offerts pour une autre session\n" +
                           "2. Inscription à un cours\n" +
                           "> Choix: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1: semesterMenu();
            case 2: registrationMenu();
        }
    }

    public void courseMenu(String semester){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Les cours offerts pendant la session d'" + semester + "sont: ");

        ArrayList<Course> courses = (List<Course>) objIs.readObject();

        for (Course course: courses){
            for (int i = 0; i <= courses.size(); i++)
            System.out.println( i + ". " + course.getCode() + "\t" + course.getName());
        }

        System.out.println("> Choix: ");
    }

    public void registrationMenu(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez saisir votre prénom: ");
        System.out.println("Veuillez saisir votre nom: ");
        System.out.println("Veuillez saisir votre email: ");
        System.out.println("Veuillez saisir votre matricule: ");
        System.out.println("Veuillez saisir le code du cours: ");

        String prenom = scanner.nextLine();
        String nom = scanner.nextLine();
        String email = scanner.nextLine();
        String matricule = scanner.nextLine();
        String code = scanner.nextLine();

        // how to create Course ;w;
        Course course = new Course();

        RegistrationForm rf = new RegistrationForm(prenom, nom, email, matricule, course);
        objOs.writeObject(rf);
        objOs.flush();
    }

    public Socket newSocket() throws IOException {
        Socket clientSocket = new Socket("127.0.0.1", 80);
        return clientSocket;
    }
}
