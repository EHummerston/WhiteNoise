
public class Signature {

	int[] beats;
	int bID;
	double lastBeat;
	int tempo;
	boolean onBeat;
	Chord[] chords;
	int chID;
	
	public Signature (int[] beats, int tempo, Chord[] chords, double currentTime)
	{
		this.lastBeat = currentTime;
		this.beats = beats;
		this.tempo = tempo;
		this.chords = chords;
		this.bID = 0;
		this.chID = 0;
		this.onBeat = false;
	}
	
	public void setTime(double currentTime)
	{
		double tempoClock = 1000000000/((double)tempo/(60/beats.length));
		if(currentTime >= lastBeat+tempoClock)
		{
			lastBeat += tempoClock;
			bID++;
			if(bID>=beats.length)
			{
				bID=0;
				chID++;
				if(chID>=chords.length)
				{
					chID=0;
				}
			}
			onBeat = true;
		}
		else
		{
			onBeat = false;
		}
	}
	
	public int getBeat()
	{
		if(onBeat)
		{
			return beats[bID];
		}
		else
		{
			return 0;
		}
	}
	
	public void setTempo(int tempo)
	{
		this.tempo = tempo;
	}
	
	public Chord getChord()
	{
		return chords[chID];
	}
	
	public int getChordID()
	{
		return chID;
	}
	
}
