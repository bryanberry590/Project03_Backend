package Project03.SpringbootApplication.model;

public class RSVP {
    private String id;
    private String eventId;
    private String inviteRecipientId;
    private String status; // "yes", "no", "maybe", "no-reply"
    private String eventOwnerId;
    // private Date createdAt;
    // private Date updatedAt;

    // Constructors
    public RSVP() {}

    public RSVP(String eventId, String inviteRecipientId, String eventOwnerId) {
        this.eventId = eventId;
        this.inviteRecipientId = inviteRecipientId;
        this.eventOwnerId = eventOwnerId;
        this.status = "no-reply"; // default status
        // this.createdAt = new Date();
        // this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getInviteRecipientId() { return inviteRecipientId; }
    public void setInviteRecipientId(String inviteRecipientId) { 
        this.inviteRecipientId = inviteRecipientId; 
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEventOwnerId() { return eventOwnerId; }
    public void setEventOwnerId(String eventOwnerId) { this.eventOwnerId = eventOwnerId; }
}