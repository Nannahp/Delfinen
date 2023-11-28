public class DummyMember {
    private String name;
    private int memberID;
    public DummyMember(String name, int memberID){
        this.name = name;
        this.memberID = memberID;
    }

    public String getName() {
        return name;
    }

    public int getMemberID() {
        return memberID;
    }

    @Override
    public String toString() {
        return "DummyMember{" +
                "name='" + name + '\'' +
                ", memberID=" + memberID +
                '}';
    }
}
