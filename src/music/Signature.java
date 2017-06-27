package music;

/**
 * {@code Signature} is the managing class for chord progressions, tempos and
 * time signatures.
 * 
 * @author Edward
 */
public class Signature
{

	private int[]		beats_;
	private int			beatCurrent_;
	private double	lastPlayedTime_;
	private int			tempo_;
	private boolean	onBeat_;
	private Chord[]	chords_;
	private int			chordCurrent_;



	/**
	 * Initialises an instance of the {@code Signature} class.
	 * 
	 * @param beats Time signature to be used.
	 * @param tempo Indicates speed. Number of time signature bars played per
	 *          minute.
	 * @param chords All chords used in the piece.
	 * @param currentTime Time which the object will use as its starting point.
	 *          (Use {@link System#nanoTime()})
	 */
	public Signature(int[] beats, int tempo, Chord[] chords, double currentTime)
	{
		this.lastPlayedTime_ = currentTime;
		this.beats_ = beats;
		this.tempo_ = tempo;
		this.chords_ = chords;
		this.beatCurrent_ = 0;
		this.chordCurrent_ = 0;
		this.onBeat_ = false;
	}



	/**
	 * Checks the given time against the time a note was previously (supposed to
	 * be) played. Determines if the next note is due.
	 * 
	 * @param currentTime The new time to be compared against the previous note.
	 *          (Use {@link System#nanoTime()})
	 */
	public void setTime(double currentTime)
	{
		double tempoClock = 1000000000 / ((double) tempo_ / (60 / beats_.length));
		if (currentTime >= lastPlayedTime_ + tempoClock)
		{
			lastPlayedTime_ += tempoClock;
			beatCurrent_++;
			if (beatCurrent_ >= beats_.length)
			{
				beatCurrent_ = 0;
				chordCurrent_++;
				if (chordCurrent_ >= chords_.length)
				{
					chordCurrent_ = 0;
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
	 * @return The time signature value for the current beat OR 0 if no note is
	 *         due.
	 * @see Signature#setTime(double)
	 */
	public int getBeat()
	{
		if (onBeat_)
		{
			return beats_[beatCurrent_];
		}
		else
		{
			return 0;
		}
	}



	/**
	 * Changes the tempo, the number of time signature bars that are played per
	 * minute.
	 * 
	 * @param tempo New tempo to be used.
	 */
	public void setTempo(int tempo)
	{
		this.tempo_ = tempo;
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
