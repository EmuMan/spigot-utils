package net.emuman.spigotutils.gui;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.function.Supplier;

public class PartialDynamicSidebar extends BukkitRunnable {

    private final Scoreboard board;
    private final Objective objective;
    private String title;

    protected final List<String> lines;
    protected final List<Team> teams;

    private final Map<Integer, Supplier<String>> prefixUpdaters;
    private final Map<Integer, Supplier<String>> suffixUpdaters;

    private Supplier<String> titleUpdater = null;

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

    public PartialDynamicSidebar(String id, String title) {
        this(id, title, Bukkit.getScoreboardManager().getNewScoreboard());
    }

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

    public void setLinePrefix(int line, String text) {
        assert line >= 0 && line < teams.size();
        if (text.length() > 16) text = text.substring(0, 16);
        teams.get(line).setPrefix(text);
    }

    public void setLinePrefix(int line, Supplier<String> updater) {
        assert line >= 0 && line < teams.size();
        prefixUpdaters.put(line, updater);
    }

    public void setLineSuffix(int line, String text) {
        assert line >= 0 && line < teams.size();
        if (text.length() > 16) text = text.substring(0, 16);
        teams.get(line).setSuffix(text);
    }

    public void setLineSuffix(int line, Supplier<String> updater) {
        assert line >= 0 && line < teams.size();
        suffixUpdaters.put(line, updater);
    }

    public void removeUpdaters(int line) {
        prefixUpdaters.remove(line);
        suffixUpdaters.remove(line);
        removeTitleUpdater();
    }

    public void blankFullLine(int line) {
        blankLinePrefix(line);
        blankLineSuffix(line);
    }

    public void blankLinePrefix(int line) {
        setLinePrefix(line, "");
    }

    public void blankLineSuffix(int line) {
        setLineSuffix(line, "");
    }

    public void setTitle(String title) {
        this.title = title;
        this.objective.setDisplayName(title);
    }

    public void setTitle(Supplier<String> updater) {
        this.titleUpdater = updater;
    }

    public void removeTitleUpdater() {
        titleUpdater = null;
    }

    public String getTitle() {
        return title;
    }

    public Scoreboard getBoard() {
        return board;
    }

    public Objective getObjective() {
        return objective;
    }

    public int getLength() {
        return lines.size();
    }

    @Override
    public void run() {
        // update the corresponding lines for every updater
        prefixUpdaters.forEach((line, updater) -> setLinePrefix(line, updater.get()));
        suffixUpdaters.forEach((line, updater) -> setLineSuffix(line, updater.get()));
        if (titleUpdater != null) setTitle(titleUpdater.get());
    }
}
