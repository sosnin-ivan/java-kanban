package ru.yandex.javacource.sosnin.schedule;

import ru.yandex.javacource.sosnin.schedule.manager.TaskManager;
import ru.yandex.javacource.sosnin.schedule.tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager m = new TaskManager();
        int createdId;

        // create

        createdId = m.createTask(new Task("First Task", "Her description"));
        System.out.println("created task with id: " + createdId);
        createdId = m.createTask(new Task("Second Task", "And her description"));
        System.out.println("created task with id: " + createdId);

        createdId = m.createEpic(new Epic("First Epic", "It is very epic epic"));
        System.out.println("created epic with id: " + createdId);

        createdId = m.createSubtask(new Subtask("Subtask for first epic", "It was may your description", 3));
        System.out.println("created subtask with id: " + createdId);
        createdId = m.createSubtask(new Subtask("Another subtask for first epic", "Sadly subtask", 3));
        System.out.println("created subtask with id: " + createdId);
        createdId = m.createSubtask(new Subtask("Yet another subtask for first epic", "Subtropic", 3));
        System.out.println("created subtask with id: " + createdId);

        // get

        System.out.print("get all tasks                            ===> ");
        System.out.println(m.getAllTasks());
        System.out.print("get all epics                            ===> ");
        System.out.println(m.getAllEpics());
        System.out.print("get all subtasks                         ===> ");
        System.out.println(m.getAllSubtasks());

        System.out.print("get task by id=1                         ===> ");
        System.out.println(m.getTask(1));
        System.out.print("get epic by id=3                         ===> ");
        System.out.println(m.getEpic(3));
        System.out.print("get subtask by id=4                      ===> ");
        System.out.println(m.getSubtask(4));
        System.out.print("get task with invalid id returns null    ===> ");
        System.out.println(m.getTask(7));

        System.out.print("get subtasks by epic id=3                ===> ");
        System.out.println(m.getSubtasksByEpic(3));

        // update

        Task changedTask = m.getTask(1);
        changedTask.setName("New name for Task");
        changedTask.setDescription("New description for Task");
        changedTask.setStatus(Status.IN_PROGRESS);
        m.updateTask(changedTask);
        System.out.print("updated task with id=1 and new status    ===> ");
        System.out.println(m.getTask(1));

        Epic changedEpic = m.getEpic(3);
        changedEpic.setName("New name for Epic");
        changedEpic.setDescription("New description for Epic");
        m.updateEpic(changedEpic);
        System.out.print("updated epic with id=3                   ===> ");
        System.out.println(m.getEpic(3));

        Subtask changedSubtask = m.getSubtask(4);
        changedSubtask.setName("New name for Subtask");
        changedSubtask.setDescription("New description for Subtask");
        changedSubtask.setStatus(Status.IN_PROGRESS);
        m.updateSubtask(changedSubtask);
        System.out.print("updated subtask with id=4 and new status ===> ");
        System.out.println(m.getSubtask(4));

        // delete

        m.deleteTask(1);
        System.out.print("deleted task with id=1                   ===> ");
        System.out.println(m.getTask(1));

        m.deleteSubtask(4);
        System.out.print("deleted subtask with id=4                ===> ");
        System.out.println(m.getSubtasksByEpic(3) + " and " + m.getEpic(3));

        m.deleteEpic(3);
        System.out.print("deleted epic with id=3                   ===> ");
        System.out.println(m.getEpic(3));

        m.deleteAllTasks();
        System.out.print("deleted all tasks                        ===> ");
        System.out.println(m.getAllTasks());

        m.deleteAllEpics();
        System.out.print("deleted all epics and those subtasks     ===> ");
        System.out.print(m.getAllEpics());
        System.out.println(m.getAllSubtasks());

        createdId = m.createEpic(new Epic("Second Epic", "For testing deletion"));
        System.out.println("created epic with id: " + createdId);
        createdId = m.createSubtask(new Subtask("Subtask for second epic", "Die die my darling", 7));
        System.out.println("created subtask with id: " + createdId);
        m.deleteAllSubtasks();
        System.out.print("delete all subtasks                      ===> ");
        System.out.print(m.getAllEpics());
        System.out.println(m.getAllSubtasks());

        // statuses

        createdId = m.createEpic(new Epic("Third Epic", "Checking status"));
        System.out.println("created epic with id: " + createdId);
        createdId = m.createSubtask(new Subtask("Subtask for third epic", "Default status is NEW", 9));
        System.out.println("created subtask with id: " + createdId);
        createdId = m.createSubtask(new Subtask("Another subtask for third epic", "And this status is NEW", 9));
        System.out.println("created subtask with id: " + createdId);
        System.out.print("epic status is NEW                       ===> ");
        System.out.println(m.getEpic(9));

        changedSubtask = m.getSubtask(10);
        changedSubtask.setName("New name for Subtask");
        changedSubtask.setDescription("New description for Subtask");
        changedSubtask.setStatus(Status.IN_PROGRESS);
        m.updateSubtask(changedSubtask);
        System.out.print("one subtask status IN_PROGRESS           ===> ");
        System.out.println(m.getEpic(9));

        changedSubtask = m.getSubtask(10);
        changedSubtask.setName("New name for Subtask");
        changedSubtask.setDescription("New description for Subtask");
        changedSubtask.setStatus(Status.DONE);
        m.updateSubtask(changedSubtask);

        changedSubtask = m.getSubtask(11);
        changedSubtask.setName("New name for Subtask");
        changedSubtask.setDescription("New description for Subtask");
        changedSubtask.setStatus(Status.DONE);
        m.updateSubtask(changedSubtask);

        System.out.print("all subtasks changed to DONE             ===> ");
        System.out.println(m.getEpic(9));

        changedSubtask = m.getSubtask(10);
        changedSubtask.setName("New name for Subtask");
        changedSubtask.setDescription("New description for Subtask");
        changedSubtask.setStatus(Status.NEW);
        m.updateSubtask(changedSubtask);

        changedSubtask = m.getSubtask(11);
        changedSubtask.setName("New name for Subtask");
        changedSubtask.setDescription("New description for Subtask");
        changedSubtask.setStatus(Status.NEW);
        m.updateSubtask(changedSubtask);

        System.out.print("all subtasks changed to NEW              ===> ");
        System.out.println(m.getEpic(9));
    }
}
