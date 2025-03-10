package helios.io.notificationsystem.application.adapters

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.*

@Configuration
class KafkaNotifierSetup {

    @Bean
    fun kafkaProducerFactory(
        @Value("\${spring.kafka.bootstrap-servers}") bootstrapServer: String,
    ): ProducerFactory<String, String>? {
        val configs =
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            )
        return DefaultKafkaProducerFactory(configs)
    }

    @Bean
    fun kafkaTemplate(kafkaProducerFactory: ProducerFactory<String, String>): KafkaTemplate<String, String> =
        KafkaTemplate(kafkaProducerFactory)

}