import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

public class App {
    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture webSource = new VideoCapture();
        webSource.open(0);
        System.out.println("Hello, World!");
    }
}
