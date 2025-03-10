package helios.io.notificationsystem.domain.ingame

import helios.io.notificationsystem.domain.NotificationHandler

data class ItemAcquiredEvent (
    override val userId: Int,
    private val item: String
): InGameEvent(userId) {

    override fun processEvent(inGameNotificationHandler: NotificationHandler) {
        val message = "You've acquired the $item!"
        inGameNotificationHandler.notify(message)
    }
}