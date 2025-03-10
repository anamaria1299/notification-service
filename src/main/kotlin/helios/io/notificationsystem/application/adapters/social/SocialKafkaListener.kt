package helios.io.notificationsystem.application.adapters.social

import com.fasterxml.jackson.databind.ObjectMapper
import helios.io.notificationsystem.application.adapters.social.events.FriendRequestEvent as FriendRequestExternalEvent
import helios.io.notificationsystem.application.adapters.social.events.FriendAcceptedEvent as FriendAcceptedExternalEvent
import helios.io.notificationsystem.application.adapters.social.events.NewFollowerEvent as NewFollowerExternalEvent
import helios.io.notificationsystem.domain.NotificationService
import helios.io.notificationsystem.domain.social.FriendAcceptedEvent
import helios.io.notificationsystem.domain.social.FriendRequestEvent
import helios.io.notificationsystem.domain.social.NewFollowerEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SocialKafkaListener(
    private val notificationService: NotificationService,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(SocialKafkaListener::class.java)

    @KafkaListener(
        topics = ["\${notification-system.social.listener.friend-request-topic}"],
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "\${notification-system.social.listener.group-id}",
    )
    fun friendRequestListener(event: String) {
        logger.info("Friend request event received: $event")
        val friendRequestExternalEvent = objectMapper.readValue(event, FriendRequestExternalEvent::class.java)
        val friendRequestEvent = FriendRequestEvent(friendRequestExternalEvent.userId, friendRequestExternalEvent.requestedFriend)

        notificationService.handleSocialEvents(friendRequestEvent)
    }

    @KafkaListener(
        topics = ["\${notification-system.social.listener.friend-accepted-topic}"],
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "\${notification-system.social.listener.group-id}",
    )
    fun friendAcceptedListener(event: String) {
        logger.info("Friend accepted event received: $event")
        val friendAcceptedExternalEvent = objectMapper.readValue(event, FriendAcceptedExternalEvent::class.java)
        val friendAcceptedEvent = FriendAcceptedEvent(friendAcceptedExternalEvent.userId, friendAcceptedExternalEvent.friend)

        notificationService.handleSocialEvents(friendAcceptedEvent)
    }

    @KafkaListener(
        topics = ["\${notification-system.social.listener.new-follower-topic}"],
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "\${notification-system.social.listener.group-id}",
    )
    fun followerListener(event: String) {
        logger.info("New follower event received: $event")
        val newFollowerExternalEvent = objectMapper.readValue(event, NewFollowerExternalEvent::class.java)
        val newFollowerEvent = NewFollowerEvent(newFollowerExternalEvent.userId, newFollowerExternalEvent.follower)

        notificationService.handleSocialEvents(newFollowerEvent)
    }

}