package helios.io.notificationsystem.application.adapters.ingame

import helios.io.notificationsystem.domain.NotificationHandler
import org.springframework.kafka.core.KafkaTemplate

class InGameKafkaNotification(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val inGameTopic: String
) : NotificationHandler {

    override fun notify(message: String) {
        this.kafkaTemplate.send(inGameTopic, message)
    }
}