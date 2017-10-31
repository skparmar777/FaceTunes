import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class EmojiGenerationService {
	
	private ArrayList<Emoji> emojiList;
	private ArrayList<String> tagsList;
	
	public void generator() {
		JSONParser emojiParser = new JSONParser();
		emojiList = new ArrayList <Emoji>();
		tagsList = new ArrayList <String>();
		try {
			JSONArray emojiArr = (JSONArray) emojiParser.parse(new FileReader("emoji.txt"));
			for (int i = 0; i < emojiArr.size(); i++) {
				JSONObject emoji = (JSONObject) emojiArr.get(i);
				String emojiImage = (String) emoji.get("emoji");
				String description = (String) emoji.get("description");
				String category = (String) emoji.get("category");
				JSONArray aliases = (JSONArray) emoji.get("aliases");
				JSONArray tags = (JSONArray) emoji.get("tags");
				if (description != null && category != null) {
					if (category.equals("People")) {
						if (description.contains("face")) {
							emojiList.add(new Emoji(emojiImage, description, aliases, tags));
							for (Object tag : tags) {
								if (!tagsList.contains((String) tag))
									tagsList.add((String) tag);
							}
						}
					}
						
				}
			}
		} catch(Exception e) {
			System.out.print("Exception");
		}
	}
	
	public ArrayList<Emoji> getAllEmojis() {
		return emojiList;
	}
	
	public ArrayList<Emoji> getHappyEmojis() {
		for (Emoji e : emojiList) {
			e.setType("happiness");
		}
		Collections.sort(emojiList);
		ArrayList<Emoji> happyEmojis = new ArrayList<Emoji>();
		int score = 1;
		for (int i = emojiList.size() - 5; i < emojiList.size(); i++) {
			Emoji emoji = emojiList.get(i);
			Scores sc = new Scores();
			sc.setAll(0, 0, 0, 0, (double)score/5, 0, 0, 0);
			score++;
			emoji.setScores(sc);
			happyEmojis.add(emoji);
		}
		return happyEmojis;
	}
	
	public ArrayList<Emoji> getSadEmojis() {
		for (Emoji e : emojiList) {
			e.setType("sadness");
		}
		Collections.sort(emojiList);
		ArrayList<Emoji> sadEmojis = new ArrayList<Emoji>();
		int score = 1;
		for (int i = emojiList.size() - 5; i < emojiList.size(); i++) {
			Emoji emoji = emojiList.get(i);
			Scores sc = new Scores();
			sc.setAll(0, 0, 0, 0, 0, 0, (double)score/5, 0);
			score++;
			emoji.setScores(sc);
			sadEmojis.add(emoji);
		}
		return sadEmojis;
	}
	
	public ArrayList<Emoji> getAnnoyedEmojis() {
		for (Emoji e : emojiList) {
			e.setType("annoyed");
		}
		Collections.sort(emojiList);
		ArrayList<Emoji> annoyedEmojis = new ArrayList<Emoji>();
		int score = 1;
		for (int i = emojiList.size() - 5; i < emojiList.size(); i++) {
			Emoji emoji = emojiList.get(i);
			Scores sc = new Scores();
			sc.setAll(0, (double)score/5, 0, 0, 0, 0, 0, 0);
			score++;
			emoji.setScores(sc);
			annoyedEmojis.add(emoji);
		}
		return annoyedEmojis;
	}
	
	public ArrayList<Emoji> getAngryEmojis() {
		for (Emoji e : emojiList) {
			e.setType("angry");
		}
		Collections.sort(emojiList);
		ArrayList<Emoji> angryEmojis = new ArrayList<Emoji>();
		int score = 1;
		for (int i = emojiList.size() - 5; i < emojiList.size(); i++) {
			Emoji emoji = emojiList.get(i);
			Scores sc = new Scores();
			sc.setAll((double)score/5, 0, 0, 0, 0, 0, 0, 0);
			score++;
			emoji.setScores(sc);
			angryEmojis.add(emoji);
		}
		return angryEmojis;
	}
	
	public ArrayList<Emoji> getTiredEmojis() {
		for (Emoji e : emojiList) {
			e.setType("tired");
		}
		Collections.sort(emojiList);
		ArrayList<Emoji> tiredEmojis = new ArrayList<Emoji>();
		int score = 1;
		for (int i = emojiList.size() - 5; i < emojiList.size(); i++) {
			Emoji emoji = emojiList.get(i);
			Scores sc = new Scores();
			sc.setAll(0, 0, 0, 0, 0, (double)score/5, 0, 0);
			score++;
			emoji.setScores(sc);
			tiredEmojis.add(emoji);
		}
		return tiredEmojis;
	}
	
	public ArrayList<String> getTags() {
		return tagsList;
	}
 	
	public static void main(String[] args) {
		EmojiGenerationService egs = new EmojiGenerationService();
		egs.generator();
		ArrayList <Emoji> happyEmojiList = egs.getHappyEmojis();
		ArrayList <Emoji> sadEmojiList = egs.getSadEmojis();
		ArrayList <Emoji> annoyedEmojiList = egs.getAnnoyedEmojis();
		ArrayList <Emoji> angryEmojiList = egs.getAngryEmojis();
		ArrayList <Emoji> tiredEmojiList = egs.getTiredEmojis();
		for (Emoji e : happyEmojiList) 
			System.out.print(e.emoji + e.getScores().getHappiness()); 
		System.out.println();
		for (Emoji e : sadEmojiList) 
			System.out.print(e.emoji + e.getScores().getSadness());
		System.out.println();
		for (Emoji e : annoyedEmojiList)
			System.out.print(e.emoji + e.getScores().getContempt());
		System.out.println();
		for (Emoji e : angryEmojiList)
			System.out.print(e.emoji + e.getScores().getAnger());
		System.out.println();
		for (Emoji e : tiredEmojiList)
			System.out.print(e.emoji + e.getScores().getNeutral());
		
		
	}
}