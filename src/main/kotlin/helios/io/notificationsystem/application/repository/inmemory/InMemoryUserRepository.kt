package helios.io.notificationsystem.application.repository.inmemory

import helios.io.notificationsystem.domain.user.EventType
import helios.io.notificationsystem.domain.user.Preference
import helios.io.notificationsystem.domain.user.User
import helios.io.notificationsystem.domain.user.UserRepository

class InMemoryUserRepository() : UserRepository {

    private val users = mutableMapOf<Int, User>()

    init {

        val inGamePreference = Preference()
        inGamePreference.enableEventNotification(EventType.IN_GAME)
        val userInGameAllowed = User(1, inGamePreference)
        addUser(userInGameAllowed)


        val socialPreference = Preference()
        socialPreference.enableEventNotification(EventType.SOCIAL)
        val userSocialAllowed = User(2, socialPreference)
        addUser(userSocialAllowed)

        val bothPreference = Preference()
        bothPreference.enableEventNotification(EventType.SOCIAL)
        bothPreference.enableEventNotification(EventType.IN_GAME)
        val userBothAllowed = User(3, bothPreference)
        addUser(userBothAllowed)

        val userWithoutNotifications = User(4, Preference())
        addUser(userWithoutNotifications)
    }


    override fun getUserById(id: Int): User? {
        return users[id]
    }

    override fun addUser(user: User) {
        users[user.getId()] = user
    }
}