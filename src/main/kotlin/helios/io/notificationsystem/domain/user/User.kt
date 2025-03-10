package helios.io.notificationsystem.domain.user

data class User(
    private val id: Int,
    private val preferences: Preference
) {
    fun getId(): Int = id
    fun getPreferences(): Preference = preferences
}

class Preference {
    private val enabledEventNotifications: HashSet<EventType> = hashSetOf()

    fun enableEventNotification(eventType: EventType) {
        enabledEventNotifications.add(eventType)
    }

    fun allowEventNotification(eventType: EventType): Boolean {
        return enabledEventNotifications.contains(eventType)
    }

    override fun toString(): String {
        return "Preference(enabledEventNotifications=${enabledEventNotifications.joinToString(", ")})"
    }
}

enum class EventType {
    IN_GAME,
    SOCIAL
}