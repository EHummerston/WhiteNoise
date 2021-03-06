package music;

import java.util.Random;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import visuals.Rain;
import visuals.Ripple;

/**
 * The White Noise program is an experimental system for generating random
 * music.
 * 
 * @author Edward Hummerston
 * @version 1.0
 */
public class WhiteNoise
{

  Random rng;

  boolean running = false;
  boolean paused  = false;

  public int screenWidth = 1280, screenHeight = 720;

  Piece piece;

  private Synthesizer   synthesizer_;
  private MidiChannel[] midiChannels_;

  int     currentFPS;
  boolean drawFPS = false;

  public final static double TARGET_FPS                  = 100;
  public final static double TARGET_TIME_BETWEEN_UPDATES =
      1000000000 / TARGET_FPS;
  int                        frameCounter                = 0;

  int volume = 20;

  float helpAlpha = 1;

  // int instrID = 11;
  int instrBank = 1024;
  int instrID   = 80;

  final static int[] TIME_SIGNATURE_FOUR_FOUR =
      { 1, 4, 8, 4, 1, 4, 8, 4, 1, 4, 8, 4, 1, 4, 8, 4 };
  final static int[] TIME_SIGNATURE_FUNK      = { 1, 0, 2, 1, 0, 3 };
  final static int[] TIME_SIGNATURE_SWING     = { 1, 0, 2, 1, 0, 3 };
  final static int[] TIME_SIGNATURE_CANON     = { 1, 8, 4, 8, 2, 8, 4, 8 };

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

    new WhiteNoise();

  }



  public WhiteNoise()
  {

    rng = new Random();

    try
    {
      System.out.println("Retrieving MIDI synthesizer.");
      synthesizer_ = MidiSystem.getSynthesizer();
      System.out.println("Initialising it.");
      synthesizer_.open();
      System.out.println("Retrieving channels.");
      midiChannels_ = synthesizer_.getChannels();
      Instrument[] instr = synthesizer_.getAvailableInstruments();

      StringBuilder sb = new StringBuilder();
      String eol = System.getProperty("line.separator");
      sb.append("The orchestra has " + instr.length + " instruments." + eol);
      for (Instrument instrument : instr)
      {
        sb.append(instrument.toString());
        sb.append(eol);
      }
      System.out.println(sb.toString());

      synthesizer_.loadInstrument(instr[0]);
      System.out.println("Setting instruments.");
      for (int i = 0; i < midiChannels_.length; i++)
      {

        midiChannels_[i].programChange(instrBank, instrID);
        midiChannels_[i].setMono(false);
        if (i == 9)
        {
          midiChannels_[i].programChange(0, 0);
        }
      }
      midiChannels_[0].programChange(0, 11);
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

    this.changeToMablas();

    System.out.println("Begin Main Loop");
    while (loop)
    {

      piece.setTime(now);

      if (piece.getBeat() != 0)
      {
        if (!piece.playBeat(midiChannels_, volume, rng))
        {
          break;
        }
      }

      currentFPS = (int) Math
          .ceil(TARGET_TIME_BETWEEN_UPDATES * 10 / (now - lastUpdateTime));
      if (frameCounter == TARGET_FPS)
      {

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
    System.out.println("Closing.");

  }



  /**
   * Changes current {@link Chord} and {@link Piece} to the "Frun" piece.
   * 
   * @see Rain#TIME_SIGNATURE_FOUR_FOUR
   * @see Rain#CHORDS_FRUN
   */
  public void changeToFrun()
  {
    piece = new Piece(new TimeSignature(TIME_SIGNATURE_FOUR_FOUR, 4, 80),
        CHORDS_FRUN, System.nanoTime());
  }



  /**
   * Changes current {@link Chord} and {@link Piece} to resemble Master Blaster
   * by Stevie Wonder.
   * 
   * @see Rain#TIME_SIGNATURE_FUNK
   * @see Rain#CHORDS_MABLAS
   */
  public void changeToMablas()
  {
    piece = new Piece(new TimeSignature(TIME_SIGNATURE_FUNK, 2, 131),
        CHORDS_MABLAS, System.nanoTime());

  }



  /**
   * Changes current {@link Chord} and {@link Piece} to the "Way" structure.
   * 
   * @see Rain#TIME_SIGNATURE_SWING
   * @see Rain#CHORDS_WAY
   */
  public void changeToWay()
  {

    piece = new Piece(new TimeSignature(TIME_SIGNATURE_SWING, 2, 160),
        CHORDS_WAY, System.nanoTime());
  }



  /**
   * Changes current {@link Chord} and {@link Piece} to Pachelbel's Canon in D.
   * 
   * @see Rain#TIME_SIGNATURE_CANON
   * @see Rain#CHORDS_CANON
   */
  public void changeToCanon()
  {
    piece = new Piece(new TimeSignature(TIME_SIGNATURE_CANON, 1, 30),
        CHORDS_CANON, System.nanoTime());
  }

}
