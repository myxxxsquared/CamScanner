package camscanner;

public class Line {
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
		
		if(D == 0)
			return new Point(0, 0);
		
		return new Point(D1/D, D2/D);
	}
	
	public static int det(int a, int b, int c, int d)
	{
		return a*d-b*c;
	}
}
