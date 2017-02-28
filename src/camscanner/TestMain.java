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
		
		final Mat gray = Mat.ToMat(image);
		Mat result = EdgeDetection.canny(gray);
		
		final Line[] lines = LineDetection.hough(result);
		final Rectangle rectangle = RectangleDetection.rectangleDetect(lines);
		final BufferedImage imgresult = ImageRebuilder.rebuild(image, rectangle, 300, 300);
		
		
		final BufferedImage img = new BufferedImage(
				image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		for(int i = 0; i < image.getHeight(); ++i)
			for(int j = 0; j < image.getWidth(); ++j)
			{
				img.getRaster().setPixel(j, i, new int[]{(int)(result.getData()[i][j][0]*255)});
			}
		
		JFrame frame = new JFrame(){
			private static final long serialVersionUID = -3500187028665125224L;

			public void paint(java.awt.Graphics g)
			{
				super.paint(g);
				g.drawImage(img, 0, 0, null);
				g.setColor(Color.RED);
				for (Line line : lines) {
					g.drawLine(line.x1, line.y1, line.x2, line.y2);
				}
				g.setColor(Color.BLUE);
				g.drawLine(rectangle.x1, rectangle.y1, rectangle.x2, rectangle.y2);
				g.drawLine(rectangle.x2, rectangle.y2, rectangle.x3, rectangle.y3);
				g.drawLine(rectangle.x3, rectangle.y3, rectangle.x4, rectangle.y4);
				g.drawLine(rectangle.x4, rectangle.y4, rectangle.x1, rectangle.y1);
			}
		};
		
		JFrame frame2 = new JFrame(){
			private static final long serialVersionUID = 6415349721608360051L;

			public void paint(java.awt.Graphics g)
			{
				super.paint(g);
				g.drawImage(imgresult, 0, 0, null);
			}
		};

		frame.setSize(500, 500);
		frame.setVisible(true);
		frame2.setSize(500, 500);
		frame2.setVisible(true);
	}

}
