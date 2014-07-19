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
public class Player implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Card[] cards;
    private Integer money;
    private Integer role;
    
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
    
    
}
