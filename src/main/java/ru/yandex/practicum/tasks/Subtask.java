package main.java.ru.yandex.practicum.tasks;

import java.util.Objects;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, int id, TaskStatus status, int idEpic) {
        super(name, description, id, status);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        String result = "Subtask{" +
                "name='" + super.getName() + '\'';
        if (getDescription() != null) {
            result = result + ", description.length=" + super.getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        return result + ", id=" + super.getId() +
                ", status='" + getStatus() + '\'' +
                ", idEpic=" + idEpic + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }
}
