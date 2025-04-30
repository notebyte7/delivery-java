package ai.pesco.delivery.core.domain.sharedKernel;

import java.util.Random;

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

    public int getX() {
        return x;
    }

    private void setX(int x) {
        checkBoundaryConditions(x);
        this.x = x;
    }

    public int getY() {
        return y;
    }

    private void setY(int y) {
        checkBoundaryConditions(y);
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
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
