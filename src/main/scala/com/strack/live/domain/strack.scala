package com.strack.live.domain

import java.util.UUID

object strack {
  case class MatchTag(id: UUID, groupId: UUID, modeId: UUID)

  case class MatchPreview(id: UUID, modeName: String, teamIdList: List[UUID])

  case class PlayerTag(id: UUID, userId: UUID, userName: String, teamId: UUID, matchId: UUID)

  case class TeamTag(id: UUID, matchId: UUID)

  case class User(id: UUID, name: String)

  case class GroupTag(id: UUID, name: String)

  case class Stat(id: UUID, typeId: UUID, referenceId: UUID, value: Int)

  case class Paragraph(id: UUID, header: String, body: String)

  case class ResumeHeader(id: UUID, title: String, group: String, location: String, date: String)

  case class ResumeItem(id: UUID, headerId: UUID, text: String)
}
