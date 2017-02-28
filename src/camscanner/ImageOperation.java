package camscanner;

public class ImageOperation {

	public static Mat convolution(Mat src, Mat kernel)
	{
		assert(kernel.getDepth()==1);
		assert(kernel.getWidth()%2 == 1);
		assert(kernel.getHeight()%2 == 1);
		
		final int width = src.getWidth();
		final int height = src.getHeight();
		final int depth = src.getDepth();
		
		final int w = (kernel.getWidth()-1)/2;
		final int h = (kernel.getHeight()-1)/2;
		
		Mat result = new Mat(width, height, depth);
		
		for(int i = 0; i < height; ++i)
		{
			for(int j = 0; j < width; ++j)
			{
				for(int k = 0; k < depth; ++k)
				{
					result.getData()[i][j][k]=0;
					
					for(int i2 = -h; i2 <= h; ++i2)
					{
						for(int j2 = -w; j2 <= w; ++j2)
						{
							int y = i+i2;
							int x = j+j2;
							if(y<0) y=0;
							if(y>=height) y=height-1;
							if(x<0) x=0;
							if(x>=width) x=width-1;
							result.getData()[i][j][k]+=
									kernel.getData()[i2+h][j2+w][0]*src.getData()[y][x][k];
						}
					}
				}
			}
		}
		
		return result;
	}
	
	public static Mat generateSobelKernelX()
	{
		float[][][] data = {
				{{-1.0f},{0.0f},{1.0f}},
				{{-2.0f},{0.0f},{2.0f}},
				{{-1.0f},{0.0f},{1.0f}}};
		return new Mat(3, 3, 1, data);
	}
	
	public static Mat generateSobelKernelY()
	{
		float[][][] data = {
				{{1.0f},{2.0f},{1.0f}},
				{{0.0f},{0.0f},{0.0f}},
				{{-1.0f},{-2.0f},{-1.0f}}};
		return new Mat(3, 3, 1, data);
	}
	
	/**
	 * ¸ßË¹¾í»ýºË
	 */
	public static Mat generateGaussKernel()
	{
		float [][][] data = {{{0.002969f},{0.013306f},{0.021938f},{0.013306f},{0.002969f}},
				{{0.013306f},{0.059634f},{0.09832f},{0.059634f},{0.013306f}},
				{{0.021938f},{0.09832f},{0.1621f},{0.09832f},{0.021938f}},
				{{0.013306f},{0.059634f},{0.09832f},{0.059634f},{0.013306f}},
				{{0.002969f},{0.013306f},{0.021938f},{0.013306f},{0.002969f}}};
		return new Mat(5, 5, 1, data);
	}
}
