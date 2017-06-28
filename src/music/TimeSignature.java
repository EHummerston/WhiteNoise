package music;

import java.util.ArrayList;

public class TimeSignature
{
	private ArrayList<Integer>	noteValues_;
	private int									beatCount_;
	private int									tempo_;



	public TimeSignature(ArrayList<Integer> noteValues, int beatCount, int tempo)
	{
		this.noteValues_ = noteValues;
		this.beatCount_ = beatCount;
		this.tempo_ = tempo;
	}
}
