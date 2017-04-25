package mtg.deckanalyzer;

import io.magicthegathering.javasdk.resource.*;
import io.magicthegathering.javasdk.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.*;


public class App
{
	public static void main(String[] args)
    {
		System.out.println(CardAPI.getCard(405094).getName());
    	ArrayList<File> deckLists = new ArrayList<File>();
    	ArrayList<Card> cardList = new ArrayList<Card>();
    	File dir = new File("Decks");
    	parseDeckLists(getDeckLists(dir, deckLists), cardList);
    	writeCompetitions(deckLists, new File("Competitions.txt"));
    	writeCardList(cardList, new File("CardList.txt"));
    }

    public static ArrayList<File> getDeckLists(File dir, ArrayList<File> files)
    {
        if(dir.isDirectory())
        {
            ArrayList <File> path = new ArrayList<File>(Arrays.asList(dir.listFiles()));
            for(int i=0 ; i<path.size();++i)
            {
                if(path.get(i).isDirectory())
                {
                	getDeckLists(path.get(i),files);
                	System.out.println("getDeckLists: directory added");
                }
                if(path.get(i).isFile())
                {
                    files.add(path.get(i));
                    System.out.println("getDeckLists: file added");
                }
            }
        }
        if(dir.isFile())
        {
            files.add(dir);
        }
	        return files;
    }

