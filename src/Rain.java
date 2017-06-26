import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The White Noise program is an experimental system for generating random
 * music.
 * 
 * @author Edward Hummerston
 * @version 1.0
 */
public class Rain
{

	Random rng;

	boolean	running	= false;
	boolean	paused	= false;

	public int screenWidth = 1280, screenHeight = 720;

	Ripple[]	rips;
	Signature	phatBeats;

	Debris[] rocksNShit;

	private Synthesizer		synth;
	private MidiChannel[]	mc;

	int			currentFPS;
	boolean	drawFPS	= false;

	public final static double	TARGET_FPS									= 100;
	public final static double	TARGET_TIME_BETWEEN_UPDATES	=
			1000000000 / TARGET_FPS;
	int													frameCounter								= 0;

	int volume = 20;

	float helpAlpha = 1;

	int instrID = 11;

	final static int[]	TIME_SIGNATURE_FOUR_FOUR	=
			{ 1, 4, 8, 4, 1, 4, 8, 4, 1, 4, 8, 4, 1, 4, 8, 4 };
	final static int[]	TIME_SIGNATURE_SWING			=
			{ 1, 0, 2, 1, 0, 3, 1, 0, 2, 1, 0, 3 };
	final static int[]	TIME_SIGNATURE_SWING_2		= { 1, 0, 2, 1, 0, 3 };
	final static int[]	TIME_SIGNATURE_CANON			= { 1, 8, 4, 8, 2, 8, 4, 8 };

	final static Chord[] CHORDS_FRUN = { new Chord(69, new int[] { 0, 4, 7 }),
			new Chord(76, new int[] { 0, 3, 7, 10 }),
			new Chord(76, new int[] { 0, 3, 7 }),
			new Chord(74, new int[] { 0, 4, 7, 14 }), };

	final static Chord[] CHORDS_WAY = { new Chord(72, new int[] { 0, 4, 7 }),
			new Chord(72, new int[] { 0, 4, 7 }),
			new Chord(67, new int[] { 0, 4, 7 }),
			new Chord(67, new int[] { 0, 4, 7 }),

			new Chord(65, new int[] { 0, 4, 7 }),
			new Chord(65, new int[] { 0, 4, 7 }),
			new Chord(72, new int[] { 0, 4, 7 }),
			new Chord(72, new int[] { 0, 4, 7 }),

			new Chord(72, new int[] { 0, 4, 7 }),
			new Chord(72, new int[] { 0, 4, 7 }),
			new Chord(67, new int[] { 0, 4, 7 }),
			new Chord(67, new int[] { 0, 4, 7 }),

			new Chord(74, new int[] { 0, 3, 7 }),
			new Chord(74, new int[] { 0, 3, 7 }),
			new Chord(69, new int[] { 0, 3, 7 }),
			new Chord(67, new int[] { 0, 4, 7 }), };

	final static Chord[] CHORDS_MABLAS = { new Chord(72, new int[] { 0, 3, 7 }),
			new Chord(72, new int[] { 0, 3, 5, 7 }),
			new Chord(72, new int[] { 0, 3, 7 }),
			new Chord(70, new int[] { 0, 4, 7 }),

			new Chord(68, new int[] { 0, 4, 7 }),
			new Chord(68, new int[] { 0, 4, 6, 7 }),
			new Chord(68, new int[] { 0, 4, 7 }),
			new Chord(67, new int[] { 0, 3, 7 }),

			new Chord(65, new int[] { 0, 3, 7 }),
			new Chord(65, new int[] { 0, 3, 5, 7 }),
			new Chord(65, new int[] { 0, 3, 7 }),
			new Chord(65, new int[] { 0, 3, 7 }),

			new Chord(72, new int[] { 0, 3, 7 }),
			new Chord(72, new int[] { 0, 3, 7 }),
			new Chord(70, new int[] { 0, 4, 7 }),
			new Chord(70, new int[] { 0, 4, 7 }), };

	final static Chord[] CHORDS_CANON = { new Chord(74, new int[] { 0, 4, 7 }), // D
																																							// Maj
			new Chord(69, new int[] { 0, 4, 7 }), // A Maj
			new Chord(71, new int[] { 0, 3, 7 }), // B Min
			new Chord(66, new int[] { 0, 3, 7 }), // F# min

			new Chord(67, new int[] { 0, 4, 7 }), // G Maj
			new Chord(74, new int[] { 0, 4, 7 }), // D Maj
			new Chord(67, new int[] { 0, 4, 7 }), // G Maj
			new Chord(69, new int[] { 0, 4, 7 }), // A Maj
	};



	public static void main(String[] args)
	{

		new Rain();

	}



