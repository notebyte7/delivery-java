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

    @Test
    void constructor_NullName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Transport(null, validSpeed));
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void setName_NullOrBlankName_ThrowsIllegalArgumentException(String invalidName) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transport.setName(invalidName));
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    void setName_ValidName_UpdatesName() {
        String newName = "Bike";
        transport.setName(newName);
        assertEquals(newName, transport.getName());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 4, 10})
    void setSpeed_InvalidSpeed_ThrowsIllegalArgumentException(int invalidSpeed) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transport.setSpeed(invalidSpeed));
        assertEquals("Speed must be between 1 and 3", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void setSpeed_ValidSpeed_UpdatesSpeed(int validSpeed) {
        transport.setSpeed(validSpeed);
        assertEquals(validSpeed, transport.getSpeed());
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
        Location target = new Location(5, 3);
        Location newLocation = transport.move(current, target);

        assertEquals(3, newLocation.getX());
        assertEquals(1, newLocation.getY());
    }

    @Test
    void move_ExceedsSpeedRange_ClampsMovement() {
        transport.setSpeed(2);
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
