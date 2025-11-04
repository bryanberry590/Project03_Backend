#!/bin/bash

echo "=== Friend Request Workflow Test ==="
echo ""

# Create User 1
echo "1. Creating Alice..."
ALICE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"alice_test","password":"password123","email":"alice_test@example.com"}')
echo "$ALICE_RESPONSE"
# Extract ID from message "User created with ID: XYZ"
ALICE_ID=$(echo "$ALICE_RESPONSE" | grep -o 'ID: [^"]*' | cut -d' ' -f2 | tr -d '"}')
echo "Alice ID: $ALICE_ID"
echo ""

# Create User 2
echo "2. Creating Bob..."
BOB_RESPONSE=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"bob_test","password":"password456","email":"bob_test@example.com"}')
echo "$BOB_RESPONSE"
# Extract ID from message "User created with ID: XYZ"
BOB_ID=$(echo "$BOB_RESPONSE" | grep -o 'ID: [^"]*' | cut -d' ' -f2 | tr -d '"}')
echo "Bob ID: $BOB_ID"
echo ""

# Alice sends friend request to Bob
echo "3. Alice sending friend request to Bob..."
REQUEST_RESPONSE=$(curl -s -X POST http://localhost:8080/api/friends/request \
  -H "Content-Type: application/json" \
  -d "{\"userId\":\"$ALICE_ID\",\"friendId\":\"$BOB_ID\"}")
echo "$REQUEST_RESPONSE"
FRIENDSHIP_ID=$(echo "$REQUEST_RESPONSE" | grep -o 'ID: [^"]*' | cut -d' ' -f2 | tr -d '"}')
echo "Friendship ID: $FRIENDSHIP_ID"
echo ""

# Bob checks pending requests
echo "4. Bob checking pending friend requests..."
curl -s http://localhost:8080/api/friends/pending/$BOB_ID | python3 -m json.tool
echo ""

# Bob accepts the request
echo "5. Bob accepting friend request..."
curl -s -X PUT http://localhost:8080/api/friends/$FRIENDSHIP_ID/accept | python3 -m json.tool
echo ""

# Verify - Bob's friends
echo "6. Checking Bob's friends list..."
curl -s http://localhost:8080/api/friends/user/$BOB_ID | python3 -m json.tool
echo ""

# Verify - Alice's friends
echo "7. Checking Alice's friends list..."
curl -s http://localhost:8080/api/friends/user/$ALICE_ID | python3 -m json.tool
echo ""

echo "=== Test Complete ==="