package main.java.ru.yandex.practicum;

import main.java.ru.yandex.practicum.manager.ConstantsStatus;
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
        taskManager.setNewTaskOrSwapInMap(task);
        task = new Task("найти", "найти-найти");
        taskManager.setNewTaskOrSwapInMap(task);

        //создаём эпик и 2 субтаски
        epic = new Epic("Поездка", "Поездка-Поездка");
        taskManager.setNewEpicOrSwapInMap(epic);
        subtask = new Subtask("машина", "машина-машина", epic.getId());
        taskManager.setNewSubtaskOrSwapInMap(subtask);
        subtask = new Subtask("бензин", "бензин-бензин", epic.getId());
        taskManager.setNewSubtaskOrSwapInMap(subtask);

        //создаем эпик и субтаску
        epic = new Epic("Прогулка", "Прогулка-Прогулка");
        taskManager.setNewEpicOrSwapInMap(epic);
        subtask = new Subtask("еда", "еда-еда", epic.getId());
        taskManager.setNewSubtaskOrSwapInMap(subtask);

        printInfo();

        // создаем новую таску со другим статусом и существующим ID
        task = new Task("Другая", "Другая-Другая");
        task.setStatus(String.valueOf(ConstantsStatus.IN_PROGRESS));
        task.setId(1);
        taskManager.setNewTaskOrSwapInMap(task);

        // создаем новую субтаску со другим статусом и существующим ID
        subtask = new Subtask("Другая", "Другая-Другая", 3);
        subtask.setStatus(String.valueOf(ConstantsStatus.IN_PROGRESS));
        subtask.setId(5);
        taskManager.setNewSubtaskOrSwapInMap(subtask);

        // создаем новую субтаску со другим статусом и существующим ID
        subtask = new Subtask("Другая", "Другая-Другая", 3);
        subtask.setStatus(String.valueOf(ConstantsStatus.DONE));
        subtask.setId(4);
        taskManager.setNewSubtaskOrSwapInMap(subtask);

        printInfo();

        // создаем новый эпик и прописываем ему существующий айди и айдишники субтасок
        epic = new Epic("Новая", "Новая-Новая");
        epic.setIdSubtask(4);
        epic.setIdSubtask(5);
        epic.setId(3);
        taskManager.setNewEpicOrSwapInMap(epic); // я мог бы переписать айдишники субтасок в новый эпик внутри метода из старого, но не знаю как правильно

        // меняем статусы у существующих субтасок с помощю новых с прописанными статусами и айдишниками
        subtask = new Subtask("Свежая", "Свежая-Свежая", 6);
        subtask.setStatus(String.valueOf(ConstantsStatus.IN_PROGRESS));
        subtask.setId(7);
        taskManager.setNewSubtaskOrSwapInMap(subtask);

        subtask = new Subtask("Уходящая", "Уходящая-Уходящая", 3);
        subtask.setStatus(String.valueOf(ConstantsStatus.DONE));
        subtask.setId(5);
        taskManager.setNewSubtaskOrSwapInMap(subtask);

        printInfo();

        // удаляем эпик и таску
        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(3);

        printInfo(); //проверяем, что субтаски тоже удалились

        taskManager.deleteAllTask();
        taskManager.deleteAllSubtask();

        printInfo(); //проверяем, что статус эпика изменился
    }

    public static void printInfo() {
        System.out.println(taskManager.getAllTask() + "\n" + taskManager.getAllEpic()
                + "\n" + taskManager.getAllSubtask() + "\n");
    }
}
