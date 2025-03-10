package helios.io.notificationsystem.domain

interface NotificationHandler {

    fun notify(message: String)
}