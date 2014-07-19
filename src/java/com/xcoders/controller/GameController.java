/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author ravindu
 */
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
        
        
        //get the tables from session
        List<Table> tables = (List<Table>) session.getAttribute("tables");         
        if (tables == null) {
            //if there are no tables in session create the 1st table and add the tables to session
            tables = new ArrayList<Table>();
            Table t = new Table();
            t.setId(1);
            t.addPlayer(p);
            tables.add(t);
            session.setAttribute("tables", tables);
            return 1;
        } else {            
            //Iterate the tables till u find a table with empty seats
            for (Table table : tables) {                
                if (table.hasSeats()) {                   
                    //add player to table
                    table.addPlayer(p);
                    session.setAttribute("tables", tables);
                    //return the table number
                    return table.getId();
                }
            }            
            //if there are no tables with empty seats then create a new table 
            Table t = new Table();
            Integer tableId = tables.size() + 1;
            t.setId(tableId);
            t.addPlayer(p);
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
    
    public void placeSmallBind(Integer tableId,Integer amount) throws PokerException{
        
    }
    
    public void placeBigBind(Integer tableId,Integer amount) throws PokerException{
        
    }
    
    public void discardCard(Integer tableId,Long playerId, Card card) throws PokerException{
    
    }
    
    public void bet(Integer tableId,Long playerId,Integer amount) throws PokerException{
    
    }
    
    public void fold(Integer tableId,Long playerId,Integer amount) throws PokerException{
    
    }
    
    public void check(Integer tableId,Long playerId,Integer amount) throws PokerException{
    
    }
    
    //game logic
    
    private void startGame(Table table){
        if (table.getDealerId() == null) {
            table.setDealerId(0);
            table.setSmallBindId(1);
            table.setBigBindId(2);
        }
        table.setStatus(Table.STARTED);
    }
    
    private void dealPlayerCards(Table table){
    
    }
    
    private void flop(Table table){
    
    }
    
    private void turn(Table table){
    
    }
    
    private void river(Table table){
    
    }
    
    private void dividePot(Table table){
    
    }
}
