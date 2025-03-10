package helios.io.notificationsystem

import helios.io.notificationsystem.domain.NotificationHandler
import helios.io.notificationsystem.domain.NotificationService
import helios.io.notificationsystem.domain.ingame.LevelUpEvent
import helios.io.notificationsystem.domain.social.FriendRequestEvent
import helios.io.notificationsystem.domain.user.UserNotEnableNotificationException
import helios.io.notificationsystem.domain.user.UserRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals


@ExtendWith(SpringExtension::class)
@SpringBootTest
class NotificationTest {

    @Autowired
    private lateinit var inGameNotificationHandler: NotificationHandler
    @Autowired
    private lateinit var socialNotificationHandler: NotificationHandler
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun shouldNotProcessInGameEventsIfNotAllowed() {
        val notificationService = NotificationService(inGameNotificationHandler, socialNotificationHandler, userRepository)
        //user 2 has not enabled in game notifications
        val exception = assertThrows<UserNotEnableNotificationException> {
            notificationService.handleInGameEvents(LevelUpEvent(2, 4))
        }

        assertEquals("User [2] does not allow [IN_GAME] event notifications.", exception.message)
    }

    @Test
    fun shouldNotProcessSocialEventsIfNotAllowed() {
        val notificationService = NotificationService(inGameNotificationHandler, socialNotificationHandler, userRepository)
        //user 1 has not enabled social notifications
        val exception = assertThrows<UserNotEnableNotificationException> {
            notificationService.handleSocialEvents(FriendRequestEvent(1, 4))
        }

        assertEquals("User [1] does not allow [SOCIAL] event notifications.", exception.message)
    }
}