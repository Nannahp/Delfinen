public enum Discipline {
    CRAWL("CRAWL"),
    BACKCRAWL("BACKCRAWL"),
    BREASTSTROKE("BREASTSTROKE"),
    BUTTERFLY("BUTTERFLY"),
    MEDLEY("MEDLEY");

    public final String label;
    private Discipline(String label){
        this.label = label;
    }

}