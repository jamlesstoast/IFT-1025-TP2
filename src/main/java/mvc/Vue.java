package mvc;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

        // Liste des cours
        leftVertical.getChildren().add(0, new Text("Liste des cours"));
        leftVertical.getChildren().add(new Text("Code"));
        leftHorizontal.getChildren().add(new Text("Cours"));
        root.getChildren().add(sepHorizontal);
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
