package CENG453.group18.entity;


import CENG453.group18.enums.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cardID;
    @Column(name = "card_type")
    private CardType cardType;
    @Column(name = "count")
    private  Integer cardCount;

    public Card(CardType cardType)
    {
        this.cardType = cardType;
        cardCount = 0;
    }
    public void incrementCardCount(int count)
    {
        cardCount += count;
    }
    public void decrementCardCount(int count)
    {
        cardCount -= count;
    }
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Card card)
        {
            return this.cardType.equals(card.getCardType());
        }
        return false;
    }
}
