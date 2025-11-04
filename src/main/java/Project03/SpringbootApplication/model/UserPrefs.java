package Project03.SpringbootApplication.model;

public class UserPrefs {
    private String id;
    private String userId;
    private int theme;
    private boolean notificationEnabled;
    private int colorScheme;
    // private Date createdAt;
    // private Date updatedAt;

    // Constructors
    public UserPrefs() {}

    public UserPrefs(String userId, int theme, boolean notificationEnabled, int colorScheme) {
        this.userId = userId;
        this.theme = theme;
        this.notificationEnabled = notificationEnabled;
        this.colorScheme = colorScheme;
        // this.createdAt = new Date();
        // this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getTheme() { return theme; }
    public void setTheme(int theme) { this.theme = theme; }

    public boolean isNotificationEnabled() { return notificationEnabled; }
    public void setNotificationEnabled(boolean notificationEnabled) { 
        this.notificationEnabled = notificationEnabled; 
    }

    public int getColorScheme() { return colorScheme; }
    public void setColorScheme(int colorScheme) { this.colorScheme = colorScheme; }

    // public Date getCreatedAt() { return createdAt; }
    // public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    // public Date getUpdatedAt() { return updatedAt; }
    // public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}