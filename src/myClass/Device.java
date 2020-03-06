package myClass;

public class Device {
    private boolean foglalt;
    private String nev;

    public Device(boolean foglalt, String nev) {
        this.foglalt = foglalt;
        this.nev = nev;
    }

    public boolean isFoglalt() {
        return foglalt;
    }

    public String getNev() {
        return nev;
    }
}
