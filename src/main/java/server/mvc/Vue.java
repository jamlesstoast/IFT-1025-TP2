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

        // Creating empty text fields
        TextField textFieldP = new TextField();
        TextField textFieldN = new TextField();
        TextField textFieldE = new TextField();
        TextField textFieldM = new TextField();

        // Creating labels
        Label labelP = new Label("Prénom");
        Label labelN = new Label("Nom");
        Label labelE = new Label("Email");
        Label labelM = new Label("Matricule");

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
        TableView<Course> tableView = new TableView<>();

        // Creating dropdown list
        ComboBox<String> dropDown = new ComboBox<>();

        // Liste des cours
        leftVertical.getChildren().add(0, new Text("Liste des cours"));

        TableColumn<Course, String> codeColumn = new TableColumn<>("Code");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Course, String> nameColumn = new TableColumn<>("Cours");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableView.getColumns().addAll(codeColumn, nameColumn);


        root.getChildren().add(sepHorizontal);
        dropDown.getItems().addAll(
                "Hiver",
                "Automne",
                "Été"
        );
        leftVertical.getChildren().add(charger);

        // Formulaire d'inscription
        rightVertical.getChildren().add(0, new Text("Formulaire d'inscription"));
        rightVertical.getChildren().addAll(labelP, textFieldP, labelN, textFieldN, labelE, textFieldE, labelM, textFieldM);
        rightVertical.getChildren().add(envoyer);

        primaryStage.setTitle("Inscription UdeM");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
