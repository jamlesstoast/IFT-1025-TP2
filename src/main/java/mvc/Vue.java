package mvc;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import server.models.Course;

/**
 Cree une interface graphique pour un système d'inscription
 Elle affiche un formulaire d'inscription et une liste de cours disponibles
 */
public class Vue extends SplitPane {
        private TextField firstNameField = new TextField();
        private TextField lastNameField = new TextField();
        private TextField emailField = new TextField();
        private TextField matriculeField = new TextField();
        private Button charger = new Button("charger");
        private Button envoyer = new Button("envoyer");
        private TableView<Course> courseTable = new TableView<>();
        private TableColumn<Course, String> codeColumn = new TableColumn<>("Code");
        private TableColumn<Course, String> courseColumn = new TableColumn<>("Cours");
        private ComboBox<String> sessionList = new ComboBox<>();


        public Vue() {

                // Setting up the stage
                VBox rightVertical = new VBox();
                VBox leftVertical = new VBox();
                HBox leftHorizontal = new HBox();

                // Creating registration form
                GridPane registrationForm = new GridPane();
                registrationForm.setAlignment(Pos.CENTER);
                registrationForm.setHgap(10);
                registrationForm.setVgap(10);
                registrationForm.setPadding(new Insets(25, 25, 25, 25));

                // add form field
                registrationForm.add(new Text("Formulaire d'inscription"), 0, 0);
                registrationForm.add(new Label("Prénom"), 0, 1);
                registrationForm.add(firstNameField, 1, 1);
                registrationForm.add(new Label("Nom"), 0, 2);
                registrationForm.add(lastNameField, 1, 2);
                registrationForm.add(new Label("Email"), 0, 3);
                registrationForm.add(emailField, 1, 3);
                registrationForm.add(new Label("Matricule"), 0, 4);
                registrationForm.add(matriculeField, 1, 4);

                // set layout for the form
                registrationForm.setHgap(10);
                registrationForm.setVgap(10);
                registrationForm.setPadding(new Insets(10, 10, 10, 10));

                // Creating buttons
                charger.setTextAlignment(TextAlignment.CENTER);
                charger.setAlignment(Pos.BOTTOM_RIGHT);
                envoyer.setTextAlignment(TextAlignment.CENTER);
                envoyer.setAlignment(Pos.CENTER);

                // Creating separators
                Separator sepHorizontal = new Separator();
                sepHorizontal.setMaxWidth(50);
                Separator sepVertical = new Separator();
                sepVertical.setOrientation(Orientation.VERTICAL);

                // Creating table
                courseTable.getColumns().add(codeColumn);
                courseTable.getColumns().add(courseColumn);
                courseTable.setPlaceholder(new Label("No content in table"));

                // Creating dropdown list
                sessionList.getItems().addAll("Hiver", "Automne", "Été");

                // Liste des cours (left)
                leftVertical.getChildren().add(0, new Text("Liste des cours"));
                leftVertical.getChildren().add(courseTable);
                leftVertical.getChildren().add(sepHorizontal);
                leftVertical.getChildren().add(leftHorizontal);
                leftHorizontal.getChildren().add(0, sessionList);
                leftHorizontal.getChildren().add(charger);

                // Formulaire d'inscription (right)
                rightVertical.getChildren().add(0, registrationForm);
                rightVertical.getChildren().add(envoyer);

                // Dividing the screen
                SplitPane root = new SplitPane();
                root.getItems().addAll(leftVertical, sepVertical, rightVertical);

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

        public static String getFirstNameFieldData() {
                return firstNameField.getText();
        }

        public String getLastNameFieldData() {
                return lastNameField.getText();
        }

        public static String getEmailFieldData() {
                return emailField.getText();
        }

        public static String getMatriculeFieldData() {
                return matriculeField.getText();
        }
}