	public Rain()
	{

		rng = new Random();

		System.out.println("Initialising window.");
		RainWindow rainWindow = new RainWindow(screenWidth, screenHeight);
		rainWindow.validate();
		System.out.println("\tgot us a screen");

		try
		{
			System.out.println("Retrieving MIDI synthesizer.");
			synth = MidiSystem.getSynthesizer();
			System.out.println("Initialising it.");
			synth.open();
			System.out.println("Retrieving channels.");
			mc = synth.getChannels();
			// Instrument[] instr = synth.getAvailableInstruments();
			//
			// StringBuilder sb = new StringBuilder();
			// String eol = System.getProperty("line.separator");
			// sb.append(
			// "The orchestra has " +
			// instr.length +
			// " instruments." +
			// eol);
			// for (Instrument instrument : instr)
			// {
			// sb.append(instrument.toString());
			// sb.append(eol);
			// }
			// System.out.println(sb.toString());

			// synth.loadInstrument(instr[0]);
			System.out.println("Setting instruments.");
			for (int i = 0; i < mc.length; i++)
			{
				mc[i].programChange(0, instrID);
				mc[i].setMono(true);
			}
			System.out.println("\tlets make some noise");
		}
		catch (MidiUnavailableException e1)
		{
			e1.printStackTrace();
		}

		running = true;

		double now = System.nanoTime();
		double lastUpdateTime = System.nanoTime();

		boolean loop = true;

		this.changeToFrun();
		rips = new Ripple[mc.length];
		rocksNShit = new Debris[16];

		while (loop)
		{
			phatBeats.setTime(now);
			for (int i = 0; i < rips.length; i++)
			{
				if (rips[i] != null)
				{
					rips[i].update();
					if (rips[i].isDead())
					{
						rips[i] = null;
					}
				}
				else
				{
					if (phatBeats.getBeat() != 0 && i != 9)
					{
						if (rng.nextDouble() < 0.9 / (phatBeats.getBeat()))
						{
							playNote(i);
						}
						break;
					}
				}
			}
			float volume = 0;
			for (int i = 0; i < rocksNShit.length; i++)
			{
				if (rocksNShit[i] != null)
				{
					if (rocksNShit[i].isDead(screenWidth, screenHeight))
					{
						rocksNShit[i] = null;
					}
					else
					{
						rocksNShit[i].update();
						volume += rocksNShit[i].getVolume();
					}
				}
				else
				{
					if (volume < screenWidth * screenHeight)
					{
						rocksNShit[i] = new Debris((screenWidth), (screenHeight),
								screenWidth * screenHeight / 2);
						volume += rocksNShit[i].getVolume();
					}
				}
			}

			rainWindow.repaint();
			currentFPS = (int) Math
					.ceil(TARGET_TIME_BETWEEN_UPDATES * 10 / (now - lastUpdateTime));
			if (frameCounter == TARGET_FPS)
			{

				// System.out.println("FPS: " + currentFPS);
				frameCounter = 0;
			}
			frameCounter++;

			lastUpdateTime += TARGET_TIME_BETWEEN_UPDATES;

			while (now - lastUpdateTime < TARGET_TIME_BETWEEN_UPDATES)
			{
				Thread.yield();

				// This stops the app from consuming all your CPU. It makes this
				// slightly less accurate, but is worth it.
				// You can remove this line and it will still work (better),
				// your CPU just climbs on certain OSes.
				// FYI on some OS's this can cause pretty bad stuttering. Scroll
				// down and have a look at different peoples' solutions to this.
				try
				{
					Thread.sleep(1);
				}
				catch (Exception e)
				{}

				loop = running;

				now = System.nanoTime();
			}

		}
		rainWindow.dispose();

	}

	/**
	 * 
	 * {@link JFrame} extension to manage the "Rain" visual component.
	 * 
	 * @author Edward Hummerston
	 *
	 */
	private class RainWindow
			extends JFrame
			implements KeyListener
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3358805991593520296L;



		/**
		 * Initialises the window that will display the "Rain" visuals in a
		 * {@link RainPanel} object.
		 * 
		 * @param x Initial width of the window.
		 * @param y Initial height of the window.
		 */
		public RainWindow(int x, int y)
		{
			super("welcome to art");
			initUI(x, y);
		}



		private void initUI(int x, int y)
		{
			setLocationRelativeTo(null);

			add(new RainPanel());

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			this.setLocation(10, 10);

			x += 10;
			y += 28;
			setSize(x, y);

			this.setResizable(true);
			this.setFocusable(true);

			this.setVisible(true);

			this.addKeyListener(this);

		}

		/**
		 * 
		 * {@link JPanel} extension that sits with a {@link RainWindow} object.
		 * 
		 * @author Edward Hummerston
		 *
		 */
		private class RainPanel
				extends JPanel
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 670446242401454707L;



			public RainPanel()
			{
				setBackground(Color.WHITE);
			}



