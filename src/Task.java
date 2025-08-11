import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task
{
    private int id;
    private String description;
    private Status status = Status.TODO;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Task() {}

    public Task(int id, String description)
    {
        setId(id);
        setDescription(description);
    }

    public int getId()
    {
        return id;
    }

    private void setId(int id)
    {
        if (id <= 0)
            throw new IllegalArgumentException("ID must be a positive number");

        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty");

        this.description = description;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        if (status == null)
            throw new IllegalArgumentException("Status cannot be null");

        this.status = status;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        if (createdAt == null)
            throw new IllegalArgumentException("createdAt cannot be null");

        // Only compare if updatedAt is not null
        if (updatedAt != null && createdAt.isAfter(updatedAt))
            throw new IllegalArgumentException("createdAt cannot be after updatedAt");

        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        if (updatedAt == null)
            throw new IllegalArgumentException("updatedAt cannot be null");

        // Only compare if createdAt is not null
        if (createdAt != null && updatedAt.isBefore(createdAt))
            throw new IllegalArgumentException("updatedAt cannot be before createdAt");

        this.updatedAt = updatedAt;
    }

    public static Task fromJsonString(String jsonTaskString)
    {
        int id = 0;
        String description = null;
        Status status = Status.TODO; // default if missing
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        Pattern[] patterns = {
                Pattern.compile("\"id\"\\s*:\\s*(\\d+)"), // id
                Pattern.compile("\"description\"\\s*:\\s*\"([^\"]*)\""), // description
                Pattern.compile("\"status\"\\s*:\\s*\"([^\"]*)\""), // status
                Pattern.compile("\"Created At\"\\s*:\\s*\"([^\"]*)\""), // createdAt
                Pattern.compile("\"Updated At\"\\s*:\\s*\"([^\"]*)\"") // updatedAt
        };

        Matcher[] matchers = new Matcher[patterns.length];

        for (int i = 0; i < patterns.length; i++)
            matchers[i] = patterns[i].matcher(jsonTaskString);

        // Parse id
        if (matchers[0].find())
            id = Integer.parseInt(matchers[0].group(1));
        else
            throw new IllegalArgumentException("id field missing or invalid");

        // Parse description
        if (matchers[1].find())
            description = matchers[1].group(1);
        else
            throw new IllegalArgumentException("description field missing or invalid");

        // Parse status
        if (matchers[2].find())
        {
            String statusStr = matchers[2].group(1);
            try
            {
                status = Status.valueOf(statusStr);
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
        }

        // Parse createdAt
        if (matchers[3].find())
            createdAt = LocalDateTime.parse(matchers[3].group(1));
        else
            throw new IllegalArgumentException("createdAt field missing or invalid");

        // Parse updatedAt
        if (matchers[4].find())
            updatedAt = LocalDateTime.parse(matchers[4].group(1));
        else
            throw new IllegalArgumentException("updatedAt field missing or invalid");

        Task task = new Task(id, description);
        task.setStatus(status);
        task.setCreatedAt(createdAt);
        task.setUpdatedAt(updatedAt);

        return task;
    }

    public static String toJsonString(Task task)
    {
        return String.format("\t{\n\t\t\"id\": %d,\n\t\t\"description\": \"%s\",\n\t\t\"status\": \"%s\",\n\t\t\"Created At\": \"%s\",\n\t\t\"Updated At\": \"%s\"\n\t}",
                task.getId(), task.getDescription(), task.getStatus(), task.getCreatedAt(), task.getUpdatedAt());
    }

    @Override
    public String toString()
    {
        String createdAtFormatted = createdAt.format(FORMATTER);
        String updatedAtFormatted = (updatedAt != null)
                ? updatedAt.format(FORMATTER)
                : "N/A";

        return String.format("{\n\tid: %d,\n\tdescription: \"%s\",\n\tstatus: \"%s\",\n\tCreated At: \"%s\",\n\tUpdated At: \"%s\"\n}",
                id, description, status.getLabel(), createdAtFormatted, updatedAtFormatted);
    }
}
