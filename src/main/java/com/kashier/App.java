package com.kashier;

import com.dynamsoft.dbr.*;
import com.harium.supabase.SupabaseClient;
import com.kashier.models.Account;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

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
    public static VlcjJavaFxApplication vlcj;
    private static BarcodeReader br;
    private static volatile Image currentImg;
    public static volatile boolean found = false;
    public static volatile boolean isPlaying = false;
    public static volatile String barcodeResult = "";
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

    public static void scanBarcode(CountDownLatch latch) {
        new Thread() {
            @Override
            public void run() {
                System.out.println("Setting up stage..");
                Platform.runLater(() -> {
                    try {
                        System.out.println("AAA");
                        if (vlcj.stage == null) {
                            vlcj.start(new Stage());
                        } else {
                            vlcj.stage.show();
                        }

                        String[] options = new String[2];
                        options[0] = ":dshow-adev=none";
                        options[1] = ":live-caching=300";
                        vlcj.play("dshow://", options);

                        PublicRuntimeSettings settings = br.getRuntimeSettings();
                        settings.resultCoordinateType = EnumResultCoordinateType.RCT_PIXEL;
                        br.updateRuntimeSettings(settings);

                        // Reset values
                        found = false;
                        barcodeResult = "";

                        System.out.println("BBB");
                        // New thread to process scan
                        processScan(latch);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }.start();
    }

    private static void processScan(CountDownLatch latch) {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (!found) {
                        //System.out.println("Scanning");
                        if (!isPlaying) {
                            System.out.println("Scan stopped");
                            break;
                        }

                        if (vlcj.stage != null) {
                            currentImg = getCurrentFrame();
                            if (currentImg != null) {
                                decodeImg(currentImg);
                            }
                        }
                    }
                } catch (BarcodeReaderException | IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            }
        }.start();
    }

    public static Image getCurrentFrame() {
        return vlcj.getImageView().getImage();
    }

    private static void decodeImg(Image img) throws BarcodeReaderException, IOException {
        Date startDate = new Date();
        Long startTime = startDate.getTime();
        Long endTime = null;

        TextResult[] results = br.decodeBufferedImage(SwingFXUtils.fromFXImage(img,null), "");

        Date endDate = new Date();
        endTime = endDate.getTime();
        for (TextResult result:results) {
            System.out.println("Type: "+result.barcodeFormatString);
            System.out.println("Text: "+result.barcodeText);
        }

        if (results.length == 0) {
            System.out.println("No barcode detected.");
        } else {
            found = true;
            barcodeResult = results[0].barcodeText;
            Platform.runLater(() -> {
                try {
                    vlcj.stage.hide();
                    vlcj.stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        System.out.println("Total: "+(endTime-startTime)+"ms");
    }

    public static void main(String[] args) {
        try {
            vlcj = new VlcjJavaFxApplication();
            supabase = new SupabaseClient(SUPABASE_URL, SUPABASE_KEY);

            try {
                br = new BarcodeReader(App.DYNAMSOFT_LICENSE);
            } catch (BarcodeReaderException e) {
                throw new RuntimeException(e);
            }

            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

