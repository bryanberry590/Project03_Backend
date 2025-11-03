# Project03 Spring Boot Backend

A Spring Boot REST API backend with Firebase Firestore integration.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running Locally](#running-locally)
- [Testing with Docker](#testing-with-docker)
- [Deployment to Heroku](#deployment-to-heroku)
- [API Endpoints](#api-endpoints)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Ensure you have the following installed:
- **Java 17** or higher
- **Docker** and **Docker Compose**
- **Git**

## Getting Started

### 1. Clone the Repository

```bash
git clone [your-repository-url]
cd Project03_Backend
```

### 2. Get Firebase Configuration

Contact the project lead to receive the `firebase-config.json` file. Place it in the project root directory.

**IMPORTANT**: Never commit `firebase-config.json` to Git - it's already in `.gitignore`.

## Running Locally

### Option 1: Using Gradle Wrapper (Recommended)

```bash
# Build and run the application
./gradlew bootRun
```

### Option 2: Using JAR file

```bash
# Build the JAR
./gradlew clean build

# Run the JAR
java -jar build/libs/app.jar
```

### Verify it's working

Navigate to `http://localhost:8080/` in your browser. You should see:
```
Welcome to the home page. Routes will be listed here in the future!
```

## Testing with Docker

**Before making a Pull Request, always test with Docker to ensure everything works in a containerized environment.**

### Build and Run

```bash
# Build and start the container
docker-compose up --build

# Run in detached mode (background)
docker-compose up -d --build

# Stop the container
docker-compose down

# Side note -- docker-compose down will be useful to remove the current container so that you don't get a bunch of containers
```

### Verify Docker Build

After running `docker-compose up --build`, check:
1. ✅ Container builds without errors
2. ✅ Application starts on port 8080
3. ✅ Navigate to `http://localhost:8080/` - you should see the welcome message
4. ✅ Test the API endpoints (see [API Endpoints](#api-endpoints))

**If all checks pass, you're ready to create a Pull Request!**

## Deployment to Heroku

**Note**: Contact the project lead before deploying to Heroku. They will provide the app name and access.

### Deploy to Heroku

```bash
# Add Heroku remote (one-time setup)
heroku git:remote -a your-app-name

# Deploy
git push heroku main

# Check logs
heroku logs --tail -a your-app-name

# Open deployed app
heroku open -a your-app-name
```

The Firebase configuration is already set up as an environment variable on Heroku by the project lead.

## API Endpoints

### Current Endpoints

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/` | Home endpoint | Welcome message |
| POST | `/data` | Save data to Firestore | Success/Error message |
| GET | `/data` | Retrieve data from Firestore | JSON object |

### Example API Calls

```bash
# The following curl will post to the firebase database some test user info and then get it and display it in the console

# GET home endpoint
curl http://localhost:8080/

# POST data to Firestore
curl -X POST http://localhost:8080/data \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com"}'

# GET data from Firestore
curl http://localhost:8080/data
```

## Troubleshooting

### Port Already in Use

**Error**: `Web server failed to start. Port 8080 was already in use.`

**Solution**: 
```bash
# Find and kill process using port 8080
lsof -i :8080
kill -9 <PID>
```

### Firebase Configuration Not Found

**Error**: `Failed to initialize Firebase`

**Solution**: 
- Ensure `firebase-config.json` exists in project root
- Contact project lead if you don't have the file
- Check file permissions

### Gradle Build Fails

**Solution**: 
```bash
./gradlew clean build
```

### Docker Build Fails

**Solution**: 
```bash
# Clean everything and rebuild
docker-compose down
docker system prune -f
docker-compose up --build
```

## Before Making a Pull Request

1. ✅ Run tests: `./gradlew test`
2. ✅ Build locally: `./gradlew bootRun` and verify it works
3. ✅ Test with Docker: `docker-compose up --build` and verify it works
4. ✅ Ensure `firebase-config.json` is NOT committed
5. ✅ Write clear commit messages

## Need Help?

If you encounter issues:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review application logs
3. Contact the project lead

---

**Security Reminder**: Never commit `firebase-config.json` or any sensitive credentials to Git!


**Some Bryan Notes**
# Controllers will call helper functions from service files. An example is if you have an endpoint for products. 
# In this case, the controller will call functions from the service file. The service file will handle interactions with firebase database such as posting/deleting/getting 
# This is a little tricky to understand for me but once we have the first real controller and service setup, we will be able to reference that and understand how they interact with firebase



# Curl testing for creating users, sending friend requests, and accepting friend requests: run the command ./test_friends.sh