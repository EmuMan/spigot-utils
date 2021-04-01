package net.emuman.spigotutils.animatedtext;

import org.bukkit.ChatColor;

import java.util.List;

/**
 * A class that allows text to be wrapped in an animation, represented by a String supplier.
 *
 * A flash animation is characterized by constantly cycling solid colors.
 */
public class TextFlashAnimation extends TextAnimation {

    public final List<ChatColor> colors;

    /**
     * Creates a new TextFlashAnimation. The period is only used for optional external reference and is not mandatory.
     *
     * @param text   the text to be animated.
     * @param colors the set of colors for the text to cycle through.
     * @param period the period of the text animation.
     */
    public TextFlashAnimation(String text, List<ChatColor> colors, int period) {
        super(text, period);
        this.colors = colors;
        this.maxStage = colors.size();
    }

    /**
     * Creates a new TextFlashAnimation, with no given period.
     *
     * @param text the text to be animated.
     * @param colors the set of colors for the text to cycle through.
     */
    public TextFlashAnimation(String text, List<ChatColor> colors) {
        this(text, colors, 10);
    }

    /**
     * @return the next string in the animated sequence.
     */
    @Override
    public String getNext() {
        if (colors.get(getStage()) == null) return "";
        String next = colors.get(getStage()) + getRawText();
        advance();
        return next;
    }

}
