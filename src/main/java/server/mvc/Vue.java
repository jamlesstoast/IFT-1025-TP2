package server.mvc;
import server.models.Course;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class Vue extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Setting up the stage
        VBox rightVertical = new VBox();
        VBox leftVertical = new VBox();
        HBox leftHorizontal = new HBox();

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 100, 100, Color.BEIGE);

        // Creating registration form
        GridPane registrationForm = new GridPane();
        registrationForm.setAlignment(Pos.CENTER);
        registrationForm.setHgap(10);
        registrationForm.setVgap(10);
        registrationForm.setPadding(new Insets(25, 25, 25, 25));

        TextField textFieldP = new TextField();
        TextField textFieldN = new TextField();
        TextField textFieldE = new TextField();
        TextField textFieldM = new TextField();

        Label labelP = new Label("Prénom");
        Label labelN = new Label("Nom");
        Label labelE = new Label("Email");
        Label labelM = new Label("Matricule");

        registrationForm.add(new Text("Formulaire d'inscription"), 0, 0);
        registrationForm.add(labelP, 0, 1);
        registrationForm.add(textFieldP, 1, 1);
        registrationForm.add(labelN, 0, 2);
        registrationForm.add(textFieldN, 1, 2);
        registrationForm.add(labelE, 0, 3);
        registrationForm.add(textFieldE, 1, 3);
        registrationForm.add(labelM, 0, 4);
        registrationForm.add(textFieldM, 1, 4);

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
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Course, String> courseColumn = new TableColumn<>("Cours");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        courseTable.getColumns().addAll(courseColumn, codeColumn);


        // Creating dropdown list
        ComboBox<String> sessionList = new ComboBox<>();
        sessionList.getItems().addAll(
                "Hiver",
                "Automne",
                "Été"
        );

        // Liste des cours
        leftVertical.getChildren().add(0, new Text("Liste des cours"));
        leftVertical.getChildren().add(courseTable);
        leftVertical.getChildren().add(sepHorizontal);
        leftVertical.getChildren().add(leftHorizontal);
        leftHorizontal.getChildren().add(0, sessionList);
        leftHorizontal.getChildren().add(charger);

        // Formulaire d'inscription
        rightVertical.getChildren().add(0, registrationForm);
        rightVertical.getChildren().add(envoyer);

        // Dividing the screen
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftVertical, rightVertical);

        primaryStage.setTitle("Inscription UdeM");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
