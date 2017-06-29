package music;

import java.util.ArrayList;
import java.util.Iterator;

public class ChordProgression
{
	ArrayList<Chord>		chords_;
	ArrayList<Integer>	order_;



	public ChordProgression(ArrayList<Chord> chords, ArrayList<Integer> order)
	{
		this.chords_ = chords;
		this.order_ = order;
	}



	public ChordProgression(ArrayList<Chord> chords)
	{
		this.order_ = new ArrayList<Integer>();
		this.chords_ = new ArrayList<Chord>();

		for (Iterator<Chord> iterator = chords.iterator(); iterator.hasNext();)
		{
			Chord newChord = iterator.next();
			for (int i = 0; i <= chords_.size(); i++)
			{
				if (i == (chords_.size()))
				{
					chords_.add(newChord);
					order_.add(i);
					break;
				}
				else if (newChord.equals(chords_.get(i)))
				{
					order_.add(i);
					break;
				}
			}
		}
	}
}