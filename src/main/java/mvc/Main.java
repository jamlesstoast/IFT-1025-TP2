package mvc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     Elle cree un objet Stage et configure l'interface utilisateur graphique
     @param primaryStage Le stage principal pour l'application, ou la scene est definie
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Modele leModele = new Modele();
        Vue laVue = new Vue();
        Controleur leControleur = new Controleur(leModele, laVue);

        Scene scene = new Scene(laVue, 100, 100, Color.BEIGE);

        primaryStage.setTitle("Inscription UdeM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     Appelle la méthode launch de la classe Application pour démarrer l'application JavaFX
     @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }

}