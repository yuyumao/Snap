import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import CardValue._
import CardSuit._

class CardMatchConditionTest extends AnyFreeSpec with Matchers {

  "Condition MatchSuit" - {
    "should match if suit is same" - {
      val card1 = Card(Clubs, One)
      val card2 = Card(Clubs, Two)

      MatchSuit.matching(card1, card2) shouldBe true
    }

    "should not match if only value is same" - {
      val card1 = Card(Clubs, One)
      val card2 = Card(Diamonds, One)

      MatchSuit.matching(card1, card2) shouldBe false
    }
  }

  "Condition MatchValue" - {
    "should match if value is same" - {
      val card1 = Card(Clubs, One)
      val card2 = Card(Diamonds, One)

      MatchValue.matching(card1, card2) shouldBe true
    }

    "should not match if only suit is same" - {
      val card1 = Card(Clubs, One)
      val card2 = Card(Clubs, Two)

      MatchValue.matching(card1, card2) shouldBe false
    }
  }

  "Condition MatchSuitAndValue" - {
    "should match if both value and suit are same" - {
      val card1 = Card(Clubs, One)
      val card2 = Card(Clubs, One)

      MatchSuitAndValue.matching(card1, card2) shouldBe true
    }

    "should not match if only suit is same" - {
      val card1 = Card(Clubs, One)
      val card2 = Card(Clubs, Two)

      MatchSuitAndValue.matching(card1, card2) shouldBe false
    }

    "should not match if only value is same" - {
      val card1 = Card(Clubs, One)
      val card2 = Card(Diamonds, One)

      MatchSuitAndValue.matching(card1, card2) shouldBe false
    }
  }
}