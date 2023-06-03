package com.kashier.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.dynamsoft.dbr.BarcodeReader;
import com.dynamsoft.dbr.BarcodeReaderException;
import com.dynamsoft.dbr.Point;
import com.dynamsoft.dbr.TextResult;
import com.kashier.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.embed.swing.SwingFXUtils;
import com.kashier.VlcjJavaFxApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {
    private VlcjJavaFxApplication vlcj;
    private BarcodeReader br;
    private Image currentImg;
    boolean found = false;

    @FXML private Canvas cv;
    @FXML
    private void switchToSecondary() throws Exception {
        if (vlcj.stage == null) {
            vlcj.start(new Stage());
        } else {
            vlcj.stage.show();
        }
        try {
            String[] options = new String[2];
            options[0] = ":dshow-adev=none";
            options[1] = ":live-caching=300";
            vlcj.play("dshow://", options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            br = new BarcodeReader(App.DYNAMSOFT_LICENSE);
        } catch (BarcodeReaderException e) {
            throw new RuntimeException(e);
        }
        vlcj = new VlcjJavaFxApplication();
    }

    public Image getCurrentFrame() {
        return vlcj.getImageView().getImage();
    }

    @FXML
    public void captureFrame() throws Exception {
        // capture one frame from the video stream
        System.out.println("Capture Frame");
        if (vlcj.stage!=null) {
            currentImg = getCurrentFrame();
            redrawImage(currentImg);
            decodeImg(currentImg);
        }
    }

    private void redrawImage(Image img) {
        cv.setWidth(img.getWidth());
        cv.setHeight(img.getHeight());
        GraphicsContext gc = cv.getGraphicsContext2D();
        gc.drawImage(img, 0, 0, cv.getWidth(), cv.getHeight());
    }

    private void overlayCode(TextResult result) {
        GraphicsContext gc=cv.getGraphicsContext2D();

        List<Point> points= new ArrayList<Point>();
        for (Point point : result.localizationResult.resultPoints) {
            points.add(point);
        }
        points.add(result.localizationResult.resultPoints[0]);

        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.beginPath();

        for (int i = 0;i<points.size()-1;i++) {
            Point point=points.get(i);
            Point nextPoint=points.get(i+1);
            gc.moveTo(point.x, point.y);
            gc.lineTo(nextPoint.x, nextPoint.y);
        }
        gc.closePath();
        gc.stroke();
    }

    private void decodeImg(Image img) throws BarcodeReaderException, IOException {
        Date startDate = new Date();
        Long startTime = startDate.getTime();
        Long endTime = null;

        TextResult[] results = br.decodeBufferedImage(SwingFXUtils.fromFXImage(img,null), "");

        Date endDate = new Date();
        endTime = endDate.getTime();
        for (TextResult result:results) {
            overlayCode (result);
            System.out.println("Type: "+result.barcodeFormatString);
            System.out.println("Text: "+result.barcodeText);
        }

        if (results.length == 0) {
            System.out.println("No barcode detected.");
        }

        System.out.println("Total: "+(endTime-startTime)+"ms");
    }

    @FXML
    public void readVideoStreamBtn() throws Exception {
        found = false;
        while (!found) {
            captureFrame();
        }
    }
}
