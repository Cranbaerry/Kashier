package com.kashier;

import com.harium.supabase.SupabaseClient;
import com.kashier.models.Account;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static final String SUPABASE_URL = "https://xgswsvlpfuhdaqrywzhf.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inhnc3dzdmxwZnVoZGFxcnl3emhmIiwicm9sZSI6ImFub24iLCJpYXQiOjE2ODU1MTk1MzYsImV4cCI6MjAwMTA5NTUzNn0.64nZasGodkopk4TF0uSU95s4uwvAGavsYbjL9P2Bkh4";
    public static final String DYNAMSOFT_LICENSE = "t0073oQAAADu5LbghXCWDSmoiWEKJB/NXefhMH+urpIkKJl/9LHGsOHyqDrGZ0TIs1EjH78dz1PYE2K+3nWoWCWtaT0ibDc8FBWwjCQ==";
    public static SupabaseClient supabase;
    public static Account account;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("auth"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Kashier Demo");
        stage.initStyle(StageStyle.DECORATED.UNDECORATED);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        if (scene == null) return;
        scene.setRoot(loadFXML(fxml));
    }

    public static void setSize(int width, int height) {
        if (scene == null) return;
        scene.getWindow().setWidth(width);
        scene.getWindow().setHeight(height);
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        try {
            supabase = new SupabaseClient(SUPABASE_URL, SUPABASE_KEY);
            launch();

//            BarcodeReader.initLicense("DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9");
//
//            // 2.Create an instance of Barcode Reader.
//            BarcodeReader dbr = new BarcodeReader();
//            TextResult[] results = dbr.decodeFileInMemory();
//            dbr.de
//
//            // 4.Output the barcode text.
//            if (results != null && results.length > 0) {
//                for (int i = 0; i < results.length; i++) {
//                    TextResult result = results[i];
//                    System.out.println("Barcode " + i + ":" + result.barcodeText);
//                }
//            } else {
//                System.out.println("No data detected.");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

