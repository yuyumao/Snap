import akka.actor.Actor

case class Player(id: String, private var deck: Deck, matchCondition: CardMatchCondition) extends Actor {
  var lastCardOnBoard: Option[Card] = None
  override def receive: Receive = {
    case StartNextRound =>
      val (message, newDeck) = playACard()
      sender ! message
      deck = newDeck
    case PlayedACard(playedCard) =>
      if (lastCardOnBoard.exists(c => matchCondition.matching(c, playedCard))) {
        sender ! SnapShout(id)
        lastCardOnBoard = None
      } else {
        sender() ! ConfirmReceived(id)
        //I want it be to fair to the other player to have a chance to call Snap,
        //therefore I only update the lastCardOnBoard here rather than under `StartNextRound`
        lastCardOnBoard = Some(playedCard)
      }
  }
  private def playACard(): (Message, Deck) = {
    val (maybeCard, nextDeck) = deck.playTheTopCard()
    maybeCard.map { card =>
      (PlayACard(id, card), nextDeck)
    }.getOrElse((EmptyHand(id), deck))
  }
}
