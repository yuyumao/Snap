import PlayerId.PlayerId
import akka.actor.Actor

case class Player(id: PlayerId, startingDeck: Deck, matchCondition: CardMatchCondition) extends Actor {
  override def receive: Receive = onMessage(startingDeck, None)

  private def playACard(currentDeck: Deck): (Message, Deck) = {
    val (maybeCard, nextDeck) = currentDeck.playTheTopCard()
    maybeCard.map { card =>
      (PlayACard(id, card), nextDeck)
    }.getOrElse((EmptyHand(id), currentDeck))
  }

  private def onMessage(currentDeck: Deck, lastCardOnBoard: Option[Card]): Receive = {
    case StartNextRound() =>
      val (message, newDeck) = playACard(currentDeck)
      sender ! message
      context.become(onMessage(newDeck, lastCardOnBoard))
    case PlayedACard(playedCard) =>
      if (lastCardOnBoard.exists(c => matchCondition.matching(c, playedCard))) {
        sender ! SnapShout(id)
        context.become(onMessage(currentDeck, None))
      } else {
        sender() ! ConfirmReceived(id)
        //I want it be to fair to the other player to have a chance to call Snap,
        //therefore I only update the lastCardOnBoard here rather than under `StartNextRound`
        context.become(onMessage(currentDeck, Some(playedCard)))
      }
  }
}
