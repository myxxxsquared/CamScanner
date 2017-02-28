package camscanner;

public class Convolution {
	
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

}
