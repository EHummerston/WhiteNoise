package music;

import java.util.ArrayList;

/**
 * The {@code TimeSignature} class represents a musical bar within which rhythms
 * can be derived.
 * 
 * @author Edward Hummerston
 */
public class TimeSignature
{
  private ArrayList<Integer> noteValues_;
  private int                beatCount_;
  private int                tempo_;



  /**
   * Initialises a {@code TimeSignature} object with the given values.
   * {@code tempo} does not support irregular time signatures where all beats
   * are not the same length.
   * 
   * @param noteValues List of integers indicating emphasis of notes on that
   *          segment of time.
   * @param beatCount Indicates how many "beats" are in each bar. Represents the
   *          upper numeral in time (or meter) signature notation.
   * @param tempo How many beats are played in a minute.
   */
  public TimeSignature(ArrayList<Integer> noteValues, int beatCount, int tempo)
  {
    this.noteValues_ = noteValues;
    this.beatCount_ = beatCount;
    this.tempo_ = tempo;
  }



  /**
   * Constructor which converts a primitive array ({@code int[]}) into the
   * stored {@link ArrayList}. Initialises a {@code TimeSignature} object with
   * the given values. {@code tempo} does not support irregular time signatures
   * where all beats are not the same length.
   * 
   * @param noteValues List of integers indicating emphasis of notes on that
   *          segment of time.
   * @param beatCount Indicates how many "beats" are in each bar. Represents the
   *          upper numeral in time (or meter) signature notation.
   * @param tempo How many beats are played in a minute.
   */
  public TimeSignature(int[] noteValues, int beatCount, int tempo)
  {
    this.noteValues_ = new ArrayList<Integer>();
    for (int i = 0; i < noteValues.length; i++)
    {
      noteValues_.add(noteValues[i]);
    }
    this.beatCount_ = beatCount;
    this.tempo_ = tempo;
  }



  public int getNoteValue(int index)
  {
    return noteValues_.get(index);
  }



  public int getNoteValuesLength()
  {
    return noteValues_.size();
  }



  public int getBeatCount()
  {
    return beatCount_;
  }



  /**
   * Retrieves the current speed of the {@code TimeSignature}.
   * 
   * @return How many beats are played in a minute.
   */
  public int getTempo()
  {
    return tempo_;
  }



  /**
   * Sets a new speed for this {@code TimeSignature} to be played at. May have a
   * rough transition when a faster tempo results in the next note being
   * overdue.
   * 
   * @param tempo_ How many beats are played in a minute.
   */
  public void setTempo(int tempo)
  {
    this.tempo_ = tempo;
  }



  /**
   * Determines the duration of each segment of the {@code noteValues} list in
   * nanoseconds. Does <strong>not</strong> represent the duration of each
   * <i>beat</i>.
   * 
   * @return Duration of each segment in nanoseconds.
   */
  public double getNanoDuration()
  {
    double bps = (double) tempo_ / 60.0;
    double segmentsPerBeat = (double) noteValues_.size() / (double) beatCount_;
    double fractionsPerSecond = bps * segmentsPerBeat;

    double tempoClock = (Math.pow(10, 9)) / fractionsPerSecond;

    return tempoClock;
  }
}
