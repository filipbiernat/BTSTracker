package pl.edu.agh.student.fbierna.btstracker.airscanner;

public enum Network {
    GSM("GSM (2G)", 0),
    WCDMA("WCDMA (3G)", 1),
    LTE("LTE (3G)", 2);

    private String stringValue;
    private int intValue;
    Network(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}