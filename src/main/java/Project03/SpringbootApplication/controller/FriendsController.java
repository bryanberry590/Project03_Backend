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

import Project03.SpringbootApplication.service.FriendsService;

@RestController
@RequestMapping("/api/friends")
public class FriendsController {

    @Autowired
    private FriendsService friendsService;

    /**
     * POST /api/friends/request - Send friend request
     */
    @PostMapping("/request")
    public Map<String, Object> sendFriendRequest(@RequestBody Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userId = data.get("userId");
            String friendId = data.get("friendId");
            
            String result = friendsService.sendFriendRequest(userId, friendId);
            response.put("success", true);
            response.put("message", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * PUT /api/friends/{id}/accept - Accept friend request
     */
    @PutMapping("/{id}/accept")
    public Map<String, Object> acceptFriendRequest(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = friendsService.acceptFriendRequest(id);
            response.put("success", true);
            response.put("message", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * GET /api/friends/user/{userId} - Get user's friends
     */
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserFriends(@PathVariable String userId) {
        try {
            return friendsService.getUserFriends(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching friends: " + e.getMessage());
        }
    }

    /**
     * GET /api/friends/pending/{userId} - Get pending requests
     */
    @GetMapping("/pending/{userId}")
    public List<Map<String, Object>> getPendingRequests(@PathVariable String userId) {
        try {
            return friendsService.getPendingRequests(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching requests: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/friends/{id} - Remove friend
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> removeFriend(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = friendsService.removeFriend(id);
            response.put("success", true);
            response.put("message", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

        /**
     * GET /api/friends - Get all friendships (admin)
     * Returns all friendship records regardless of user
     */
    @GetMapping
    public List<Map<String, Object>> getAllFriendships() {
        try {
            return friendsService.getAllFriendships();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all friendships: " + e.getMessage());
        }
    }


}