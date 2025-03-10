package helios.io.notificationsystem.domain.social

import helios.io.notificationsystem.domain.NotificationHandler

data class NewFollowerEvent(
    override val userId: Int,
    private val follower: Int,
): SocialEvent(userId) {

    override fun processEvent(socialNotificationHandler: NotificationHandler) {
        val message = "Player $follower start follow you."
        socialNotificationHandler.notify(message)
    }
}