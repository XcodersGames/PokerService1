/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xcoders.gameitems;

import java.io.Serializable;

/**
 *
 * @author linux
 */
public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final int WAITING = 0;
    public static final int STARTED = 1;
    public static final int WAIT_SMALLBIND = 2;
    public static final int WAIT_BIGBIND = 3;
    public static final int PRE_FLOP = 4;
    public static final int FLOP = 0;
    public static final int POST_FLOP = 5;
    public static final int TURN= 0;
    public static final int POST_TURN = 6;
    public static final int RIVER = 0;
    public static final int SHOWDOWN = 0;
    
    private Integer dealerId;
    private Integer smallBindId;
    private Integer bigBindId;
    
    private Integer id;
    private Player[] players;
    private CardPack cardPack;
    private Card[] playCards;
    private Integer pot;
    private Integer status;
    private Integer round;
    
    public Table(){
        cardPack = new CardPack();
        players = new Player[6];
        playCards = new Card[5];
        pot = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public CardPack getCardPack() {
        return cardPack;
    }

    public void setCardPack(CardPack cardPack) {
        this.cardPack = cardPack;
    }

    public Card[] getPlayCards() {
        return playCards;
    }

    public void setPlayCards(Card[] playCards) {
        this.playCards = playCards;
    }

    public Integer getPot() {
        return pot;
    }

    public void setPot(Integer pot) {
        this.pot = pot;
    }
    
    public Boolean hasSeats(){
        return getNumberOfPlayers() < 6 ? true : false ;
    }
    
    public Integer getNumberOfPlayers(){
        int x = 0;
        for (Player player : players) {
            if (player != null) {
                x++;
            }
        }
        return x;
    }
    
    public void addPlayer(Player p){
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = p;
                break;
            }
        }        
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getSmallBindId() {
        return smallBindId;
    }

    public void setSmallBindId(Integer smallBindId) {
        this.smallBindId = smallBindId;
    }

    public Integer getBigBindId() {
        return bigBindId;
    }

    public void setBigBindId(Integer bigBindId) {
        this.bigBindId = bigBindId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }
    
    
}
