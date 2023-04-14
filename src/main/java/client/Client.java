package client;

import server.Server;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Cree des connexions qui sont envoyees au serveur et lui envoie des demandes de commande
 * @author Julie Yang (20239909)
 * @author Celina Zhang (20207461)
 */
public class Client {
    private final String host;         // Adresse de connexion du client
    private final int port;            // Port de connexion du client
    private Socket clientSocket;       // socket cote client
    private ObjectOutputStream objOs;  // Stream d'entree
    private ObjectInputStream objIs;   // Stream de sortie


    // Client constructor
    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    /**
     * Charge la liste de cours qui sont offerts a une session choisie
     * @param semester La session choisie
     * @return La liste de cours pour la session selectionnee
     * @throws IOException Erreur stream d'entree/sortie est survenue
     * @throws ClassNotFoundException Erreur de lecture du type d'objet serialise
     */
    public ArrayList<Course> loadCourse(String semester) throws IOException, ClassNotFoundException {
        createSocket();

        objOs.writeObject(Server.LOAD_COMMAND + " " + semester);
        objOs.flush();

        return (ArrayList<Course>) objIs.readObject();
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
    public RegistrationForm createForm(String prenom, String nom, String email, String matricule, Course course) throws IOException {
        createSocket();

        RegistrationForm form = new RegistrationForm(prenom, nom, email, matricule, course);

        objOs.writeObject(Server.REGISTER_COMMAND);
        objOs.writeObject(form);
        objOs.flush();

        return form;
    }

    /**
     * Cherche le message de retroaction du serveur
     * @return Le message de succes/echec de l'inscription
     * @throws IOException Erreur d'entree/sortie au stream
     * @throws ClassNotFoundException Erreur de lecture du type d'objet serialise
     */
    public String message() throws IOException, ClassNotFoundException {
        return (String) objIs.readObject();
    }

    /**
     * Cree un socket à l'adresse du Localhost sur le même port que le serveur
     * @throws IOException Si une erreur d'entree/sortie est survenue
     */
    public void createSocket() throws IOException {
        clientSocket = new Socket(host, port);
        objOs = new ObjectOutputStream(clientSocket.getOutputStream());
        objIs = new ObjectInputStream(clientSocket.getInputStream());
    }
}
