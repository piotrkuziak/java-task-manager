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
        ValidationUtils.validateId(id);
        ValidationUtils.validateDescription(description);
        ValidationUtils.validateStatus(status);
        ValidationUtils.validateCreatedAt(createdAt);
        ValidationUtils.validateUpdatedAt(createdAt, updatedAt);
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
