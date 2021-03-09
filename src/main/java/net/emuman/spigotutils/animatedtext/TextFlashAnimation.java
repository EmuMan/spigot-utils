package net.emuman.spigotutils.animatedtext;

import org.bukkit.ChatColor;

import java.util.List;

public class TextFlashAnimation extends TextAnimation {

    public final List<ChatColor> colors;

    public TextFlashAnimation(String text, List<ChatColor> colors, int period) {
        super(text, period);
        this.colors = colors;
        this.maxStage = colors.size();
    }

    public TextFlashAnimation(String text, List<ChatColor> colors) {
        this(text, colors, 10);
    }

    @Override
    public String getNext() {
        if (colors.get(getStage()) == null) return "";
        String next = colors.get(getStage()) + getRawText();
        advance();
        return next;
    }

}
