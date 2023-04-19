package mvc;

import server.models.Course;

import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.*;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.fxml.FXML;

/**
 * Fait le lien entre l'interface graphique (vue) et sa representation logique (modele)
 */
public class Controller {

    /**
     * Le modele de l'interface graphique
     */
    private Model model;

    /**
     * Le cours selectionne
     */
    private Course selectedCourse;

    /**
     * Button qui charge la liste de cours desires
     */
    @FXML private Button chargerButton;

    /**
     * Button qui envoye un formulaire d'inscription
     */
    @FXML private Button envoyerButton;

    /**
     * Le champ de texte pour le prenom
     */
    @FXML private TextField firstNameTextfield;

    /**
     * Le champ de texte pour le nom
     */
    @FXML private TextField lastNameTextfield;

    /**
     * Le champ de texte pour l'adresse courriel
     */
    @FXML private TextField emailTextfield;

    /**
     * Le champ de texte pour le matricule
     */
    @FXML private TextField matriculeTextfield;

    /**
     * Menu deroulant des sessions offertes
     */
    @FXML private ComboBox<String> semester;

    /**
     * Tableau contenant la liste des cours offerts a une session choisie
     */
    @FXML private TableView <Course> table;

    /**
     * Colonne du tableau contenant le code du cours
     */
    @FXML private TableColumn <Course, String> code;

    /**
     * Colonne du tableau contenant le nom du cours
     */
    @FXML private TableColumn <Course, String> cours;

    /**
     * Construit un objet Controleur
     * @param model le modele utilise par le controleur
     */
    public Controller(Model model) {
        this.model = model;
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
        List<Course> courses = model.loadCourses(selectedSemester);

        table.getItems().clear();

        ObservableList<Course> observableCourses = FXCollections.observableArrayList(courses);
        table.setItems(observableCourses);
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        cours.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    /**
     * Envoie les informations de l'inscription a la classe Modele pour enregistrer l'utilisateur au cours selectionne
     * Affiche des messages d'erreurs ou de reussite
     */
    @FXML
    private void envoyer() {
        String firstName = firstNameTextfield.getText();
        String lastName = lastNameTextfield.getText();
        String email = emailTextfield.getText();
        String matricule = matriculeTextfield.getText();
        List<String> errorMessages = model.validateForm(firstName, lastName, email, matricule);

        if (selectedCourse.getName() == null) {
            errorMessages.add("Vous devez sélectionner un cours!");
        }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || matricule.isEmpty()) {
            errorMessages.add("Vous devez remplir tous les champs du formulaire!");
        }

        if (errorMessages.isEmpty() && selectedCourse.getName() != null) {
            try {
                model.registerStudent(firstName, lastName, email, matricule, selectedCourse);
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