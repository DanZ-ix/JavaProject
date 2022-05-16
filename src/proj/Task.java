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
    private String worker_str;
    private String reviewer_str;
    private int taskId;
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
    public Task(String name, String priority, String description,
                String deadLine, String worker, String reviewer, int taskId, String status)
    {
        this.name = name;
        this.description = description;
        this.deadLine = deadLine;
        this.priority = priority;
        this.setWorker_str(worker);
        this.setReviewer_str(reviewer);
        this.status = status;
        this.setTaskId(taskId);
    }


    //region getters-setters

    public String getStatus()
    {
        return(this.status);
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

    public String getWorker_str() {
        return worker_str;
    }

    public void setWorker_str(String worker_str) {
        this.worker_str = worker_str;
    }

    public String getReviewer_str() {
        return reviewer_str;
    }

    public void setReviewer_str(String reviewer_str) {
        this.reviewer_str = reviewer_str;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    //endregion

}
