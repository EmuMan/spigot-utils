package net.emuman.spigotutils.animatedtext;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * A class that allows text to be wrapped in an animation, represented by a String supplier.
 *
 * A wipe animation is characterized by a linear transition between two colors, with any number of specified intermediaries.
 */
public class TextWipeAnimation extends TextAnimation {

    private final ChatColor color1;
    private final ChatColor color2;
    private final List<ChatColor> intermediaries;

    /**
     * Creates a new TextWipeAnimation. The period is only used for optional external reference and is not mandatory.
     *
     * @param text           the text to be animated.
     * @param color1         the primary color of the text (before the wipe).
     * @param color2         the secondary color of the text (after the wipe).
     * @param intermediaries the set of colors to act as intermediaries between the two colors.
     * @param period         the period of the text animation.
     */
    public TextWipeAnimation(String text, ChatColor color1, ChatColor color2, List<ChatColor> intermediaries, int period) {
        super(text, period);
        this.color1 = color1;
        this.color2 = color2;
        this.intermediaries = intermediaries;
        this.maxStage = text.length() + 1 + (intermediaries == null ? 0 : intermediaries.size());
    }

    /**
     * Creates a new TextWipeAnimation, with no given period.
     *
     * @param text           the text to be animated.
     * @param color1         the primary color of the text (before the wipe).
     * @param color2         the secondary color of the text (after the wipe).
     * @param intermediaries the set of colors to act as intermediaries between the two colors.
     */
    public TextWipeAnimation(String text, ChatColor color1, ChatColor color2, List<ChatColor> intermediaries) {
        this(text, color1, color2, intermediaries, 3);
    }

    /**
     * Creates a new TextWipeAnimation with no intermediaries. The period is only used for optional external reference and is not mandatory.
     *
     * @param text           the text to be animated.
     * @param color1         the primary color of the text (before the wipe).
     * @param color2         the secondary color of the text (after the wipe).
     * @param period         the period of the text animation.
     */
    public TextWipeAnimation(String text, ChatColor color1, ChatColor color2, int period) {
        this(text, color1, color2, null, period);
    }

    /**
     * Creates a new TextWipeAnimation, with no given intermediaries or period.
     *
     * @param text           the text to be animated.
     * @param color1         the primary color of the text (before the wipe).
     * @param color2         the secondary color of the text (after the wipe).
     */
    public TextWipeAnimation(String text, ChatColor color1, ChatColor color2) {
        this(text, color1, color2, null, 3);
    }

    /**
     * @return the next string in the animated sequence.
     */
    @Override
    public String getNext() {
        int numberOfIntermediaries = intermediaries == null ? 0 : intermediaries.size();
        StringBuilder next = new StringBuilder();
        next.append(color1).append(getRawText(), 0, Math.max(getStage() - numberOfIntermediaries, 0));
        if (intermediaries != null) {
            // I don't really know exactly how all this math works out but it does so I won't touch it
            for (int i = numberOfIntermediaries - 1; i >= 0; i--) {
                // Clamp the index of the substring to 0 and the length of the string
                next.append(intermediaries.get(numberOfIntermediaries - i - 1)).append(getRawText(),
                        Math.min(Math.max(getStage() - i - 1, 0), getRawText().length()),
                        Math.min(Math.max(getStage() - i, 0), getRawText().length()));
            }
        }
        next.append(color2).append(getRawText(), Math.min(getStage(), getRawText().length()), getRawText().length());
        advance();
        return next.toString();
    }
}
