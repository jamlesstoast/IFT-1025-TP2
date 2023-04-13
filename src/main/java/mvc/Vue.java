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
public class Vue extends Application {

    /**
     Appelle la méthode launch de la classe Application pour démarrer l'application JavaFX
     @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     Elle cree un objet Stage et configure l'interface utilisateur graphique
     @param primaryStage Le stage principal pour l'application, ou la scene est definie
     */
    @Override
    public void start(Stage primaryStage) {

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

        // creating textfields
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField matriculeField = new TextField();

        // creating labels
        Label labelP = new Label("Prénom");
        Label labelN = new Label("Nom");
        Label labelE = new Label("Email");
        Label labelM = new Label("Matricule");

        // add form fields
        registrationForm.add(new Text("Formulaire d'inscription"), 0, 0);
        registrationForm.add(labelP, 0, 1);
        registrationForm.add(firstNameField, 1, 1);
        registrationForm.add(labelN, 0, 2);
        registrationForm.add(lastNameField, 1, 2);
        registrationForm.add(labelE, 0, 3);
        registrationForm.add(emailField, 1, 3);
        registrationForm.add(labelM, 0, 4);
        registrationForm.add(matriculeField, 1, 4);

        // set layout for the form
        registrationForm.setHgap(10);
        registrationForm.setVgap(10);
        registrationForm.setPadding(new Insets(10, 10, 10, 10));

        // Creating buttons
        Button charger = new Button("charger");
        charger.setTextAlignment(TextAlignment.CENTER);
        charger.setAlignment(Pos.BOTTOM_RIGHT);
        Button envoyer = new Button("envoyer");
        envoyer.setTextAlignment(TextAlignment.CENTER);
        envoyer.setAlignment(Pos.CENTER);

        // Creating separators
        Separator sepHorizontal = new Separator();
        sepHorizontal.setMaxWidth(50);
        Separator sepVertical = new Separator();
        sepVertical.setOrientation(Orientation.VERTICAL);

        // Creating table
        TableView<Course> courseTable = new TableView<>();

        TableColumn<Course, String> codeColumn = new TableColumn<>("Code");
        TableColumn<Course, String> courseColumn = new TableColumn<>("Cours");

        courseTable.getColumns().add(codeColumn);
        courseTable.getColumns().add(courseColumn);

        courseTable.setPlaceholder(new Label("No content in table"));

        // Creating dropdown list
        ComboBox<String> sessionList = new ComboBox<>();
        sessionList.getItems().addAll(
                "Hiver",
                "Automne",
                "Été"
        );

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

        Scene scene = new Scene(root, 100, 100, Color.BEIGE);

        primaryStage.setTitle("Inscription UdeM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}