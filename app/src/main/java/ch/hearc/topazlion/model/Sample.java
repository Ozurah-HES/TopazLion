package ch.hearc.topazlion.model;

public class Sample {
    private String info;

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "Sample [info=" + info + "]";
    }
}
