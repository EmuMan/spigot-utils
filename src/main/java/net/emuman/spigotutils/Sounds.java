package net.emuman.spigotutils;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class Sounds {

    /**
     * Returns the pitch float to be passed into Player#playSound, based off of an integer note value.
     *
     * @param note the note that should be played (0 is C4, 24 is C6, or something like that, it depends on the sample).
     * @return     the float representing the desired pitch.
     */
    public static float getPitchForNote(int note) {
        // Pitch is logarithmic
        return (float) Math.pow(2.0d, (double) note / 12.0d) / 2.0f;
    }

    /**
     * Converts a sequence of integers representing pitches to note objects.
     * @param notes the sequence of integers to be converted (0-24 inclusive, negative numbers will be turned into null objects).
     * @return      the array of Note objects.
     */
    public static Note[] toNoteSequence(int[] notes) {
        // negative values should be assigned to null notes.
        // Streams have failed me and i do not know why :pensive:
//        return (Note[]) Arrays.stream(notes).mapToObj(note -> (note < 0) ? null : new Note(note)).toArray();
        Note[] noteArray = new Note[notes.length];
        for (int i = 0; i < notes.length; i++) {
            if (notes[i] < 0) {
                noteArray[i] = null;
            } else {
                noteArray[i] = new Note(notes[i]);
            }
        }
        return noteArray;
    }

    /**
     * Plays a sequence of notes at the given rate. Null values are not played and are treated as rests.
     *
     * @param plugin     the plugin to play the notes on.
     * @param player     the player that will hear the notes.
     * @param instrument the instrument that the notes will be played on.
     * @param notes      the sequence of Note objects representing the note (use toNoteSequence for ease).
     * @param noteRate   the rate at which the notes will be played at (in game ticks).
     */
    public static void playSequence(JavaPlugin plugin, Player player, Instrument instrument, Note[] notes, int noteRate) {
        new BukkitRunnable() {
            int currentNote = 0;
            @Override
            public void run() {
                if (notes[currentNote] != null) {
                    player.playNote(player.getLocation(), instrument, notes[currentNote]);
                }
                currentNote++;
                if (currentNote == notes.length) cancel();
            }
        }.runTaskTimer(plugin, 0L, noteRate);
    }

    /**
     * Plays a sequence of sounds at the given rate. Works with sounds that are not technically notes.
     *
     * Negative note values are not played and are treated as rests.
     *
     * @param plugin   the plugin to play the notes on.
     * @param player   the player that will hear the notes.
     * @param sound    the sound that the notes will be played with.
     * @param category the audio category that the sounds will fall under.
     * @param volume   the volume of the sounds.
     * @param notes    the sequence of integers representing the notes (-1 will be turned into a null object).
     * @param noteRate   the rate at which the notes will be played at (in game ticks).
     */
    public static void playSequence(JavaPlugin plugin, Player player, Sound sound, SoundCategory category, float volume, int[] notes, int noteRate) {
        new BukkitRunnable() {
            int currentNote = 0;
            @Override
            public void run() {
                if (notes[currentNote] >= 0) {
                    player.playSound(player.getLocation(), sound, category, volume, getPitchForNote(notes[currentNote]));
                }
                currentNote++;
                if (currentNote == notes.length) cancel();
            }
        }.runTaskTimer(plugin, 0L, noteRate);
    }

}
