package Project03.SpringbootApplication.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Project03.SpringbootApplication.service.UserPrefsService;

@RestController
@RequestMapping("/api/user-prefs")
public class UserPrefsController {

    @Autowired
    private UserPrefsService userPrefsService;

    /**
     * POST /api/user-prefs - Create user preferences
     */
    @PostMapping
    public Map<String, Object> createUserPrefs(@RequestBody Map<String, Object> data) {
        try {
            String userId = (String) data.get("userId");
            Integer theme = data.get("theme") != null ? ((Number) data.get("theme")).intValue() : null;
            Boolean notificationEnabled = (Boolean) data.get("notificationEnabled");
            Integer colorScheme = data.get("colorScheme") != null ? ((Number) data.get("colorScheme")).intValue() : null;
            
            return userPrefsService.createUserPrefs(userId, theme, notificationEnabled, colorScheme);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * POST /api/user-prefs/default - Create preferences with default values
     */
    @PostMapping("/default")
    public Map<String, Object> createDefaultUserPrefs(@RequestBody Map<String, String> data) {
        try {
            String userId = data.get("userId");
            return userPrefsService.createDefaultUserPrefs(userId);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * GET /api/user-prefs/user/{userId} - Get user preferences by user ID
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserPrefs(@PathVariable String userId) {
        try {
            return userPrefsService.getUserPrefs(userId);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * GET /api/user-prefs/{id} - Get user preferences by prefs ID
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUserPrefsById(@PathVariable String id) {
        try {
            return userPrefsService.getUserPrefsById(id);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * PUT /api/user-prefs/user/{userId} - Update user preferences
     */
    @PutMapping("/user/{userId}")
    public Map<String, Object> updateUserPrefs(@PathVariable String userId, 
                                               @RequestBody Map<String, Object> updates) {
        try {
            return userPrefsService.updateUserPrefs(userId, updates);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * PUT /api/user-prefs/user/{userId}/theme - Update theme only
     */
    @PutMapping("/user/{userId}/theme")
    public Map<String, Object> updateTheme(@PathVariable String userId, 
                                          @RequestBody Map<String, Integer> data) {
        try {
            int theme = data.get("theme");
            return userPrefsService.updateTheme(userId, theme);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * PUT /api/user-prefs/user/{userId}/notifications - Update notifications only
     */
    @PutMapping("/user/{userId}/notifications")
    public Map<String, Object> updateNotifications(@PathVariable String userId, 
                                                   @RequestBody Map<String, Boolean> data) {
        try {
            boolean enabled = data.get("enabled");
            return userPrefsService.updateNotificationEnabled(userId, enabled);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * PUT /api/user-prefs/user/{userId}/color-scheme - Update color scheme only
     */
    @PutMapping("/user/{userId}/color-scheme")
    public Map<String, Object> updateColorScheme(@PathVariable String userId, 
                                                 @RequestBody Map<String, Integer> data) {
        try {
            int colorScheme = data.get("colorScheme");
            return userPrefsService.updateColorScheme(userId, colorScheme);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * DELETE /api/user-prefs/user/{userId} - Delete user preferences
     */
    @DeleteMapping("/user/{userId}")
    public Map<String, Object> deleteUserPrefs(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = userPrefsService.deleteUserPrefs(userId);
            response.put("success", true);
            response.put("message", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * GET /api/user-prefs - Get all user preferences (admin)
     */
    @GetMapping
    public List<Map<String, Object>> getAllUserPrefs() {
        try {
            return userPrefsService.getAllUserPrefs();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user preferences: " + e.getMessage());
        }
    }
}