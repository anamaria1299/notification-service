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
        "helios.io.event.in-game.level-up",
        "helios.io.event.in-game.challenge-completed",
        "helios.io.event.in-game.item-acquired",
        "helios.io.event.in-game.player-vs-player",
        "helios.io.notification.in-game"
    ],
    bootstrapServersProperty = "spring.kafka.bootstrap-servers",
    controlledShutdown = true)
class InGameNotificationITests {

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
            subscribe(listOf("helios.io.notification.in-game"))
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
    fun shouldProcessLevelUpEvent() {
        val levelUpEvent = """{"userId": 1, "level": 2}"""
        kafkaTemplate.send("helios.io.event.in-game.level-up", levelUpEvent)
        kafkaTemplate.flush()

        assertEquals("Congratulations! You've reached level 2!", getMessage())
    }

    @Test
    fun shouldProcessChallengeCompletedEvent() {
        val challengeCompletedEvent = """{"userId": 1, "challenge": "Gold challenge"}"""
        kafkaTemplate.send("helios.io.event.in-game.challenge-completed", challengeCompletedEvent)
        kafkaTemplate.flush()

        assertEquals("You've completed the challenge Gold challenge!", getMessage())
    }

    @Test
    fun shouldProcessItemAcquiredEvent() {
        val itemAcquiredEvent = """{"userId": 1, "item": "new"}"""
        kafkaTemplate.send("helios.io.event.in-game.item-acquired", itemAcquiredEvent)
        kafkaTemplate.flush()

        assertEquals("You've acquired the new!", getMessage())
    }

    @Test
    fun shouldProcessPlayerVsPlayerEvent() {
        val playerVsPlayerEvent = """{"userId": 1, "opponent": 2, "action": "attacked"}"""
        kafkaTemplate.send("helios.io.event.in-game.player-vs-player", playerVsPlayerEvent)
        kafkaTemplate.flush()

        assertEquals("Player 2 has attacked you!", getMessage())
    }
}