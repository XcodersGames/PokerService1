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


public class GameController {
    //WS Methods
    public Integer joinGame(HttpSession session,Long id) {
     //check if the session is null(this is the first time )
        // if session is null create a session
        
        //find the member with id
        Member m = new MemberJpaController().findMember(id);        
        //create a player and assign id,name and money
        Player p = new Player();
        p.setId(id);
        p.setName(m.getUserName());
        p.setMoney(m.getScore() < 2000? 2000 : m.getScore());
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
    
    public void register(String userName,String password) throws PokerException{
        Member m = new Member();
        m.setUserName(userName);
        m.setPassword(password);
        try {
            new MemberJpaController().create(m);
        } catch (Exception e) {           
            throw new PokerException(e.getMessage());
        }
    }
    
    public Long login(String userName,String password) throws PokerException{
        Member m = new MemberJpaController().findMember(userName, password);
        if (m == null) {
            throw new PokerException("login failed");
        }else{
            return m.getId();
        }
    }
    
    public Table getTable(HttpSession session,Integer id) throws PokerException{
        List<Table> tables = (List<Table>) session.getAttribute("tables");
        if (tables == null) {
            throw new PokerException("table not found");
        }
        for (Table table : tables) {
            if (table.getId().equals(id)) {
                return  table;
            }
        }
        throw new PokerException("table not found");
    }
    
    public void placeSmallBind(HttpSession session,Integer tableId,Long userId,Integer amount) throws PokerException{
        Table table = getTable(session, tableId);
        if (!table.getStatus().equals(Table.WAIT_SMALLBIND)) {
            throw new PokerException("Table is not waiting for small bind");
        }
        
        if (!table.getPlayers()[table.getSmallBindId()].getId().equals(userId)) {
            throw new PokerException("This player cannot place small bind");
        }        
        table.getPlayers()[table.getSmallBindId()].setBet(amount);
        table.setStatus(Table.WAIT_BIGBIND);               
        table.getPlayers()[table.getSmallBindId()].setStatus(Player.INACTIVE);
        table.getPlayers()[table.getBigBindId()].setStatus(Player.ACTIVE);
        System.out.println("small bind set : " + amount);
    }
    
    public void placeBigBind(HttpSession session,Integer tableId,Long userId,Integer amount) throws PokerException{
        Table table = getTable(session, tableId);
        if (!table.getStatus().equals(Table.WAIT_SMALLBIND)) {
            throw new PokerException("Table is not waiting for big bind");
        }
        
        if (!table.getPlayers()[table.getBigBindId()].getId().equals(userId)) {
            throw new PokerException("This player cannot place big bind");
        }        
        table.getPlayers()[table.getBigBindId()].setBet(amount);
        table.setStatus(Table.PRE_FLOP);
        table.getPlayers()[table.getBigBindId()].setStatus(Player.INACTIVE);
        if (table.getBigBindId() != 5) {
            if (table.getPlayers()[table.getBigBindId() + 1] != null) {
                table.getPlayers()[table.getBigBindId() + 1].setStatus(Player.ACTIVE);
            }
        }else{
            table.getPlayers()[0].setStatus(Player.ACTIVE);
        }
        System.out.println("big bind set : " + amount);
    }
    
    public void discardCard(HttpSession session,Integer tableId,Long playerId, Card card) throws PokerException{
        Table table = getTable(session, tableId);
        for (Player p : table.getPlayers()) {
            if (p == null) {
                continue;
            }            
            if (p.getId().equals(playerId)) {                
                for ( int i = 0; i < p.getCards().length; i++) {
                    Card c = p.getCards()[i];
                    if (c.equals(card)) {
                        p.getCards()[i] = null;
                        reArrangeCards(p.getCards());                        
                        break;
                    }
                }
                break;
            }
        }
    }
    
    public void bet(HttpSession session,Integer tableId,Long playerId,Integer amount) throws PokerException{
    
    }
    
    //to be written by viji
    public void fold(HttpSession session,Integer tableId,Long playerId) throws PokerException{
    
    }
    
    public void check(HttpSession session,Integer tableId,Long playerId,Integer amount) throws PokerException{
    
    }
    
    //game logic
    
    private void startGame(Table table){
        if (table.getDealerId() == null) {
            if (table.getNumberOfPlayers() > 2) {
                table.setDealerId(0);
                table.setSmallBindId(1);
                table.setBigBindId(2);
            }else{
                table.setDealerId(0);
                table.setSmallBindId(0);
                table.setBigBindId(2);
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
    
    //to be written by viji
    private void dealPlayerCards(Table table){
    
    }
    
    //to be written by viji
    private void flop(Table table){
    
    }
    
    //to be written by viji
    private void turn(Table table){
    
    }
    
    //to be written by viji
    private void river(Table table){
    
    }
    
    //to be written by ishantha
    private void dividePot(Table table){
    
    }
    
    //to be written by viji
    private void reArrangeCards(Card[] cards){
    
    }
    
    private void selectNextActivePlayer(Table table){
    
    }
}
