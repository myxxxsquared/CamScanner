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
	
	public static Mat generateGaussKernel()
	{
		float [][][] data = {{{0.002969f},{0.013306f},{0.021938f},{0.013306f},{0.002969f}},
				{{0.013306f},{0.059634f},{0.09832f},{0.059634f},{0.013306f}},
				{{0.021938f},{0.09832f},{0.1621f},{0.09832f},{0.021938f}},
				{{0.013306f},{0.059634f},{0.09832f},{0.059634f},{0.013306f}},
				{{0.002969f},{0.013306f},{0.021938f},{0.013306f},{0.002969f}}};
		return new Mat(5, 5, 1, data);
	}
	
	public static Mat canny(Mat src)
	{
		assert(src.getDepth()==1);
		
		int width = src.getWidth();
		int height = src.getHeight();
		
		Mat blur = Convolution.convolution(src, generateGaussKernel());
		Mat sx = Convolution.convolution(blur, generateSobelKernelX());
		Mat sy = Convolution.convolution(blur, generateSobelKernelY());
		
		int[][] theta = new int[width][height];
		float[][] rho = new float[width][height];
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
					theta[i][j] = 2;
				else if(d > -0.577 && d < 0.577)
					theta[i][j] = 0;
				else if(d > 0)
					theta[i][j] = 3;
				else
					theta[i][j] = 1;
			}
		
		boolean[][] edge = new boolean[width][height];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
			{
				
			}
	}
}
