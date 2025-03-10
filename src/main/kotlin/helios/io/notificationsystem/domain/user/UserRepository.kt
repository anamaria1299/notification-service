package helios.io.notificationsystem.domain.user

interface UserRepository {

    fun getUserById(id: Int): User?

    fun addUser(user: User)
}