package mvc;

import server.models.Course;
import server.models.RegistrationForm;

import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.*;
import javafx.scene.control.*;

public class Controleur {
    private Vue vue;
    private Modele modele;

    public Controleur(Vue vue, Modele modele) {
        this.vue = vue;
        this.modele = modele;


        this.vue.getChargerButton().setOnAction((action) -> {
            try {
                System.out.println("Bouton 'charger' a été cliqué");      // for testing purposes, we can remove this
                this.charger();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        this.vue.getEnvoyerButton().setOnAction((action) -> {
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
        String selectedSemester = Vue.getSessionList().getValue();
        List<Course> courses = modele.loadCourses(selectedSemester);

        // Clear existing data from table (if new semester is chosen)
        Vue.getCourseTable().getItems().clear();

        // Add new data to table
        Vue.getCourseTable().getItems().addAll(courses);
        Vue.getCodeColumn().setCellValueFactory(new PropertyValueFactory<>("code"));
        Vue.getCourseColumn().setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void envoyer() throws IOException {
        // Button clicked -> Modele is called
        String selectedSemester = Vue.getSessionList().getValue();
        modele.registerStudent(selectedSemester);

        Course selectedCourse = Vue.getCourseTable().getSelectionModel().getSelectedItem();

        // Add event handler to courseTable
        Vue.getCourseTable().setOnMouseClicked(event -> {
            // Set the selected course in the Modele
            modele.setSelectedCourse(selectedCourse);
        });

        // If registration form is valid and a course is selected from table -> call Modele.registerStudent
        if (Modele.validateForm() && selectedCourse != null) {
            try {
                modele.registerStudent(selectedSemester);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Display success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Message");
            alert.setContentText("Félicitations! " + firstName + " " + lastName +
                    " est inscrit(e) avec succès pour le cours " + courseCode + "!");
            alert.showAndWait();
        } else {
            // Display error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Le formulaire est invalide");
            alert.showAndWait();
        }
    }
}
