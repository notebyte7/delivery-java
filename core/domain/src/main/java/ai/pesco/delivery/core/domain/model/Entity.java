package ai.pesco.delivery.core.domain.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class Entity<ID> {
    protected ID id;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Aggregate<?> aggregate = (Aggregate<?>) o;
        return Objects.equals(id, aggregate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