    public static void parseDeckLists(ArrayList<File> deckLists, ArrayList<Card> cardList)
    {
		Scanner input = null;
		FileWriter fw1 = null;
		FileWriter fw2 = null;
		BufferedWriter bw1 = null;
		BufferedWriter bw2 = null;
    	try {
    		List<Card> nextCard = new ArrayList<Card>();
    		fw1 = new FileWriter("UsageList.txt");
    		fw2 = new FileWriter("Representation.txt");
	    	bw1 = new BufferedWriter(fw1);
	    	bw2 = new BufferedWriter(fw2);
	    	for(int i = 0; i < deckLists.size(); i++)
	    	{
    			List<String> cardAttrib = new ArrayList<String>();
	    		input = new Scanner(deckLists.get(i), "UTF-8");
	    		while(input.hasNext())
	    		{
	    			int copies = input.nextInt();
	    			cardAttrib.add("name=\"" + input.nextLine().substring(1) + "\"");
	    			if(cardAttrib.get(0).contains("Æ"))
	    			{
	    				String temp = cardAttrib.get(0);
	    				temp = temp.substring(0, temp.indexOf('Æ')) + "Ae" + temp.substring(temp.indexOf('Æ') + 1, temp.length());
	    				System.out.println("Æ found, corrected cardAttrib to " + temp);
	    				cardAttrib.set(0, temp);
	    			}
	    			if(cardAttrib.get(0).contains("//"))
	    			{
	    				String temp = cardAttrib.get(0);
	    				cardAttrib.set(0, temp.substring(0, temp.indexOf('/') - 1) + "\"");
	    				System.out.println(cardAttrib.get(0));
	    				nextCard.add(CardAPI.getAllCards(cardAttrib).get(0));
	    				cardAttrib.set(0, "name=\"" + temp.substring(temp.indexOf('/') + 3));
	    				System.out.println(cardAttrib.get(0));
	    				nextCard.add(CardAPI.getAllCards(cardAttrib).get(0));
	    			} else
	    			{
		    			nextCard.add(CardAPI.getAllCards(cardAttrib).get(0));
		    			System.out.println("cardAttrib=" + cardAttrib.get(0) + ", nextCard=" + nextCard.get(0).getName());
		    			cardAttrib.clear();
	    			}
	    			try
	    			{
		    			if(!cardList.contains(nextCard.get(0)))
		    			{
		    				cardList.add(nextCard.get(0));
		    				System.out.println("parseDeckLists: card added");
		    				if(nextCard.size() == 2)
		    				{
		    					if(!cardList.contains(nextCard.get(1)))
		    					{
			    					cardList.add(nextCard.get(1));
				    				System.out.println("parseDeckLists: 2nd half of split card added");
		    					}
		    				}
		    			}
	    			} catch(NullPointerException ex)
	    			{
	    				System.out.println("Dupe of card w/0 mana cost found");
	    			} finally
	    			{
	    			writeToUsageAndRepLists(nextCard.get(0), copies, deckLists.get(i).getName().substring(0, deckLists.get(i).getName().length()-4), deckLists.get(i).getParent(),bw1,bw2);
	    			if(nextCard.size() == 2)
	    			{
	    				writeToUsageAndRepLists(nextCard.get(1), copies, deckLists.get(i).getName().substring(0, deckLists.get(i).getName().length()-4), deckLists.get(i).getParent(),bw1,bw2);
	    			}
	    			nextCard.clear();
	    			}
	    		}
	    		System.out.println("Deck " + (i + 1) + " finished");
	    	}
    	} catch(Exception ex)
    	{
    		ex.printStackTrace();
    	} finally
    	{
    		if(input != null)
    			input.close();

			try {

				if (bw1 != null)
					bw1.close();

				if (fw1 != null)
					fw1.close();

				if (bw2 != null)
					bw2.close();

				if (fw2 != null)
					fw2.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
    	}
    }

    public static void writeToUsageAndRepLists(Card c, int copies, String deck, String comp, BufferedWriter bw1, BufferedWriter bw2) throws IOException
    {
    	//Print Usage
    	bw1.write(c.getName()+"|"+deck+"|"+comp.substring(6,comp.length() - 5)+"|"+comp.substring(comp.length() - 4)+";");

    	//Print Representation
    	bw2.write(deck+"|"+c.getName()+"|"+copies + ";");
    }

    public static void writeCardList(ArrayList<Card> cardList, File f)
    {
    	FileWriter fw = null;
		BufferedWriter bw = null;
		try
		{
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);

			Card c;
			for(int i = 0; i < cardList.size(); i++)
			{
		    	boolean[] colors = new boolean[5];
				c = cardList.get(i);
				String str = c.getName()+"|"+c.getType()+"|"+(int)c.getCmc();

				if(c.getColors() != null)
				{
					for(String j:c.getColors())
			    	{
			    		if(j.equals("White"))
			    			colors[0] = true;
			    		if(j.equals("Blue"))
			    			colors[1] = true;
			    		if(j.equals("Black"))
			    			colors[2] = true;
			    		if(j.equals("Red"))
			    			colors[3] = true;
			    		if(j.equals("Green"))
			    			colors[4] = true;
			    	}
				}

		    	for(boolean k:colors)
		    	{
		    		if(k)
		    			str = str + "|" + "1";
		    		else
		    			str = str + "|" + "1";
		    	}

		    	str = str + "|" + c.getText() + ";";
		    	bw.write(str);
			}
		} catch(Exception ex)
    	{
    		ex.printStackTrace();
    	} finally
    	{
			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
    	}
    }

    public static void writeCompetitions(ArrayList<File> deckList, File f)
    {
    	FileWriter fw = null;
    	BufferedWriter bw = null;
    	try
    	{
    		fw = new FileWriter(f);
    		bw = new BufferedWriter(fw);

    		ArrayList<String> tourneys = new ArrayList<String>();
    		for(File i:deckList)
    		{
    			if(!(tourneys.contains(i.getParent())))
    				tourneys.add(i.getParent());
    		}

    		for(int j = 0; j < tourneys.size(); j++)
    		{
    			String t = tourneys.get(j);
    			bw.write(t.substring(6,t.length()-5) + "|" + t.substring(t.length() - 4) + ";");
    		}
    	} catch(Exception ex)
    	{
    		ex.printStackTrace();
    	} finally
    	{
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
    	}
    }
}
