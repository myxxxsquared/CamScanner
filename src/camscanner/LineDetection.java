package camscanner;

import java.util.ArrayList;

public class LineDetection {
	
	public static class Line
	{
		public int x1, y1, x2, y2;
		
		public Line()
		{
			x1 = y1 = x2 = y2 = 0;
		}
		
		public Line(int x1, int y1, int x2, int y2)
		{
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}
	
	public static Line[] hough(Mat edgeimage)
	{
		assert(edgeimage.getDepth() == 1);
		
		final int width = edgeimage.getWidth();
		final int height = edgeimage.getHeight();
		final int houghw = 314;
		final int houghhh = ((int)(Math.sqrt((double)height*height+(double)width*width)) + 1);
		final int houghh = 2*((int)(Math.sqrt((double)height*height+(double)width*width)) + 1);
		
		int[][] houghimg = new int[houghw][houghh];
		
		for(int i = 0; i < houghw; ++i)
			for(int j  = 0; j < houghh; ++j)
				houghimg[i][j] = 0;
		
		boolean[][] sourceimg = new boolean[height][width];
		
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				if(sourceimg[i][j] = (edgeimage.getData()[i][j][0]>0.5f))
				{
					int lasty = j + houghhh;
					
					for(int x = 1; x < houghw; ++x)
					{
						double theta = x/100-1.58;
						int y = (int)Math.round(Math.cos(theta)*j+Math.sin(theta)*i) + houghhh;
						int midy = (lasty + y) / 2;
						if(y > lasty)
						{
							for(int cury = lasty; cury <= midy; ++cury)
								houghimg[x-1][cury]++;
							for(int cury = midy + 1; cury < y; ++cury)
								houghimg[x][cury]++;
						}
						else
						{
							for(int cury = lasty; cury >= midy; --cury)
								houghimg[x-1][cury]++;
							for(int cury = midy + 1; cury > y; --cury)
								houghimg[x][cury]++;
						}
						
						lasty = y;
					}
				}
		
		boolean[][] lineimg = new boolean[houghw][houghh];
		
		final int NMSRANGE = 2;
		final int DETECTVALUE = 20;
		
		for(int i = 0; i < houghw; ++i)
			for(int j = 0; j < houghw; ++j)
			{
				int cur = houghimg[i][j];
				boolean istrue = cur > DETECTVALUE;
				
				for(int i2 = -NMSRANGE; i2 <= NMSRANGE && istrue; ++i2)
					for(int j2 = -NMSRANGE; j2 <= NMSRANGE; ++j2)
						try {
							if(houghimg[i+i2][j+j2] > cur)
							{
								istrue=false;
								break;
							}
						} catch (IndexOutOfBoundsException e) {}
				
				lineimg[i][j] = istrue;
			}
		
		ArrayList<Line> result = new ArrayList<Line>();
		
		final int SEARCHRANGE = 1;
		final int SEARCHDIFF = 5;
		final int SEARCHSTRENGTH = 5;
		
		for(int i = 0; i < houghw; ++i)
		{
			double theta = i/100-1.58;
			double sintheta = Math.sin(theta);
			double costheta = Math.cos(theta);
			
			for(int j = 0; j < houghw; ++j)
				if(lineimg[i][j])
				{
					if(theta > 0.79 || theta < -0.79)
					{
						int strength = 0;
						int beginval = -1;
						int beginx = -1;
						int diff = 0;
						
						for(int y = 0; y < height; ++y)
						{
							int x = (int) Math.round((j - houghhh - y * sintheta)/costheta);
							boolean have = false;
							for(int k = -SEARCHRANGE; k <= SEARCHRANGE; ++k)
							{
								int x2 = x+k;
								if(x2<0 || x2 >= width)
									break;
								if(sourceimg[y][x2])
								{
									have = true;
									break;
								}
							}
								
							if(have)
							{
								if(beginval==-1)
								{
									beginval = y;
									beginx = x;
									strength=0;
								}
								strength++;
								diff = SEARCHDIFF;
							}
							else
							{
								diff--;
								if(diff == 0)
								{
									if(strength > SEARCHSTRENGTH)
										result.add(new Line(beginx, beginval,  (int) Math.round((j - houghhh - (y-5) * sintheta)/costheta), y - 5));
									strength = 0;
									beginval = -1;
								}
							}
						}
					}
					else
					{
						int strength = 0;
						int beginval = -1;
						int beginy = -1;
						int diff = 0;
						
						for(int x = 0; x < width; ++x)
						{
							int y = (int) Math.round((j - houghhh - x * costheta)/sintheta);
							boolean have = false;
							for(int k = -SEARCHRANGE; k <= SEARCHRANGE; ++k)
							{
								int y2 = y+k;
								if(y2<0 || y2 > width)
									break;
								if(sourceimg[y2][x])
								{
									have = true;
									break;
								}
							}
							
							if(have)
							{
								if(beginval==-1)
								{
									beginval = x;
									beginy = y;
									strength = 0;
								}
								strength++;
								diff = SEARCHDIFF;
							}
							else
							{
								diff--;
								if(diff == 0)
								{
									if(strength > SEARCHSTRENGTH)
										result.add(new Line(beginval, beginy, x - 5, (int) Math.round((j - houghhh - (x-5) * costheta)/sintheta)));
									beginval = -1;
									strength = 0;
								}
							}
						}
					}
				}
		}
		
		Line[] arrayresult = new Line[result.size()];
		for(int i = 0; i < result.size(); ++i)
		{
			arrayresult[i] = result.get(i);
		}
		return arrayresult;
	}
}