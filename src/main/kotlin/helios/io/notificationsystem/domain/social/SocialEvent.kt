package helios.io.notificationsystem.domain.social

import helios.io.notificationsystem.domain.NotificationHandler

open class SocialEvent  (
    open val userId: Int,
) {
    open fun processEvent(socialNotificationHandler: NotificationHandler) {
    }
}