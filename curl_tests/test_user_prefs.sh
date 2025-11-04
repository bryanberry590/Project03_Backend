echo "=== User Preferences Test ==="
echo ""

# Create a user first
echo "1. Creating test user..."
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test_prefs","password":"pass123","email":"test@prefs.com"}')
USER_ID=$(echo "$USER_RESPONSE" | grep -o 'ID: [^"]*' | cut -d' ' -f2 | tr -d '"}')
echo "User ID: $USER_ID"
echo ""

# Create preferences with defaults
echo "2. Creating default preferences..."
curl -s -X POST http://localhost:8080/api/user-prefs/default \
  -H "Content-Type: application/json" \
  -d "{\"userId\":\"$USER_ID\"}" | python3 -m json.tool
echo ""

# Get preferences
echo "3. Getting user preferences..."
curl -s http://localhost:8080/api/user-prefs/user/$USER_ID | python3 -m json.tool
echo ""

# Update theme
echo "4. Updating theme to 2..."
curl -s -X PUT http://localhost:8080/api/user-prefs/user/$USER_ID/theme \
  -H "Content-Type: application/json" \
  -d '{"theme": 2}' | python3 -m json.tool
echo ""

# Update multiple fields
echo "5. Updating multiple preferences..."
curl -s -X PUT http://localhost:8080/api/user-prefs/user/$USER_ID \
  -H "Content-Type: application/json" \
  -d '{"theme": 3, "colorScheme": 1, "notificationEnabled": false}' | python3 -m json.tool
echo ""

# Get updated preferences
echo "6. Getting updated preferences..."
curl -s http://localhost:8080/api/user-prefs/user/$USER_ID | python3 -m json.tool
echo ""

echo "=== Test Complete ==="