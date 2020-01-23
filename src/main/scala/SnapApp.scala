import scala.util.Try

object SnapApp extends App {
  println("Snap Starting")

  println("Please enter the total number of decks")
  val totalNumberOfDecks = userInputANumber()
  println(s"Total number of cards for this game is $totalNumberOfDecks")

  val cardMatchCondition = userInputCardMatchCondition()
  println(s"Cards will be matched $cardMatchCondition")

  val gameBoard = GameBoard(Deck.createFullDecks(totalNumberOfDecks), cardMatchCondition)
  

  def userInputANumber(): Int = {
    Try(scala.io.StdIn.readLine().toInt).getOrElse {
      println("Please enter a valid number")
      userInputANumber()
    }
  }

  @scala.annotation.tailrec
  def userInputCardMatchCondition(): CardMatchCondition = {
    println("How would you like two card to be matched")
    println(s"$MatchSuit - enter 1")
    println(s"$MatchValue - enter 2")
    println(s"$MatchSuitAndValue - enter 3")
    userInputANumber() match {
      case 1 => MatchSuit
      case 2 => MatchValue
      case 3 => MatchSuitAndValue
      case _ =>
        println("Please enter a valid option")
        userInputCardMatchCondition()
    }
  }
}
