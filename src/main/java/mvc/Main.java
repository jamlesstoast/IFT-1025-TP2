package mvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import client.*;

public class Main extends Application {

    /**
     * Elle cree un objet Stage et configure l'interface utilisateur graphique
     * @param primaryStage Le stage principal pour l'application
     * @throws Exception running app error
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mvc/view.fxml"));
        fxmlLoader.setController(new Controleur(new Modele(new Client(ClientSimple.LOCALHOST, ClientSimple.PORT))));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Inscription UdeM");
        primaryStage.setScene(new Scene(root, 600, 400, Color.BEIGE));
        primaryStage.show();
    }

    /**
     * Lance l'application JavaFX
     * @param args Les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}