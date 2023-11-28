import java.time.LocalDate;

public class TrainingScore {

    // Attributter

   // private int score;
    private int time;
    private LocalDate date;
    private Discipline discipline;


    // Konstruktør

    public TrainingScore( int time, LocalDate date, Discipline discipline) {
       // this.score = score;
        this.time = time;
        this.date = date;
        this.discipline = discipline;

    }


    // Getter - metode for at få og indstille score, tid, dato og disciplin.


    public int getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    // Setter - metode for at få of indstille tid, score, dato og disciplin.

    public void setTime(int time) {
        this.time = time;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}

