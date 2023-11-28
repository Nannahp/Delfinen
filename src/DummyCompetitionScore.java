public class DummyCompetitionScore {
    private String name ;
    private int time;
    public DummyCompetitionScore(String name, int time){
        this.name = name;
        this.time = time;
    }


    @Override
    public String toString() {
        return  name + ':'+ time +
                "s";
    }
}
