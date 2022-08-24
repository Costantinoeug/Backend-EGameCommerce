package it.costantino.egamecommerce.support.exception;

import it.costantino.egamecommerce.entities.Game;

public class QuantityExceededException extends RuntimeException{
    public QuantityExceededException(){}
    public QuantityExceededException(Game relatedGame){}
}
