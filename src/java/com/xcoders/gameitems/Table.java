package com.xcoders.gameitems;

import java.io.Serializable;


public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final int WAITING = 0;
    public static final int STARTED = 1;
    public static final int WAIT_SMALLBIND = 2;
    public static final int WAIT_BIGBIND = 3;
    public static final int PRE_FLOP = 4;
    public static final int FLOP = 0;
    public static final int POST_FLOP = 5;
    public static final int WAIT_CARD_DISCARD = 6;
    public static final int TURN= 0;
    public static final int POST_TURN = 7;
    public static final int RIVER = 0;
    public static final int SHOWDOWN = 8;
    
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
    private Integer maxBet;
    
    public Table(){
        cardPack = new CardPack();
        players = new Player[6];
        playCards = new Card[5];
        pot = 0;
        maxBet = 0;
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

    public Integer getMaxBet() {
        return maxBet;
    }

    public void setMaxBet(Integer maxBet) {
        this.maxBet = maxBet;
    }
    
    public void selectFristPlayerActive(){
        for (Player player : players) {
            if (player == null || !player.getStatus().equals(Player.ACTIVE)) {
                continue;
            }
            player.setStatus(Player.INACTIVE);
        }
        players[0].setStatus(Player.ACTIVE);
    }
    
    public Integer selectNextActivePlayer() {
        int activePlayerIndex = 0;
        
        //find the currently active player and make inactive
        for (int i = 0; i < players.length ; i++) {
            Player p = players[i];
            if (p == null) {
                continue;
            }
            if(p.getStatus().equals(Player.ACTIVE)){
                activePlayerIndex = i;
                p.setStatus(Player.INACTIVE);
                break;
            }
        }
        //System.out.println("1.active player index -> " + activePlayerIndex);
        //find next active player candidate index
        activePlayerIndex = activePlayerIndex == players.length - 1 ? 0 : activePlayerIndex + 1 ;
       // System.out.println("2.active player index -> " + activePlayerIndex);
        // make sure the next active player candidate is 
        //not null
        //is inactive (not folded,not all in)
        while(players[activePlayerIndex] == null || !players[activePlayerIndex].getStatus().equals(Player.INACTIVE) ){
           System.out.println("3.active player index -> " + activePlayerIndex + " " +players[activePlayerIndex] );
           activePlayerIndex = activePlayerIndex == players.length - 1 ? 0 : activePlayerIndex + 1 ;
        }
        players[activePlayerIndex].setStatus(Player.ACTIVE);
        
        for (Player player : players) {
            if (player == null) {
                continue;
            }
            System.out.println("p : " + player.getId() + " " + player.getName() + " " + player.getStatus() + " " + player.getBet() + " " + player.getMoney());
        }
        
        return activePlayerIndex;
    }
    
    public Player getActivePlayer(){
        return players[getActivePlayerIndex()];
    }
    
    public Integer getActivePlayerIndex(){
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getStatus().equals(Player.ACTIVE)) {
                return i;
            }
        }
        return -1;
    }

    public Boolean allPlayersDiscardedCard() {
        for (Player player : players) {
            if (player == null) {
                continue;
            }
            if (!player.isHasDiscardedThirdCard()) {
                return false;
            }
        }
        return true;
    }
    
    
}
