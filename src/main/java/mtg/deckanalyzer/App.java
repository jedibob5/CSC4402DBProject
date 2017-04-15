package mtg.deckanalyzer;

import io.magicthegathering.javasdk.api.*;
import io.magicthegathering.javasdk.resource.*;
import io.magicthegathering.javasdk.exception.*;

public class App
{
    public static void main( String[] args )
    {
        Card newCard = CardAPI.getCard(1);
        System.out.println(newCard.getName());
    }
}
