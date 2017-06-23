# White Noise

Making use of native MIDI libraries in Java, White Noise is an experimental system for generating "random"<sup>[citation needed]</sup> "music".<sup>[citation needed]</sup>

## Why it exists

For an author with a passion for procedural generation and music theory, White Noise exists to express and practice concepts from both schools, with the interest of producing something that is pleasant and perhaps even interesting to listen to.

## How it works

The program uses data structures of **Time Signatures** and **Chords**.

### Time Signatures

A time signature is used in White Noise to decide *when* to play notes.

We represent a time signature with a list of evenly-spaced segments of a [bar](https://en.wikipedia.org/wiki/Bar_(music)).  
Example:  
`_` `_` `_` `_`

In each entry, a non-zero value represents the possibility that a note can be played in that space of time.  
Example: ([Swing](https://en.wikipedia.org/wiki/Swing_(jazz_performance_style)#Swing_as_a_rhythmic_style) Feel)  
`■` `_` `■` `■` `_` `■` `■` `_` `■` `■` `_` `■`

Finally each entry has a value indicating the *likelihood* that a note should be played in that space. (Lower values have a higher chance of being played.)  
Example:  
`1` `_` `3` `1` `_` `3` `1` `_` `3` `1` `_` `3`

### Chords

[Chords](https://en.wikipedia.org/wiki/Chord_(music)) in White Noise are a base ([root](https://en.wikipedia.org/wiki/Root_(chord))) note, and a list of intervals which, from being offset from the root, can be interpreted as the remaining [pitch classes](https://en.wikipedia.org/wiki/Pitch_class) to make up the chord.  
Example: (C Major)

Root | MIDI Value
---- | ---- 
C0 | 0
E0 | 4
G0 | 7

These notes are then offset by some number of octaves (multiples of twelve in MIDI) and played in time according to the **Time Signature**.

### Chord Progressions

A single time signature for a piece is fine, but only one chord would be boring. White Noise uses [Chord Progressions](https://en.wikipedia.org/wiki/Chord_progression) to change between chords and therefore available pitches. It plays through one iteration of the **Time Signature** bar, using notes from the first **Chord** in the progression, and then repeats the bar again with the next chord.

So finally we have a **Piece** which is composed of a **Time Signature**, some **Chords** and a **Chord Progression**.

### Example: (*Pachebel's Canon in D*)

#### Time Signature

`1` `8` `4` `8` `2` `8` `4` `8`

#### Chords

Label | Root | Interval 1 | Interval 2
--- | --- | --- | ---
DMaj | 2 | 4 | 7
AMaj | 9 | 4 | 7
Bmin | 11 | 3 | 7
F#min | 6 | 3 | 7
GMaj | 7 | 4 | 7

#### Chord Progression

DMaj, AMaj, Bmin, F#min, GMaj, DMaj, GMaj, AMaj