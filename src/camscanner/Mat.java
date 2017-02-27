package camscanner;

import java.awt.image.BufferedImage;

public class Mat
{
	private int width;
	private int height;
	private int depth;
	private float[][][] data;
	
	public int getDepth()
	{
		return depth;
	}
	
	public Mat(int width, int height, int depth)
	{
		this.width = width;
		this.height = height;
		this.depth = depth;
		data = new float[height][width][depth];
	}
	
	public Mat(int width, int height, int depth, float[][][] data)
	{
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.data = new float[height][width][depth];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				for(int k = 0; k < depth; ++k)
					this.data[i][j][k] = data[i][j][k];
	}
	
	public static Mat ToGrayMat(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int depth = image.getRaster().getNumBands();
		
		Mat result = new Mat(image.getWidth(), image.getHeight(), 1);
		int[] data = image.getRaster().getPixels(0, 0, image.getWidth(), image.getHeight(), (int[])null);
		
		int index = 0;
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
			{
				result.data[i][j][0] = 0.0f;
				for(int k = 0; k < depth; ++k)
					result.data[i][j][0] += data[index++];
				result.data[i][j][0]/=depth*255.0f;
			}
		return result;
	}
	
	public static Mat ToMat(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int depth = image.getRaster().getNumBands();
		
		Mat result = new Mat(width, height, depth);
		int[] data = image.getRaster().getPixels(0, 0, image.getWidth(), image.getHeight(), (int[])null);
		
		int index = 0;
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				for(int k = 0; k < depth; ++k)
					result.data[i][j][k] = data[index++] / 255.0f;
				
		return result;
	}
		
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public float[][][] getData(){
		return data;
	}
}
