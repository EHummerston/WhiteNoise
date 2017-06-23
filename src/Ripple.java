import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.Random;


public class Ripple {
	private int x, y;
	private double volume;
	private final double DEATH_VOLUME;
	private final double MAX_MAX, MAX_MIN;

	public Ripple(int x, int y, double size)
	{
		Random rng = new Random();
		this.x = rng.nextInt(x);
		this.y = rng.nextInt(y);
		this.MAX_MAX = x*y;
		this.MAX_MIN = Math.sqrt(MAX_MAX);
		this.DEATH_VOLUME = (MAX_MAX-MAX_MIN)*size+MAX_MIN;
		this.volume = 1;
	}

	public void update()
	{
		volume += ((DEATH_VOLUME)/Rain.TARGET_FPS);
	}

	public void draw(Graphics2D g2d)
	{

		int radius = (int)(Math.sqrt(volume)/(Math.PI));
		float alpha = (float)(1-((radius)/((Math.sqrt(DEATH_VOLUME)/(Math.PI)))))*2;
		if(alpha<0)
		{
			alpha = 0;
		}

		for(int i = 0; i<4;i++)
		{
			int radius2 = radius-i;
			float alpha2 = alpha-0.75f*i;
			if(alpha2>1)
			{
				alpha2 = 1;
			}
			if(alpha2>0)
			{
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha2  ));
				g2d.drawOval(x-(int)radius2, y-(int)radius2, 2*(int)radius2, 2*(int)radius2);
			}
		}
	}

	public boolean isDead()
	{
		return (volume >= DEATH_VOLUME);
	}

	public double getSize()
	{
		return DEATH_VOLUME / MAX_MAX;
	}

}
