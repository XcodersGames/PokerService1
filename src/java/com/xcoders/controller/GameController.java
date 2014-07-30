package com.xcoders.controller;

import com.xcoders.entity.Member;
import com.xcoders.gameitems.Card;
import com.xcoders.gameitems.Player;
import com.xcoders.gameitems.Table;
import com.xcoders.jpacontroller.MemberJpaController;
import com.xcoders.ws.PokerException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.swing.text.TabExpander;

public class GameController {
    //WS Methods

    public Integer joinGame(HttpSession session, Long id) {
        //check if the session is null(this is the first time )
        // if session is null create a session

        //find the member with id
        Member m = new MemberJpaController().findMember(id);
        //create a player and assign id,name and money
        Player p = new Player();
        p.setId(id);
        p.setName(m.getUserName());
        p.setMoney(m.getScore() < 2000 ? 2000 : m.getScore());
        p.setStatus(Player.WAITING);

        //get the tables from session
        List<Table> tables = (List<Table>) session.getAttribute("tables");
        if (tables == null) {
            //if there are no tables in session create the 1st table and add the tables to session
            tables = new ArrayList<Table>();
            Table t = new Table();
            t.setId(1);
            t.addPlayer(p);
            t.setStatus(Table.WAITING);
            tables.add(t);
            session.setAttribute("tables", tables);
            return 1;
        } else {
            //Iterate the tables till u find a table with empty seats
            for (Table table : tables) {
                if (table.hasSeats()) {
                    //add player to table
                    table.addPlayer(p);
                    if (table.getNumberOfPlayers() >= 2) {
                        startGame(table);
                    }
                    //session.setAttribute("tables", tables);
                    //return the table number
                    return table.getId();
                }
            }
            //if there are no tables with empty seats then create a new table 
            Table t = new Table();
            Integer tableId = tables.size() + 1;
            t.setId(tableId);
            t.addPlayer(p);
            t.setStatus(Table.WAITING);
            tables.add(t);
            // add the table to session and return the id of new table
            session.setAttribute("tables", tables);
            return tableId;
        }
    }

    public void register(String userName, String password) throws PokerException {
        Member m = new Member();
        m.setUserName(userName);
        m.setPassword(password);
        try {
            new MemberJpaController().create(m);
        } catch (Exception e) {
            throw new PokerException(e.getMessage());
        }
    }

    public Long login(String userName, String password) throws PokerException {
        Member m = new MemberJpaController().findMember(userName, password);
        if (m == null) {
            throw new PokerException("login failed");
        } else {
            return m.getId();
        }
    }

    public Table getTable(HttpSession session, Integer id) throws PokerException {
        List<Table> tables = (List<Table>) session.getAttribute("tables");
        if (tables == null) {
            throw new PokerException("table not found");
        }
        for (Table table : tables) {
            if (table.getId().equals(id)) {
                return table;
            }
        }
        throw new PokerException("table not found");
    }

    public void placeSmallBind(HttpSession session, Integer tableId, Long userId, Integer amount) throws PokerException {
        Table table = getTable(session, tableId);
        if (!table.getStatus().equals(Table.WAIT_SMALLBIND)) {
            throw new PokerException("Table is not waiting for small bind");
        }

        System.out.println("table->sm ID :" + table.getPlayers()[table.getSmallBindId()].getId() + " snd id : " + userId);
        if (!table.getPlayers()[table.getSmallBindId()].getId().equals(userId)) {
            throw new PokerException("This player cannot place small bind");
        }
        table.getPlayers()[table.getSmallBindId()].setBet(amount);
        table.setStatus(Table.WAIT_BIGBIND);
        table.getPlayers()[table.getSmallBindId()].setStatus(Player.INACTIVE);
        System.out.println("");
        table.getPlayers()[table.getBigBindId()].setStatus(Player.ACTIVE);
        System.out.println("small bind set : " + amount);
    }

