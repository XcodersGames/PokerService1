package com.xcoders.gameitems;

import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ravindu
 */
public class Card implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final int SPADES = 0;
    public static final int HEARTS = 1;
    public static final int DIAMONDS = 2;
    public static final int CLUBS = 3;
    private Integer value;
    private Integer type;

    public Card() {
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCardTypeName() {
        if (type == null) {
            return "";
        }
        switch (type) {
            case 0:
                return "Spades";
            case 1:
                return "Hearts";
            case 2:
                return "Diamonds";
            case 3:
                return "Clubs";
            default:
                return "";
        }
    }
    
    public String getCardValueName() {
        if (value == null) {
            return "";
        }
        switch (value) {
            case 11:
                return "Jack";                
            case 12:
                return "Queen";
            case 13:
                return "King";
            case 14:
                return "Asia";
            default:
                return String.valueOf(value);
        }
    }
}
