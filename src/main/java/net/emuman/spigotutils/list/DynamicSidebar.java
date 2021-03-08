package net.emuman.spigotutils.list;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DynamicSidebar extends BukkitRunnable {

    private Scoreboard board;
    private Objective objective;
    private String title;

    private String[] visibleLines;
    private Map<Integer, Supplier<String>> updaters;

    public DynamicSidebar(String id, String title, Scoreboard board) {
        this.board = board;
        this.objective = board.registerNewObjective(id, "dummy", title);
        this.title = title;
        this.visibleLines = new String[15];
        this.updaters = new HashMap<>();

        for (int i = 0; i < 15; i++) blankLine(i);

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public DynamicSidebar(String id, String title) {
        this(id, title, Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void setLine(int line, String text) {
        assert line >= 0 && line < 15;
        if (Arrays.asList(visibleLines).contains(text)) return; // cannot be more than one of the same line visible
        if (visibleLines[line].equals(text)) return; // don't have to update the line, it is the same
        if (visibleLines[line] != null) {
            // Push the current line off the screen before adding the new one
            Score score = this.objective.getScore(visibleLines[line]);
            score.setScore(-1);
        }
        updaters.remove(line); // remove any existing updater so the line doesn't get immediately overwritten
        this.objective.getScore(text).setScore(14 - line);
        visibleLines[line] = text;
    }

    public void setLine(int line, Supplier<String> updater) {
        updaters.put(line, updater);
    }

    public String getLine(int index) {
        assert index >= 0 && index < 15;
        return visibleLines[index];
    }

    public void blankLine(int line) {
        setLine(line, StringUtils.repeat(" ", line + 1));
    }

    public void setTitle(String title) {
        this.title = title;
        this.objective.setDisplayName(title);
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

    @Override
    public void run() {
        // update the corresponding lines for every updater
        updaters.forEach((line, updater) -> setLine(line, updater.get()));
    }
}
