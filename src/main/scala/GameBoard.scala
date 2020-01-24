import akka.actor.{Actor, ActorRef, ActorSystem, Props}

case class GameBoard(deck: Deck, matchCondition: CardMatchCondition, system: ActorSystem) extends Actor {

  type PlayerId = String
  private val playerAId = "A"
  private val playerBId = "B"
  private val (deckA, deckB) = deck.shuffle().cards.zipWithIndex.partition{ case(_, i) => i % 2 == 0 }
  private val playerAActor: ActorRef = system.actorOf(Props(Player(playerAId, Deck(deckA.map(_._1)), matchCondition: CardMatchCondition)))
  private val playerBActor = system.actorOf(Props(Player(playerBId, Deck(deckB.map(_._1)), matchCondition: CardMatchCondition)))

  case class PlayerState(isReady: Boolean, winningDeck: Deck) {
    def becomeReady(): PlayerState = this.copy(isReady = true)
    def becomeNotReady(): PlayerState = this.copy(isReady = false)
    def addToWinningDeck(deck: Deck): PlayerState = this.copy(winningDeck = winningDeck.combine(deck))
  }

  private var currentFaceUpDeckOnBoard = Deck.empty()
  private var playerAState = PlayerState(isReady = false, Deck.empty())
  private var playerBState = PlayerState(isReady = false, Deck.empty())
  private var round = 0

  startNextRound()
  println("Game started with player A")

  override def receive: Receive = {
    case PlayACard(id, c) =>
      val message = PlayedACard(c)
      println(s"Player $id played $c")
      currentFaceUpDeckOnBoard = currentFaceUpDeckOnBoard.addCard(c)
      //PlayerA will still have some advantage here, but making it fai  erer is too difficult.
      playerAActor ! message
      playerBActor ! message
    case ConfirmReceived(id) =>
      updatePlayerState(id)
      validateStateAndStartNextRound()
    case SnapShout(id) =>
      println(s"Player $id called snap")
      if (currentFaceUpDeckOnBoard.topTwoCardsAreTheSame(matchCondition)) {
        println(s"Player $id is winning ${currentFaceUpDeckOnBoard.size} cards from current round")
        calculateNewState(id)(state => state.becomeReady().addToWinningDeck(currentFaceUpDeckOnBoard))
        validateStateAndStartNextRound()
        currentFaceUpDeckOnBoard = Deck.empty()
      } else if (currentFaceUpDeckOnBoard.cards.isEmpty){
        println(s"Player $id called snap, but too late")
        calculateNewState(id)(state => state.becomeReady())
        validateStateAndStartNextRound()
      } else {
        println("Error: Top two cards do not match, player cheating (some race condition)")
      }
    case EmptyHand(id) =>
      println(s"Player $id's deck is empty")
      val sizeA = playerAState.winningDeck.size
      val sizeB = playerBState.winningDeck.size
      println(s"Winning deck size for Player A is $sizeA")
      println(s"Winning deck size for Player B is $sizeB")
      if (sizeA > sizeB) {
        println(s"Player $playerAId is winning !!! ")
      } else if (sizeA < sizeB) {
        println(s"Player $playerBId is winning !!! ")
      } else {
        println(s"We have two Winners!!!")
      }
  }

  private def calculateNewState(id: String)(calculateNewState: PlayerState => PlayerState): Unit = {
    if (id == playerAId) {
      playerAState = calculateNewState(playerAState)
    } else {
      playerBState = calculateNewState(playerBState)
    }
  }

  private def startNextRound(): Unit = {
    if (round % 2 == 0) {
      playerAActor ! StartNextRound
    } else {
      playerBActor ! StartNextRound
    }
    round += 1
  }

  private def validateStateAndStartNextRound(): Unit = {
    if (playerAState.isReady && playerBState.isReady) {
      playerAState = playerAState.becomeNotReady()
      playerBState = playerBState.becomeNotReady()
      startNextRound()
    }
  }

  private def updatePlayerState(id: String): Unit = {
    calculateNewState(id)(state => state.becomeReady())
  }
}