package ai.pesco.delivery.core.domain.model.courierAggregate;

import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransportTest {
    private Transport transport;
    private final String validName = "Car";
    private final int validSpeed = 2;

    @BeforeEach
    void setUp() {
        transport = new Transport(validName, validSpeed);
    }

    @Test
    void constructor_ValidParameters_SetsFieldsCorrectly() {
        assertNotNull(transport.getId());
        assertEquals(validName, transport.getName());
        assertEquals(validSpeed, transport.getSpeed());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void constructor_InvalidName_ThrowsIllegalArgumentException(String invalidName) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Transport(invalidName, validSpeed));
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 4, 10})
    void constructor_InvalidSpeed_ThrowsIllegalArgumentException(int invalidSpeed) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Transport(validName, invalidSpeed));
        assertEquals("Speed must be between 1 and 3", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void rename_InvalidName_ThrowsIllegalArgumentException(String invalidName) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transport.rename(invalidName));
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    void rename_ValidName_UpdatesName() {
        String newName = "Bike";
        transport.rename(newName);
        assertEquals(newName.trim(), transport.getName());
    }

    @Test
    void move_NullCurrentLocation_ThrowsIllegalArgumentException() {
        Location target = new Location(10, 10);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transport.move(null, target));
        assertEquals("Current location cannot be null", exception.getMessage());
    }

    @Test
    void move_NullTargetLocation_ThrowsIllegalArgumentException() {
        Location current = new Location(1, 1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transport.move(current, null));
        assertEquals("Target location cannot be null", exception.getMessage());
    }

    @Test
    void move_WithinSpeedRange_ReturnsNewLocation() {
        Location current = new Location(1, 1);
        Location target = new Location(2, 3);
        Location newLocation = transport.move(current, target);

        assertEquals(2, newLocation.getX());
        assertEquals(2, newLocation.getY());
    }

    @Test
    void move_ExceedsSpeedRange_ClampsMovement() {
        Location current = new Location(1, 1);
        Location target = new Location(5, 5);
        Location newLocation = transport.move(current, target);

        assertEquals(3, newLocation.getX()); // Clamped to speed (2)
        assertEquals(1, newLocation.getY()); // No range left after moving X
    }

    @Test
    void equals_SameId_ReturnsTrue() {
        Transport transport1 = new Transport("Car", 2) {
            @Override
            public UUID getId() {
                return UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            }
        };
        Transport transport2 = new Transport("Bike", 3) {
            @Override
            public UUID getId() {
                return UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            }
        };
        assertEquals(transport1, transport2);
    }

    @Test
    void equals_DifferentId_ReturnsFalse() {
        Transport transport1 = new Transport("Car", 2);
        Transport transport2 = new Transport("Car", 2);
        assertNotEquals(transport1, transport2); // Different UUIDs
    }
}
