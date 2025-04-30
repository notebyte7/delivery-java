package ai.pesco.delivery.core.domain.model.courierAggregate;

import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

import static java.lang.Math.clamp;

@Getter
@EqualsAndHashCode(of = "id")
public class Transport {
    private final UUID id;
    private String name;
    private int speed;

    public Transport(String name, int speed) {
        checkName(name);
        checkSpeed(speed);

        this.id = UUID.randomUUID();
        setName(name);
        setSpeed(speed);
    }

    public void setName(String name) {
        checkName(name);
        this.name = name;
    }

    public void setSpeed(int speed) {
        checkSpeed(speed);
        this.speed = speed;
    }

    private void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    private void checkSpeed(int speed) {
        if (speed < 1 || speed > 3) {
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
