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

    public Controleur(Modele modele) {
        this.modele = modele;


        chargerButton.setOnAction((action) -> {
            try {
                System.out.println("Bouton 'charger' a été cliqué");      // for testing purposes, we can remove this
                this.charger();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        envoyerButton.setOnAction((action) -> {
            try {
                System.out.println("Bouton 'envoyer' a été cliqué");      // for testing purposes, we can remove this
                this.envoyer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Charge le code et le nom du cours dans une table selon le semestre selectionne
     * @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     * @throws ClassNotFoundException Si la classe d'un objet serialise est corrompue
     */
    @FXML
    private void charger() throws IOException, ClassNotFoundException {
        // semester selected from ComboBox
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

    // NEEDS REWORKING
    @FXML
    private void envoyer() throws IOException {

        String selectedSemester = semester.getValue();
        modele.registerStudent(firstName, lastName, email, matricule, selectedCourse);

        // Set the selection mode of the table to single
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        String selectedCode = "";
        String selectedCourse = "";

        // Add event handler to courseTable
        table.setOnMouseClicked(event -> {
            // Get the selected row
            Course selectedRow = table.getSelectionModel().getSelectedItem();

            // Get the value of the code and course fields
            selectedCode = selectedRow.getCode();
            selectedCourse = selectedRow.getName();
        });

        // Check that a course was chosen from table
        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Vous devez sélectionner un cours!");
            alert.showAndWait();
        }

        // Get form input values
        String firstName = firstNameTextfield.getText();
        String lastName = lastNameTextfield.getText();
        String email = emailTextfield.getText();
        String matricule = matriculeTextfield.getText();
        List<String> errorMessages = modele.validateForm(firstName, lastName, email, matricule);

        // If registration form is valid and a course is selected from table -> call Modele.registerStudent
        if (errorMessages.isEmpty() && selectedRow != null) {
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
                    " est inscrit(e) avec succès pour le cours " + selectedCode + "!");
            alert.showAndWait();
        } else {
            // Display error message
            for (String errorMessage : errorMessages) {
                showAlert(errorMessage);
            }
        }
    }

    /**
     Affiche une alerte avec un message d'erreur donne en paramètre
     @param errorMessage le message d'erreur a afficher dans l'alerte
     */
    private void showAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
