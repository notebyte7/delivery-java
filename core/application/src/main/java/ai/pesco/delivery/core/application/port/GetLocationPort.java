package ai.pesco.delivery.core.application.port;

import ai.pesco.delivery.core.domain.model.sharedKernel.Location;

public interface GetLocationPort {
    Location getFromStreet(String street);
}
