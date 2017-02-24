package camscanner;


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