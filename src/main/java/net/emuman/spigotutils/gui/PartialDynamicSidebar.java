package net.emuman.spigotutils.gui;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * A sidebar that can be dynamically set and updated.
 *
 * The difference between this class and the FullDynamicSidebar is that only the first and last 16 characters in any given line can be updated.
 *
 * However, this comes with the advantage that less than 15 lines may be present, so use this class instead when possible.
 */
public class PartialDynamicSidebar extends BukkitRunnable {

    private final Scoreboard board;
    private final Objective objective;
    private String title;

    protected final List<String> lines;
    protected final List<Team> teams;

    private final Map<Integer, Supplier<String>> prefixUpdaters;
    private final Map<Integer, Supplier<String>> suffixUpdaters;

    private Supplier<String> titleUpdater = null;

    /**
     * Creates a new PartialDynamicSidebar.
     *
     * @param id    the unique ID for the sidebar (must be 16 characters or less).
     * @param title the display name for the sidebar, appearing at the top.
     * @param board the scoreboard to assign the sidebar to.
     */
    public PartialDynamicSidebar(String id, String title, Scoreboard board) {
        this.board = board;
        this.objective = board.registerNewObjective(id, "dummy", title);
        this.title = title;
        this.teams = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.prefixUpdaters = new HashMap<>();
        this.suffixUpdaters = new HashMap<>();

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Creates a new PartialDynamicSidebar, using a newly created scoreboard.
     *
     * @param id    the unique ID for the sidebar (must be 16 characters or less).
     * @param title the display name for the sidebar, appearing at the top.
     */
    public PartialDynamicSidebar(String id, String title) {
        this(id, title, Bukkit.getScoreboardManager().getNewScoreboard());
    }

    /**
     * Adds a new line to the dynamic sidebar.
     *
     * @param body the body content of the line (must be 16 characters or under in length).
     */
    public void addLine(String body) {
        if (teams.size() > 15) return;
        if (body.length() > 16) body = body.substring(0, 16);
        if (lines.contains(body)) return; // cannot have duplicates
        lines.add(body);
        Team newTeam = board.registerNewTeam("sidebar_row_" + teams.size());
        teams.add(newTeam);
        newTeam.addEntry(body);
        for (String line : lines) {
            // Shift every line score up one so we can insert this line as the lowest score, 0
            int currentScore = objective.getScore(line).getScore();
            objective.getScore(line).setScore(currentScore + 1);
        }
        objective.getScore(body).setScore(0);
    }

    /**
     * Sets the first 16 characters of a line to the provided static text.
     *
     * @param line the index of the line to set (must be between 0 and 14, both inclusive).
     * @param text the text to populate the prefix of the line.
     */
    public void setLinePrefix(int line, String text) {
        assert line >= 0 && line < teams.size();
        if (text.length() > 16) text = text.substring(0, 16);
        teams.get(line).setPrefix(text);
    }

    /**
     * Sets the first 16 characters of a line to the provided dynamic updater.
     *
     * @param line the index of the line to set (must be between 0 and 14, both inclusive).
     * @param updater the supplier that provides the line prefix with content.
     */
    public void setLinePrefix(int line, Supplier<String> updater) {
        assert line >= 0 && line < teams.size();
        prefixUpdaters.put(line, updater);
    }

    /**
     * Sets the last 16 characters of a line to the provided static text.
     *
     * @param line the index of the line to set (must be between 0 and 14, both inclusive).
     * @param text the text to populate the suffix of the line.
     */
    public void setLineSuffix(int line, String text) {
        assert line >= 0 && line < teams.size();
        if (text.length() > 16) text = text.substring(0, 16);
        teams.get(line).setSuffix(text);
    }

    /**
     * Sets the last 16 characters of a line to the provided dynamic updater.
     *
     * @param line the index of the line to set (must be between 0 and 14, both inclusive).
     * @param updater the supplier that provides the line suffix with content.
     */
    public void setLineSuffix(int line, Supplier<String> updater) {
        assert line >= 0 && line < teams.size();
        suffixUpdaters.put(line, updater);
    }

    /**
     * Removes all of the updaters for the given line, allowing new static strings to take precedence.
     *
     * @param line the line for which to remove the dynamic updaters.
     */
    public void removeUpdaters(int line) {
        prefixUpdaters.remove(line);
        suffixUpdaters.remove(line);
        removeTitleUpdater();
    }

    /**
     * Blanks the prefix and suffix of the given line. Does not modify the line body (use FullDynamicSidebar for that functionality).
     *
     * @param line the line that should be blanked out.
     */
    public void blankFullLine(int line) {
        blankLinePrefix(line);
        blankLineSuffix(line);
    }

    /**
     * Blanks the prefix of the given line.
     *
     * @param line the line that should be blanked out.
     */
    public void blankLinePrefix(int line) {
        setLinePrefix(line, "");
    }

    /**
     * Blanks the suffix of the given line.
     *
     * @param line the line that should be blanked out.
     */
    public void blankLineSuffix(int line) {
        setLineSuffix(line, "");
    }

    /**
     * @param title the new title of the scoreboard.
     */
    public void setTitle(String title) {
        this.title = title;
        this.objective.setDisplayName(title);
    }

    /**
     * @param updater the String supplier for the title of the scoreboard.
     */
    public void setTitle(Supplier<String> updater) {
        this.titleUpdater = updater;
    }

    /**
     * Removes the updater for the title, allowing new static strings to take precedence.
     */
    public void removeTitleUpdater() {
        titleUpdater = null;
    }

    /**
     * @return the title of the scoreboard.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the scoreboard object of the sidebar, to be passed into Player#setScoreboard.
     */
    public Scoreboard getBoard() {
        return board;
    }

    /**
     * @return the objective that this scoreboard uses for the sidebar (generally does not need to be called).
     */
    public Objective getObjective() {
        return objective;
    }

    /**
     * @return the number of lines in the scoreboard.
     */
    public int getLength() {
        return lines.size();
    }

    /**
     * Updates all of the lines using the provided updaters. Should generally only be called by Bukkit. Use methods extended from BukkitRunnable to run this class.
     */
    @Override
    public void run() {
        // update the corresponding lines for every updater
        prefixUpdaters.forEach((line, updater) -> setLinePrefix(line, updater.get()));
        suffixUpdaters.forEach((line, updater) -> setLineSuffix(line, updater.get()));
        if (titleUpdater != null) setTitle(titleUpdater.get());
    }
}
