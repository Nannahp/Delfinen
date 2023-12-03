import java.io.Serializable;
import java.time.LocalDate;

public class TrainingScore implements Serializable, Comparable {
    private int time;
    private LocalDate date;
    private Discipline discipline;

    public TrainingScore(int time, LocalDate date, Discipline discipline) {
        this.time = time;
        this.date = date;
        this.discipline = discipline;
    }

    public int getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int compareTo(Object o) {
        if (this.discipline == ((TrainingScore) o).getDiscipline()) {
            return 0;
        } else return -1;
    }

    @Override
    public String toString() {
        return "TrainingScore{" +
                "time=" + time +
                ", date=" + date +
                ", discipline=" + discipline +
                '}';
    }

}
