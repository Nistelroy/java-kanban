package main.java.ru.yandex.practicum.tasks;

import main.java.ru.yandex.practicum.manager.TaskManager;

import java.util.ArrayList;

public class Epic extends Task implements HasId{




    private ArrayList<Integer> idSubtask = new ArrayList<>();

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
    private String status;

    public Epic(String name, String description) {
        super(name, description);
        status = "NEW";
    }
    public ArrayList<Integer> getIdSubtask() {

        return idSubtask;
    }
    public void setIdSubtask(int idNewSubtask) {
        idSubtask.add(idNewSubtask);
    }

    public void removeIdSubtask(int remId){
        for (int i = 0; i < idSubtask.size(); i++) {
            if (idSubtask.get(i) == remId) {
                idSubtask.remove(i);
            }
        }


    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + super.getName() + '\'' ;
        if(getDescription() != null) {
            result = result + ", description.length=" + super.getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        return result +  ", id=" + super.getId() +
                ", status='" + status + '\'' +
                ", idSubtask=" + idSubtask + '\'' +
                '}';








      /*  return "Epic{" +
                "name='" + super.getName() + '\'' +
                ", description='" + super.getDescription().length() + '\'' +
                ", id=" + super.getId() +
                ", status='" + status + '\'' +
                ", idSubtask=" + idSubtask + '\'' +
                '}';*/
    }


}


