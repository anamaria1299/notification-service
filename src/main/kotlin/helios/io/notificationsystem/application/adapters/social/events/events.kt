package helios.io.notificationsystem.application.adapters.social.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class FriendRequestEvent @JsonCreator constructor(
    @JsonProperty("requestedFriend") val requestedFriend: Int,
    @JsonProperty("userId") val userId: Int
)

data class FriendAcceptedEvent @JsonCreator constructor(
    @JsonProperty("friend") val friend: Int,
    @JsonProperty("userId") val userId: Int
)

data class NewFollowerEvent @JsonCreator constructor (
    @JsonProperty("follower") val follower: Int,
    @JsonProperty("userId") val userId: Int
)
