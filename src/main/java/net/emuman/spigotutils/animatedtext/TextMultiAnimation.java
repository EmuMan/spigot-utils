package net.emuman.spigotutils.animatedtext;

import net.emuman.spigotutils.time.ControllableLoop;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public TextMultiAnimation(List<TextAnimation> animations, OrderType orderType) {
        super(animations.size() > 0 ? animations.get(0).getRawText() : null);
        this.animations = animations;
        this.orderType = orderType;
        this.maxStage = animations.size();
        this.currentDirection = orderType == OrderType.REPEAT_BACKWARDS ? -1 : 1;
        this.random = new Random();
    }

    private void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public int getPeriod() {
        return animations.get(getStage()).getPeriod();
    }

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

    @Override
    public String getNext() {
        if (animations.size() == 0) return "";
        TextAnimation currentAnimation = animations.get(getStage());
        if (currentAnimation.getStage() == currentAnimation.getMaxStage() - 1) advance();
        return currentAnimation.getNext(); // It is important that we advance this last
        // because it uses the last stage of the current animation and also resets it
    }

}
