#!/bin/bash

# Define variables for profile, docker image name, and compose file
APP_NAME="zanemr-auth-service"
PROFILE=$1
IMAGE_NAME="$APP_NAME"

# Function to display usage instructions
usage() {
  echo "Usage: $0 [dev|prod]"
  exit 1
}

# Check if profile is provided and valid
if [ -z "$PROFILE" ]; then
  echo "Error: No profile specified!"
  usage
fi

if [ "$PROFILE" != "dev" ] && [ "$PROFILE" != "prod" ]; then
  echo "Error: Invalid profile specified!"
  usage
fi

echo "Starting with profile: $PROFILE"

# Step 1: Build the application using Gradle
echo "Running Gradle buildImage..."
./gradlew clean bootJar -Dspring.profiles.active="$PROFILE"

if [ $? -ne 0 ]; then
  echo "Gradle buildImage failed!"
  exit 1
fi

# Step 2: Build the Docker image for the Spring Boot app
echo "Building Docker image for profile: $PROFILE"
docker build -t "$IMAGE_NAME" .

if [ $? -ne 0 ]; then
  echo "Docker image build failed!"
  exit 1
fi

# Step 3: Start the Docker Compose stack with the appropriate profile
echo "Starting Docker Compose with profile: $PROFILE"
SPRING_PROFILES_ACTIVE=$PROFILE docker compose up --build