    public void placeBigBind(HttpSession session, Integer tableId, Long userId, Integer amount) throws PokerException {
        Table table = getTable(session, tableId);
        if (!table.getStatus().equals(Table.WAIT_BIGBIND)) {
            throw new PokerException("Table is not waiting for big bind");
        }

        if (!table.getPlayers()[table.getBigBindId()].getId().equals(userId)) {
            throw new PokerException("This player cannot place big bind");
        }
        table.getPlayers()[table.getBigBindId()].setBet(amount);
        table.setStatus(Table.PRE_FLOP);
        /*
         table.getPlayers()[table.getBigBindId()].setStatus(Player.INACTIVE);
         if (table.getBigBindId() != 5) {
         if (table.getPlayers()[table.getBigBindId() + 1] != null) {
         table.getPlayers()[table.getBigBindId() + 1].setStatus(Player.ACTIVE);
         }
         } else {
         table.selectFirst
         }*/
        System.out.println("big bind set : " + amount);
        dealPlayerCards(table);
        table.setStatus(Table.PRE_FLOP);
        System.out.println("player cards dealt");
        table.selectNextActivePlayer();
    }

    public void discardCard(HttpSession session, Integer tableId, Long playerId, Card card) throws PokerException {
        Table table = getTable(session, tableId);
        for (Player player : table.getPlayers()) {
            if (player == null) {
                continue;
            }
            System.out.print("@before>player " + player.getId() + " ");
            for (Card card1 : player.getCards()) {
                if (card1 == null) {
                    continue;
                }
                System.out.print(card1 +" ");
            }
            System.out.println();
        }
        //============
        for (Player p : table.getPlayers()) {
            if (p == null) {
                continue;
            }
            if (p.getId().equals(playerId)) {
                for (int i = 0; i < p.getCards().length; i++) {
                    Card c = p.getCards()[i];
                    if (c.equals(card)) {
                        p.getCards()[i] = null;
                        reArrangeCards(p.getCards());
                        p.setHasDiscardedThirdCard(true);
                        break;
                    }
                }
                break;
            }
        }
        System.out.println("player " + playerId + " discarded " + card);
        if(table.allPlayersDiscardedCard()){
            System.out.println("all players have discarded calling turn");
            turn(table);
        }else{
            System.out.println("all have not discarded");
            table.selectNextActivePlayer();
        }
        
        //===================
        for (Player player : table.getPlayers()) {
            if (player == null) {
                continue;
            }
            System.out.print("@after>player " + player.getId() + " ");
            for (Card card1 : player.getCards()) {
                if (card1 == null) {
                    continue;
                }
                System.out.print(card1 +" ");
            }
            System.out.println();
        }
    }

    public void bet(HttpSession session, Integer tableId, Long playerId, Integer amount) throws PokerException {
        System.out.println("bet called for " + playerId + " : " + amount);
        Table table = getTable(session, tableId);
        if (!(table.getStatus().equals(Table.PRE_FLOP) || table.getStatus().equals(Table.POST_FLOP) || table.getStatus().equals(Table.POST_TURN) || table.getStatus().equals(Table.SHOWDOWN))) {
            throw new PokerException("Table is not waiting for a bet");
        }

        Player[] players = table.getPlayers();
        Player currentPlayer = table.getActivePlayer();
        if (!currentPlayer.getId().equals(playerId)) {
            throw new PokerException("Not player turn to bet");
        }
        
        currentPlayer.setBet(currentPlayer.getBet() + amount);
        currentPlayer.setMoney(currentPlayer.getMoney() - amount);
        
        if (table.getMaxBet() < currentPlayer.getBet()) {
            System.out.println("its a raise");
            table.setMaxBet(amount);
        }else

        if (table.getMaxBet().equals(currentPlayer.getBet())) {
            System.out.println("its a check");
        }else if (currentPlayer.getMoney() <= 0) {
            System.out.println("its an allin");
            currentPlayer.setStatus(Player.ALL_IN);
        }

        

        if (potEqual(table)) {
            System.out.println("pot equal");
            switch (table.getStatus()) {
                case Table.PRE_FLOP:
                    flop(table);  
                    System.out.println("preflop bet is equal post flop");
                    break;
                case Table.POST_FLOP:
                    table.setStatus(Table.WAIT_CARD_DISCARD);
                    table.selectFristPlayerActive();
                    System.out.println("post flop bet is equal now turn");
                    break;
                case Table.POST_TURN:
                    System.out.println("end of river");
                    river(table);
                    break;
                case Table.SHOWDOWN:
                    System.out.println("end of show down");
                    break;                
            }
        } else {
            System.out.println("pot not equal");
            table.selectNextActivePlayer();
        }
    }

