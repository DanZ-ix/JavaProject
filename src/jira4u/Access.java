package jira4u;

public class Access {

    private String value;


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
