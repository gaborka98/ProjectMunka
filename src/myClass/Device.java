package myClass;

public class Device {
    int index;
    private boolean foglalt;
    private String nev;

    public Device(int index, String name, boolean foglalt) {
        this.foglalt = foglalt;
        this.nev = name;
        this.index = index;
    }

    public boolean isFoglalt() {
        return foglalt;
    }

    public String getNev() {
        return nev;
    }

    public int getIndex() {
        return index;
    }
}
