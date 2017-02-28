package camscanner;

public class Rectangle {
	public int x1, y1, x2, y2, x3, y3, x4, y4;
	
	public Rectangle()
	{
		x1=y1=x2=y2=x3=y3=x4=y4=0;
	}
	
	public Rectangle(int x1, int y1, int x2, int y2,
			int x3, int y3, int x4, int y4)
	{
		this.x1=x1;
		this.x2=x2;
		this.x3=x3;
		this.x4=x4;
		this.y1=y1;
		this.y2=y2;
		this.y3=y3;
		this.y4=y4;
	}
	
	public int area()
	{
		return Math.abs((x3-x1)*(y4-y2)-(x4-x2)*(y3-y1));
	}
}
