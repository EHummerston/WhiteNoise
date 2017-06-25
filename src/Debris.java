import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Debris {
	private float x, y;
	private float radius, xVel, yVel, volume, alpha;
	private final Color c;

	public Debris(int width, int height, float tvolume) {
		Random rng = new Random();

		volume = tvolume * (0.25f + rng.nextFloat() * 0.5f);
		radius = (float) (Math.sqrt(volume) / (Math.PI));

		float velRange = radius / 2;

		float outside = (int) radius - 1;
		int side = rng.nextInt(4);

		xVel = (rng.nextFloat() - 0.5f) * velRange;
		yVel = (rng.nextFloat() - 0.5f) * velRange;

		switch (side) {
		case 0:
			x = -outside;
			y = rng.nextInt(height);
			xVel = Math.abs(xVel);
			break;
		case 1:
			x = width + outside;
			y = rng.nextInt(height);
			xVel = -Math.abs(xVel);
			break;
		case 2:
			y = -outside;
			x = rng.nextInt(width);
			yVel = Math.abs(yVel);
			break;
		case 3:
			y = height + outside;
			x = rng.nextInt(width);
			yVel = -Math.abs(yVel);
			break;
		}

		alpha = 0.25f + rng.nextFloat() * 0.5f;

		int red = 128 + rng.nextInt(128);
		int green = rng.nextInt(128);
		int blue = rng.nextInt((red + green) / 3);
		c = new Color(red, green, blue);

	}

	public void update() {
		x += (xVel / (float) Rain.TARGET_FPS);
		y += (yVel / (float) Rain.TARGET_FPS);
	}

	public float getVolume() {
		return volume;
	}

	public boolean isDead(int width, int height) {
		return (-radius > x || -radius > y || x > width + radius || y > height + radius);
	}

	public void draw(Graphics2D g2d) {
		int rad = (int) radius;
		g2d.setColor(c);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.fillOval((int) x - rad, (int) y - rad, rad * 2, rad * 2);
	}

}
