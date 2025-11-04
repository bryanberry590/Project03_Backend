#!/bin/bash

echo "=== Events and RSVPs Test ==="
echo ""

# Create two users first
echo "1. Creating test users..."
USER1=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"pass123","email":"john@test.com"}')
USER1_ID=$(echo "$USER1" | grep -o 'ID: [^"]*' | cut -d' ' -f2 | tr -d '"}')
echo "User 1 ID: $USER1_ID"

USER2=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"jane","password":"pass456","email":"jane@test.com"}')
USER2_ID=$(echo "$USER2" | grep -o 'ID: [^"]*' | cut -d' ' -f2 | tr -d '"}')
echo "User 2 ID: $USER2_ID"
echo ""

# Create an event
echo "2. Creating event..."
EVENT=$(curl -s -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d "{\"eventTitle\":\"Team Meeting\",\"description\":\"Monthly sync\",\"startTime\":\"2024-11-15T10:00:00Z\",\"endTime\":\"2024-11-15T11:00:00Z\",\"date\":\"2024-11-15T00:00:00Z\",\"recurring\":false,\"userId\":\"$USER1_ID\",\"isEvent\":true}")
echo "$EVENT"
EVENT_ID=$(echo "$EVENT" | grep -o '"eventId":"[^"]*' | cut -d'"' -f4)
echo "Event ID: $EVENT_ID"
echo ""

# Send invitation
echo "3. Sending invitation..."
RSVP=$(curl -s -X POST http://localhost:8080/api/rsvps/invite \
  -H "Content-Type: application/json" \
  -d "{\"eventId\":\"$EVENT_ID\",\"inviteRecipientId\":\"$USER2_ID\",\"eventOwnerId\":\"$USER1_ID\"}")
echo "$RSVP"
RSVP_ID=$(echo "$RSVP" | grep -o '"rsvpId":"[^"]*' | cut -d'"' -f4)
echo "RSVP ID: $RSVP_ID"
echo ""

# Check pending invitations
echo "4. Checking pending invitations for User 2..."
curl -s http://localhost:8080/api/rsvps/user/$USER2_ID/pending | python3 -m json.tool
echo ""

# User 2 accepts invitation
echo "5. User 2 accepting invitation..."
curl -s -X PUT http://localhost:8080/api/rsvps/$RSVP_ID/status \
  -H "Content-Type: application/json" \
  -d '{"status":"yes"}' | python3 -m json.tool
echo ""

# Get RSVP summary
echo "6. Getting RSVP summary..."
curl -s http://localhost:8080/api/rsvps/event/$EVENT_ID/summary | python3 -m json.tool
echo ""

echo "=== Test Complete ==="