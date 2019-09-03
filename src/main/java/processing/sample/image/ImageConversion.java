package processing.sample.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageConversion {

    public static void main(String[] args) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File("/Users/naman.nigam/GitHub/content-processing/src/main/resources/media/file_example_JPG_1MB.jpg"));

        // write the bufferedImage back to outputFile
        ImageIO.write(bufferedImage, "png", new File("output.png"));

        // this writes the bufferedImage into a byte array called resultingBytes
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOut);
        byte[] resultingBytes = byteArrayOut.toByteArray();
    }
}