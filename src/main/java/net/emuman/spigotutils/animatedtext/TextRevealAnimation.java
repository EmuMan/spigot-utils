package net.emuman.spigotutils.animatedtext;

/**
 * A class that allows text to be wrapped in an animation, represented by a String supplier.
 *
 * A reveal animation is characterized by single-colored text gradually appearing on screen.
 */
public class TextRevealAnimation extends TextAnimation {

    /**
     * Creates a new TextRevealAnimation. The period is only used for optional external reference and is not mandatory.
     *
     * @param text   the text to be animated.
     * @param period the period of the text animation.
     */
    public TextRevealAnimation(String text, int period) {
        super(text, period);
        this.maxStage = getRawText().length() + 1;
    }

    /**
     * Creates a new TextRevealAnimation, with no given period.
     *
     * @param text the text to be animated.
     */
    public TextRevealAnimation(String text) {
        this(text, 5);
    }

    /**
     * @return the next string in the animated sequence.
     */
    @Override
    public String getNext() {
        String next = getRawText().substring(0, getStage());
        advance();
        return next;
    }

}
