import scala.util.Random

case class Deck(cards: List[Card]) {
  def shuffle(): Deck = Deck(Random.shuffle(cards))

  def playTheTopCard(): (Option[Card], Deck) = {
    //tail throws if list is empty, but drop(1) returns an empty list
    (cards.headOption, Deck(cards.drop(1)))
  }

  def combine(otherDeck: Deck): Deck = {
    Deck(cards ++: otherDeck.cards)
  }

  def addCard(card: Card): Deck = {
    Deck(card :: cards)
  }
}

case object Deck {
  def empty(): Deck = Deck(List())

  def createFullDecks(numberOfDecks: Int): Deck = {
    (1 to numberOfDecks)
      .foldLeft(empty())((currentDeck, _) => currentDeck.combine(createOneFullDeck()))

  }
  def createOneFullDeck(): Deck = {
    val cards = CardSuit.values.unsorted.flatMap { suit =>
      CardValue.values.unsorted.map { value =>
        Card(suit, value)
      }
    }
    Deck(cards.toList)
  }
}