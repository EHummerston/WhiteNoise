package music;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

import visuals.Rain;

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

  Piece piece;

  private Sequencer sequencer_;
  private Sequence  sequence_;
  private Track     track_;

  final static int  RESOLUTION       = 10;
  final static int  BPM              = 240;
  final static long TICKS_PER_SECOND = (long) (RESOLUTION * (BPM / 60.0));
  final static long TICK_SIZE        = (long) (1.0 / TICKS_PER_SECOND);

  final static int volume = 83;

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
      sequence_ = new Sequence(Sequence.PPQ, RESOLUTION);
      track_ = sequence_.createTrack();
    }
    catch (InvalidMidiDataException e1)
    {
      e1.printStackTrace();
    }

    running = true;

    double now = RESOLUTION;

    boolean loop = true;

    this.changeToCanon();

    System.out.println("Begin Main Loop");
    while (loop)
    {

      piece.setTime(now);

      if (piece.getBeat() != 0)
      {
        try
        {
          if (!piece.playBeat(track_, volume, rng))
          {
            break;
          }
        }
        catch (InvalidMidiDataException e)
        {
          e.printStackTrace();
          break;
        }
      }

      now += RESOLUTION;

    }

    try
    {
      DateTimeFormatter dtf =
          DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
      LocalDateTime date = LocalDateTime.now();
      String dateString = dtf.format(date);

      System.out.println("bin/" + dateString + ".mid");

      MidiSystem.write(sequence_, 0, new File("bin/" + dateString + ".mid"));
    }
    catch (IOException e1)
    {
      e1.printStackTrace();
    }

    System.out.println("Get default sequencer.");
    try
    {

      sequencer_ = MidiSystem.getSequencer();

      if (sequencer_ == null)
      {
        System.err.println("Error -- sequencer device is not supported.");
        return;
      }
      System.out.println("\tAcquire resources and make operational.");
      sequencer_.open();
      sequencer_.setTempoInBPM(BPM);
      sequencer_.setSequence(sequence_);
      sequencer_.start();

      while (sequencer_.isRunning())
      {
        System.out
            .println((sequencer_.getTickPosition() / TICKS_PER_SECOND) + "s");
        Thread.sleep(1000);
      }

      sequencer_.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
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
        CHORDS_FRUN, RESOLUTION);
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
        CHORDS_MABLAS, RESOLUTION);

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
        CHORDS_WAY, RESOLUTION);
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
        CHORDS_CANON, RESOLUTION);
  }

}
