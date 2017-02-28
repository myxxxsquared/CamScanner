package camscanner;

import java.util.ArrayList;


public class LineDetection {

	/**
	 * 哈夫变换直线检测，输入归一化边缘图
	 */
	public static Line[] hough(Mat edgeimage)
	{
		assert(edgeimage.getDepth() == 1);
		
		//非最大抑制量
		final int NMSRANGE = 10;
		//检测线阈值
		final int DETECTVALUE = 100;
		//搜索附近线范围
		final int SEARCHRANGE = 5;
		//最大间隔
		final int SEARCHDIFF = 10;
		//最小长度
		final int SEARCHSTRENGTH = 300;
		//直线延长长度
		final int EXTENDLENGTH = 20;
		
		final int width = edgeimage.getWidth();
		final int height = edgeimage.getHeight();

		final int houghhh = ((int)(Math.sqrt((double)height*height+(double)width*width)) + 1);
		final int houghh = 2*houghhh;
		final int houghwh = houghhh / 2;
		final int houghw = houghwh * 2;
		final double pih = Math.PI / 2;
		final double houghscale = Math.PI / houghw;
		
		final boolean[][] sourceimg = new boolean[height][width];
		final int[][] houghimg = new int[houghw][houghh];
		final boolean[][] lineimg = new boolean[houghw][houghh];
		
		final double[] sinl = new double[houghw];
		final double[] cosl = new double[houghw];
		
		for(int i = 0; i < houghw; ++i)
		{
			double theta = i * houghscale - pih;
			sinl[i] = Math.sin(theta);
			cosl[i] = Math.cos(theta);
		}
		
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				sourceimg[i][j]=edgeimage.getData()[i][j][0]>0.5f;
		
		for(int i = 0; i < houghw; ++i)
			for(int j  = 0; j < houghh; ++j)
				houghimg[i][j] = 0;
								
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
			{
				if(sourceimg[i][j])
				{
					int lasty = (int)(-i) + houghhh;
					
					for(int x = 1; x < houghw; ++x)
					{
						int y = (int)(cosl[x]*j+sinl[x]*i) + houghhh;
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
							for(int cury = midy - 1; cury > y; --cury)
								houghimg[x][cury]++;
						}
						
						lasty = y;
					}
					
				}
			}
		
		for(int x = 0; x < houghw; ++x)
			for(int y = 0; y < houghh; ++y)
			{
				int cur = houghimg[x][y];
				boolean istrue = cur > DETECTVALUE;
				
				for(int i2 = -NMSRANGE; i2 <= NMSRANGE && istrue; ++i2)
					for(int j2 = -NMSRANGE; j2 <= NMSRANGE; ++j2)
						try {
							if(houghimg[x+i2][y+j2] > cur)
							{
								istrue=false;
								break;
							}
						} catch (IndexOutOfBoundsException e) {}
				
				lineimg[x][y] = istrue;
			}
		
		ArrayList<Line> result = new ArrayList<Line>();
		
		
		for(int x = 0; x < houghw; ++x)
		{
			double theta = x*houghscale-pih;
			double sintheta = sinl[x];
			double costheta = cosl[x];
			
			for(int y = 0; y < houghh; ++y)
				if(lineimg[x][y])
				{
					if(theta < 0.79 && theta > -0.79)
					{
						int strength = 0;
						int begini = -1;
						int beginj = -1;
						int diff = 0;
						
						for(int i = 0; i < height; ++i)
						{
							int j = (int)((y - houghhh - i * sintheta)/costheta);
							boolean have = false;
							for(int k = -SEARCHRANGE; k <= SEARCHRANGE; ++k)
							{
								int j2 = j+k;
								if(j2<0 || j2 >= width)
									break;
								if(sourceimg[i][j2])
								{
									have = true;
									break;
								}
							}
							
							if(i == height - 1 && diff >= 0 && strength > SEARCHSTRENGTH)
							{
								result.add(new Line(
										(int)((y - houghhh - (begini-EXTENDLENGTH) * sintheta)/costheta), 
										begini-EXTENDLENGTH, 
										(int)((y - houghhh - (i+EXTENDLENGTH) * sintheta)/costheta),
										i+EXTENDLENGTH));
							}
							else if(have)
							{
								if(begini==-1)
								{
									begini = i;
									beginj = j;
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
										result.add(new Line(
												(int)((y - houghhh - (begini-EXTENDLENGTH) * sintheta)/costheta),
												begini-EXTENDLENGTH,
												(int)((y - houghhh - (i-SEARCHDIFF+EXTENDLENGTH) * sintheta)/costheta),
												i-SEARCHDIFF+EXTENDLENGTH));
									strength = 0;
									begini = -1;
									beginj = -1;
								}
							}
						}
					}
					else
					{
						int strength = 0;
						int begini = -1;
						int beginj = -1;
						int diff = 0;
						
						for(int j = 0; j < width; ++j)
						{
							int i = (int)((y - houghhh - j * costheta)/sintheta);
							boolean have = false;
							for(int k = -SEARCHRANGE; k <= SEARCHRANGE; ++k)
							{
								int i2 = i+k;
								if(i2<0 || i2 >= height)
									break;
								if(sourceimg[i2][j])
								{
									have = true;
									break;
								}
							}
							
							if(j == width - 1 && diff >= 0 && strength > SEARCHSTRENGTH)
							{
								result.add(new Line(
										beginj - EXTENDLENGTH,
										(int)((y - houghhh - (beginj-EXTENDLENGTH) * costheta)/sintheta),
										j+EXTENDLENGTH,
										(int)((y - houghhh - (j+EXTENDLENGTH) * costheta)/sintheta)));
							}
							else if(have)
							{
								if(begini==-1)
								{
									begini = i;
									beginj = j;
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
										result.add(new Line(
												beginj - EXTENDLENGTH,
												(int)((y - houghhh - (beginj-EXTENDLENGTH) * costheta)/sintheta),
												j - SEARCHDIFF + EXTENDLENGTH,
												(int)((y - houghhh - (j-SEARCHDIFF+EXTENDLENGTH) * costheta)/sintheta)));
									begini = -1;
									beginj = -1;
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