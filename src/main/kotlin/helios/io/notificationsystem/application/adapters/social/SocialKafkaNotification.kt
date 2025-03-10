package helios.io.notificationsystem.application.adapters.social

import helios.io.notificationsystem.domain.NotificationHandler
import org.springframework.kafka.core.KafkaTemplate

class SocialKafkaNotification(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val socialTopic: String
) : NotificationHandler {

    override fun notify(message: String) {
        this.kafkaTemplate.send(socialTopic, message)
    }
}