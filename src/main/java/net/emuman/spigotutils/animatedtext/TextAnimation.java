package net.emuman.spigotutils.animatedtext;

public abstract class TextAnimation {

    protected int maxStage; // generally exclusive
    private int stage;
    private final String rawText;
    private int period;

    public TextAnimation(String text, int period) {
        this.rawText = text;
        this.stage = 0;
        this.period = period;
    }

    public TextAnimation(String text) {
        this(text, 0);
    }

    public String getRawText() {
        return rawText;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public void advance() {
        stage++;
        if (isAtMaxStage()) {
            stage = 0;
        }
    }

    public boolean isAtMaxStage() {
        return stage >= maxStage;
    }

    public int getMaxStage() {
        return maxStage;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }

    public abstract String getNext();

}
