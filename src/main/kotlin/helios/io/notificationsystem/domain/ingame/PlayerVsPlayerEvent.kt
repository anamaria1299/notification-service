package helios.io.notificationsystem.domain.ingame

import helios.io.notificationsystem.domain.NotificationHandler

data class PlayerVsPlayerEvent (
    override val userId: Int,
    private val opponent: Int,
    private val action: String
): InGameEvent(userId) {

    override fun processEvent(inGameNotificationHandler: NotificationHandler) {
        val message = "Player $opponent has $action you!"
        inGameNotificationHandler.notify(message)
    }
}