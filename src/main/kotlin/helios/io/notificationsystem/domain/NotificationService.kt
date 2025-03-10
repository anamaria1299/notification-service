package helios.io.notificationsystem.domain

import helios.io.notificationsystem.domain.ingame.InGameEvent
import helios.io.notificationsystem.domain.social.SocialEvent
import helios.io.notificationsystem.domain.user.*

open class NotificationService(
    private val inGameNotificationHandler: NotificationHandler,
    private val socialNotificationHandler: NotificationHandler,
    private val userRepository: UserRepository
) {
    fun handleInGameEvents(event: InGameEvent) {

        val userId = event.userId
        verifyEnableNotification(userId, EventType.IN_GAME)

        event.processEvent(inGameNotificationHandler)
    }

    fun handleSocialEvents(event: SocialEvent) {

        val userId = event.userId
        verifyEnableNotification(userId, EventType.SOCIAL)

        event.processEvent(socialNotificationHandler)
    }

    private fun verifyEnableNotification(userId: Int, eventType: EventType) {

        val user = userRepository.getUserById(userId) ?: throw UserNotFoundException("User [$userId] not found")
        val userPreferences = user.getPreferences()

        if (!userPreferences.allowEventNotification(eventType)) {
            throw UserNotEnableNotificationException("User [$userId] does not allow [${eventType.name}] event notifications.")
        }
    }
}