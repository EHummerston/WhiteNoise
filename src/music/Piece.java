package music;

import java.util.Random;

import javax.sound.midi.MidiChannel;

/**
 * {@code Piece} is the managing class for chord progressions, tempos and
 * {@link TimeSignature}s.
 * 
 * @author Edward Hummerston
 */
public class Piece
{

  private TimeSignature timeSignature_;
  private int           beatCurrent_;
  private double        lastPlayedTime_;
  private boolean       onBeat_;
  private Chord[]       chords_;
  private int           chordCurrent_;

  int loops;



  /**
   * Initialises an instance of the {@code Piece} class.
   * 
   * @param beats {@link TimeSignature} to be used.
   * @param chords All chords used in the piece.
   * @param startTime Time which the object will use as its starting point. (Use
   *          {@link System#nanoTime()})
   */
  public Piece(TimeSignature timeSignature, Chord[] chords, double startTime)
  {
    this.lastPlayedTime_ = startTime;
    this.timeSignature_ = timeSignature;
    this.chords_ = chords;
    this.beatCurrent_ = -1;
    this.chordCurrent_ = 0;
    this.onBeat_ = false;
    this.loops = 0;
  }



  /**
   * Checks the given time against the time a note was previously (supposed to
   * be) played. Determines if the next note is due based on the
   * {@link TimeSignature}. Affects the result of {@link Piece#getBeat()}.
   * 
   * @param currentTime The new time to be compared against the previous note.
   *          (Use {@link System#nanoTime()})
   */
  public void setTime(double currentTime)
  {
    double tempoClock = timeSignature_.getNanoDuration();
    double noteDue = (lastPlayedTime_ + tempoClock) - currentTime;
    if (noteDue <= 0)
    {
      lastPlayedTime_ += tempoClock;
      beatCurrent_++;
      if (beatCurrent_ >= timeSignature_.getNoteValuesLength())
      {
        beatCurrent_ = 0;
        chordCurrent_++;
        if (chordCurrent_ >= chords_.length)
        {
          chordCurrent_ = 0;
          loops++;
        }
      }
      onBeat_ = true;
    }
    else
    {
      onBeat_ = false;
    }
  }



  /**
   * Retrieves the value set by the time signature for the current beat (if
   * any).
   * 
   * @return The {@link TimeSignature} value for the current beat OR 0 if no
   *         note is due.
   * @see Piece#setTime(double)
   */
  public int getBeat()
  {
    if (onBeat_)
    {
      return timeSignature_.getNoteValue(beatCurrent_);
    }
    else
    {
      return 0;
    }
  }



  /**
   * Plays notes to the supplied MIDI synthesizer channels, based on the current
   * position within the {@link ChordProgression} and {@link TimeSignature}.
   * Randomly chooses melody pitch based on active {@link Chord}.
   *
   * @param midiChannels MIDI Synthesizer Channels to play notes to. <br />
   *          Channels used:
   *          <ol start="0">
   *          <li>Melody</li>
   *          <li>Chord Accompaniment</li>
   *          <li value="9">Percussion</li>
   *          </ol>
   * @param volume MIDI Channel to play note in.
   * @param random {@link Random} to use for determining if and what notes to
   *          play in the melody.
   */
  public boolean playBeat(MidiChannel[] midiChannels, int volume, Random random)
  {
    if (loops > 3)
    {
      midiChannels[0].allNotesOff();
      midiChannels[1].allNotesOff();
      return false;
    }
    Chord currentChord = chords_[chordCurrent_];

    // Melody
    if (random.nextDouble() <= (double) 0.9 /
        timeSignature_.getNoteValue(beatCurrent_))
    {
      midiChannels[0].allNotesOff();
      int octRange = 3;

      int note = currentChord
          .getInterval(random.nextInt(currentChord.getChordLength()));
      note += 12 * random.nextInt(octRange);

      midiChannels[0].noteOn(note,
          10 + volume - timeSignature_.getNoteValue(beatCurrent_));
    }

    // Accompaniment
    if (beatCurrent_ == 0 && loops > 0)
    {
      midiChannels[1].allNotesOff();
      for (int i = 0; i < currentChord.getChordLength(); i++)
      {
        midiChannels[1].noteOn(currentChord.getInterval(i) - 12,
            volume * 3 / 4);
      }
      System.out.println("\t" + Chord.intToNote(currentChord.getInterval(0)));
    }

    // Drums
    if (loops > 1)
    {
      midiChannels[9].noteOn(42, volume);
      if (beatCurrent_ == 0)
      {
        midiChannels[9].noteOn(36, volume);
      }
      if (beatCurrent_ == timeSignature_.getNoteValuesLength() / 2)
      {
        midiChannels[9].noteOn(40, volume);
      }
    }

    return true;
  }



  /**
   * Changes the tempo, the number of {@link TimeSignature} bars that are played
   * per minute.
   * 
   * @param tempo New tempo to be used.
   */
  public void setTempo(int tempo)
  {
    timeSignature_.setTempo(tempo);
  }



  /**
   * Retrieves the {@link Chord} from the chord progression that the piece has
   * set as current.
   * 
   * @return The current chord.
   */
  public Chord getChord()
  {
    return chords_[chordCurrent_];
  }



  /**
   * Retrieves the iterator value that the class uses to step through the chord
   * progression.
   * 
   * @return The iterator value of the chord progression.
   */
  public int getChordID()
  {
    return chordCurrent_;
  }

}
