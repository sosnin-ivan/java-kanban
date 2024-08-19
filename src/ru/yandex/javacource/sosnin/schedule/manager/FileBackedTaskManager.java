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

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
        loadFromFile(file);
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write("id,type,name,status,description,epic\n");
            for (Task task : tasks.values()) {
                String line = task.toString();
                bw.write(line + ",\n");
            }
            for (Epic epic : epics.values()) {
                String line = epic.toString();
                bw.write(line + ",\n");
            }
            for (Subtask subtask : subtasks.values()) {
                String line = subtask.toString();
                bw.write(line + ",\n");
            }
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
                        createTask(task);
                        break;
                    case EPIC:
                        Epic epic = Epic.fromString(line);
                        createEpic(epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = Subtask.fromString(line);
                        createSubtask(subtask);
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
        save();
        return taskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int epicId = super.createEpic(epic);
        save();
        return epicId;
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        Integer subtaskId = super.createSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }
}
