package main.java.ru.yandex.practicum;

import main.java.ru.yandex.practicum.managers.Managers;
import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

public class Main {
    static TaskManager taskManager;

    public static void main(String[] args) {
        Task task;
        Epic epic;
        Subtask subtask;
        taskManager = Managers.getFileManager();

        //создаем две таски
        task = new Task("купить", "купить-купить");
        taskManager.setNewTask(task);
        task = new Task("найти", "найти-найти");
        taskManager.setNewTask(task);

        //создаём эпик и 3 субтаски
        epic = new Epic("Поездка", "Поездка-Поездка");
        taskManager.setNewEpic(epic);
        subtask = new Subtask("машина", "машина-машина", epic.getId());
        taskManager.setNewSubtask(subtask);
        subtask = new Subtask("бензин", "бензин-бензин", epic.getId());
        taskManager.setNewSubtask(subtask);
        subtask = new Subtask("палатка", "палатка-палатка", epic.getId());
        taskManager.setNewSubtask(subtask);

        //создаём эпик без субтасок
        epic = new Epic("Полёт", "Полёт-Полёт");
        taskManager.setNewEpic(epic);

        //делаем 10 запросов
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(7);

        //проверяем историю (5 уникальных)
        printHistory(taskManager);

        //создаём новый менеджер из сохранённого файла
        TaskManager taskManagerTwo = Managers.getFileManagerLoadFromFile();

        //печатаем его историю, сравниваем с предыдущей
        printHistory(taskManagerTwo);

        //делаем ещё запросы
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getEpicById(7);

        //проверяем, что порядок изменился
        printHistory(taskManager);

        //снова создаём из бекапа
        taskManagerTwo = Managers.getFileManagerLoadFromFile();

        //результат опять совпадает
        printHistory(taskManagerTwo);
    }

    private static void printHistory(TaskManager taskManager) {
        if (!(taskManager.getHistory().isEmpty())) {
            for (Task t : taskManager.getHistory()) {
                System.out.println(t);
            }
        } else System.out.println("null");
        System.out.println();
    }
}
