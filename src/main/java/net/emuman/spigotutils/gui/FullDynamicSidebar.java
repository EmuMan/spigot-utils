package net.emuman.spigotutils.gui;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.function.Supplier;

public class FullDynamicSidebar extends PartialDynamicSidebar {

    private final Map<Integer, Supplier<String>> lineUpdaters;
    private final Map<Integer, Supplier<String>> bodyUpdaters;

    public FullDynamicSidebar(String id, String title, Scoreboard board) {
        super(id, title, board);
        this.lineUpdaters = new HashMap<>();
        this.bodyUpdaters = new HashMap<>();

        for (int i = 0; i < 15; i++) {
            String line = StringUtils.repeat(" ", i + 1);
            addLine(line);
        }
    }

    public FullDynamicSidebar(String id, String title) {
        this(id, title, Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void setFullLine(int line, String text) {
        assert line >= 0 && line < 15;
        blankFullLine(line);
        // TODO: Color codes will probably break this
        int size = text.length();
        // split the string up into different parts to distribute to the prefix, body, and suffix
        setLinePrefix(line, text.substring(0, Math.min(size, 16)));
        if (size > 16) setLineBody(line, text.substring(16, Math.min(size, 32)));
        if (size > 32) setLineSuffix(line, text.substring(32, Math.min(size, 48)));
    }

    public void setFullLine(int line, Supplier<String> updater) {
        assert line >= 0 && line < 15;
        lineUpdaters.put(line, updater);
    }

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

    public void setLineBody(int line, Supplier<String> updater) {
        assert line >= 0 && line < 15;
        bodyUpdaters.put(line, updater);
    }

    public void removeUpdaters(int line) {
        super.removeUpdaters(line);
        lineUpdaters.remove(line);
        bodyUpdaters.remove(line);
    }

    public void blankFullLine(int line) {
        super.blankFullLine(line);
        blankLineBody(line);
    }

    public void blankLineBody(int line) {
        setLineBody(line, StringUtils.repeat(" ", line + 1));
    }

    @Override
    public void run() {
        // update the corresponding lines for every updater
        super.run();
        lineUpdaters.forEach((line, updater) -> setFullLine(line, updater.get()));
        bodyUpdaters.forEach((line, updater) -> setLineBody(line, updater.get()));
    }
}
