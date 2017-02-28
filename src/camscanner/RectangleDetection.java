package camscanner;

import java.util.ArrayList;

public class RectangleDetection {
	public static boolean isDetPositive(
			int x1, int y1, 
			int x2, int y2,
			int x3, int y3)
	{
		return (x2-x1)*(y3-y1) > (x3-x1)*(y2-y1);
	}
	
	public static boolean isCross(
			int x1, int y1, int x2, int y2,
			int x3, int y3, int x4, int y4)
	{
		return (isDetPositive(x1, y1, x2, y2, x3, y3) ^ isDetPositive(x1, y1, x2, y2, x4, y4))
				&& (isDetPositive(x3, y3, x4, y4, x1, y1) ^ isDetPositive(x3, y3, x4, y4, x2, y2));
	}
	
	public static Point cross(Line l1, Line l2)
	{
		int A1 = l1.y2 - l1.y1;
		int B1 = l1.x1 - l1.x2;
		int C1 = l1.x1 * l1.y2 - l1.x2 * l1.y1;
		int A2 = l2.y2 - l2.y1;
		int B2 = l2.x1 - l2.x2;
		int C2 = l2.x1 * l2.y2 - l2.x2 * l2.y1;
		
		int D = det(A1, B1, A2, B2);
		int D1 = det(C1, B1, C2, B2);
		int D2 = det(A1, C1, A2, C2);
		
		return new Point(D1/D, D2/D);
	}
	
	public static int det(int a, int b, int c, int d)
	{
		return a*d-b*c;
	}
		
	public static Rectangle rectangleDetect(Line[] lines)
	{
		final int count = lines.length;
		final boolean[][] iscross = new boolean[count][count];
		for(int i = 0; i < count; ++i)
		{
			iscross[i][i] = false;
			for(int j = i+1; j < count; ++j)
			{
				iscross[i][j] = iscross[j][i] = isCross(
						lines[i].x1, lines[i].y1, lines[i].x2, lines[i].y2,
						lines[j].x1, lines[j].y1, lines[j].x2, lines[j].y2);
			}
		}
		
		ArrayList<Rectangle> list = new ArrayList<>();
		
		for(int i1 = 0; i1 < count; ++i1)
		{
			for(int i2 = 0; i2 < count; ++i2)
			{
				if(!iscross[i1][i2])
					continue;
				
				for(int i3 = 0; i3 < count; ++i3)
				{
					if(!iscross[i2][i3])
						continue;
					
					for(int i4 = 0; i4 < count; ++i4)
					{
						if(!iscross[i3][i4] || !iscross[i4][i1])
							continue;
						
						Point cross1 = cross(lines[i1], lines[i2]);
						Point cross2 = cross(lines[i2], lines[i3]);
						Point cross3 = cross(lines[i3], lines[i4]);
						Point cross4 = cross(lines[i4], lines[i1]);
						
						if(!isCross(cross1.x, cross1.y, cross3.x, cross3.y,
								cross2.x, cross2.y, cross4.x, cross4.y))
							continue;
						
						list.add(new Rectangle(
								cross1.x, cross1.y,
								cross2.x, cross2.y,
								cross3.x, cross3.y,
								cross4.x, cross4.y));
					}
				}
			}
		}
		
		int maxindex = 0;
		int max = list.get(0).area();
		for(int i = 1; i < list.size(); ++i)
			if(list.get(i).area() > max)
				maxindex = i;
		
		return list.get(maxindex);
	}
}
