package com.xcoders.gameitems;

import java.io.Serializable;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Card{" + getCardTypeName() + ":" + getCardValueName() + '}';
    }
    
    
}
