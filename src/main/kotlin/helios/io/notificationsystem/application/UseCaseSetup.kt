package helios.io.notificationsystem.application

import com.fasterxml.jackson.databind.ObjectMapper
import helios.io.notificationsystem.application.adapters.ingame.InGameKafkaNotification
import helios.io.notificationsystem.application.adapters.social.SocialKafkaNotification
import helios.io.notificationsystem.application.repository.inmemory.InMemoryUserRepository
import helios.io.notificationsystem.domain.NotificationService
import helios.io.notificationsystem.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate

@Configuration
class UseCaseSetup {

    @Bean
    fun inGameNotificationHandler(
        kafkaTemplate: KafkaTemplate<String, String>,
        @Value("\${spring.kafka.in-game-notifier-topic}") kafkaTopic: String,
    ): InGameKafkaNotification = InGameKafkaNotification(kafkaTemplate, kafkaTopic)

    @Bean
    fun socialNotificationHandler(
        kafkaTemplate: KafkaTemplate<String, String>,
        @Value("\${spring.kafka.social-notifier-topic}") kafkaTopic: String,
    ): SocialKafkaNotification = SocialKafkaNotification(kafkaTemplate, kafkaTopic)

    @Bean
    fun userRepository(): UserRepository = InMemoryUserRepository()

    @Bean
    fun notificationService(
        inGameNotificationHandler: InGameKafkaNotification,
        socialNotificationHandler: SocialKafkaNotification,
        userRepository: UserRepository,
    ) = NotificationService(inGameNotificationHandler, socialNotificationHandler, userRepository)

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
}