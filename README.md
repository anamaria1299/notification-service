# Notification service 

This project implements a real-time notification system for an online gaming platform. The goal is to keep players informed of important events happening both within the game (e.g., leveling up, acquiring items, completing challenges, and PvP events) and in the social network (e.g., friend requests, accepted friend requests, and new followers).

## Features
### In-Game Events:
* Level Up: Notify players when they reach a new level (e.g., “Congratulations! You’ve reached level 15!”).
* Item Acquired: Notify players when they obtain a rare or valuable item (e.g., “You’ve acquired the legendary Sword of Azeroth!”).
* Challenge Completed: Notify players when they complete a challenging quest or achievement.
* PvP Events (optional): Notify players when they are attacked or defeated by another player.
### Social Events:
* Friend Request: Notify when another player sends a friend request.
* Friend Accepted: Notify when a friend request is accepted.
* New Follower: Notify when a new player starts following them.
### User Preferences:
Each user can enable or disable notifications for different event categories (e.g., in-game events and social events). The system verifies these preferences before sending any notification.

## Architecture and project structure 

The project is divided into two primary modules—Domain and Application—which minimizes technology coupling and promotes maintainability and scalability.

### Domain:
Contains domain classes such as notification and user entities. This is where the business logic for verifying user preferences and creating notifications is implemented.
### Application / Adapters:
Includes adapters for Kafka that receive and process events emitted by different parts of the system (e.g., game events and social events). The Kafka listener invokes the business logic to process each event and, based on the user’s preferences, decides whether to generate a notification.

## Assumptions
1.	It is assumed that different events will be received from Kafka topics.
2.	It is assumed that the notification system will send notifications to two specific topics: social and in-game.

## Testing
Integration tests were conducted using Embedded Kafka for social and in-game events. Additionally, unit tests were performed on the service to analyze user preference behavior. Integration testing was chosen because it helps validate the system’s complete behavior, including its interactions with external dependencies such as Kafka.

## How to use it?

The project includes a Docker Compose configuration that sets up Kafka and its dependencies, allowing the notification service to run smoothly.

Steps to Run the Service:

1.	Start Kafka and dependencies using Docker Compose:
   ```sh
   docker-compose up -d
   ```
This will start the required Kafka services in the background.

2. Once Kafka is up and running, start the notification service:

```shell
./gradlew bootRun
```

or use the IDE of your preference.

And that’s it! The service will now be running and ready to process notifications.

## Evidence


https://github.com/user-attachments/assets/38b18030-061b-447b-a80f-fdfeed147055