    //written by viji
    public void fold(HttpSession session, Integer tableId, Long playerId) throws PokerException {
        Table table = getTable(session, tableId);
        for(Player p : table.getPlayers()){
            if(p==null){
                continue;
            }
            if(p.getId().equals(playerId)){
                p.setStatus(Player.FOLDED);       
                
            }
        }
    }

    public void check(HttpSession session, Integer tableId, Long playerId, Integer amount) throws PokerException {
    }

    //game logic
    private void startGame(Table table) {
        if (table.getDealerId() == null) {
            if (table.getNumberOfPlayers() > 2) {
                table.setDealerId(0);
                table.setSmallBindId(1);
                table.setBigBindId(2);
            } else {
                table.setDealerId(0);
                table.setSmallBindId(0);
                table.setBigBindId(1);
            }

        }
        table.setStatus(Table.WAIT_SMALLBIND);
        for (Player player : table.getPlayers()) {
            if (player == null) {
                continue;
            }
            player.setStatus(Player.INACTIVE);
        }
        table.getPlayers()[table.getSmallBindId()].setStatus(Player.ACTIVE);
    }

    //written by viji
    private void dealPlayerCards(Table table) {
        System.out.println("deal player cards");
        table.getCardPack().shuffle();

        for (Player p : table.getPlayers()) {
            if (p == null) {
                continue;
            }
            p.setCards(table.getCardPack().getCards(3));
        }
    }

    //written by viji
    private void flop(Table table) {
        Card[] tableCards = table.getPlayCards();
        Card[] flopCards = table.getCardPack().getCards(3);
        for(int i=0; i< 3;i++){
            tableCards[i] = flopCards[i];
        }
        table.setStatus(Table.POST_FLOP);
        table.selectFristPlayerActive();
    }

    //written by viji
    private void turn(Table table) {
        Card[] tableCards = table.getPlayCards();
        Card[] turnCard = table.getCardPack().getCards(1);
        tableCards[3] = turnCard[0];
        table.setStatus(Table.POST_TURN);
        table.selectFristPlayerActive();
    }

    //to be written by viji
    private void river(Table table) {
        Card[] tableCards = table.getPlayCards();
        Card[] riverCard = table.getCardPack().getCards(1);
        tableCards[4] = riverCard[0];
        table.setStatus(Table.SHOWDOWN);
        table.selectFristPlayerActive();
    }

    //to be written by ishantha
    private void dividePot(Table table) {
    }

    //written by viji
    private void reArrangeCards(Card[] cards) {
        for(int i=0, j=0; i<cards.length; i++){           
            if(cards[i]!=null){
                cards[j]=cards[i];
                j++;
            }            
        }
        cards[cards.length-1]=null;            
    }

    private Boolean potEqual(Table table) {
        boolean allEqual = true;
        int max = 0;
        for (Player player : table.getPlayers()) {
            if (player == null) {
                continue;
            }
            
            System.out.println("player : " + player.getName() + " : status : " + player.getStatus() + " : " + player.getBet() + " max: " + max);
            if (player.getStatus().equals(Player.ACTIVE) || player.getStatus().equals(Player.INACTIVE)) {
                if (max == 0) {
                    max = player.getBet();
                } else if (player.getBet() != max) {
                    return false;
                }
            }
        }
        return allEqual;
    }
}
