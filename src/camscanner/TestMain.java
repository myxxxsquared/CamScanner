package camscanner;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.*;
import javax.swing.*;

public class TestMain {

	public static void main(String[] args) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("Z:\\test.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Mat gray = Mat.ToGrayMat(image);
		Mat result = EdgeDetection.canny(gray);
		JFrame frame = new JFrame("titlename");
		frame.setSize(500, 500);
		frame.setVisible(true);
		BufferedImage img = new BufferedImage(
				image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		for(int i = 0; i < image.getHeight(); ++i)
			for(int j = 0; j < image.getWidth(); ++j)
			{
				img.getRaster().setPixel(j, i, new int[]{(int)(result.getData()[i][j][0]*255)});
			}
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(img));
		label.setVisible(true);
		frame.getContentPane().add(label);
	}

}
