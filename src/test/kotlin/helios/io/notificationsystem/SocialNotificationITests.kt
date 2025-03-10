package helios.io.notificationsystem

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Duration
import kotlin.test.assertEquals


@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
    partitions = 1,
    topics = [
        "helios.io.event.social.friend-request",
        "helios.io.event.social.friend-accepted",
        "helios.io.event.social.new-follower",
        "helios.io.notification.social"
    ],
    bootstrapServersProperty = "spring.kafka.bootstrap-servers",
    controlledShutdown = true)
class SocialNotificationITests {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var embeddedKafka: EmbeddedKafkaBroker

    private lateinit var consumer: Consumer<String, String>

    fun createConsumer(): Consumer<String, String> {
        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to embeddedKafka.brokersAsString,
                ConsumerConfig.GROUP_ID_CONFIG to "test-group",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            )
        )
        return consumerFactory.createConsumer().apply {
            subscribe(listOf("helios.io.notification.social"))
        }
    }

    @BeforeEach
    fun setUp() {
        consumer = createConsumer()
    }

    @AfterEach
    fun tearDown() {
        consumer.close()
    }

    private fun getMessage(): String {
        val records = consumer.poll(Duration.ofSeconds(10))
        return records.firstOrNull()?.value() ?: ""
    }

    @Test
    fun shouldProcessFriendRequestEvent() {
        val friendRequestedEvent = """{"userId": 2, "requestedFriend": 4}"""
        kafkaTemplate.send("helios.io.event.social.friend-request", friendRequestedEvent)
        kafkaTemplate.flush()

        assertEquals("Player 4 has sent you a friend request.", getMessage())
    }

    @Test
    fun shouldProcessFriendAcceptedEvent() {
        val friendAcceptedEvent = """{"userId": 2, "friend": 4}"""
        kafkaTemplate.send("helios.io.event.social.friend-accepted", friendAcceptedEvent)
        kafkaTemplate.flush()

        assertEquals("Player 4 has accepted your friend request.", getMessage())
    }

    @Test
    fun shouldProcessNewFollowerEvent() {
        val newFollowerEvent = """{"userId": 2, "follower": 3}"""
        kafkaTemplate.send("helios.io.event.social.new-follower", newFollowerEvent)
        kafkaTemplate.flush()

        assertEquals("Player 3 start follow you.", getMessage())
    }
}