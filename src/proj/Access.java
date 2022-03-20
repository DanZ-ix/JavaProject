package proj;

public class Access {

    private String value;
    public static final Access LOW = new Access("LOW");
    public static final Access HIGH = new Access("HIGH");

    public Access (String value)
    {
        this.setValue(value);
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
