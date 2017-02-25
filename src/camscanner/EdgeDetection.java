package camscanner;

public class EdgeDetection {
	
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
	
	/**
	 * Canny±ßÔµ¼ì²â
	 */
	public static Mat canny(Mat src)
	{
		assert(src.getDepth()==1);
		
		final float MINVAL = 0.05f;
		final float MAXVAL = 0.15f;
		final int LOOPNUM = 3;
		final int NMSRANGE = 1;
		
		int width = src.getWidth();
		int height = src.getHeight();
		
		Mat blur = Convolution.convolution(src, generateGaussKernel());
		Mat sx = Convolution.convolution(blur, generateSobelKernelX());
		Mat sy = Convolution.convolution(blur, generateSobelKernelY());
		
		int[][] theta = new int[height][width];
		float[][] rho = new float[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
			{
				float dx, dy;
				dx = sx.getData()[i][j][0];
				dy = sy.getData()[i][j][0];
				rho[i][j] = (float)Math.sqrt(dx*dx+dy*dy);
				if(dx == 0)
					dx = 0.00001f;
				float d = dy/dx;
				if(d > 1.73 || d < -1.73)
					theta[i][j] = 1;
				else if(d > -0.577 && d < 0.577)
					theta[i][j] = 2;
				else if(d > 0)
					theta[i][j] = 3;
				else
					theta[i][j] = 1;
			}
		
		
		
		int[][] direction = {{1, 0}, {1, 1}, {0, 1}, {1, -1}};
		float[][] rhonms = new float[height][width];
		
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
			{
				boolean maxium = true;
				float cur = rho[i][j];
				for(int k = -NMSRANGE; k <= NMSRANGE; ++k)
				{
					if(k==0)
						continue;
					int dir = theta[i][j];
					int y = i + direction[dir][0]*k;
					int x = j + direction[dir][1]*k;
					try {
						if(cur < rho[y][x])
						{
							maxium=false;
							break;
						}
						
					} catch (IndexOutOfBoundsException e) {}
				}
				rhonms[i][j] = maxium ? cur : 0.0f;
			}
		
		boolean[][] isedge = new boolean[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				isedge[i][j] = rhonms[i][j] > MAXVAL;
		
		int[][] neighbours = {{0,1},{0,-1},{-1,0},{1,0},{-1,-1},{1,-1},{-1,1},{1,1}};
		
		for(int k = 0; k < LOOPNUM; ++k)
			for(int i = 0; i < height; ++i)
				for(int j = 0; j < width; ++j)
					if(!isedge[i][j] && rhonms[i][j]>MINVAL)
					{
						boolean betrue = false;
						for (int[] js : neighbours) {
							try {
								if(isedge[i+js[0]][j+js[1]])
								{
									betrue=true;
									break;
								}
							} catch (IndexOutOfBoundsException e) {}
						}
						if(betrue)
							isedge[i][j] = true;
					}
		
		Mat result = new Mat(width, height, 1);
		
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				result.getData()[i][j][0] = isedge[i][j] ? 1.0f : 0.0f;
		
		return result;
	}
}
