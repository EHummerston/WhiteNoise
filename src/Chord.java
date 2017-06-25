
public class Chord {

	private int pedal;
	private int[] intervals;

	public Chord(int baseNote, int[] intervals) {
		this.pedal = baseNote;
		this.intervals = intervals;
	}

	public Chord(char pedal, char accid, int[] intervals) {
		int base = (int) accid;

		base -= 65;
		if (base > 26) {
			base -= 32;
		}
		switch (accid) {
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
		this.pedal = base;

		this.intervals = intervals;
	}

	public int getChordLength() {
		return intervals.length;
	}

	public int getInterval(int id) {
		return pedal + intervals[id];
	}

	public static String intToNote(int pitch) {
		String strang = new String();
		switch (pitch % 12) {
		case 0:
			strang += "C";
			break;
		case 1:
			strang += "C#";
			break;
		case 2:
			strang += "D";
			break;
		case 3:
			strang += "Eb";
			break;
		case 4:
			strang += "E";
			break;
		case 5:
			strang += "F";
			break;
		case 6:
			strang += "F#";
			break;
		case 7:
			strang += "G";
			break;
		case 8:
			strang += "Ab";
			break;
		case 9:
			strang += "A";
			break;
		case 10:
			strang += "Bb";
			break;
		case 11:
			strang += "B";
			break;
		}

		return strang;
	}

	public void offsetOctave(int offset) {
		pedal += 12 * offset;
	}

}
