package net.emuman.spigotutils.animatedtext;

import org.bukkit.ChatColor;

import java.util.List;

public class TextColorWaveAnimation extends TextAnimation {

    private final List<ChatColor> colors;

    public TextColorWaveAnimation(String text, List<ChatColor> colors, int period) {
        super(text, period);
        this.colors = colors;
        this.maxStage = colors.size();
    }

    public TextColorWaveAnimation(String text, List<ChatColor> colors) {
        this(text, colors, 5);
    }

    @Override
    public String getNext() {
        StringBuilder next = new StringBuilder();
        int currentColorIndex = getStage();
        for (int i = 0; i < getRawText().length(); i++) {
            if (colors.get(currentColorIndex) == null) next.append(' ');
            else next.append(colors.get(currentColorIndex).toString()).append(getRawText(), i, i + 1);
            currentColorIndex++;
            if (currentColorIndex >= colors.size()) currentColorIndex = 0;
        }
        advance();
        return next.toString();
    }

}
