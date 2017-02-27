package camscanner;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.*;
import javax.swing.*;

public class TestMain {

	public static void main(String[] args) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("Z:\\realtest.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final BufferedImage image2 = image;
		
		final Mat gray = Mat.ToMat(image);
//		LineDetection.hough(gray);
		Mat result = EdgeDetection.canny(gray);
		
		final Line[] lines = LineDetection.hough(result);
		
		final BufferedImage img = new BufferedImage(
				image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		for(int i = 0; i < image.getHeight(); ++i)
			for(int j = 0; j < image.getWidth(); ++j)
			{
				img.getRaster().setPixel(j, i, new int[]{(int)(result.getData()[i][j][0]*255)});
			}
		
		JFrame frame = new JFrame(){
			public void paint(java.awt.Graphics g)
			{
				super.paint(g);
				g.drawImage(img, 0, 0, null);
				g.setColor(Color.RED);
				for (Line line : lines) {
					g.drawLine(line.x1, line.y1, line.x2, line.y2);
				}
			}
		};

//		JFrame frame = new JFrame("titlename");
		frame.setSize(500, 500);
		frame.setVisible(true);
		
//		try {
//			ImageIO.write(img, "png", new File("Z:\\out.png"));
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		JLabel label = new JLabel();
//		label.setIcon(new ImageIcon(img));
//		label.setVisible(true);
//		frame.getContentPane().add(label);
	}

}
