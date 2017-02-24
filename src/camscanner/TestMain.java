package camscanner;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.*;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedImage img = 
					ImageIO.read(new File("C:\\Users\\Admin\\Desktop\\QQͼƬ20161221153554.jpg"));
			ImgProcess process = new ImgProcess(img);
			process.edgeDetection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
