import PlayerId.PlayerId

//Most important class of this project, tracking the global state
case class GameBoard(matchCondition: CardMatchCondition) {

  //`isReady` indicates this player has received the card for the current round, and it's ready for the next round. This is
  // to avoid any race condition between rounds.
  //`winningDeck` stores the cards it won so far
  case class PlayerState(isReady: Boolean, winningDeck: Deck) {
    def becomeReady(): PlayerState = this.copy(isReady = true)
    def becomeNotReady(): PlayerState = this.copy(isReady = false)
    def addToWinningDeck(deck: Deck): PlayerState = this.copy(winningDeck = winningDeck.combine(deck))
  }

  private var currentFaceUpDeckOnBoard = Deck.empty()
  private var playerAState = PlayerState(isReady = false, Deck.empty())
  private var playerBState = PlayerState(isReady = false, Deck.empty())
  private var round = 0

  def snapShout(id: PlayerId): List[SendMessageRequest] = {
    if (currentFaceUpDeckOnBoard.topTwoCardsAreTheSame(matchCondition)) {
      println(s"Player $id is winning ${currentFaceUpDeckOnBoard.size} cards from current round")
      calculateNewState(id)(state => state.becomeReady().addToWinningDeck(currentFaceUpDeckOnBoard))
      currentFaceUpDeckOnBoard = Deck.empty()
      startNextRoundIfBothPlayersAreReady()
    } else if (currentFaceUpDeckOnBoard.cards.isEmpty){
      println(s"Player $id called snap, but too late")
      playerBecomeReady(id)
      startNextRoundIfBothPlayersAreReady()
    } else {
      println("Error: Top two cards do not match, player cheating (some race condition)")
      List()
    }
  }

  def emptyHand(): List[SendMessageRequest] = {
    val sizeA = playerAState.winningDeck.size
    val sizeB = playerBState.winningDeck.size
    println(s"Winning deck size for Player A is $sizeA")
    println(s"Winning deck size for Player B is $sizeB")
    if (sizeA > sizeB) {
      println(s"Player ${PlayerId.PlayerA} is winning !!! ")
    } else if (sizeA < sizeB) {
      println(s"Player ${PlayerId.PlayerB} is winning !!! ")
    } else {
      println(s"We have two Winners!!!")
    }
    List()
  }

  def playACard(c: Card): List[SendMessageRequest] = {
    addCardToCurrentFaceUpDeck(c)
    //PlayerA will still have some slight advantage here, but making it fairer is too difficult.
    List(SendMessageRequest(PlayerId.PlayerA, PlayedACard(c)), SendMessageRequest(PlayerId.PlayerB, PlayedACard(c)))
  }

  def startNextRound(): List[SendMessageRequest] = {
    round += 1
    if (round % 2 != 0) {
      List(SendMessageRequest(PlayerId.PlayerA, StartNextRound()))
    } else {
      List(SendMessageRequest(PlayerId.PlayerB, StartNextRound()))
    }
  }

  def confirmReceived(id: PlayerId): List[SendMessageRequest] = {
    playerBecomeReady(id)
    startNextRoundIfBothPlayersAreReady()
  }

  private def startNextRoundIfBothPlayersAreReady(): List[SendMessageRequest] = {
    if (playerAState.isReady && playerBState.isReady) {
      playerAState = playerAState.becomeNotReady()
      playerBState = playerBState.becomeNotReady()
      startNextRound()
    } else {
      List()
    }
  }

  private def addCardToCurrentFaceUpDeck(card: Card): Unit = {
    currentFaceUpDeckOnBoard = currentFaceUpDeckOnBoard.addCard(card)
  }

  private def playerBecomeReady(id: PlayerId): Unit = {
    calculateNewState(id)(state => state.becomeReady())
  }

  private def calculateNewState(id: PlayerId)(toNewState: PlayerState => PlayerState): Unit = {
    id match {
      case PlayerId.PlayerA => playerAState = toNewState(playerAState)
      case PlayerId.PlayerB => playerBState = toNewState(playerBState)
    }
  }
}