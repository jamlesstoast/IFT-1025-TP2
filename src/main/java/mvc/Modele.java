package mvc;

import client.Client;
import server.models.Course;

import java.io.IOException;
import java.util.*;

public class Modele {
    /**
     Charge une liste de cours pour un semestre donne en utilisant la methode {@link Client#loadCourse(String)}
     @param semester Le semestre pour lequel on veut charger les cours
     @return Une liste de cours pour le semestre donne
     @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     @throws ClassNotFoundException Si la classe d'un objet serialise est corrompue
     */
    public ArrayList<Course> loadCourses(String semester) throws IOException, ClassNotFoundException {
        return Client.loadCourse(semester);
    }

    /**
     Valide les champs d'un formulaire et renvoie une liste de messages d'erreur
     @param firstName Le prenom entre dans le formulaire
     @param lastName Le nom entre dans le formulaire
     @param email L'adresse e-mail entree dans le formulaire
     @param matricule Le numero de matricule entre dans le formulaire
     @return Une liste de chaines de caracteres representant les messages d'erreur pour les champs invalides
     */
    public List<String> validateForm(String firstName, String lastName, String email, String matricule) {
        List<String> errorMessages = new ArrayList<>();

        if (!firstName.matches("[A-Z][a-z]*")) {
            errorMessages.add("Le champ 'Prénom' est invalide");
        }

        if (!lastName.matches("[A-Z][a-z]*")) {
            errorMessages.add("Le champ 'Nom' est invalide");
        }

        if (!email.matches("(.+)@(.+)")) {
            errorMessages.add("Le champ 'Email' est invalide");
        }

        if (!matricule.matches("([0-9]{8})")) {
            errorMessages.add("Le champ 'Matricule' est invalide");
        }

        return errorMessages;
    }

    /**
     Enregistre l'utilisateur avec ses informations dans un formulaire en utilisant
     la methode {@link Client#createForm(String, String, String, String, Course)}
     @param firstName Le prenom de l'utilisateur
     @param lastName Le nom de l'utilisateur
     @param email L'adresse e-mail de l'utilisateur
     @param matricule Le matricule de l'utilisateur
     @param course Le cours choisi par l'utilisateur
     @throws IOException Si une erreur est survenue au stream d'entree/sortie
     */
    public void registerStudent(String firstName, String lastName, String email,
                                String matricule, Course course) throws IOException {
        Client.createForm(firstName, lastName, email, matricule, course);
    }
}
