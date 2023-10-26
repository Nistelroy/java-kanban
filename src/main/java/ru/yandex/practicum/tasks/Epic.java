package main.java.ru.yandex.practicum.tasks;

import java.util.ArrayList;

public class Epic extends Task implements HasId {
    private String status;
    private ArrayList<Integer> idSubtask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        status = "NEW";
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Integer> getIdSubtask() {

        return idSubtask;
    }

    public void setIdSubtask(int idNewSubtask) {
        idSubtask.add(idNewSubtask);
    }



    public void removeEpicSubtasks(int remId) {
        for (int i = 0; i < idSubtask.size(); i++) {
            if (idSubtask.get(i) == remId) {
                idSubtask.remove(i);
            }
        }
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + super.getName() + '\'';
        if (getDescription() != null) {
            result = result + ", description.length=" + super.getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        return result + ", id=" + super.getId() +
                ", status='" + status + '\'' +
                ", idSubtask=" + idSubtask + '\'' +
                '}';
    }
}


