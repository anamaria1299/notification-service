package helios.io.notificationsystem.domain.ingame

import helios.io.notificationsystem.domain.NotificationHandler

data class ChallengeCompletedEvent (
    override val userId: Int,
    private val challenge: String
): InGameEvent(userId) {

    override fun processEvent(inGameNotificationHandler: NotificationHandler) {
        val message = "You've completed the challenge $challenge!"
        inGameNotificationHandler.notify(message)
    }
}