public class DummyTrainingScore {
    private String name;
    private int time;

    public DummyTrainingScore(String name, int time){
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return  name + ':'+ time +
                "s";
    }
}
