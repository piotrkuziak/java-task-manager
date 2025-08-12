import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskManager
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

    private void loadTasksFromJson()
    {
        final String jsonContent = FileOperations.readFile(filePath);
        final List<Task> loadedTasks = JsonParser.parseTaskArray(jsonContent);
        this.tasks.addAll(loadedTasks);
    }

    private void saveTasksToJson()
    {
        final String jsonContent = JsonFormatter.tasksToJson(tasks);
        FileOperations.writeFile(filePath, jsonContent);
    }

    private void updateNextId()
    {
        nextId = tasks.stream()
                    .mapToInt(Task::getId)
                    .max()
                    .orElse(0) + 1;
    }

    public void addTask(String description)
    {
        tasks.add(new Task(nextId++, description));
        saveTasksToJson();
    }

    public List<Task> getTasks()
    {
        return tasks;
    }

    public Optional<Task> getTask(int id)
    {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public List<Task> getTasksByStatus(Status status)
    {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void updateDescription(int id, String description)
    {
        Task oldTask = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        Task newTask = oldTask.withDescription(description);
        tasks.set(tasks.indexOf(oldTask), newTask);
        saveTasksToJson();
    }

    public void updateStatus(int id, Status status)
    {
        Task oldTask = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        Task newTask = oldTask.withStatus(status);
        tasks.set(tasks.indexOf(oldTask), newTask);
        saveTasksToJson();
    }

    public void deleteTask(int id)
    {
        Task task = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        tasks.remove(task);
        saveTasksToJson();
    }
}
