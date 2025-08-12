import java.time.LocalDateTime;
import java.util.Optional;

public final class ValidationUtils
{
    private ValidationUtils() {}

    public static void validateId(int id)
    {
        if (id <= 0)
            throw new IllegalArgumentException("ID must be a positive number");
    }

    public static void validateDescription(String description)
    {
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty");
    }

    public static void validateStatus(Status status)
    {
        if (status == null)
            throw new IllegalArgumentException("Status cannot be null");
    }

    public static void validateCreatedAt(LocalDateTime createdAt)
    {
        if (createdAt == null)
            throw new IllegalArgumentException("createdAt cannot be null");
    }

    public static void validateUpdatedAt(LocalDateTime createdAt, Optional<LocalDateTime> updatedAt)
    {
        // Only compare if updatedAt is not null
        updatedAt.ifPresent(updateTime -> {
            if (createdAt.isAfter(updateTime))
                throw new IllegalArgumentException("createdAt cannot be after updatedAt");
        });
    }
}
