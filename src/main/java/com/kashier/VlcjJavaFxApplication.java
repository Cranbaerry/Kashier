package com.kashier;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import static uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory.videoSurfaceForImageView;


/**
 * JavaFX App
 */
public class VlcjJavaFxApplication extends Application {

    private MediaPlayerFactory mediaPlayerFactory;

    private EmbeddedMediaPlayer embeddedMediaPlayer;

    private ImageView videoImageView;
    public Stage stage;

    public VlcjJavaFxApplication() {
        mediaPlayerFactory = new MediaPlayerFactory();
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
    }

    @Override
    public final void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        videoImageView = new ImageView();
        videoImageView.setPreserveRatio(true);
        embeddedMediaPlayer.videoSurface().set(videoSurfaceForImageView(videoImageView));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        videoImageView.fitWidthProperty().bind(root.widthProperty());
        videoImageView.fitHeightProperty().bind(root.heightProperty());

        root.setCenter(videoImageView);

        Scene scene = new Scene(root, 500, 500, Color.BLACK);
        primaryStage.setTitle("Camera");
        primaryStage.setScene(scene);
        primaryStage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    // embeddedMediaPlayer.release();
                    System.out.println("Stage is closing");
                    stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void play(String mrl,String[] options) {
        App.isPlaying = true;
        embeddedMediaPlayer.media().play(mrl,options);
    }

    @Override
    public void stop() throws Exception {
        App.isPlaying = false;
        embeddedMediaPlayer.controls().stop();
        super.stop();
    }

    public ImageView getImageView() {
        return videoImageView;
    }

    public static void main(String[] args) {
        launch(args);
    }

}