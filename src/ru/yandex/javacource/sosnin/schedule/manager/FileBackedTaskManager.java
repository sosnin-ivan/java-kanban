package ru.yandex.javacource.sosnin.schedule.manager;

import ru.yandex.javacource.sosnin.schedule.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String HEADER = "id,type,name,status,description,epic,start_time,duration";
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
        loadFromFile(file);
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write(HEADER);
            bw.newLine();
            for (Task task : tasks.values()) {
                String line = task.toString(task);
                bw.write(line);
                bw.newLine();
            }
            for (Epic epic : epics.values()) {
                String line = epic.toString(epic);
                bw.write(line);
                bw.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                String line = subtask.toString(subtask);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage(), e);
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

                Task task = fromString(line);
                final int id = task.getId();
                switch (task.getType()) {
                    case TASK:
                        tasks.put(id, task);
                        break;
                    case EPIC:
                        epics.put(id, (Epic) task);
                        break;
                    case SUBTASK:
                        subtasks.put(id, (Subtask) task);
                        break;
                    default:
                        break;
                }
            }
            for (Task task : tasks.values()) {
                addTaskToPrioritized(task);
            }
            for (Subtask subtask : subtasks.values()) {
                final Epic epic = epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
                addTaskToPrioritized(subtask);
            }
            for (Epic epic : epics.values()) {
                updateEpicDates(epic);
                addTaskToPrioritized(epic);
            }
            generatorId = count;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
    }

    private static Task fromString(String[] values) {
        final int id = Integer.parseInt(values[0]);
        final TaskType taskType = TaskType.valueOf(values[1]);
        final String name = values[2];
        final TaskStatus status = TaskStatus.valueOf(values[3]);
        final String description = values[4];
        final LocalDateTime startTime = Objects.equals(values[6], "null") ?
                null : LocalDateTime.parse(values[6]);
        final Duration duration = Objects.equals(values[7], "null") ?
                null : Duration.ofMinutes(Long.parseLong(values[7]));

        if (taskType == TaskType.TASK) {
            return new Task(id, name, description, status, startTime, duration);
        } else if (taskType == TaskType.SUBTASK) {
            final int epicId = Integer.parseInt(values[5]);
            return new Subtask(id, name, description, status, startTime, duration, epicId);
        } else {
            return new Epic(id, name, description, status);
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
