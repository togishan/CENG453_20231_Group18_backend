package CENG453.group18.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CENG453.group18.enums.CardType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlayerCardDeck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deckID;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    public void incrementResourceCounts(CardType cardType, int count)
    {
        for (Card card : cards) {
            if (cardType == card.getCardType()) {
                card.incrementCardCount(count);
            }

        }

    }

    public void decrementResourceCounts(CardType cardType, int count)
    {
        for (Card card : cards) {
            if (cardType == card.getCardType()) {
                card.decrementCardCount(count);
            }
        }
    }
    public void addCard(Card card) {
        this.cards.add(card);
    }

    public Map<CardType, Integer> getResourceCounts() {
        Map<CardType, Integer> resourceCounts = new HashMap<>();

        for (Card card : cards) {
            resourceCounts.put(card.getCardType(), card.getCardCount());
        }

        return resourceCounts;
    }
}