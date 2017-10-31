import java.util.Comparator;

import org.json.simple.JSONArray;

public class Emoji implements Comparable{
	public String emoji;
	public String description; 
	public JSONArray alias; 
	public JSONArray tags;
	public Scores score;
	public static String type;
	
	/** 
	 * Creates a new emoji object
	 * @param emoji
	 * @param description
	 * @param alias
	 * @param tags
	 */
	public Emoji(String emoji, String description, JSONArray alias, JSONArray tags) {
		this.emoji = emoji;
		this.description = description; 
		this.alias = alias; 
		this.tags = tags;
		String[] tagArr = new String[tags.size()];
		for (int i = 0; i < tagArr.length; i++) 
			tagArr[i] = (String) tags.get(i);
		Tag tagList = new Tag(tagArr);
		this.score = tagList.getScore();
	}
	
	public Scores getScores() {
		return this.score;
	}
	
	public void setScores(Scores score) {
		this.score = score;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	@Override
	/**
	 * Compares two emoji objects in accordance to each of the score attributes
	 */
	public int compareTo(Object compareEmoji) {
		if (type.equals("happiness")) {
			if (getScores().getHappiness() < ((Emoji) compareEmoji).getScores().getHappiness()) 
				return -1;
			else if (getScores().getHappiness() > ((Emoji) compareEmoji).getScores().getHappiness())
				return 1; 
			else
				return 0;
		} else if (type.equals("sadness")) {
			if (getScores().getSadness() < ((Emoji) compareEmoji).getScores().getSadness()) 
				return -1;
			else if (getScores().getSadness() > ((Emoji) compareEmoji).getScores().getSadness())
				return 1; 
			else
				return 0;
		} else if (type.equals("annoyed")) {
			if (getScores().getContempt() < ((Emoji) compareEmoji).getScores().getContempt()) 
				return -1;
			else if (getScores().getContempt() > ((Emoji) compareEmoji).getScores().getContempt())
				return 1; 
			else
				return 0;
		} else if (type.equals("angry")) {
			if (getScores().getAnger() < ((Emoji) compareEmoji).getScores().getAnger()) 
				return -1;
			else if (getScores().getAnger() > ((Emoji) compareEmoji).getScores().getAnger())
				return 1; 
			else
				return 0;
		} else if (type.equals("tired")) {
			if (getScores().getNeutral() < ((Emoji) compareEmoji).getScores().getNeutral()) 
				return -1;
			else if (getScores().getNeutral() > ((Emoji) compareEmoji).getScores().getNeutral())
				return 1; 
			else
				return 0;
		} return 0;
	}
	
	 
	
}
