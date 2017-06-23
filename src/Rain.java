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


public class Rain {

	Random rng;


	boolean running = false;
	boolean paused = false;

	public int screenWidth = 1280, screenHeight = 720;

	Ripple[] rips;
	Signature phatBeats;

	Debris[] rocksNShit;

	private Synthesizer synth;
	private MidiChannel[] mc;

	int currentFPS;
	boolean drawFPS = false;

	public final static double TARGET_FPS = 100;
	public final static double TARGET_TIME_BETWEEN_UPDATES = 1000000000 / TARGET_FPS;
	int frameCounter = 0;

	int vol = 20;

	float helpAlpha = 1;

	int instrID = 11;
	
	final static int[] fourFour = {1,4,8,4,1,4,8,4,1,4,8,4,1,4,8,4};
	final static int[] swing = {1,0,2,1,0,3,1,0,2,1,0,3};
	final static int[] swing2 = {1,0,2,1,0,3};
	final static int[] canonTime = {1,8,4,8,2,8,4,8};

	final static Chord[] frun = {
		new Chord(69,new int[] {0,4,7}),
		new Chord(76,new int[] {0,3,7,10}),
		new Chord(76,new int[] {0,3,7}),
		new Chord(74,new int[] {0,4,7,14}),
	};

	final static Chord[] way = {
		new Chord(72,new int[] {0,4,7}),
		new Chord(72,new int[] {0,4,7}),
		new Chord(67,new int[] {0,4,7}),
		new Chord(67,new int[] {0,4,7}),
		
		new Chord(65,new int[] {0,4,7}),
		new Chord(65,new int[] {0,4,7}),
		new Chord(72,new int[] {0,4,7}),
		new Chord(72,new int[] {0,4,7}),
		
		new Chord(72,new int[] {0,4,7}),
		new Chord(72,new int[] {0,4,7}),
		new Chord(67,new int[] {0,4,7}),
		new Chord(67,new int[] {0,4,7}),
		
		new Chord(74,new int[] {0,3,7}),
		new Chord(74,new int[] {0,3,7}),
		new Chord(69,new int[] {0,3,7}),
		new Chord(67,new int[] {0,4,7}),
	};

	final static Chord[] mablas = {
		new Chord(72,new int[] {0,3,7}),
		new Chord(72,new int[] {0,3,5,7}),
		new Chord(72,new int[] {0,3,7}),
		new Chord(70,new int[] {0,4,7}),

		new Chord(68,new int[] {0,4,7}),
		new Chord(68,new int[] {0,4,6,7}),
		new Chord(68,new int[] {0,4,7}),
		new Chord(67,new int[] {0,3,7}),

		new Chord(65,new int[] {0,3,7}),
		new Chord(65,new int[] {0,3,5,7}),
		new Chord(65,new int[] {0,3,7}),
		new Chord(65,new int[] {0,3,7}),

		new Chord(72,new int[] {0,3,7}),
		new Chord(72,new int[] {0,3,7}),
		new Chord(70,new int[] {0,4,7}),
		new Chord(70,new int[] {0,4,7}),
	};
	
	final static Chord[] canon = {
			new Chord(74,new int[] {0,4,7}),	// D Maj
			new Chord(69,new int[] {0,4,7}),	// A Maj
			new Chord(71,new int[] {0,3,7}),	// B Min	
			new Chord(66,new int[] {0,3,7}),	// F# min

			new Chord(67,new int[] {0,4,7}),	// G Maj
			new Chord(74,new int[] {0,4,7}),	// D Maj
			new Chord(67,new int[] {0,4,7}),	// G Maj
			new Chord(69,new int[] {0,4,7}),	// A Maj
		};


	public static void main(String[] args) {


		new Rain();


	}

