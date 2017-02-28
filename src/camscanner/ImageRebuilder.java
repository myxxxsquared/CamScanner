package camscanner;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ImageRebuilder {
	public static BufferedImage rebuild(BufferedImage src, Rectangle rect, int width, int height)
	{
		final BufferedImage image = new BufferedImage(width, height, src.getType());
		WritableRaster rastersrc = src.getRaster();
		WritableRaster rasterdst = image.getRaster();
		
		for(int i = 0; i < height; ++i)
		{
			float xscale = (float)i / (height - 1);
			float xscale2 = 1-xscale;
			
			for(int j = 0; j < width; ++j)
			{
				float yscale = (float)j / (width - 1);
				float yscale2 = 1-yscale;
				
				Point cross = Line.cross(
						new Line(
								(int)(rect.x1*xscale+rect.x2*xscale2), 
								(int)(rect.y1*xscale+rect.y2*xscale2),
								(int)(rect.x4*xscale+rect.x3*xscale2), 
								(int)(rect.y4*xscale+rect.y3*xscale2)),
						new Line(
								(int)(rect.x1*yscale+rect.x4*yscale2), 
								(int)(rect.y1*yscale+rect.y4*yscale2),
								(int)(rect.x2*yscale+rect.x3*yscale2), 
								(int)(rect.y2*yscale+rect.y3*yscale2)));
				
				int[] clr;
				try{
					clr = rastersrc.getPixel(cross.x, cross.y, (int[])null);
					rasterdst.setPixel(i, j, clr);
				}
				catch(IndexOutOfBoundsException ex){}
			}
		}
		
		return image;
	}
}
