import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Task(
        int id,
        String description,
        Status status,
        LocalDateTime createdAt,
        Optional<LocalDateTime> updatedAt
)
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Task
    {
        if (id <= 0)
            throw new IllegalArgumentException("ID must be a positive number");

        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty");

        if (status == null)
            throw new IllegalArgumentException("Status cannot be null");

        if (createdAt == null)
            throw new IllegalArgumentException("createdAt cannot be null");

        // Only compare if updatedAt is not null
        updatedAt.ifPresent(updateTime -> {
            if (createdAt.isAfter(updateTime))
                throw new IllegalArgumentException("createdAt cannot be after updatedAt");
        });
    }

    public static Task createNew(final int newId, final String newDescription)
    {
        return new Task(newId, newDescription, Status.TODO, LocalDateTime.now(), Optional.empty());
    }

    public Task withDescription(final String newDescription)
    {
        return new Task(this.id, newDescription, this.status, this.createdAt, Optional.of(LocalDateTime.now()));
    }

    public Task withStatus(final Status newStatus)
    {
        return new Task(this.id, this.description, newStatus, this.createdAt, Optional.of(LocalDateTime.now()));
    }

//    public static String toJsonString(Task task)
//    {
//        return String.format("\t{\n\t\t\"id\": %d,\n\t\t\"description\": \"%s\",\n\t\t\"status\": \"%s\",\n\t\t\"Created At\": \"%s\",\n\t\t\"Updated At\": \"%s\"\n\t}",
//                task.getId(), task.getDescription(), task.getStatus(), task.getCreatedAt(), task.getUpdatedAt());
//    }

//    @Override
//    public String toString()
//    {
//        String createdAtFormatted = createdAt.format(FORMATTER);
//        String updatedAtFormatted = (updatedAt != null)
//                ? updatedAt.format(FORMATTER)
//                : "N/A";
//
//        return String.format("{\n\tid: %d,\n\tdescription: \"%s\",\n\tstatus: \"%s\",\n\tCreated At: \"%s\",\n\tUpdated At: \"%s\"\n}",
//                id, description, status.getLabel(), createdAtFormatted, updatedAtFormatted);
//    }
}
