package server;

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import server.models.Course;
import server.models.RegistrationForm;

/**
 * Regroupe toutes les methodes liees au serveur qui ecoute les connexions entrantes du client et traite les demandes de
 * commande du client
 * @author Julie Yang (20239909)
 * @author Celina Zhang (20207461)
 */
public class Server {

    /**
     * Nom de la commande d'inscription
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * Nom de la commande de charge de cours
     */
    public final static String LOAD_COMMAND = "CHARGER";
    /**
     * Caractere de tabulation
     */
    private final static String TAB = "\t";
    /**
     * Socket du serveur
     */
    private final ServerSocket server;
    /**
     * Socket du client
     */
    private Socket client;
    /**
     * Stream d'entree
     */
    private ObjectInputStream objectInputStream;
    /**
     * Stream de sortie
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * Liste de gestionnaires d'evenements
     */
    private final ArrayList<EventHandler> handlers;
    /**
     * Liste des cours disponibles
     */
    private ArrayList<Course> courses;

    /**
     * Initialise la liste des gestionnaires d'evenements
     * @param port Le port sur lequel le server ecoute les connexions entrantes
     * @throws IOException Si une erreur d'entree ou de sortie est survenue
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute un gestionnaire d'evenements a la liste des gestionnaires d'evenements
     * @param h Le gestionnaire d'evenement a ajouter
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Alerte tous les gestionnaires d'evenements d'une nouvelle commande
     * @param cmd La nouuvelle commande a traiter
     * @param arg L'argument de la commande
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Ecoute continue des connexions entrantes du client et accepte la connexion
     * Lorsque connecte, initialise les stream d'entrees et de sorties
     */
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

    /**
     * Ecoute les commandes entrantes du client
     * @throws IOException Si une erreur de stream d'entree/sortie est survenue
     * @throws ClassNotFoundException Si la classe d'un objet serialise est invalide
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Traite la ligne de commande et associe l'argument a la commande
     * @param line La ligne de commande a traiter
     * @return Une paire de commande et de son argument
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Deconnecte le client du serveur
     * @throws IOException Si une erreur d'entree/sortie survient lors de la fermeture du socket.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Gere les evenements en fonction de la commande donnee
     * @param cmd la commande a executer
     * @param arg l'argument de la commande
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Filtre les cours disponibles selon une session donnee
     * @param arg La session pour laquelle on veut recuperer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        courses = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/main/java/server/data/cours.txt"));
            String line = reader.readLine();

            while (line != null) {
                String[] course = line.split("\t");
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

            objectOutputStream.writeObject(courses);
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Verifie que le cours du formulaire existe pour la session selectionnee
     * Renvoie l'information du formulaire vers un fichier texte et un message de retroaction au client
     */
    public void handleRegistration() {
        try {
            RegistrationForm rf = (RegistrationForm) objectInputStream.readObject();
            Course course = rf.getCourse();

            String courseCode = course.getCode();
            String semester = course.getSession();
            String firstName = rf.getPrenom();

            boolean validCourse = false;
            String msg = "Échec! Le cours " + courseCode + " n'est pas disponible à la session d'" +
                    semester.toLowerCase() + ".";

            for (Course c: courses) {
                if (c.getCode().equals(courseCode)) {
                    validCourse = true;
                }
            }

            if (validCourse) {
                FileWriter fw = new FileWriter("src/main/java/server/data/inscription.txt", true);

                fw.write(semester + TAB + courseCode + TAB + rf.getMatricule() + TAB + firstName + TAB + rf.getNom()
                        + TAB + rf.getEmail() + "\n");
                fw.close();

                msg = "Félicitations! Inscription réussie de " + firstName + " au cours " + courseCode + ".";
            }

            objectOutputStream.writeObject(msg);
            objectOutputStream.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}