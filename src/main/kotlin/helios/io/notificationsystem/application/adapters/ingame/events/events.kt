package helios.io.notificationsystem.application.adapters.ingame.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class ChallengeCompletedEvent @JsonCreator constructor(
    @JsonProperty("userId") val userId: Int,
    @JsonProperty("challenge") val challenge: String
)

data class LevelUpEvent @JsonCreator constructor(
    @JsonProperty("userId") val userId: Int,
    @JsonProperty("level") val level: Int
)

data class ItemAcquiredEvent @JsonCreator constructor (
    @JsonProperty("userId") val userId: Int,
    @JsonProperty("item") val item: String
)

data class PlayerVsPlayerEvent (
    @JsonProperty("userId") val userId: Int,
    @JsonProperty("opponent") val opponent: Int,
    @JsonProperty("action") val action: String
)