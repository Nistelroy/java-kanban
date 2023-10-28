package main.java.ru.yandex.practicum;

import main.java.ru.yandex.practicum.manager.TaskStatus;
import main.java.ru.yandex.practicum.manager.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

public class Main {
    static TaskManager taskManager;

    public static void main(String[] args) {
        Task task;
        Epic epic;
        Subtask subtask;
        taskManager = new TaskManager();

        //создаем две таски
        task = new Task("купить", "купить-купить");
        taskManager.setNewTask(task);
        task = new Task("найти", "найти-найти");
        taskManager.setNewTask(task);

        //создаём эпик и 2 субтаски
        epic = new Epic("Поездка", "Поездка-Поездка");
        taskManager.setNewEpic(epic);
        subtask = new Subtask("машина", "машина-машина", epic.getId());
        taskManager.setNewSubtask(subtask);
        subtask = new Subtask("бензин", "бензин-бензин", epic.getId());
        taskManager.setNewSubtask(subtask);

        //создаем эпик и субтаску
        epic = new Epic("Прогулка", "Прогулка-Прогулка");
        taskManager.setNewEpic(epic);
        subtask = new Subtask("еда", "еда-еда", epic.getId());
        taskManager.setNewSubtask(subtask);

        printInfo();

        // создаем новую таску со другим статусом и существующим ID
        task = new Task("Другая", "Другая-Другая");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setId(1);
        taskManager.updateTaskInMap(task);

        // создаем новую субтаску со другим статусом и существующим ID
        subtask = new Subtask("Другая", "Другая-Другая", 3);
        subtask.setStatus(TaskStatus.DONE);
        subtask.setId(5);
        taskManager.updateSubtaskInMap(subtask);

        // создаем новую субтаску со другим статусом и существующим ID
        subtask = new Subtask("Другая", "Другая-Другая", 3);
        subtask.setStatus(TaskStatus.NEW);
        subtask.setId(4);
        taskManager.updateSubtaskInMap(subtask);

        printInfo();

        // создаем новый эпик и прописываем ему существующий айди и айдишники субтасок
        epic = new Epic("Новая", "Новая-Новая");
        epic.setIdSubtask(4);
        epic.setIdSubtask(5);
        epic.setId(3);
        taskManager.updateEpicInMap(epic); // я мог бы переписать айдишники субтасок в новый эпик внутри метода из старого, но не знаю как правильно

        // меняем статусы у существующих субтасок с помощю новых с прописанными статусами и айдишниками
        subtask = new Subtask("Свежая", "Свежая-Свежая", 6);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        subtask.setId(7);
        taskManager.updateSubtaskInMap(subtask);

        subtask = new Subtask("Уходящая", "Уходящая-Уходящая", 3);
        subtask.setStatus(TaskStatus.DONE);
        subtask.setId(5);
        taskManager.updateSubtaskInMap(subtask);

        printInfo();

        // удаляем эпик и таску
        taskManager.deleteTaskById(2);
        taskManager.deleteSubtaskById(4);

        printInfo(); //проверяем, что статус эпика изменился

        taskManager.deleteAllTask();
        taskManager.deleteAllSubtask();

        printInfo(); //проверяем, что статус эпика изменился
    }

    public static void printInfo() {
        System.out.println(taskManager.getAllTask() + "\n" + taskManager.getAllEpic()
                + "\n" + taskManager.getAllSubtask() + "\n");
    }
}
