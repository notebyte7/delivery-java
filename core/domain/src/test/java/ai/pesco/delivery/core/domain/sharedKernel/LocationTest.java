package ai.pesco.delivery.core.domain.sharedKernel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {

    @Test
    void shouldExceptionWhenCreateLocationWithZeroValues() {
        assertThrows(IllegalArgumentException.class, () -> new Location(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Location(0, 1));
        assertThrows(IllegalArgumentException.class, () -> new Location(1, 0));
    }

    @Test
    void shouldCreateLocationWithValidCoordinates() {
        Location location = new Location(1, 2);
        assertEquals(1, location.getX());
        assertEquals(2, location.getY());
    }

    @Test
    void shouldCalculateDistance() {
        Location a = new Location(2, 3);
        Location b = new Location(8, 4);
        assertEquals(7, a.calculateDistance(b));
    }

    @Test
    void shouldEquals() {
        Location loc1 = new Location(1, 2);
        Location loc2 = new Location(1, 1);
        Location loc3 = new Location(1, 2);

        assertEquals(loc1, loc3);
    }

    @Test
    void shouldNotEquals() {
        Location loc1 = new Location(1, 2);
        Location loc2 = new Location(1, 1);
        Location loc3 = new Location(1, 2);

        assertNotEquals(loc1, loc2);
    }

    @Test
    void shouldRandomCorrectlyBoundaryConditions() {
        for (int i = 0; i < 100; i++) {
            Location randomLocation = Location.random();
            assertTrue(randomLocation.getX() >= 1 && randomLocation.getX() <= 10);
            assertTrue(randomLocation.getY() >= 1 && randomLocation.getY() <= 10);
        }
    }
}