			private void doDrawing(Graphics g)
			{

				screenWidth = this.getWidth();
				screenHeight = this.getHeight();

				if (running)
				{
					Graphics2D g2d = (Graphics2D) g;

					for (int i = 0; i < rocksNShit.length; i++)
					{
						if (rocksNShit[i] != null)
						{
							rocksNShit[i].draw(g2d);
						}
					}

					g2d.setColor(Color.BLUE);
					for (int i = 0; i < rips.length; i++)
					{
						if (rips[i] != null)
						{
							rips[i].draw(g2d);
						}
					}

					if (drawFPS)
					{
						String txt = new String();
						txt += currentFPS * 10;
						txt += "%";
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
						g2d.setColor(Color.RED);
						g2d.drawString(txt, 0, screenHeight);
					}

					if (helpAlpha > 0)
					{
						g2d.setComposite(
								AlphaComposite.getInstance(AlphaComposite.SRC_OVER, helpAlpha));
						g2d.setColor(Color.BLACK);

						String txt = new String();
						txt += "UP and DOWN change volume";
						g2d.drawString(txt, 0, screenHeight / 2);

						txt = new String();
						txt += "1 - 4 change styles";
						g2d.drawString(txt, 0,
								screenHeight / 2 + g.getFontMetrics().getHeight());

						helpAlpha -= (float) 1 / TARGET_FPS / 4;
					}
				}
			}



			@Override
			public void paintComponent(Graphics g)
			{

				super.paintComponent(g);

				doDrawing(g);
			}

		}



		@Override
		public void keyPressed(KeyEvent e)
		{

			switch (e.getKeyCode())
			{
			case KeyEvent.VK_ESCAPE:
				running = false;
				break;
			case KeyEvent.VK_SPACE:
				paused = !paused;
				break;
			case KeyEvent.VK_1:
				changeToFrun();
				break;
			case KeyEvent.VK_2:
				changeToMablas();
				break;
			case KeyEvent.VK_3:
				changeToWay();
				break;
			case KeyEvent.VK_4:
				changeToCanon();
				break;
			case KeyEvent.VK_R:
				drawFPS = !drawFPS;
				helpAlpha = 1;
				break;
			case KeyEvent.VK_DOWN:
				if (volume > 0)
				{
					volume -= 5;
				}
				break;
			case KeyEvent.VK_UP:
				if (volume < 50)
				{
					volume += 5;
				}
				break;
			}

		}



		@Override
		public void keyReleased(KeyEvent arg0)
		{}



		@Override
		public void keyTyped(KeyEvent arg0)
		{}

	}



	/**
	 * Plays a note in the specified channel, and creates a visual {@link Ripple}.
	 * Randomly chooses pitch based on active {@link Chord}.
	 *
	 * @param id MIDI Channel to play note in.
	 */
	private void playNote(int id)
	{
		int octRange = 3;
		Chord chord = phatBeats.getChord();
		chord.offsetOctave(0);
		int note = chord.getInterval(rng.nextInt(chord.getChordLength()));
		note += 12 * rng.nextInt(octRange);
		// note+=12*(octRange);
		int lowNote = chord.getInterval(0);
		int highNote =
				chord.getInterval(chord.getChordLength() - 1) + 12 * octRange;

		double pitchRatio =
				Math.pow(1 - ((double) (note - lowNote)) / (highNote - lowNote), 2);

		mc[id].noteOn(note, volume);

		// System.out.print(Chord.intToNote(note)+ " " + phatBeats.getChordID()
		// + "\t") ;

		// System.out.println(id+"\t"+synth.getAvailableInstruments()[mc[id].getProgram()].toString());

		if (mc[id].getProgram() != instrID)
		{
			System.out
					.println("HONK channel" + id + " Instrument: " + mc[id].getProgram());
		}

		rips[id] = new Ripple(screenWidth, screenHeight, pitchRatio);

	}



	/**
	 * Changes current {@link Chord} and {@link Signature} to the "Frun" piece. 
	 * 
	 * @see Rain#TIME_SIGNATURE_FOUR_FOUR
	 * @see Rain#CHORDS_FRUN
	 */
	public void changeToFrun()
	{
		phatBeats = new Signature(TIME_SIGNATURE_FOUR_FOUR, 20, CHORDS_FRUN,
				System.nanoTime());
	}



	/**
	 * Changes current {@link Chord} and {@link Signature} to resemble Master
	 * Blaster by Stevie Wonder.
	 * 
	 * @see Rain#TIME_SIGNATURE_SWING
	 * @see Rain#CHORDS_MABLAS
	 */
	public void changeToMablas()
	{
		phatBeats = new Signature(TIME_SIGNATURE_SWING, 50, CHORDS_MABLAS,
				System.nanoTime());
	}



	/**
	 * Changes current {@link Chord} and {@link Signature} to the "Way" structure.
	 * 
	 * @see Rain#TIME_SIGNATURE_SWING_2
	 * @see Rain#CHORDS_WAY
	 */
	public void changeToWay()
	{

		phatBeats = new Signature(TIME_SIGNATURE_SWING_2, 80, CHORDS_WAY,
				System.nanoTime());
	}



	/**
	 * Changes current {@link Chord} and {@link Signature} to Pachelbel's Canon in
	 * D.
	 * 
	 * @see Rain#TIME_SIGNATURE_CANON
	 * @see Rain#CHORDS_CANON
	 */
	public void changeToCanon()
	{
		phatBeats = new Signature(TIME_SIGNATURE_CANON, 30, CHORDS_CANON,
				System.nanoTime());
	}

}
