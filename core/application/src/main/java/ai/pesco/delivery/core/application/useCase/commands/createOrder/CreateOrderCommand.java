package ai.pesco.delivery.core.application.useCase.commands.createOrder;

import java.util.UUID;

public record CreateOrderCommand(UUID basketId, String street) {
}
