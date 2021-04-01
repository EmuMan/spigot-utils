package net.emuman.spigotutils.animatedtext;

/**
 * An abstract class that allows text to be wrapped in an animation, represented by a String supplier.
 */
public abstract class TextAnimation {

    protected int maxStage; // generally exclusive
    private int stage;
    private final String rawText;
    private int period;

    /**
     * Creates a new TextAnimation. The period is only used for optional external reference and is not mandatory.
     *
     * @param text   the text to be animated.
     * @param period the period of the text animation.
     */
    public TextAnimation(String text, int period) {
        this.rawText = text;
        this.stage = 0;
        this.period = period;
    }

    /**
     * Creates a new TextAnimation, with no given period.
     *
     * @param text the text to be animated.
     */
    public TextAnimation(String text) {
        this(text, 0);
    }

    /**
     * @return the raw text input, without any animations.
     */
    public String getRawText() {
        return rawText;
    }

    /**
     * @param stage the stage for the animation to be set to.
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    /**
     * @return the current stage of the animation.
     */
    public int getStage() {
        return stage;
    }

    /**
     * Advances the animation stage, taking into account the max stage limit.
     */
    public void advance() {
        stage++;
        if (isAtMaxStage()) {
            stage = 0;
        }
    }

    /**
     * @return true if the animation stage is at max, false otherwise.
     */
    public boolean isAtMaxStage() {
        return stage >= maxStage;
    }

    /**
     * @return the maximum animation stage, i.e., the last animation frame.
     */
    public int getMaxStage() {
        return maxStage;
    }

    /**
     * Sets the period of this animation, only used for optional external reference.
     *
     * @param period the new period of the animation.
     */
    public void setPeriod(int period) {
        this.period = period;
    }

    /**
     * Gets the period of this animation, only used for optional external reference.
     *
     * @return the period of the animation.
     */
    public int getPeriod() {
        return period;
    }

    /**
     * @return the next string in the animated sequence.
     */
    public abstract String getNext();

}
