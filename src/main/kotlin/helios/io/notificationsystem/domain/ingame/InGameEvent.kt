package helios.io.notificationsystem.domain.ingame

import helios.io.notificationsystem.domain.NotificationHandler

open class InGameEvent (
    open val userId: Int,
) {
    open fun processEvent(inGameNotificationHandler: NotificationHandler) {
    }
}