	public Rain()
	{

		rng = new Random();

		System.out.println("Initialising window.");
		Drawing ps = new Drawing(screenWidth,screenHeight);
		ps.validate();
		System.out.println("\tgot us a screen");

		try
		{
			System.out.println("Retrieving MIDI synthesizer.");
			synth = MidiSystem.getSynthesizer();
			System.out.println("Initialising it.");
			synth.open();
			System.out.println("Retrieving channels.");
			mc = synth.getChannels();
			//			Instrument[] instr = synth.getAvailableInstruments();
			//
			//			StringBuilder sb = new StringBuilder();
			//			String eol = System.getProperty("line.separator");
			//			sb.append(
			//					"The orchestra has " + 
			//							instr.length + 
			//							" instruments." + 
			//							eol);
			//			for (Instrument instrument : instr)
			//			{
			//				sb.append(instrument.toString());
			//				sb.append(eol);
			//			}
			//			System.out.println(sb.toString());


			//synth.loadInstrument(instr[0]);
			System.out.println("Setting instruments.");
			for(int i = 0; i < mc.length; i++)
			{
				mc[i].programChange(0,instrID);
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

		while(loop)
		{
			phatBeats.setTime(now);
			for(int i = 0; i < rips.length; i++)
			{
				if(rips[i]!=null)
				{
					rips[i].update();
					if(rips[i].isDead())
					{
						rips[i]=null;
					}
				}
				else
				{
					if(phatBeats.getBeat()!=0&&i!=9)
					{
						if(rng.nextDouble()<0.9/(phatBeats.getBeat()))
						{
							newRip(i);
						}
						break;
					}
				}
			}
			float volume = 0;
			for(int i = 0; i < rocksNShit.length; i++)
			{
				if(rocksNShit[i]!=null)
				{
					if(rocksNShit[i].isDead(screenWidth, screenHeight))
					{
						rocksNShit[i] = null;
					}
					else
					{
						rocksNShit[i].update();
						volume+=rocksNShit[i].getVolume();
					}
				}
				else
				{
					if(volume<screenWidth*screenHeight)
					{
						rocksNShit[i] = new Debris((screenWidth),(screenHeight),screenWidth*screenHeight/2);
						volume+=rocksNShit[i].getVolume();
					}
				}
			}


			ps.repaint();
			currentFPS = (int) Math.ceil( TARGET_TIME_BETWEEN_UPDATES*10 / (now-lastUpdateTime));
			if(frameCounter==TARGET_FPS)
			{

				//System.out.println("FPS: " + currentFPS);
				frameCounter = 0;
			}
			frameCounter++;

			lastUpdateTime += TARGET_TIME_BETWEEN_UPDATES;

			while ( now - lastUpdateTime < TARGET_TIME_BETWEEN_UPDATES)
			{
				Thread.yield();

				//This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
				//You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
				//FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
				try {Thread.sleep(1);} catch(Exception e) {}

				loop = running;

				now = System.nanoTime();
			}

		}
		ps.dispose();

	}

	private class Drawing extends JFrame implements KeyListener {


		/**
		 * 
		 */
		private static final long serialVersionUID = -3358805991593520296L;

		public Drawing(int x, int y) {
			super("welcome to art");
			initUI(x,y);
		}

		private void initUI(int x, int y) {
			setLocationRelativeTo(null);

			add(new Surface());

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			this.setLocation(10, 10);


			x+=10;
			y+=28;
			setSize(x, y);


			this.setResizable(true);
			this.setFocusable(true);

			this.setVisible(true);

			this.addKeyListener(this);


		}

		private class Surface extends JPanel {


			/**
			 * 
			 */
			private static final long serialVersionUID = 670446242401454707L;

			public Surface()
			{
				setBackground(Color.WHITE);
			}

			private void doDrawing(Graphics g) {

				screenWidth=this.getWidth();
				screenHeight=this.getHeight();

				if(running)
				{
					Graphics2D g2d = (Graphics2D) g;

					for(int i = 0; i < rocksNShit.length; i++)
					{
						if(rocksNShit[i]!=null)
						{
							rocksNShit[i].draw(g2d);
						}
					}

					g2d.setColor(Color.BLUE);
					for(int i = 0; i < rips.length; i++)
					{
						if(rips[i]!=null)
						{
							rips[i].draw(g2d);
						}
					}



					if(drawFPS)
					{
						String txt = new String();
						txt+=currentFPS*10;
						txt+="%";
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
						g2d.setColor(Color.RED);
						g2d.drawString(txt, 0,screenHeight);
					}

					if(helpAlpha>0)
					{					
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, helpAlpha  ));
						g2d.setColor(Color.BLACK);

						String txt = new String();
						txt+="UP and DOWN change volume";
						g2d.drawString(txt,0,screenHeight/2);

						txt = new String();
						txt+="1 - 4 change styles";
						g2d.drawString(txt,0,screenHeight/2+g.getFontMetrics().getHeight());

						helpAlpha-=(float)1/TARGET_FPS/4;
					}
				}
			}



			@Override
			public void paintComponent(Graphics g) {

				super.paintComponent(g);

				doDrawing(g);
			}

		}

		@Override
		public void keyPressed(KeyEvent e) {

			switch( e.getKeyCode())
			{
			case KeyEvent.VK_ESCAPE:
				running=false;
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
				drawFPS=!drawFPS;
				helpAlpha = 1;
				break;
			case KeyEvent.VK_DOWN:
				if(vol>0)
				{
					vol-=5;
				}
				break;
			case KeyEvent.VK_UP:
				if(vol<50)
				{
					vol+=5;
				}
				break;
			}

		}

		@Override public void keyReleased(KeyEvent arg0) { }

		@Override public void keyTyped(KeyEvent arg0) { }

	}

	private void newRip(int id)
	{
		int octRange = 3;
		Chord chord = phatBeats.getChord();
		chord.offsetOctave(0);
		int note = chord.getInterval(rng.nextInt(chord.getChordLength()));
		note+=12*rng.nextInt(octRange);
		//note+=12*(octRange);
		int lowNote = chord.getInterval(0);
		int highNote = chord.getInterval(chord.getChordLength()-1) + 12*octRange;

		double pitchRatio = Math.pow(1-((double)(note-lowNote))/(highNote-lowNote),2);

		mc[id].noteOn(note,vol);

		//System.out.print(Chord.intToNote(note)+ " " + phatBeats.getChordID() + "\t") ;

		//System.out.println(id+"\t"+synth.getAvailableInstruments()[mc[id].getProgram()].toString());

		if(mc[id].getProgram()!=instrID)
		{
			System.out.println("HONK channel" + id + " Instrument: " + mc[id].getProgram());
		}

		rips[id]= new Ripple(screenWidth, screenHeight, pitchRatio);

	}

	public void changeToFrun()
	{
		phatBeats = new Signature(fourFour, 20, frun, System.nanoTime());
	}

	public void changeToMablas()
	{
		phatBeats = new Signature(swing, 50, mablas, System.nanoTime());
	}

	public void changeToWay()
	{
		
		phatBeats = new Signature(swing2, 80, way, System.nanoTime());
	}
	
	public void changeToCanon()
	{
		phatBeats = new Signature(canonTime, 30, canon, System.nanoTime());
	}


}
