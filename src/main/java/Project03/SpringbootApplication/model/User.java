package Project03.SpringbootApplication.model;

public class User {
    private String id;
    private String username;
    private String password; // This should be hashed!
    private String email;
    // private Date createdAt;
    // private Date updatedAt;

    // Constructors
    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        // this.createdAt = new Date();
        // this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    // public Date getCreatedAt() { return createdAt; }
    // public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    // public Date getUpdatedAt() { return updatedAt; }
    // public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}