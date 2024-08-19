package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.Epic;
import ru.yandex.javacource.sosnin.schedule.tasks.Subtask;
import ru.yandex.javacource.sosnin.schedule.tasks.Task;
import ru.yandex.javacource.sosnin.schedule.tasks.Type;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager() {
        super();
        file = new File("tasks.csv");
        loadFromFile(file);
    }

    private void save(Task task) {
        String line = task.toString();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Error while saving task");
            } catch (ManagerSaveException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            int count = 0;
            while (br.ready()) {
                String[] line = br.readLine().split(",");
                if (count == 0) {
                    count++;
                    continue;
                }
                count++;
                switch (Type.valueOf(line[1])) {
                    case TASK:
                        Task task = Task.fromString(line);
                        super.createTask(task);
                        break;
                    case EPIC:
                        Epic epic = Epic.fromString(line);
                        super.createEpic(epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = Subtask.fromString(line);
                        super.createSubtask(subtask);
                        break;
                    default:
                        break;
                }
            }
            generatorId = count;
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
    }

    @Override
    public int createTask(Task task) {
        int taskId = super.createTask(task);
        save(task);
        return taskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int epicId = super.createEpic(epic);
        save(epic);
        return epicId;
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        Integer subtaskId = super.createSubtask(subtask);
        save(subtask);
        return subtaskId;
    }
}
