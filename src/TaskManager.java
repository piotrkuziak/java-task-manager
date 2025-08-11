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
        createJsonFile();

        loadTasksFromJson();

        updateNextId();
    }

    private void createJsonFile()
    {
        if (!Files.exists(filePath))
        {
            try
            {
                Files.createFile(filePath);
                Files.writeString(filePath, "{}");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void loadTasksFromJson()
    {
        try
        {
            String jsonText = Files.readString(filePath);

            if (!jsonText.trim().equals("{}"))
            {
                // Remove square brackets
                jsonText = jsonText.replaceAll("\\[", "").replaceAll("]", "");

                // Split tasks into array
                String[] tasks = jsonText.split("}\\s*,\\s*\\{");

                // Remove braces and commas from each task string
                for (int i = 0; i < tasks.length; i++)
                    tasks[i] = tasks[i].replaceAll("\\{", "").replaceAll("}", "").replaceAll(",", "");

                for (String task : tasks)
                    this.tasks.add(Task.fromJsonString(task));
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void saveTasksToJson()
    {
        StringBuilder jsonTasks = new StringBuilder();
        jsonTasks.append("[\n");

        for (Task task : tasks)
        {
            jsonTasks.append(Task.toJsonString(task));
            if (task != tasks.getLast())
                jsonTasks.append(",\n");
        }

        jsonTasks.append("\n]");

        try (FileWriter writer = new FileWriter(String.valueOf(filePath)))
        {
            writer.write(String.valueOf(jsonTasks));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
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
        Task task = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        task.setDescription(description);
        task.setUpdatedAt(LocalDateTime.now());
        saveTasksToJson();
    }

    public void updateStatus(int id, Status status)
    {
        Task task = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        saveTasksToJson();
    }

    public void deleteTask(int id)
    {
        Task task = getTask(id).orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " doesn't exist"));
        tasks.remove(task);
        saveTasksToJson();
    }
}
