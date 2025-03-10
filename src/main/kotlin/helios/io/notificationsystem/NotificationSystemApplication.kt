package helios.io.notificationsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class NotificationSystemApplication

fun main(args: Array<String>) {
    runApplication<NotificationSystemApplication>(*args)
    print("Running notification app")
}
