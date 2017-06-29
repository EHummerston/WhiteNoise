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
   * @param noteValues
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
   * Determines the duration of each segment of the {@code noteValues} list.
   * Does <strong>not</strong> represent the duration of each <i>beat</i>.
   * 
   * @return Duration of each segment in nanoseconds.
   */
  public double getNoteDuration()
  {
    double beatFractions = (double) beatCount_ / (double) noteValues_.size();
    double fractionsPerSecond = (double) tempo_ / 60.0 * beatFractions;

    double tempoClock = 1000000000 / fractionsPerSecond;

    return tempoClock;
  }
}
