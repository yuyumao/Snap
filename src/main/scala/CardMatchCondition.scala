sealed trait CardMatchCondition {
  def matching(thisCard: Card, thatCard: Card): Boolean
}

object MatchSuit extends CardMatchCondition {
  override def toString: String = "by Suit"

  def matching(thisCard: Card, thatCard: Card): Boolean = {
    thisCard.suit == thatCard.suit
  }
}

object MatchValue extends CardMatchCondition {
  override def toString: String = "by Value"

  def matching(thisCard: Card, thatCard: Card): Boolean = {
    thisCard.value == thatCard.value
  }
}

object MatchSuitAndValue extends CardMatchCondition {
  override def toString: String = "by Both Suit And Value"

  def matching(thisCard: Card, thatCard: Card): Boolean = {
    thisCard.suit == thatCard.suit && thisCard.value == thatCard.value
  }
}
