package music;

public class Signature
{

	private int[]		beats_;
	private int			beatCurrent_;
	private double	beatPrevious_;
	private int			tempo_;
	private boolean	onBeat_;
	private Chord[]	chords_;
	private int			chordCurrent_;



	public Signature(int[] beats, int tempo, Chord[] chords, double currentTime)
	{
		this.beatPrevious_ = currentTime;
		this.beats_ = beats;
		this.tempo_ = tempo;
		this.chords_ = chords;
		this.beatCurrent_ = 0;
		this.chordCurrent_ = 0;
		this.onBeat_ = false;
	}



	public void setTime(double currentTime)
	{
		double tempoClock = 1000000000 / ((double) tempo_ / (60 / beats_.length));
		if (currentTime >= beatPrevious_ + tempoClock)
		{
			beatPrevious_ += tempoClock;
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



	public void setTempo(int tempo)
	{
		this.tempo_ = tempo;
	}



	public Chord getChord()
	{
		return chords_[chordCurrent_];
	}



	public int getChordID()
	{
		return chordCurrent_;
	}

}
