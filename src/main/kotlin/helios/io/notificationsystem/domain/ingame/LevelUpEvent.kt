package helios.io.notificationsystem.domain.ingame

import helios.io.notificationsystem.domain.NotificationHandler

data class LevelUpEvent(
    override val userId: Int,
    private val level: Int
): InGameEvent(userId) {

    override fun processEvent(inGameNotificationHandler: NotificationHandler) {
        val message = "Congratulations! You've reached level $level!"
        inGameNotificationHandler.notify(message)
    }
}