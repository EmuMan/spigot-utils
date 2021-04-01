package net.emuman.spigotutils.gui;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * A sidebar that can be dynamically set and updated.
 *
 * The difference between this class and the PartialDynamicSidebar is that all 48 characters in any given line can be updated.
 *
 * However, this comes with a slight performance/execution cost, and all 15 lines must be present, so when possible, use PartialDynamicSidebar.
 */
public class FullDynamicSidebar extends PartialDynamicSidebar {

    private final Map<Integer, Supplier<String>> lineUpdaters;
    private final Map<Integer, Supplier<String>> bodyUpdaters;

    /**
     * Creates a new FullDynamicSidebar.
     *
     * @param id    the unique ID for the sidebar (must be 16 characters or less).
     * @param title the display name for the sidebar, appearing at the top.
     * @param board the scoreboard to assign the sidebar to.
     */
    public FullDynamicSidebar(String id, String title, Scoreboard board) {
        super(id, title, board);
        this.lineUpdaters = new HashMap<>();
        this.bodyUpdaters = new HashMap<>();

        for (int i = 0; i < 15; i++) {
            String line = StringUtils.repeat(" ", i + 1);
            super.addLine(line); // This class's addLine is overridden with an unimplemented method
        }
    }

    /**
     * Creates a new FullDynamicSidebar, using a newly created scoreboard.
     *
     * @param id    the unique ID for the sidebar (must be 16 characters or less).
     * @param title the display name for the sidebar, appearing at the top.
     */
    public FullDynamicSidebar(String id, String title) {
        this(id, title, Bukkit.getScoreboardManager().getNewScoreboard());
    }

    /**
     * This method does not fit within the implementation of the FullDynamicSidebar, and should therefore not be used.
     *
     * @param body N/A
     * @throws UnsupportedOperationException since there is no implementation.
     */
    @Override
    public void addLine(String body) {
        throw new UnsupportedOperationException("This method is not available for this implementation.");
    }

    /**
     * Sets a full 48-character long line to the provided static text.
     *
     * It is important to note that two lines cannot have the same middle 16 characters.
     *
     * @param line the index of the line to set (must be between 0 and 14, both inclusive).
     * @param text the text to populate the line.
     */
    public void setFullLine(int line, String text) {
        assert line >= 0 && line < 15;
        blankFullLine(line);
        // TODO: Color codes will probably break in this
        int size = text.length();
        // split the string up into different parts to distribute to the prefix, body, and suffix
        setLinePrefix(line, text.substring(0, Math.min(size, 16)));
        if (size > 16) setLineBody(line, text.substring(16, Math.min(size, 32)));
        if (size > 32) setLineSuffix(line, text.substring(32, Math.min(size, 48)));
    }

    /**
     * Sets a full 48-character long line to the provided dynamic updater.
     *
     * It is important to note that two lines cannot have the same middle 16 characters.
     *
     * @param line the index of the line to set (must be between 0 and 14, both inclusive).
     * @param updater the supplier that provides the line with content.
     */
    public void setFullLine(int line, Supplier<String> updater) {
        assert line >= 0 && line < 15;
        lineUpdaters.put(line, updater);
    }

    /**
     * Sets the middle 16 characters of a line to the provided static text.
     *
     * It is important to note that two lines cannot have the same body.
     *
     * @param line the index of the line to set (must be between 0 and 14, both inclusive).
     * @param text the text to populate the body of the line.
     */
    public void setLineBody(int line, String text) {
        assert line >= 0 && line < 15;
        if (text.length() > 16) text = text.substring(0, 16);
        if (lines.contains(text)) return; // cannot be more than one of the same line visible
        if (lines.get(line) != null && lines.get(line).equals(text)) return; // don't have to update the line, it is the same
        if (lines.get(line) != null) {
            // Push the current line off the screen before adding the new one
            Score score = this.getObjective().getScore(lines.get(line));
            score.setScore(-1);
        }
        // Add the entry to the team if it does not already exist so the prefix/suffix are applied
        if (!teams.get(line).getEntries().contains(text)) teams.get(line).addEntry(text);
        this.getObjective().getScore(text).setScore(14 - line);
        lines.set(line, text);
    }

    /**
     * Sets the middle 16 characters of a line to the provided dynamic updater.
     *
     * It is important to note that two lines cannot have the same body.
     *
     * @param line the index of the line to set (must be between 0 and 14, both inclusive).
     * @param updater the supplier that provides the line body with content.
     */
    public void setLineBody(int line, Supplier<String> updater) {
        assert line >= 0 && line < 15;
        bodyUpdaters.put(line, updater);
    }

    /**
     * Removes all of the updaters for the given line, allowing new static strings to take precedence.
     *
     * @param line the line for which to remove the dynamic updaters.
     */
    public void removeUpdaters(int line) {
        super.removeUpdaters(line);
        lineUpdaters.remove(line);
        bodyUpdaters.remove(line);
    }

    /**
     * Blanks out the given line, taking the limitation that multiple bodies cannot be the same into account.
     *
     * @param line the line that should be blanked out.
     */
    @Override
    public void blankFullLine(int line) {
        super.blankFullLine(line);
        blankLineBody(line);
    }

    /**
     * Blanks out the body for the given line, taking the limitation that multiple bodies cannot be the same into account.
     *
     * @param line the line that should have its body (middle 16 characters) blanked out.
     */
    public void blankLineBody(int line) {
        setLineBody(line, StringUtils.repeat(" ", line + 1));
    }

    /**
     * Updates all of the lines using the provided updaters. Should generally only be called by Bukkit. Use methods extended from BukkitRunnable to run this class.
     */
    @Override
    public void run() {
        // update the corresponding lines for every updater
        super.run();
        lineUpdaters.forEach((line, updater) -> setFullLine(line, updater.get()));
        bodyUpdaters.forEach((line, updater) -> setLineBody(line, updater.get()));
    }
}
