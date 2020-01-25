import PlayerId.PlayerId
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

//Coordinating both players
case class GameBoardActor(deck: Deck, matchCondition: CardMatchCondition, system: ActorSystem) extends Actor {

  //shuffle the deck, then evenly dealt cards to players
  private val (deckA, deckB) = deck.shuffle().cards.zipWithIndex.partition{ case(_, i) => i % 2 == 0 }
  private val playerAActor: ActorRef = system.actorOf(Props(Player(PlayerId.PlayerA, Deck(deckA.map(_._1)), matchCondition: CardMatchCondition)))
  private val playerBActor: ActorRef = system.actorOf(Props(Player(PlayerId.PlayerB, Deck(deckB.map(_._1)), matchCondition: CardMatchCondition)))
  private val gameBoard = GameBoard(matchCondition)

  sendMessageToPlayer(gameBoard.startNextRound())
  println("Game started with player A")

  override def receive: Receive = {
    case PlayACard(id, c) =>
      println(s"Player $id played $c")
      sendMessageToPlayer(gameBoard.playACard(c))
    case ConfirmReceived(id) =>
      sendMessageToPlayer(gameBoard.confirmReceived(id))
    case SnapShout(id) =>
      println(s"Player $id called snap")
      sendMessageToPlayer(gameBoard.snapShout(id))
    case EmptyHand(id) =>
      println(s"Player $id's deck is empty")
      sendMessageToPlayer(gameBoard.emptyHand())
  }

  private def sendMessageToPlayer(request: List[SendMessageRequest]): Unit = {
    request.foreach { r =>
      r.playerId match {
        case PlayerId.PlayerA => playerAActor ! r.message
        case PlayerId.PlayerB => playerBActor ! r.message
      }
    }
  }
}

case class SendMessageRequest(playerId: PlayerId, message: Message)