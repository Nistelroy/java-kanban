package main.java.ru.yandex.practicum.tasks;

public class Subtask extends Task implements HasId{



    private int idEpic;

    public Subtask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
    }
    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        String result = "Subtask{" +
                "name='" + super.getName() + '\'' ;
        if(getDescription() != null) {
            result = result + ", description.length=" + super.getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        return result +  ", id=" + super.getId() +
                ", status='" + getStatus() + '\'' +
                ", idEpic=" + idEpic + '\'' +
                '}';





        /*return "Subtask{" +
                "name='" + super.getName() + '\'' +
                ", description='" + super.getDescription().length() + '\'' +
                ", id=" + super.getId() +
                ", status='" + getStatus() + '\'' +
                ", idEpic=" + idEpic + '\'' +
                '}';*/
    }
}
