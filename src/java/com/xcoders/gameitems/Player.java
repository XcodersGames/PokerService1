package com.xcoders.gameitems;

import java.io.Serializable;


public class Player implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public static final int WAITING = 0;
    public static final int INACTIVE = 1;
    public static final int ACTIVE = 2;
    public static final int FOLDED = 3;
    public static final int ALL_IN = 4;
    
    private Long id;
    private String name;
    private Card[] cards;
    private Integer money;
    private Integer bet;
    private Integer status;
    private Boolean hasDiscardedThirdCard = false;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getBet() {
        return bet;
    }

    public void setBet(Integer bet) {
        this.bet = bet;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean isHasDiscardedThirdCard() {
        return hasDiscardedThirdCard;
    }

    public void setHasDiscardedThirdCard(Boolean hasDiscardedThirdCard) {
        this.hasDiscardedThirdCard = hasDiscardedThirdCard;
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", name=" + name + ", status=" + status + '}';
    }
    
    
    
}
