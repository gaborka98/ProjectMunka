package myClass;

public class Device {
    private int index;
    private boolean foglalt;
    private String nev;
    private int maxRent;

    public Device(int index, String name, boolean foglalt, int maxRent) {
        this.foglalt = foglalt;
        this.nev = name;
        this.index = index;
        this.maxRent = maxRent;
    }

    public int getMaxRent() {
        return maxRent;
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
