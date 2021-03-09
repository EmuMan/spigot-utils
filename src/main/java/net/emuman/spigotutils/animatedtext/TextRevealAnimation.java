package net.emuman.spigotutils.animatedtext;

public class TextRevealAnimation extends TextAnimation {

    public TextRevealAnimation(String text, int period) {
        super(text, period);
        this.maxStage = getRawText().length() + 1;
    }

    public TextRevealAnimation(String text) {
        this(text, 5);
    }

    @Override
    public String getNext() {
        String next = getRawText().substring(0, getStage());
        advance();
        return next;
    }

}
