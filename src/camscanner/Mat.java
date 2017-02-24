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
