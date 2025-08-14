import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TaskManager
{
    private final List<Task> tasks = new ArrayList<>();
    private final Path filePath = Paths.get("tasks.json");
    private int nextId = 0;

    public TaskManager()
    {
        // Create the file if it doesn't exist
        FileOperations.createFileIfNotExists(filePath);

        loadTasksFromJson();

        updateNextId();
    }

    private final void loadTasksFromJson()
    {
        final String jsonContent = FileOperations.readFile(filePath);
        final List<Task> loadedTasks = JsonParser.parseTaskArray(jsonContent);
        this.tasks.clear();
        this.tasks.addAll(loadedTasks);
    }

    private final void saveTasksToJson()
    {
        final String jsonContent = JsonFormatter.tasksToJson(tasks);
        FileOperations.writeFile(filePath, jsonContent);
    }

    private final void updateNextId()
    {
        nextId = tasks.stream()
                    .mapToInt(Task::id)
                    .max()
                    .orElse(0) + 1;
    }

    public final int addTask(final String description)
    {
        tasks.add(Task.createNew(nextId++, description));
        saveTasksToJson();

        return nextId - 1;
    }

    public final List<Task> getTasks()
    {
        return List.copyOf(tasks);
    }

    public final Optional<Task> getTask(final int id)
    {
        return tasks.stream()
                .filter(t -> t.id() == id)
                .findFirst();
    }

    public final List<Task> getTasksByStatus(final Status status)
    {
        return tasks.stream()
                .filter(task -> task.status() == status)
                .collect(Collectors.toList());
    }

    public final void updateDescription(final int id, final String description)
    {
        Task oldTask = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        Task newTask = oldTask.withDescription(description);
        tasks.set(tasks.indexOf(oldTask), newTask);
        saveTasksToJson();
    }

    public final void updateStatus(final int id, final Status status)
    {
        Task oldTask = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        Task newTask = oldTask.withStatus(status);
        tasks.set(tasks.indexOf(oldTask), newTask);
        saveTasksToJson();
    }

    public final void deleteTask(final int id)
    {
        Task task = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        tasks.remove(task);
        saveTasksToJson();
    }
}
