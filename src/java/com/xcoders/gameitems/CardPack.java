package com.xcoders.gameitems;

import java.io.Serializable;
import java.util.Random;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ravindu
 */
public class CardPack implements Serializable{
    private static final long serialVersionUID = 1L;
    private Card[] cards;
     private final int TOTAL_CARDS = 52;
     
    public CardPack() {
        cards = new Card[TOTAL_CARDS];
        
        // creates a new card pack with the 4 types
        // cards from 2 -10 and
        // J is considered as 11
        // Q is considered as 12
        // K is considered as 13
        // A is considered as 14
        // took as as 14 instead of 1 cz it would be easy in comparison when A has the greatest value
        for (int i = 0; i < 4; i++) {
            //took this card out of the loop just to make A the 1st card
            Card cardA = new Card();
            cardA.setValue(14);
            cardA.setType(i);
            cards[i * 13] = cardA;
            
            //create the other 12 cards
            for (int j = 2; j <= 13; j++) {
                Card card = new Card();
                card.setValue(j);
                card.setType(i);
                cards[(i * 13) + (j-1)] = card;
                //0 -12
                //13 - 25
                //26- 38
                //39   - 51
            }
        }
    }
    
    public Card[] getCards(){
        return cards;
    }
    
    public void shuffle() {
        Random randNo=new Random();
       for(int i=0;i<cards.length;i++){
            int j=randNo.nextInt(TOTAL_CARDS);
            Card card1=cards[i];
            cards[i]=cards[j];
            cards[j]=card1;
        }
    }

    
    int j=0;
    public Card[] getCards(int n) {
        Card[] fewCards=new Card[n];
        for(int i=0;i<n;i++){
            fewCards[i]=cards[j];
            j++;
        }
        return fewCards;
    }
    
}
