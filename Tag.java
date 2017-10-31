import java.util.Arrays;
import java.util.List;

public class Tag {

	private Scores score;

	private final List <String> ANGER = Arrays.asList("mad", "angry", "evil", "horns", "devil", "rage", "pout", "smug", "annoyed");
	// "hot"
	private final List <String> CONTEMPT = Arrays.asList("meh", "annoyed", "liar", "hush", "roll eyes");
	//, "whine", "liar", "whew"
	private final List <String> DISGUST = Arrays.asList("sick", "barf", "disgusted", "achoo", "ill");
	private final List <String> FEAR = Arrays.asList("horror", "scared", "nervous", "sweat");
	//private final List <String> HAPPINESS = Arrays.asList("smile", "happy", "joy", "haha", "laugh", "pleased",  "silly");
	private final List <String> HAPPINESS = Arrays.asList("smiley", "smile", "happy", "haha", "blush", "pleased");
	// "phew", "smug", "proud", "cool", "flirt", "love", "crush", "impressed", "angel", "blush"
	private final List <String> NEUTRAL = Arrays.asList("tired", "zzz", "mute", "silence");
	// , "geek", "glasses", "hush", "oops", "rich", "prank","tongue", "lick", "meh"
	//private final List <String> SADNESS = Arrays.asList("hurt", "cry", "bawling", "tears", "sad", "tear");
	private final List <String> SADNESS = Arrays.asList("nervous", "struggling", "upset", "whine", "tired", "stunned", "sad", "cry", "bawling", "tear");
	// "upset", "struggling", "hurt",
	private final List <String> SURPRISE = Arrays.asList("wow", "amazed", "gasp", "shocked", "speechless", "stunned", "surprise");
	
	
	public Tag(String[] tags) {
		double anger = 0;
		double contempt = 0;
		double disgust = 0;
		double fear = 0;
		double happiness = 0;
		double neutral = 0;
		double sadness = 0;
		double surprise = 0;
		this.score = new Scores();
		if (tags.length != 0) {
			for (String s : tags) {
				if (ANGER.contains(s)) 
					anger++;
				else if (CONTEMPT.contains(s))
					contempt++;
				else if (DISGUST.contains(s))
					disgust++;
				else if (FEAR.contains(s))
					fear++;
				else if (HAPPINESS.contains(s))
					happiness++;
				else if (NEUTRAL.contains(s))
					neutral++;
				else if (SADNESS.contains(s))
					sadness++;
				else if (SURPRISE.contains(s))
					surprise++;
			}
			int length = tags.length;
			score.setAll(anger/length, contempt/length, disgust/length, fear/length, 
					happiness/length, neutral/length, sadness/length, surprise/length);
		} else {
			score.setAll(0,0,0,0,0,0,0,0);
		}
	}
	
	public Scores getScore() {
		return score;
	}
	
	
}
