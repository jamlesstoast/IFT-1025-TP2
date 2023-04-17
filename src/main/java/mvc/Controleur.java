package mvc;

import server.models.Course;

import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.*;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.fxml.FXML;

public class Controleur {
    private Modele modele;
    @FXML
    private Button chargerButton;
    @FXML
    private Button envoyerButton;
    @FXML
    private TextField firstNameTextfield;
    @FXML
    private TextField lastNameTextfield;
    @FXML
    private TextField emailTextfield;
    @FXML
    private TextField matriculeTextfield;
    @FXML
    private ComboBox<String> semester;
    @FXML
    private TableView <Course> table;
    @FXML
    private TableColumn <Course, String> code;
    @FXML
    private TableColumn <Course, String> cours;

    /**
     * Utilise un modele pour effectuer des operations de traitement de donnees et
     * pour repondre aux interactions de l'utilisateur
     * @param modele le modele utilise par le controleur
     */
    public Controleur(Modele modele) {
        this.modele = modele;
    }

    /**
     * Initialise ComboBox avec les choix "Automne", "Hiver" et "Été"
     */
    @FXML
    public void initialize() {
        // Add value to dropdown list
        ObservableList<String> choices = FXCollections.observableArrayList("Automne", "Hiver", "Ete");
        semester.setItems(choices);
        // Default value when program runs
        semester.setValue("Hiver");
    }

    /**
     * Charge le code et le nom du cours dans une table selon le semestre selectionne
     * @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     * @throws ClassNotFoundException Si la classe d'un objet serialise est corrompue
     */
    @FXML
    private void charger() throws IOException, ClassNotFoundException {
        // Load courses for semester selected from ComboBox
        String selectedSemester = semester.getValue();
        List<Course> courses = modele.loadCourses(selectedSemester);

        // Clear existing data from table (if new semester is chosen)
        table.getItems().clear();

        // Add new data to table
        ObservableList<Course> observableCourses = FXCollections.observableArrayList(courses);
        table.setItems(observableCourses);
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        cours.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    /**
     * Envoie les informations du formulaire d'inscription de l'utilisateur a la classe Modele
     * pour enregistrer l'utilisateur pour le cours selectionne dans la table
     * Affiche egalement des messages d'erreur ou de reussite
     * @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     */
    @FXML
    private void envoyer() throws IOException {
        // Get form input values
        String firstName = firstNameTextfield.getText();
        String lastName = lastNameTextfield.getText();
        String email = emailTextfield.getText();
        String matricule = matriculeTextfield.getText();
        List<String> errorMessages = modele.validateForm(firstName, lastName, email, matricule);
        Course selectedCourse = new Course(null, null, null);
        String selectedSemester = semester.getValue();

        // Set the selection mode to single (can only pick 1 course)
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Add event handler to courseTable
        table.setOnMouseClicked(event -> {
            // Get the selected row
            Course selectedRow = table.getSelectionModel().getSelectedItem();

            // Get the value of the code and course fields
            selectedCourse.setName(selectedRow.getName());
            selectedCourse.setCode(selectedRow.getCode());
            selectedCourse.setSession(selectedSemester);
        });

        // Check that a course was chosen from table
        if (selectedCourse.getName() == null) {
            errorMessages.add("Vous devez sélectionner un cours!");
        }

        // Check that all fields are filled
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || matricule.isEmpty()) {
            errorMessages.add("Vous devez remplir tous les champs du formulaire!");
        }

        // If registration form is valid and a course is selected from table -> call Modele.registerStudent
        if (errorMessages.isEmpty() && selectedCourse.getName() != null) {
            try {
                modele.registerStudent(firstName, lastName, email, matricule, selectedCourse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Display success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Message");
            alert.setContentText("Félicitations! " + firstName + " " + lastName +
                    " est inscrit(e) avec succès pour le cours " + selectedCourse.getCode() + "!");
            alert.showAndWait();
        } else {
            // Display error message
            showAlert(errorMessages);
            }
        }

    /**
     * Affiche une alerte avec les messages d'erreurs donnes en parametre
     * @param errorMessages Une liste de chaines de caracteres representant les messages d'erreur a afficher
     */
    private void showAlert(List<String> errorMessages) {

        // For messages to be shown one under the other
        StringBuilder stringBuilder = new StringBuilder();
        for (String errorMessage : errorMessages) {
            stringBuilder.append(errorMessage).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(stringBuilder.toString());
        alert.showAndWait();
    }
}