package net.emuman.spigotutils.animatedtext;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.List;

public class TextWipeAnimation extends TextAnimation {

    private final ChatColor color1;
    private final ChatColor color2;
    private final List<ChatColor> intermediaries;

    public TextWipeAnimation(String text, ChatColor color1, ChatColor color2, List<ChatColor> intermediaries, int period) {
        super(text, period);
        this.color1 = color1;
        this.color2 = color2;
        this.intermediaries = intermediaries;
        this.maxStage = text.length() + 1 + (intermediaries == null ? 0 : intermediaries.size());
    }

    public TextWipeAnimation(String text, ChatColor color1, ChatColor color2, List<ChatColor> intermediaries) {
        this(text, color1, color2, intermediaries, 3);
    }

    public TextWipeAnimation(String text, ChatColor color1, ChatColor color2, int period) {
        this(text, color1, color2, null, period);
    }

    public TextWipeAnimation(String text, ChatColor color1, ChatColor color2) {
        this(text, color1, color2, null, 3);
    }

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
