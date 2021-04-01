package net.emuman.spigotutils.animatedtext;

import org.bukkit.ChatColor;

import java.util.List;

/**
 * A class that allows text to be wrapped in an animation, represented by a String supplier.
 *
 * A wave animation is characterized by a single color with several other colors running across it.
 */
public class TextColorWaveAnimation extends TextAnimation {

    private final List<ChatColor> colors;

    /**
     * Creates a new TextColorWaveAnimation. The period is only used for optional external reference and is not mandatory.
     *
     * @param text   the text to be animated.
     * @param colors the set of colors to move across the text.
     * @param period the period of the text animation.
     */
    public TextColorWaveAnimation(String text, List<ChatColor> colors, int period) {
        super(text, period);
        this.colors = colors;
        this.maxStage = colors.size();
    }

    /**
     * Creates a new TextColorWaveAnimation, with no given period.
     *
     * @param text the text to be animated.
     * @param colors the set of colors to move across the text.
     */
    public TextColorWaveAnimation(String text, List<ChatColor> colors) {
        this(text, colors, 5);
    }

    /**
     * @return the next string in the animated sequence.
     */
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
