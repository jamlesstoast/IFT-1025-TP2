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
    private Course selectedCourse;
    @FXML private Button chargerButton;
    @FXML private Button envoyerButton;
    @FXML private TextField firstNameTextfield;
    @FXML private TextField lastNameTextfield;
    @FXML private TextField emailTextfield;
    @FXML private TextField matriculeTextfield;
    @FXML private ComboBox<String> semester;
    @FXML private TableView <Course> table;
    @FXML private TableColumn <Course, String> code;
    @FXML private TableColumn <Course, String> cours;

    /**
     * Utilise un modele pour effectuer des operations de traitement de donnees et
     * pour repondre aux interactions de l'utilisateur
     * @param modele le modele utilise par le controleur
     */
    public Controleur(Modele modele) {
        this.modele = modele;
    }

    /**
     * Initialise la page d'inscription
     */
    @FXML
    public void initialize() {
        ObservableList<String> choices = FXCollections.observableArrayList("Automne", "Hiver", "Ete");
        semester.setItems(choices);
        semester.setValue("Hiver");

        selectedCourse = new Course(null, null, null);
        String selectedSemester = semester.getValue();

        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.setOnMouseClicked(event -> {
            Course selectedRow = table.getSelectionModel().getSelectedItem();

            selectedCourse.setName(selectedRow.getName());
            selectedCourse.setCode(selectedRow.getCode());
            selectedCourse.setSession(selectedSemester);
        });
    }

    /**
     * Charge le code et le nom du cours dans une table selon le semestre selectionne
     * @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     * @throws ClassNotFoundException Si la classe d'un objet serialise est corrompue
     */
    @FXML
    private void charger() throws IOException, ClassNotFoundException {
        String selectedSemester = semester.getValue();
        List<Course> courses = modele.loadCourses(selectedSemester);

        table.getItems().clear();

        ObservableList<Course> observableCourses = FXCollections.observableArrayList(courses);
        table.setItems(observableCourses);
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        cours.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    /**
     * Envoie les informations de l'inscription a la classe Modele pour enregistrer l'utilisateur au cours selectionne
     * Affiche des messages d'erreur ou de reussite
     */
    @FXML
    private void envoyer() {
        String firstName = firstNameTextfield.getText();
        String lastName = lastNameTextfield.getText();
        String email = emailTextfield.getText();
        String matricule = matriculeTextfield.getText();
        List<String> errorMessages = modele.validateForm(firstName, lastName, email, matricule);

        if (selectedCourse.getName() == null) {
            errorMessages.add("Vous devez sélectionner un cours!");
        }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || matricule.isEmpty()) {
            errorMessages.add("Vous devez remplir tous les champs du formulaire!");
        }

        if (errorMessages.isEmpty() && selectedCourse.getName() != null) {
            try {
                modele.registerStudent(firstName, lastName, email, matricule, selectedCourse);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Message");
            alert.setContentText("Félicitations! " + firstName + " " + lastName +
                    " est inscrit(e) avec succès pour le cours " + selectedCourse.getCode() + "!");
            alert.showAndWait();

            firstNameTextfield.clear();
            lastNameTextfield.clear();
            emailTextfield.clear();
            matriculeTextfield.clear();
        } else {
            showAlert(errorMessages);
            }
        }

    /**
     * Affiche une alerte avec les messages d'erreurs donnes en parametre
     * @param errorMessages Une liste de chaines de caracteres representant les messages d'erreur a afficher
     */
    private void showAlert(List<String> errorMessages) {

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