#!/bin/bash

# Define variables for profile, docker image name, and compose file
APP_NAME="zanemr-auth-service"
PROFILE=$1
PG_RESTORE=false # Default to false
IMAGE_NAME="$APP_NAME"

# Function to display usage instructions
usage() {
  echo "Usage: $0 [dev|prod] [--with-data=/path/to/data/file.sql]"
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

# Check for the --with-data flag
if [[ "$2" == "--with-data" ]]; then
  PG_RESTORE=true
fi

echo "Starting with profile: $PROFILE"

# Step 1: Build the application using Gradle
echo "Running Gradle buildImage..."
SPRING_PROFILES_ACTIVE=$PROFILE ./gradlew clean bootJar

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

if [ "$PG_RESTORE" == "true" ]; then
  echo "Starting Docker Compose with profile: $PROFILE and PG_RESTORE=true"
  SPRING_PROFILES_ACTIVE=$PROFILE PG_RESTORE=true docker compose up --build -d
else
  echo "Starting Docker Compose with profile: $PROFILE"
  SPRING_PROFILES_ACTIVE=$PROFILE docker compose up --build -d
fi