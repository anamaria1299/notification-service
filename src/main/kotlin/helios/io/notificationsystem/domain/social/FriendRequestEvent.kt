package helios.io.notificationsystem.domain.social

import helios.io.notificationsystem.domain.NotificationHandler

data class FriendRequestEvent(
    override val userId: Int,
    private val requestedFriend: Int,
): SocialEvent(userId) {

    override fun processEvent(socialNotificationHandler: NotificationHandler) {
        val message = "Player $requestedFriend has sent you a friend request."
        socialNotificationHandler.notify(message)
    }
}