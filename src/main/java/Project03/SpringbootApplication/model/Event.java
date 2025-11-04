package Project03.SpringbootApplication.model;

import java.util.Date;

public class Event {
    private String id;
    private String eventTitle;
    private String description;
    private Date startTime;
    private Date endTime;
    private Date date;
    private boolean recurring;
    private String userId; // creator
    private boolean isEvent; // true = event, false = free time
    // private Date createdAt;
    // private Date updatedAt;

    // Constructors
    public Event() {}

    public Event(String eventTitle, String description, Date startTime, Date endTime, 
                 Date date, boolean recurring, String userId, boolean isEvent) {
        this.eventTitle = eventTitle;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.recurring = recurring;
        this.userId = userId;
        this.isEvent = isEvent;
        // this.createdAt = new Date();
        // this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public boolean isRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public boolean isEvent() { return isEvent; }
    public void setEvent(boolean event) { isEvent = event; }

    // public Date getCreatedAt() { return createdAt; }
    // public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    // public Date getUpdatedAt() { return updatedAt; }
    // public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}