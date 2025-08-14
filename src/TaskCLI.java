import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TaskCLI
{
    private static final Map<String, Status> statusMap = Map.of(
            "todo", Status.TODO,
            "in-progress", Status.IN_PROGRESS,
            "done", Status.DONE,
            "mark-in-progress", Status.IN_PROGRESS,
            "mark-done", Status.DONE
    );

    private static TaskCommand.CommandAdd parseAddCommand(String[] args)
    {
        if (args.length != 2)
            throw new IllegalArgumentException("Adding a task requires giving two arguments: [command, description]");

        return new TaskCommand.CommandAdd(args[1]);
    }

    private static TaskCommand.CommandDelete parseDeleteCommand(String[] args)
    {
        if (args.length != 2)
            throw new IllegalArgumentException("Deleting a task requires giving two arguments: [command, id]");

        try
        {
            int id = Integer.parseInt(args[1]);
            return new TaskCommand.CommandDelete(id);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("ID must be a number, got: " + args[1]);
        }
    }

    private static TaskCommand.CommandUpdateDescription parseUpdateDescription(String[] args)
    {
        if (args.length != 3)
            throw new IllegalArgumentException("Updating task description requires giving three arguments: [command, id, description]");

        try
        {
            int id = Integer.parseInt(args[1]);
            return new TaskCommand.CommandUpdateDescription(id, args[2]);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("ID must be a number, got: " + args[1]);
        }
    }

    private static TaskCommand.CommandMarkStatus parseMarkStatus(String[] args)
    {
        if (args.length != 2)
            throw new IllegalArgumentException("Updating task status requires giving two arguments: [command, id]");

        try
        {
            Status status = TaskCLI.statusMap.get(args[0]);
            int id = Integer.parseInt(args[1]);
            return new TaskCommand.CommandMarkStatus(id, status);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("ID must be a number, got: " + args[1]);
        }
    }

    private static TaskCommand.CommandList parseListCommand(String[] args)
    {
        if (args.length < 1 || args.length > 2)
            throw new IllegalArgumentException("Listing tasks requires giving one or two arguments: [command, status]");

        Optional<Status> status = Optional.empty();

        if (args.length == 2)
        {
            Status s = TaskCLI.statusMap.get(args[1]);

            if (s == null)
                throw new IllegalArgumentException("Status must be todo, in-progress or done, got: " + args[1]);

            status = Optional.of(s);
        }

        return new TaskCommand.CommandList(status);
    }

    private static void executeCommand(TaskCommand command)
    {
        final TaskManager taskManager = new TaskManager();

        switch (command)
        {
            case TaskCommand.CommandAdd add -> {
                int newTaskId = taskManager.addTask(add.description());
                System.out.println("Task added successfully (ID: " + newTaskId + ")");
            }
            case TaskCommand.CommandDelete delete -> {
                taskManager.deleteTask(delete.id());
                System.out.println("Task deleted successfully");
            }
            case TaskCommand.CommandUpdateDescription updateDescription -> {
                taskManager.updateDescription(updateDescription.id(), updateDescription.description());
                System.out.println("Task updated successfully");
            }
            case TaskCommand.CommandMarkStatus markStatus -> {
                taskManager.updateStatus(markStatus.id(), markStatus.status());
                System.out.println("Task status updated successfully");
            }
            case TaskCommand.CommandList list -> {
                List<Task> tasks = list.status()
                        .map(taskManager::getTasksByStatus)
                        .orElseGet(taskManager::getTasks);

                TaskFormatter.displayTasksTable(tasks);
            }
        }
    }

    private static void printUsageAndExit()
    {
        final String instructions = """
                Usage: java TaskCLI <command> [arguments]
                Available commands:
                  add <description>         - Add a new task
                  delete <id>               - Delete a task
                  update <id> <description> - Update a task description
                  mark-done <id>            - Mark task as done
                  mark-in-progress <id>     - Mark task as in progress
                  list [status]             - List tasks (optionally by status)\s
               \s""";

        System.out.println(instructions);
        System.exit(0);
    }

    public static void main(String[] args)
    {
        if (args.length == 0)
            printUsageAndExit();

        TaskCommand command = switch (args[0])
        {
            case "add" -> TaskCLI.parseAddCommand(args);
            case "delete" -> TaskCLI.parseDeleteCommand(args);
            case "update" -> TaskCLI.parseUpdateDescription(args);
            case "mark-in-progress", "mark-done" -> TaskCLI.parseMarkStatus(args);
            case "list" -> TaskCLI.parseListCommand(args);
            default -> throw new IllegalArgumentException("Wrong command");
        };

        TaskCLI.executeCommand(command);
    }
}
