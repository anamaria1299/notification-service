package helios.io.notificationsystem.domain.social

import helios.io.notificationsystem.domain.NotificationHandler

data class FriendAcceptedEvent(
    override val userId: Int,
    private val friend: Int,
): SocialEvent(userId) {

    override fun processEvent(socialNotificationHandler: NotificationHandler) {
        val message = "Player $friend has accepted your friend request."
        socialNotificationHandler.notify(message)
    }
}