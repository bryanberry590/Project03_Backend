package Project03.SpringbootApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import Project03.SpringbootApplication.service.ExampleService;

@RestController
public class BaseController {
    
    @Autowired
    private ExampleService exampleService;

    @GetMapping("/")
    public String index() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Project03 API Documentation</title>
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            max-width: 1400px;
                            margin: 40px auto;
                            padding: 20px;
                            background-color: #f5f5f5;
                        }
                        h1 {
                            color: #333;
                            border-bottom: 3px solid #4CAF50;
                            padding-bottom: 10px;
                        }
                        h2 {
                            color: #555;
                            margin-top: 30px;
                            border-bottom: 2px solid #ddd;
                            padding-bottom: 5px;
                        }
                        table {
                            width: 100%;
                            border-collapse: collapse;
                            background-color: white;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                            margin-bottom: 30px;
                        }
                        th {
                            background-color: #4CAF50;
                            color: white;
                            padding: 12px;
                            text-align: left;
                            font-weight: 600;
                        }
                        td {
                            padding: 12px;
                            border-bottom: 1px solid #ddd;
                        }
                        tr:hover {
                            background-color: #f5f5f5;
                        }
                        .method {
                            font-weight: bold;
                            padding: 4px 8px;
                            border-radius: 4px;
                            font-size: 12px;
                        }
                        .post { background-color: #49cc90; color: white; }
                        .get { background-color: #61affe; color: white; }
                        .put { background-color: #fca130; color: white; }
                        .delete { background-color: #f93e3e; color: white; }
                        .route {
                            font-family: 'Courier New', monospace;
                            color: #333;
                        }
                        .section {
                            background-color: white;
                            padding: 20px;
                            margin-bottom: 20px;
                            border-radius: 8px;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        }
                        .base-url {
                            background-color: #e3f2fd;
                            padding: 10px;
                            border-left: 4px solid #2196F3;
                            margin: 20px 0;
                            font-family: 'Courier New', monospace;
                        }
                    </style>
                </head>
                <body>
                    <h1>🚀 Project03 API Documentation</h1>
                    <div class="base-url">
                        <strong>Base URL:</strong> http://localhost:8080
                    </div>
                    
                    <div class="section">
                        <h2>👤 User Routes</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Method</th>
                                    <th>Route</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><span class="method post">POST</span></td>
                                    <td class="route">/api/users</td>
                                    <td>Create new user</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/users/{id}</td>
                                    <td>Get user by ID</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/users/username/{username}</td>
                                    <td>Get user by username</td>
                                </tr>
                                <tr>
                                    <td><span class="method post">POST</span></td>
                                    <td class="route">/api/users/login</td>
                                    <td>Validate user credentials (login)</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/users</td>
                                    <td>Get all users</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section">
                        <h2>👥 Friend Routes</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Method</th>
                                    <th>Route</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><span class="method post">POST</span></td>
                                    <td class="route">/api/friends/request</td>
                                    <td>Send friend request</td>
                                </tr>
                                <tr>
                                    <td><span class="method put">PUT</span></td>
                                    <td class="route">/api/friends/{id}/accept</td>
                                    <td>Accept friend request</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/friends/user/{userId}</td>
                                    <td>Get user's friends</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/friends/pending/{userId}</td>
                                    <td>Get pending friend requests</td>
                                </tr>
                                <tr>
                                    <td><span class="method delete">DELETE</span></td>
                                    <td class="route">/api/friends/{id}</td>
                                    <td>Remove friend/delete friendship</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section">
                        <h2>📅 Event Routes</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Method</th>
                                    <th>Route</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><span class="method post">POST</span></td>
                                    <td class="route">/api/events</td>
                                    <td>Create new event</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/events/{id}</td>
                                    <td>Get event by ID</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/events/user/{userId}</td>
                                    <td>Get all events created by user</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/events/type/{isEvent}</td>
                                    <td>Get events by type (true=event, false=free time)</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/events</td>
                                    <td>Get all events</td>
                                </tr>
                                <tr>
                                    <td><span class="method put">PUT</span></td>
                                    <td class="route">/api/events/{id}</td>
                                    <td>Update event</td>
                                </tr>
                                <tr>
                                    <td><span class="method delete">DELETE</span></td>
                                    <td class="route">/api/events/{id}</td>
                                    <td>Delete event (and associated RSVPs)</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section">
                        <h2>✉️ RSVP Routes</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Method</th>
                                    <th>Route</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><span class="method post">POST</span></td>
                                    <td class="route">/api/rsvps/invite</td>
                                    <td>Send single event invitation</td>
                                </tr>
                                <tr>
                                    <td><span class="method post">POST</span></td>
                                    <td class="route">/api/rsvps/invite/bulk</td>
                                    <td>Send bulk invitations to multiple users</td>
                                </tr>
                                <tr>
                                    <td><span class="method put">PUT</span></td>
                                    <td class="route">/api/rsvps/{id}/status</td>
                                    <td>Update RSVP status (yes/no/maybe/no-reply)</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/rsvps/event/{eventId}</td>
                                    <td>Get all RSVPs for an event</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/rsvps/event/{eventId}/status/{status}</td>
                                    <td>Get RSVPs by status for an event</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/rsvps/user/{userId}</td>
                                    <td>Get all invitations for a user</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/rsvps/user/{userId}/pending</td>
                                    <td>Get pending invitations for a user</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/api/rsvps/event/{eventId}/summary</td>
                                    <td>Get RSVP summary (counts by status)</td>
                                </tr>
                                <tr>
                                    <td><span class="method delete">DELETE</span></td>
                                    <td class="route">/api/rsvps/{id}</td>
                                    <td>Delete RSVP</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section">
                        <h2>🧪 Test Routes</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Method</th>
                                    <th>Route</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><span class="method post">POST</span></td>
                                    <td class="route">/data</td>
                                    <td>Save test data to Firestore</td>
                                </tr>
                                <tr>
                                    <td><span class="method get">GET</span></td>
                                    <td class="route">/data</td>
                                    <td>Get test data from Firestore</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="section" style="background-color: #fff3cd; border-left: 4px solid #ffc107;">
                        <h2>📖 Quick Start</h2>
                        <p><strong>1. Create a user:</strong></p>
                        <pre style="background-color: #f8f9fa; padding: 10px; border-radius: 4px; overflow-x: auto;">
curl -X POST http://localhost:8080/api/users \\
  -H "Content-Type: application/json" \\
  -d '{"username":"john","password":"pass123","email":"john@test.com"}'</pre>
                        
                        <p><strong>2. Login:</strong></p>
                        <pre style="background-color: #f8f9fa; padding: 10px; border-radius: 4px; overflow-x: auto;">
curl -X POST http://localhost:8080/api/users/login \\
  -H "Content-Type: application/json" \\
  -d '{"username":"john","password":"pass123"}'</pre>
                        
                        <p><strong>3. Create an event:</strong></p>
                        <pre style="background-color: #f8f9fa; padding: 10px; border-radius: 4px; overflow-x: auto;">
curl -X POST http://localhost:8080/api/events \\
  -H "Content-Type: application/json" \\
  -d '{"eventTitle":"Meeting","description":"Team sync","userId":"USER_ID","isEvent":true}'</pre>
                    </div>

                    <footer style="text-align: center; margin-top: 40px; color: #666; font-size: 14px;">
                        <p>Project03 Spring Boot API | Built with ❤️ using Java 17 & Firebase & THE GOAT CLAUDE</p>
                    </footer>
                </body>
                </html>
                """;
    }
    
    // @PostMapping("/data")
    // public String saveData(@RequestBody Map<String, Object> data) {
    //     try {
    //         return exampleService.saveData("test-collection", "test-doc", data);
    //     } catch (Exception e) {
    //         return "Error: " + e.getMessage();
    //     }
    // }
    
    // @GetMapping("/data")
    // public Map<String, Object> getData() {
    //     try {
    //         return exampleService.getData("test-collection", "test-doc");
    //     } catch (Exception e) {
    //         Map<String, Object> error = new HashMap<>();
    //         error.put("error", e.getMessage());
    //         return error;
    //     }
    // }
}