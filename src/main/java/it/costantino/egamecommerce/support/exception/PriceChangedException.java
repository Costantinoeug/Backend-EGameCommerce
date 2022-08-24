package it.costantino.egamecommerce.support.exception;

import javax.persistence.PrimaryKeyJoinColumn;

public class PriceChangedException extends RuntimeException{
    public PriceChangedException(){
    }
    public PriceChangedException(float originalPrice, float currentPrice){}
}
