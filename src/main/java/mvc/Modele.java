package mvc;

import client.Client;
import javafx.scene.control.TableView;
import server.models.Course;

import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;

public class Modele {
    private Button charger;
    private Button envoyer;
    private TableView<Course> courseTable;

    public Modele(Button charger, Button envoyer, TableView<Course> courseTable) {
        this.charger = charger;
        this.envoyer = envoyer;
        this.courseTable = courseTable;
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

    private Course selectedCourse;

    public void setSelectedCourse(Course course) {
        selectedCourse = course;
    }

    // Load data from 'cours.txt' (part 1)
    public ArrayList<Course> loadCourses(String semester) throws IOException, ClassNotFoundException {
        return Client.courseMenu(semester);
    }

    // Save data to 'inscription.txt' if registration form is valid (part 2)
    public void registerStudent(String semester) {
        Client.registrationMenu(semester);

        // Check that selectedCourse is not null
        if (selectedCourse == null) {
            throw new IllegalStateException("Vous devez sélectionner un cours!");
        }
    }
}
