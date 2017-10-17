package music;

import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

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

    lastPlayedTime_ = currentTime;
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
   * @throws InvalidMidiDataException
   */
  public boolean playBeat(Track track, int volume, Random random)
      throws InvalidMidiDataException
  {
    if (loops > 3)
    {
      return false;
    }

    Chord currentChord = chords_[chordCurrent_];

    // Melody
    if (random.nextDouble() <= (double) 0.9 /
        timeSignature_.getNoteValue(beatCurrent_))
    {

      int octRange = 3;

      int note = currentChord
          .getInterval(random.nextInt(currentChord.getChordLength()));
      note += 12 * random.nextInt(octRange);
      track.add(getEvent(0, note, 10 + volume));
    }

    // Accompaniment
    if (beatCurrent_ == 0 && loops > 0)
    {

      for (int i = 0; i < currentChord.getChordLength(); i++)
      {
        track
            .add(getEvent(1, currentChord.getInterval(i) - 12, volume * 3 / 4));
      }
    }

    // Drums
    if (loops > 1)
    {
      track.add(getEvent(9, 42, volume));

      if (beatCurrent_ == 0)
      {
        track.add(getEvent(9, 36, volume));
      }
      if (beatCurrent_ == timeSignature_.getNoteValuesLength() / 2)
      {
        track.add(getEvent(9, 40, volume));
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



  private MidiEvent getEvent(int channel, int pitch, int volume)
      throws InvalidMidiDataException
  {
    ShortMessage msg = new ShortMessage();
    msg.setMessage(ShortMessage.NOTE_ON, channel, pitch, volume);
    return new MidiEvent(msg, (long) lastPlayedTime_);
  }
}
