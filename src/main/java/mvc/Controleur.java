package mvc;

import server.models.Course;
import server.models.RegistrationForm;

import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import javafx.scene.control.*;

public class Controleur {
    private Vue vue;
    private Modele modele;
    private ComboBox<String> sessionList;
    private TableView<Course> courseTable;
    private TableColumn<Course, String> courseColumn;
    private TableColumn<Course, String> codeColumn;
    private ObjectInputStream objectInputStream;


    public Controleur(Vue vue, Modele modele, ComboBox<String> sessionList, TableView<Course> courseTable,
                      TableColumn<Course, String> courseColumn, TableColumn<Course, String> codeColumn,
                      ObjectInputStream objectInputStream) {
        this.vue = vue;
        this.modele = modele;
        this.sessionList = sessionList;
        this.courseTable = courseTable;
        this.courseColumn = courseColumn;
        this.codeColumn = codeColumn;
        this.objectInputStream = objectInputStream;


        this.modele.getChargerButton().setOnAction((action) -> {
            try {
                System.out.println("Bouton 'charger' a été cliqué");      // for testing purposes, we can remove this
                this.charger();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        this.modele.getEnvoyerButton().setOnAction((action) -> {
            try {
                System.out.println("Bouton 'envoyer' a été cliqué");      // for testing purposes, we can remove this
                this.envoyer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     Charge le code et le nom du cours dans une table selon le semestre selectionne
     @throws IOException Si une erreur d'entree/sortie est survenue lors de la lecture de stream
     @throws ClassNotFoundException Si la classe d'un objet serialise est corrompue
     */
    private void charger() throws IOException, ClassNotFoundException {
        // Button clicked -> Modele is called
        String selectedSemester = sessionList.getValue();
        List<Course> courses = modele.loadCourses(selectedSemester);

        // Clear existing data from table (if new semester is chosen)
        courseTable.getItems().clear();

        // Add new data to table
        courseTable.getItems().addAll(courses);
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void envoyer() throws IOException {
        // Button clicked -> Modele is called
        String semester = sessionList.getValue();
        modele.registerStudent();

        // Add event handler to courseTable -> course needs to be chosen for registration form to be sent
        courseTable.setOnMouseClicked(event -> {
            // Get the selected course from the table view
            Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            // Set the selected course in the model
            modele.setSelectedCourse(selectedCourse);
        });


        // Display a success message (needs reworking)
        RegistrationForm rf = (RegistrationForm) objectInputStream.readObject();
        Course course = rf.getCourse();

        String courseCode = course.getCode();
        String firstName = rf.getPrenom();
        String lastName = rf.getNom();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Message");
        alert.setContentText("Félicitations! " + firstName + " " + lastName + "est inscrit(e) avec succès pour le cours "
                + courseCode + "!");

        alert.showAndWait();
    }
}
