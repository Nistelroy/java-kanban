package main.java.ru.yandex.practicum;

import main.java.ru.yandex.practicum.managers.Managers;
import main.java.ru.yandex.practicum.managers.TaskManager;
import main.java.ru.yandex.practicum.managers.file.FileBackedTasksManager;
import main.java.ru.yandex.practicum.tasks.Epic;
import main.java.ru.yandex.practicum.tasks.Subtask;
import main.java.ru.yandex.practicum.tasks.Task;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    static TaskManager taskManager;
    private static final Path file = Paths.get("src/main/resources/data.csv");

    public static void main(String[] args) {

        Task task;
        Epic epic;
        Subtask subtask;
        taskManager = Managers.getDefault(file);

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


        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(7);

        //проверяем историю (5 уникальных)
        printHistory(taskManager);

        //создаём новый менеджер из сохранённого файла
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(file);

        //печатаем его историю, сравниваем с предыдущей
        printHistory(fileBackedManager);

        //делаем запрос такски, которая уже была в истории
        taskManager.getSubtaskById(4);

        //старый запрос удалился, она снова на верху списка
        printHistory(taskManager);

        //снова создаём из бекапа
        fileBackedManager = FileBackedTasksManager.loadFromFile(file);

        //результат опять совпадает
        printHistory(fileBackedManager);

        //меняем таску, удаляем эпик
        task.setName("Дорого");
        taskManager.updateTaskInMap(task);
        taskManager.deleteEpicById(3);

        //старый запрос удалился, она снова на верху списка
        printHistory(taskManager);

        //снова создаём из бекапа
        fileBackedManager = FileBackedTasksManager.loadFromFile(file);

        //результат опять совпадает
        printHistory(fileBackedManager);

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
