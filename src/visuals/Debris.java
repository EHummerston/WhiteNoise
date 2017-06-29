package visuals;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Debris
{
  private float       x_, y_;
  private float       radius_, xVel_, yVel_, volume_, alpha_;
  private final Color c_;



  public Debris(int width, int height, float tvolume)
  {
    Random rng = new Random();

    volume_ = tvolume * (0.25f + rng.nextFloat() * 0.5f);
    radius_ = (float) (Math.sqrt(volume_) / (Math.PI));

    float velRange = radius_ / 2;

    float outside = (int) radius_ - 1;
    int side = rng.nextInt(4);

    xVel_ = (rng.nextFloat() - 0.5f) * velRange;
    yVel_ = (rng.nextFloat() - 0.5f) * velRange;

    switch (side)
    {
    case 0:
      x_ = -outside;
      y_ = rng.nextInt(height);
      xVel_ = Math.abs(xVel_);
      break;
    case 1:
      x_ = width + outside;
      y_ = rng.nextInt(height);
      xVel_ = -Math.abs(xVel_);
      break;
    case 2:
      y_ = -outside;
      x_ = rng.nextInt(width);
      yVel_ = Math.abs(yVel_);
      break;
    case 3:
      y_ = height + outside;
      x_ = rng.nextInt(width);
      yVel_ = -Math.abs(yVel_);
      break;
    }

    alpha_ = 0.25f + rng.nextFloat() * 0.5f;

    int red = 128 + rng.nextInt(128);
    int green = rng.nextInt(128);
    int blue = rng.nextInt((red + green) / 3);
    c_ = new Color(red, green, blue);

  }



  public void update()
  {
    x_ += (xVel_ / (float) Rain.TARGET_FPS);
    y_ += (yVel_ / (float) Rain.TARGET_FPS);
  }



  public float getVolume()
  {
    return volume_;
  }



  public boolean isDead(int width, int height)
  {
    return (-radius_ > x_ || -radius_ > y_ || x_ > width + radius_ ||
        y_ > height + radius_);
  }



  public void draw(Graphics2D g2d)
  {
    int rad = (int) radius_;
    g2d.setColor(c_);
    g2d.setComposite(
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha_));
    g2d.fillOval((int) x_ - rad, (int) y_ - rad, rad * 2, rad * 2);
  }

}
