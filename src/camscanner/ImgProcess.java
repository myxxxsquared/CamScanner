package camscanner;

import java.awt.image.*;

// 输入 -> Canny边缘检测 -> 直线检测 -> 矩形检测 -> 最大矩形选择 -> 输出

public class ImgProcess {
	BufferedImage image;
	
	int width;
	int height;
	int bands;
	float[][] grayImage;
	
	float[][] edgeImage;
	
	public ImgProcess(BufferedImage img)
	{
		this.image = img;
	}
	
	public void initProcess()
	{
		WritableRaster wr = image.getRaster();
		int[] data = null;
		width = wr.getWidth();
		height = wr.getHeight();
		bands = wr.getNumBands();
		data = wr.getPixels(0, 0, width, height, data);
		
		grayImage = new float[height][width];
		
		int index = 0;
		
		for(int i = 0; i < height; ++i)
		{
			for(int j = 0; j < width; ++j)
			{
				grayImage[i][j] = 0.0f;
				for(int k = 0; k < bands; ++k)
				{
					grayImage[i][j] += (float)data[index];					
					++index;
				}
				grayImage[i][j] /= 765.0f;
			}
		}
	}
	
	public static float[][] convolution(float[][] src, float[][] kernel)
	{
		
		return null;
	}
	
	public void edgeDetection()
	{
		edgeImage = new float[height][width];
		
		for(int i = 1; i < height; ++i)
			for(int j = 1; j < width; ++j)
			{
				edgeImage[i][j] = Math.abs(grayImage[i][j] - grayImage[i - 1][j])
						+ Math.abs(grayImage[i][j] - grayImage[i][j - 1]);
			}
	}
}
