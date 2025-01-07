#!/bin/bash

# Define variables for profile, docker image name, and compose file
APP_NAME="zanemr-auth-service"
PROFILE=$1
PG_RESTORE=false # Default to false
DATA_FILE=""      # Default to empty
IMAGE_NAME="$APP_NAME"

# Function to display usage instructions
usage() {
  echo "Usage: $0 [dev|test|prod] [--with-data=/path/to/data/file.sql]"
  exit 1
}

# Check if profile is provided and valid
if [ -z "$PROFILE" ]; then
  echo "Error: No profile specified!"
  usage
fi

if [ "$PROFILE" != "dev" ] && [ "$PROFILE" != "prod" ] && [ "$PROFILE" != "test" ]; then
  echo "Error: Invalid profile specified!"
  usage
fi

# Check for the --with-data flag
if [[ "$2" == --with-data=* ]]; then
  PG_RESTORE=true
  DATA_FILE="${2#--with-data=}"
fi

echo "Starting with profile: $PROFILE"

if [ "$PROFILE" == "prod" ]; then
  cp service/banner_prod.png src/main/resources/static/img/banner.png
fi
if [ "$PROFILE" == "test" ]; then
  cp service/banner_test.png src/main/resources/static/img/banner.png
  PROFILE="prod"
fi

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
  echo "Starting Docker Compose with profile: $PROFILE and data restore from $DATA_FILE"
  SPRING_PROFILES_ACTIVE=$PROFILE PG_RESTORE=true DATA_FILE="$DATA_FILE" docker compose up --build
else
  echo "Starting Docker Compose with profile: $PROFILE"
  SPRING_PROFILES_ACTIVE=$PROFILE docker compose up --build
fi