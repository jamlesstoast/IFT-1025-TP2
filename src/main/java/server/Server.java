package server;

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import server.models.Course;
import server.models.RegistrationForm;

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    public final static String TAB = "\t";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;
    private ArrayList<Course> courses;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
        this.courses = new ArrayList<>();
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        courses = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/main/java/server/data/cours.txt"));
            String line = reader.readLine();

            while (line != null) {
                String[] course = line.split("\t");
                Arrays.toString(course);
                /* course[0] == course code
                   course[1] == course name
                   course[2] == semester
                 */

                if ( course[2].equals(arg) ){
                    Course selectedCourse = new Course(course[1], course[0], course[2]);
                    courses.add(selectedCourse);
                }

                line = reader.readLine();
            }

            System.out.println(courses);
            objectOutputStream.writeObject(courses);
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        System.out.println("handleRegistration()");
        try {
            RegistrationForm rf = (RegistrationForm) objectInputStream.readObject();
            Course course = rf.getCourse();

            System.out.println(rf);

            String courseCode = course.getCode();
            String firstName = rf.getPrenom();
            boolean validCourse = false;

            for (Course c: courses) {

                if (c.getCode().equals(courseCode)) {
                    validCourse = true;
                }
            }

            if (validCourse) {
                BufferedWriter writer =
                        new BufferedWriter(new FileWriter("src/main/java/server/data/inscription.txt"));

                String s = course.getSession() + TAB + courseCode + TAB + rf.getMatricule() + TAB + firstName +
                        TAB + rf.getNom() + TAB + rf.getEmail();

                writer.append(s);
                writer.close();

                String success = "Félicitations! Inscription réussie de " + firstName + " au cours " + courseCode + ".";
                objectOutputStream.writeObject(success);
            }
            else {
                String failure = "Échec! Inscription échoué de " + firstName + " au cours " + courseCode + ".";
                objectOutputStream.writeObject(failure);
            }

            objectOutputStream.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}