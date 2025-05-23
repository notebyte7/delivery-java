package ai.pesco.delivery.core.domain.model.courierAggregate;

import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

import static java.lang.Math.clamp;

@Getter
@EqualsAndHashCode(of = "id")
public class Transport {
    public static final int MIN_SPEED = 1;
    public static final int MAX_SPEED = 3;
    private final UUID id;
    private String name;
    private final int speed;

    public Transport(String name, int speed) {
        checkName(name);
        checkSpeed(speed);

        this.id = UUID.randomUUID();
        this.name = name;
        this.speed = speed;
    }

    private void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public void rename(String name) {
        checkName(name);
        this.name = name.trim();
    }

    private void checkSpeed(int speed) {
        if (speed < MIN_SPEED || speed > MAX_SPEED) {
            throw new IllegalArgumentException("Speed must be between 1 and 3");
        }
    }

    public Location move(Location current, Location target) {
        if (current == null) throw new IllegalArgumentException("Current location cannot be null");
        if (target == null) throw new IllegalArgumentException("Target location cannot be null");

        var difX = target.getX() - current.getX();
        var difY = target.getY() - current.getY();
        var cruisingRange = speed;

        int moveX = clamp(difX, -cruisingRange, cruisingRange);
        cruisingRange -= Math.abs(moveX);

        int moveY = clamp(difY, -cruisingRange, cruisingRange);

        return new Location(current.getX() + moveX, current.getY() + moveY);
    }
}
