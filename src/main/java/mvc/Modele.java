package mvc;

import client.Client;
import javafx.scene.control.Alert;
import server.models.Course;
import server.models.RegistrationForm;

import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.TextField;

public class Modele {
    private Button charger;
    private Button envoyer;
    private TableView<Course> courseTable;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField matriculeField;


    public Modele(Button charger, Button envoyer, TableView<Course> courseTable, TextField firstNameField,
                  TextField lastNameField, TextField emailField, TextField matriculeField) {
        this.charger = charger;
        this.envoyer = envoyer;
        this.courseTable = courseTable;
        this.firstNameField = firstNameField;
        this.lastNameField = lastNameField;
        this.emailField = emailField;
        this.matriculeField = matriculeField;
    }

    public Button getChargerButton() {
        return this.charger;
    }

    public Button getEnvoyerButton() {
        return this.envoyer;
    }

    public TableView<Course> getCourseTable() {
        return courseTable;
    }

    public String getFirstNameFieldData() {
        return firstNameField.getText();
    }

    public String getLastNameFieldData() {
        return lastNameField.getText();
    }

    public String getEmailFieldData() {
        return emailField.getText();
    }

    public String getMatriculeFieldData() {
        return matriculeField.getText();
    }

    private Course selectedCourse;

    public void setSelectedCourse(Course course) {
        selectedCourse = course;
    }

    /**
     Charge une liste de cours pour un semestre donné en utilisant la méthode {@link Client#courseMenu(String)}.
     @param semester Le semestre pour lequel on veut charger les cours
     @return Une liste de cours pour le semestre donné
     @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     @throws ClassNotFoundException Si la classe d'un objet sérialisé est corrompue
     */
    public ArrayList<Course> loadCourses(String semester) throws IOException, ClassNotFoundException {
        return Client.courseMenu(semester);
    }

    // Save data to 'inscription.txt' if registration form is valid (part 2)
    public void registerStudent() throws IOException {
        // Get form input values
        String firstName = getFirstNameFieldData();
        String lastName = getLastNameFieldData();
        String email = getEmailFieldData();
        String matricule = getMatriculeFieldData();

        // Create a new student object
        RegistrationForm student = new RegistrationForm(firstName, lastName, email, matricule, selectedCourse);

        // Write the student object to a text file
        Client.registrationMenu(semester);

        // Check that selectedCourse is not null
        if (selectedCourse == null) {
            throw new IllegalStateException("Vous devez sélectionner un cours!");
        }

        // Check that all fields are filled
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || matricule.isEmpty()) {
            throw new IllegalStateException("Veuillez remplir tous les champs du formulaire!");
        }

        // Error alert messages
        if (!firstName.matches("[A-Z][a-z]*")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Le Champ 'Prénom' est invalide");

            alert.showAndWait();
        }
    }
}
