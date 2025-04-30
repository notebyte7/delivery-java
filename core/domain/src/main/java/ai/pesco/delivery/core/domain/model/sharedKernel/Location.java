package ai.pesco.delivery.core.domain.model.sharedKernel;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Random;

@Getter
@EqualsAndHashCode
public class Location {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 10;

    private int x;
    private int y;

    private Location() {
    }

    public Location(int x, int y) {
        setX(x);
        setY(y);
    }

    private void setX(int x) {
        checkBoundaryConditions(x);
        this.x = x;
    }

    private void setY(int y) {
        checkBoundaryConditions(y);
        this.y = y;
    }

    private void checkBoundaryConditions(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("Value must be greater than or equal to " + MIN_VALUE);
        }
        if (value > MAX_VALUE) {
            throw new IllegalArgumentException("Value must be less than or equal to " + MAX_VALUE);
        }
    }

    public int calculateDistance(Location location) {
        return Math.abs(x - location.getX()) + Math.abs(y - location.getY());
    }

    public static Location random() {
        Random random = new Random();
        int x = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
        int y = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
        return new Location(x, y);
    }
}
