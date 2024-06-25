import Tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager m = new TaskManager();

        // create

        m.createTask(new Task("First Task", "Her description"));
        m.createTask(new Task("Second Task", "And her description"));

        m.createEpic(new Epic("First Epic", "It is very epic epic"));
        m.createSubtask(new Subtask("Subtask for first epic", "It was may your description", 3));
        m.createSubtask(new Subtask("Another subtask for first epic", "Sadly subtask", 3));
        m.createSubtask(new Subtask("Yet another subtask for first epic", "Subtropic", 3));

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

        m.updateTask(1, new Task("Updated task", "Updated description", Status.IN_PROGRESS));
        System.out.print("updated task with id=1 and new status    ===> ");
        System.out.println(m.getTask(1));

        m.updateEpic(3, new Epic("Updated epic", "Updated description"));
        System.out.print("updated epic with id=3                   ===> ");
        System.out.println(m.getEpic(3));

        m.updateSubtask(4, new Subtask("Updated subtask", "Updated description", Status.DONE, 3));
        System.out.print("updated subtask with id=4 and new status ===> ");
        System.out.println(m.getSubtask(4));

        // delete

        m.deleteTask(1);
        System.out.print("deleted task with id=1                   ===> ");
        System.out.println(m.getTask(1));

        m.deleteSubtask(4);
        System.out.print("deleted subtask with id=4                ===> ");
        System.out.println(m.getSubtasksByEpic(3));

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

        m.createEpic(new Epic("Second Epic", "For testing deletion"));
        m.createSubtask(new Subtask("Subtask for second epic", "Die die my darling", 7));
        m.deleteAllSubtasks();
        System.out.print("delete all subtasks                      ===> ");
        System.out.print(m.getAllEpics());
        System.out.println(m.getAllSubtasks());

        // statuses

        m.createEpic(new Epic("Third Epic", "Checking status"));
        m.createSubtask(new Subtask("Subtask for third epic", "Default status is NEW", 9));
        m.createSubtask(new Subtask("Another subtask for third epic", "And this status is NEW", 9));
        System.out.print("epic status is NEW                       ===> ");
        System.out.println(m.getEpic(9));

        m.updateSubtask(10, new Subtask("Updated subtask", "Changed status", Status.IN_PROGRESS, 9));
        System.out.print("one subtask status IN_PROGRESS           ===> ");
        System.out.println(m.getEpic(9));

        m.updateSubtask(10, new Subtask("Updated subtask", "Changed status", Status.DONE, 9));
        m.updateSubtask(11, new Subtask("Updated subtask", "Changed status", Status.DONE, 9));
        System.out.print("all subtasks changed to DONE             ===> ");
        System.out.println(m.getEpic(9));

        m.updateSubtask(10, new Subtask("Updated subtask", "Changed status", Status.NEW, 9));
        m.updateSubtask(11, new Subtask("Updated subtask", "Changed status", Status.NEW, 9));
        System.out.print("all subtasks changed to NEW              ===> ");
        System.out.println(m.getEpic(9));
    }
}
