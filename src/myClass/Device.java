package myClass;

import java.util.Date;

public class Device {
    private int index;
    private boolean foglalt;
    private String nev;
    private Date rentDate;


    public Device(int index, String name, boolean foglalt, Date rentDate) {
        this.foglalt = foglalt;
        this.nev = name;
        this.index = index;
        this.rentDate = rentDate;
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

    public Date getRentDate() {
        return rentDate;
    }
}
