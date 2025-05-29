package ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity;

import ai.pesco.delivery.core.domain.model.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class JpaEntity<DOMAIN_ENTITY extends Entity<?>> {
    protected abstract Class<DOMAIN_ENTITY> getDomainAggregateClass();

    /**
     * Converts JPA entity to domain aggregate.
     *
     * @throws IllegalArgumentException if mapping fails
     */
    public DOMAIN_ENTITY toDomain() {
        Map<String, Field> jpaEntityFields = getFieldsOf(this.getClass()).stream()
                .collect(Collectors.toMap(Field::getName, f -> f));

        // 1. создаём доменный объект без id
        DOMAIN_ENTITY domainAggregate = createDomainAggregate(jpaEntityFields);

        // 2. получаем id из текущей JPA-сущности
        try {
            Field jpaIdField = getDeclaredFieldInHierarchy(this.getClass(), "id");
            jpaIdField.setAccessible(true);
            Object idValue = jpaIdField.get(this);

            Field domainIdField = getDeclaredFieldInHierarchy(domainAggregate.getClass(), "id");
            domainIdField.setAccessible(true);
            domainIdField.set(domainAggregate, idValue);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to copy 'id' field for " + getDomainAggregateClass().getSimpleName(), e);
        }

        mapJpaEntityFieldsToDomainAggregate(domainAggregate, jpaEntityFields);
        return domainAggregate;
    }

    private DOMAIN_ENTITY createDomainAggregate(Map<String, Field> jpaEntityFields) {
        try {
            // Get all declared constructors and find the one with the most parameters
            var constructors = getDomainAggregateClass().getDeclaredConstructors();
            var primaryConstructor = Arrays.stream(constructors)
                    .max(Comparator.comparingInt(Constructor::getParameterCount))
                    .orElseThrow(() -> new IllegalArgumentException("No constructor found for domain " + getDomainAggregateClass().getSimpleName()));

            var parameters = primaryConstructor.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Field field = jpaEntityFields.get(parameters[i].getName());
                if (field == null) {
                    throw new IllegalArgumentException(missingFieldForEntityErrorMessage(parameters[i].getName()));
                }
                if (JpaEntity.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(this);
                    Method method = fieldValue.getClass().getMethod("toDomain");
                    args[i] = method.invoke(fieldValue);
                } else {
                    args[i] = getFieldValue(field);
                }
            }

            primaryConstructor.setAccessible(true);
            @SuppressWarnings("unchecked")
            DOMAIN_ENTITY instance = (DOMAIN_ENTITY) primaryConstructor.newInstance(args);
            return instance;

        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to create domain aggregate: " + ex.getMessage(), ex);
        }
    }

    private void mapJpaEntityFieldsToDomainAggregate(DOMAIN_ENTITY domainAggregate, Map<String, Field> jpaEntityFields) {
        for (Field domainField : getFieldsOf(domainAggregate.getClass())) {
            Field jpaField = jpaEntityFields.get(domainField.getName());
            if (jpaField == null) continue;

            if (JpaEntity.class.isAssignableFrom(jpaField.getType())) {
                continue;
            }

            try {
                domainField.setAccessible(true);
                domainField.set(domainAggregate, getFieldValue(jpaField));
            } catch (IllegalArgumentException ex) {
                if (ex.getMessage() != null && ex.getMessage().contains("Can not set")) {
                    throw new IllegalArgumentException(String.format(
                            "Mismatched type of the field %s for domain %s. Must be %s. Actual is %s",
                            jpaField.getName(),
                            getDomainAggregateClass().getSimpleName(),
                            domainField.getType(),
                            jpaField.getType()
                    ));
                }
                throw ex;
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Access error during field mapping: " + ex.getMessage(), ex);
            }
        }
    }

    private Object getFieldValue(Field field) {
        try {
            field.setAccessible(true);
            return field.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access field: " + field.getName(), e);
        }
    }

    private List<Field> getFieldsOf(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()) && !FIELD_EXCLUSIONS.contains(f.getName()))
                .collect(Collectors.toList());
    }

    private String missingFieldForEntityErrorMessage(String fieldName) {
        return String.format("Field %s of domain %s is missing for entity %s",
                fieldName,
                getDomainAggregateClass().getSimpleName(),
                this.getClass().getSimpleName());
    }

    private Field getDeclaredFieldInHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy");
    }

    // Static exclusions
    private static final Set<String> FIELD_EXCLUSIONS = Set.of("domainAggregateClass");
}
