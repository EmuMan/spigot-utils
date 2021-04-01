package net.emuman.spigotutils.animatedtext;

import net.emuman.spigotutils.time.ControllableLoop;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A class that allows text to be wrapped in an animation, represented by a String supplier.
 *
 * A multi animation is characterized by several other animations run in or out of sequence.
 */
public class TextMultiAnimation extends TextAnimation {

    public enum OrderType {
        REPEAT_FORWARDS,
        REPEAT_BACKWARDS,
        PING_PONG,
        SHUFFLE
    }

    private final List<TextAnimation> animations;

    private OrderType orderType;
    private int currentDirection; // For use with the ping-pong order type
    private final Random random;

    /**
     * Creates a new TextMultiAnimation.
     *
     * @param animations the list of animations to be cycled through.
     * @param orderType  the order in which to cycle through the animations.
     */
    public TextMultiAnimation(List<TextAnimation> animations, OrderType orderType) {
        super(animations.size() > 0 ? animations.get(0).getRawText() : null);
        this.animations = animations;
        this.orderType = orderType;
        this.maxStage = animations.size();
        this.currentDirection = orderType == OrderType.REPEAT_BACKWARDS ? -1 : 1;
        this.random = new Random();
    }

    /**
     * @param orderType the new order in which to cycle through the animations.
     */
    private void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    /**
     * @return the current period, represented by the set period of the current animation.
     */
    @Override
    public int getPeriod() {
        return animations.get(getStage()).getPeriod();
    }

    /**
     * Advances the animation stage, taking into account the max stage limit, and swapping animations if necessary.
     */
    @Override
    public void advance() {
        switch (orderType) {
            case REPEAT_FORWARDS:
                setStage(getStage() + currentDirection);
                if (isAtMaxStage()) setStage(0);
                break;
            case REPEAT_BACKWARDS:
                setStage(getStage() + currentDirection);
                if (getStage() == 0) setStage(maxStage - 1);
                break;
            case PING_PONG:
                if (getStage() == maxStage - 1) {
                    currentDirection = -1;
                } else if (getStage() == 0) {
                    currentDirection = 1;
                }
                setStage(getStage() + currentDirection);
                break;
            case SHUFFLE:
                setStage(random.nextInt(maxStage));
                break;
        }
    }

    /**
     * @return the next string in the animated sequence.
     */
    @Override
    public String getNext() {
        if (animations.size() == 0) return "";
        TextAnimation currentAnimation = animations.get(getStage());
        if (currentAnimation.getStage() == currentAnimation.getMaxStage() - 1) advance();
        return currentAnimation.getNext(); // It is important that we advance this last
        // because it uses the last stage of the current animation and also resets it
    }

}
