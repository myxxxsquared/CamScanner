package camscanner;

import java.util.ArrayList;

public class RectangleDetection {
		
	public static Rectangle rectangleDetect(Line[] lines)
	{
		final int count = lines.length;
		final boolean[][] iscross = new boolean[count][count];
		for(int i = 0; i < count; ++i)
		{
			iscross[i][i] = false;
			for(int j = i+1; j < count; ++j)
			{
				iscross[i][j] = iscross[j][i] = Line.isCross(
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
						
						Point cross1 = Line.cross(lines[i1], lines[i2]);
						Point cross2 = Line.cross(lines[i2], lines[i3]);
						Point cross3 = Line.cross(lines[i3], lines[i4]);
						Point cross4 = Line.cross(lines[i4], lines[i1]);
						
						if(!Line.isCross(cross1.x, cross1.y, cross3.x, cross3.y,
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
