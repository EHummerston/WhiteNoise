package music;

/**
 * The {@code Chord} class is a data structure which attempts to represent a
 * collection of musical pitches. Instances of this class are used in the
 * {@code ChordProgression} (TODO: update ChordProgression reference) class.
 * 
 * @author Edward Hummerston
 */
public class Chord
{

  private int   root_;
  private int[] intervals_;



  /**
   * Constructs the {@code Chord} instance using {@code char}s to interpret the
   * root note.
   * 
   * @param root The MIDI value for the root note. C is 0, each semitone higher
   *          is +1.
   * @param intervals A list of integers, each indicating how many semitones an
   *          interval is above the root note.
   */
  public Chord(int root, int[] intervals)
  {
    this.root_ = root;
    this.intervals_ = intervals;
  }



  /**
   * Constructs the {@code Chord} instance using {@code char}s to interpret the
   * root note.
   * 
   * @param pedal The letter of the root note pitch.
   * @param accidental The sharp/flat symbol (if any). Use '#' for sharp and 'b'
   *          for flat. Any other {@code char} is interpreted as natural.
   * @param intervals A list of integers, each indicating how many semitones an
   *          interval is above the root note.
   */
  public Chord(char pedal, char accidental, int[] intervals)
  {
    int base = (int) accidental;

    base -= 65;
    if (base > 26)
    {
      base -= 32;
    }
    switch (accidental)
    {
    default:
      break;
    case '#':
      base++;
      break;
    case 'b':
      base--;
      break;
    }
    base = base % 12;

    base += 69;
    this.root_ = base;

    this.intervals_ = intervals;
  }



  public int getChordLength()
  {
    return intervals_.length;
  }



  /**
   * Returns the MIDI value for a pitch in this chord.
   * 
   * @param id Indicates which note of the chord to retrieve. {@code 0} will
   *          return the root note.
   * @return The MIDI value of the intended pitch.
   */
  public int getInterval(int id)
  {
    return root_ + intervals_[id];
  }



  /**
   * Converts a MIDI note value to a string.
   * 
   * @param pitch MIDI value of a pitch.
   * @return String of the note's name. It is a capital letter, followed by an
   *         accidental if appropriate.
   */
  public static String intToNote(int pitch)
  {
    String outString = new String();
    switch (pitch % 12)
    {
    case 0:
      outString += "C";
      break;
    case 1:
      outString += "C#";
      break;
    case 2:
      outString += "D";
      break;
    case 3:
      outString += "Eb";
      break;
    case 4:
      outString += "E";
      break;
    case 5:
      outString += "F";
      break;
    case 6:
      outString += "F#";
      break;
    case 7:
      outString += "G";
      break;
    case 8:
      outString += "Ab";
      break;
    case 9:
      outString += "A";
      break;
    case 10:
      outString += "Bb";
      break;
    case 11:
      outString += "B";
      break;
    }

    return outString;
  }



  public boolean equals(Chord other)
  {
    for (int i = 0; (i < this.getChordLength()) && (i < other.getChordLength());
        i++)
    {
      if (!(this.getInterval(i) == other.getInterval(i)))
      {
        return false;
      }
    }
    return true;
  }
}
