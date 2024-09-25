package com.strack.live.domain

import java.util.UUID

object strack {
  case class MatchTag(id: UUID, groupId: UUID, modeId: UUID)

  case class PlayerTag(id: UUID, userId: UUID, teamId: UUID, matchId: UUID)

  case class User(id: UUID, name: String)
  
  object MatchTag {

  }

}
