package com.kashier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    
    private ArrayList<Inventory> inv = new ArrayList<Inventory>();
    Inventory item1 = new Inventory("Ramen Set", 10, 200000, "qr1");
    Inventory item2 = new Inventory("Mushroom Cream Soup", 10, 50000, "qr2");
    Inventory item3 = new Inventory("Chicken Katsu Set", 10, 100000, "qr3");
    Inventory item4 = new Inventory("Chicken Corden Blue", 10, 150000, "qr4");
    Inventory item5 = new Inventory("French Fries", 10, 20000, "qr5");
    Inventory item6 = new Inventory("Spaghetti Carbonara", 10, 50000, "qr6");
    Inventory item7 = new Inventory("Fish'n Chips", 10, 100000, "qr7");
    Inventory item8 = new Inventory("Chicken Teriyaki Set", 10, 100000, "qr8");
    Inventory item9 = new Inventory("Fettucine Mushroom & Cream", 10, 200000, "qr9");
  

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    

    public static void main(String[] args) {
        launch();
    }

}