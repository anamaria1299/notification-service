package helios.io.notificationsystem.application.adapters.ingame

import com.fasterxml.jackson.databind.ObjectMapper
import helios.io.notificationsystem.application.adapters.ingame.events.ChallengeCompletedEvent as ChallengeCompletedExternalEvent
import helios.io.notificationsystem.application.adapters.ingame.events.ItemAcquiredEvent as ItemAcquiredExternalEvent
import helios.io.notificationsystem.application.adapters.ingame.events.LevelUpEvent as LevelUpExternalEvent
import helios.io.notificationsystem.application.adapters.ingame.events.PlayerVsPlayerEvent as PlayerVsPlayerExternalEvent
import helios.io.notificationsystem.domain.NotificationService
import helios.io.notificationsystem.domain.ingame.ChallengeCompletedEvent
import helios.io.notificationsystem.domain.ingame.ItemAcquiredEvent
import helios.io.notificationsystem.domain.ingame.LevelUpEvent
import helios.io.notificationsystem.domain.ingame.PlayerVsPlayerEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class InGameKafkaListener(
    private val notificationService: NotificationService,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(InGameKafkaListener::class.java)

    @KafkaListener(
        topics = ["\${notification-system.in-game.listener.level-up-topic}"],
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "\${notification-system.in-game.listener.group-id}",
    )
    fun levelUpListener(event: String) {
        logger.info("Level up event received: $event")
        val levelUpExternalEvent = objectMapper.readValue(event, LevelUpExternalEvent::class.java)
        val levelUpEvent = LevelUpEvent(levelUpExternalEvent.userId, levelUpExternalEvent.level)

        notificationService.handleInGameEvents(levelUpEvent)
    }

    @KafkaListener(
        topics = ["\${notification-system.in-game.listener.item-acquired-topic}"],
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "\${notification-system.in-game.listener.group-id}",
    )
    fun itemAcquiredListener(event: String) {
        logger.info("Item acquired event received: $event")
        val itemAcquiredExternalEvent = objectMapper.readValue(event, ItemAcquiredExternalEvent::class.java)
        val itemAcquiredEvent = ItemAcquiredEvent(
            itemAcquiredExternalEvent.userId,
            itemAcquiredExternalEvent.item
        )

        notificationService.handleInGameEvents(itemAcquiredEvent)
    }

    @KafkaListener(
        topics = ["\${notification-system.in-game.listener.challenge-completed-topic}"],
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "\${notification-system.in-game.listener.group-id}",
    )
    fun challengeCompletedListener(event: String) {
        logger.info("Challenge completed event received: $event")
        val challengeCompletedExternalEvent = objectMapper.readValue(event, ChallengeCompletedExternalEvent::class.java)
        val challengeCompletedEvent = ChallengeCompletedEvent(
            challengeCompletedExternalEvent.userId,
            challengeCompletedExternalEvent.challenge
        )

        notificationService.handleInGameEvents(challengeCompletedEvent)
    }

    @KafkaListener(
        topics = ["\${notification-system.in-game.listener.player-vs-player-topic}"],
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "\${notification-system.in-game.listener.group-id}",
    )
    fun playerVsPlayerListener(event: String) {
        logger.info("Player vs Player event received: $event")
        val playerVsPlayerExternalEvent = objectMapper.readValue(event, PlayerVsPlayerExternalEvent::class.java)
        val challengeCompletedEvent = PlayerVsPlayerEvent(
            playerVsPlayerExternalEvent.userId,
            playerVsPlayerExternalEvent.opponent,
            playerVsPlayerExternalEvent.action
        )

        notificationService.handleInGameEvents(challengeCompletedEvent)
    }
}