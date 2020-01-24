sealed trait Message

//Board ask user to play a card
final case class StartNextRound() extends Message

//Player play a card from their own face down deck to the board
final case class PlayACard(id: String, card: Card) extends Message

//Board reveal the card to both players
final case class PlayedACard(card: Card) extends Message

//Player confirm he has received the new card been played on the board, and Snap!!!
final case class SnapShout(id: String) extends Message

//Player confirm he has received the new card been played on the board, and no snap
final case class ConfirmReceived(id: String) extends Message

//One player is out of card, game end
final case class EmptyHand(id: String) extends Message