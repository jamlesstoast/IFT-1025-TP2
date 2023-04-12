package mvc;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import server.models.Course;

/**
 * The Vue class is a JavaFX application that creates a graphical user interface for
 * an inscription system for "Université de Montréal"
 * It displays a registration form on the right side of the screen and list of
 * available courses on the left of the screen.
 * The Vue class extends the Application class from the JavaFX library and overrides
 * its start method to set up the GUI.
 */
public class Vue extends Application {

    /**
     * The main method of the Vue class is a static methode that takes an array of
     * strings as it argument and calls the launch method from the Application class
     * to start the JavaFX application
     *
     * @param args the command line argument
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method of the Vue class is called by the JavaFX framework when
     * the application is started. It creates a Stage object and sets up the GUI by
     * creating and arranging various JavaFX UI components using different JavaFX
     * layout classes.
     *
     * @param   primaryStage   the primary stage for the application, where the scene
     *                         is set
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
        TextField textFieldP = new TextField();
        TextField textFieldN = new TextField();
        TextField textFieldE = new TextField();
        TextField textFieldM = new TextField();

        // creating labels
        Label labelP = new Label("Prénom");
        Label labelN = new Label("Nom");
        Label labelE = new Label("Email");
        Label labelM = new Label("Matricule");

        // add form fields
        registrationForm.add(new Text("Formulaire d'inscription"), 0, 0);
        registrationForm.add(labelP, 0, 1);
        registrationForm.add(textFieldP, 1, 1);
        registrationForm.add(labelN, 0, 2);
        registrationForm.add(textFieldN, 1, 2);
        registrationForm.add(labelE, 0, 3);
        registrationForm.add(textFieldE, 1, 3);
        registrationForm.add(labelM, 0, 4);
        registrationForm.add(textFieldM, 1, 4);

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