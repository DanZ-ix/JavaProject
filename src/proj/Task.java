package proj;




public class Task {

    //region fields
    private String name;
    private String description;
    private String deadLine;
    private String priority;
    private User worker;
    private User reviewer;
    private String status;
    //endregion

    public Task(String name, String priority, String description, String deadLine, User worker, User reviewer)
    {
        this.name = name;
        this.description = description;
        this.deadLine = deadLine;
        this.priority = priority;
        this.worker = worker;
        this.reviewer = reviewer;
        this.status = "В работе";

    }


    //region getters-setters

    public String getStatus()
    {
        return(this.status);
    }

    public void setDone()
    {
        this.status = "Выполнено";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    //endregion

}
