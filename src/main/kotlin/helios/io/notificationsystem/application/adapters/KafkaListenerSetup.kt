package helios.io.notificationsystem.application.adapters

import helios.io.notificationsystem.domain.user.UserNotEnableNotificationException
import helios.io.notificationsystem.domain.user.UserNotFoundException
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.util.backoff.FixedBackOff

@EnableKafka
@Configuration
class KafkaListenerSetup {

    private val logger: Logger = LoggerFactory.getLogger(KafkaListenerSetup::class.java)

    @Bean
    fun consumerFactory(
        @Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
    ): ConsumerFactory<String, String> {
        val configs =
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            )
        return DefaultKafkaConsumerFactory(configs)
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        defaultErrorHandler: DefaultErrorHandler
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.consumerFactory = consumerFactory
        factory.containerProperties.ackMode = ContainerProperties.AckMode.RECORD
        factory.setCommonErrorHandler(defaultErrorHandler)

        return factory
    }

    @Bean
    fun defaultErrorHandler(
        @Value("\${spring.kafka.retry-interval-millis}") backOffIntervalInMillis: Long,
        @Value("\${spring.kafka.max-retries}") backOffMaxRetries: Long,
    ): DefaultErrorHandler {
        val fixedBackOff = FixedBackOff(backOffIntervalInMillis, backOffMaxRetries)
        val errorHandler =
            object: DefaultErrorHandler(
                {record, e -> logger.info("Recovered message [${record.value()}]. Reason ${e.message}", e)},
                fixedBackOff
            ) {
                override fun handleRemaining(
                    thrownException: Exception,
                    records: MutableList<ConsumerRecord<*, *>>,
                    consumer: Consumer<*, *>,
                    container: MessageListenerContainer,
                ) {
                    val recordsMessages = records.map { it.value() }
                    logger().error(
                        thrownException,
                        "Unable to process messages $recordsMessages. Message will be retried " +
                                "if possible.\n Reason: ${thrownException.message}",
                    )
                    super.handleRemaining(thrownException, records, consumer, container)
                }
            }
        errorHandler.defaultFalse()
        errorHandler.addNotRetryableExceptions(UserNotEnableNotificationException::class.java)
        errorHandler.addNotRetryableExceptions(UserNotFoundException::class.java)

        return errorHandler
    }

}