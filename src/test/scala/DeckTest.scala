import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class DeckTest extends AnyFreeSpec with Matchers {

  "Generate a new complete deck" - {
    val deck = Deck.createOneFullDeck()
    deck.cards.size shouldBe(52)
    deck.cards.distinct.size shouldBe(52)
  }

  //shuffle might have a very little chance to end up with the same sequence, but I am not worrying that here - could use scala check for property based testing.
  "Shuffle should give a different sequence" - {
    val deck = Deck.createOneFullDeck()
    val shuffledDeck = deck.shuffle()
    shuffledDeck.cards should contain theSameElementsAs deck.cards
    shuffledDeck.cards should not be equal(deck.cards)
  }

  "Create empty deck" - {
    Deck.empty().cards shouldBe empty
  }

  //could write more test cases for combine, or even prove its a Monoid and adding an empty method, but that's overkill....
  "combine other deck" - {
    val card1 = Card(CardSuit.Clubs, CardValue.One)
    val card2 = Card(CardSuit.Diamonds, CardValue.Ten)
    val deck1 = Deck(List(card1))
    val deck2 = Deck(List(card2))

    val combinedDeck = deck1.combine(deck2)
    combinedDeck.cards should contain theSameElementsAs List(card1, card2)
  }

  "Create n full decks" - {
    val deck = Deck.createFullDecks(5)
    deck.cards.size shouldBe(52 * 5)
    deck.cards.foreach { card =>
      deck.cards.count(_ == card) shouldBe 5
    }
  }

  "add a card" - {
    val card1 = Card(CardSuit.Clubs, CardValue.One)
    val card2 = Card(CardSuit.Diamonds, CardValue.Ten)
    val deck = Deck(List(card1))

    val newDeck = deck.addCard(card2)
    newDeck.cards should contain theSameElementsAs List(card1, card2)
  }

  "the top two card" - {
    "match the condition" - {
      val card1 = Card(CardSuit.Clubs, CardValue.One)
      val card2 = Card(CardSuit.Diamonds, CardValue.Ten)
      val card3 = Card(CardSuit.Diamonds, CardValue.Seven)
      val matchingCondition = MatchSuit

      val newDeck = Deck.empty().addCard(card1)
        .addCard(card2)
        .addCard(card3)

      newDeck.topTwoCardsAreTheSame(matchingCondition) shouldBe true
    }

    "don't match the condition" - {
      val card1 = Card(CardSuit.Clubs, CardValue.One)
      val card2 = Card(CardSuit.Diamonds, CardValue.Ten)
      val card3 = Card(CardSuit.Diamonds, CardValue.Seven)
      val matchingCondition = MatchSuit

      val newDeck = Deck.empty().addCard(card3)
        .addCard(card2)
        .addCard(card1)

      newDeck.topTwoCardsAreTheSame(matchingCondition) shouldBe false
    }

    "don't match the condition" - {
      val card1 = Card(CardSuit.Clubs, CardValue.One)
      val card2 = Card(CardSuit.Diamonds, CardValue.Ten)
      val card3 = Card(CardSuit.Diamonds, CardValue.Seven)
      val matchingCondition = MatchSuit

      val newDeck = Deck.empty().addCard(card3)
        .addCard(card2)
        .addCard(card1)

      newDeck.topTwoCardsAreTheSame(matchingCondition) shouldBe false
    }

    "not empty card" - {
      val card1 = Card(CardSuit.Clubs, CardValue.One)
      val matchingCondition = MatchSuit

      val newDeck = Deck.empty().addCard(card1)

      newDeck.topTwoCardsAreTheSame(matchingCondition) shouldBe false
    }
  }

  "Play top card" - {
    "Should return the first card and the remaining deck" - {
      val deck = Deck.createOneFullDeck()
      val (playedCard, newDeck) = deck.playTheTopCard()
      playedCard shouldBe deck.cards.headOption
      newDeck shouldBe Deck(deck.cards.tail)
    }

    "Should return the only card and an empty deck if there's only one card" - {
      val card = Card(CardSuit.Clubs, CardValue.One)
      val deck = Deck(List(card))
      val (playedCard, newDeck) = deck.playTheTopCard()
      playedCard shouldBe Some(card)
      newDeck.cards shouldBe empty
    }

    "Should return None and an empty deck if there's only one card" - {
      val deck = Deck(List())
      val (playedCard, newDeck) = deck.playTheTopCard()
      playedCard shouldBe None
      newDeck.cards shouldBe empty
    }
  }
}