package com.strack.live.domain

import java.util.UUID

object strack {
  case class MatchTag(id: UUID, groupId: UUID, modeId: UUID)

  case class MatchPreview(id: UUID, modeName: String, teamIdList: List[UUID])

  case class PlayerTag(id: UUID, userId: UUID, teamId: UUID, matchId: UUID)

  case class TeamTag(id: UUID, matchId: UUID)

  case class User(id: UUID, name: String)

  case class GroupTag(id: UUID, name: String)

  object MatchTag {

  }

}
