package CENG453.group18.entity;

import java.util.Map;

import CENG453.group18.enums.CardType;
import lombok.Getter;

@Getter
public class TradeOffer {
    private static int idCounter = 0;
    private int tradeOfferID;
    private int playerNo;
    private Map<CardType, Integer> offered;
    private Map<CardType, Integer> requested;

    public TradeOffer(int playerNo, Map<CardType, Integer> offered, Map<CardType, Integer> requested) {
        this.tradeOfferID = idCounter++;
        this.playerNo = playerNo;
        this.offered = offered;
        this.requested = requested;
    }

    public int getTradeOfferID() {
        return tradeOfferID;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public Map<CardType, Integer> getOffered() {
        return offered;
    }

    public Map<CardType, Integer> getRequested() {
        return requested;
    }

}
