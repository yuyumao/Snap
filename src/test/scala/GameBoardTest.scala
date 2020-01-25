import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import CardSuit._
import CardValue._

class GameBoardTest extends AnyFreeSpec with Matchers {

  "When any player play a card, it should be reveal to both players" - {
    val gameBoard = GameBoard(MatchSuit)
    val card = Card(Clubs, Two)
    val messages = gameBoard.playACard(card)

    messages.size shouldBe 2
    messages should contain(SendMessageRequest(PlayerId.PlayerA, PlayedACard(card)))
    messages should contain(SendMessageRequest(PlayerId.PlayerB, PlayedACard(card)))
  }

  "Start next round should always start with PlayerA, and then alternates" - {
    val gameBoard = GameBoard(MatchSuit)
    val message1 = gameBoard.startNextRound()
    message1 should contain theSameElementsAs(List(SendMessageRequest(PlayerId.PlayerA, StartNextRound())))

    val message2 = gameBoard.startNextRound()
    message2 should contain theSameElementsAs(List(SendMessageRequest(PlayerId.PlayerB, StartNextRound())))

    val message3 = gameBoard.startNextRound()
    message3 should contain theSameElementsAs(List(SendMessageRequest(PlayerId.PlayerA, StartNextRound())))
  }

  "Confirm received should only ask user to start next round after both player has responded" - {
    val gameBoard = GameBoard(MatchSuit)
    val message1 = gameBoard.confirmReceived(PlayerId.PlayerA)
    message1 shouldBe empty

    val message2 = gameBoard.confirmReceived(PlayerId.PlayerB)
    message2 should contain theSameElementsAs(List(SendMessageRequest(PlayerId.PlayerA, StartNextRound())))

    val message3 = gameBoard.confirmReceived(PlayerId.PlayerA)
    message3 shouldBe empty
  }
}
