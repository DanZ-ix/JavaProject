package jira4u;




public class Task {

    //region fields
    private String name;
    private String description;
    private String deadLine;
    private Priority priority;
    private User worker;
    private User reviewer;
    private String status;
    //endregion

    public Task(String name, Priority priority, String description, String deadLine, User worker, User reviewer)
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

    public String getPriority() {

        switch (this.priority)
        {
            case MIDDLE:
                return "Средний";
            case LOW:
                return "Низкий";
            case HIGH:
                return "Высокий";
            case LOWEST:
                return "Самый низкий";
            case HIGHEST:
                return "Самый высокий";
            case CRITICAL:
                return "Критический";
        }
        return "Средний";
    }

    public void setPriority(Priority priority) {
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
