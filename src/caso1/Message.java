package caso1;

public class Message {
	private int content;
	private boolean hasAnswer;
	
	public Message (int content){
		this.content = content;
	}
	
	
	public int getContent() {
		return content;
	}


	public void setContent() {
		this.content++;
	}


	public boolean isHasAnswer() {
		return hasAnswer;
	}


	public void setHasAnswer(boolean hasAnswer) {
		this.hasAnswer = hasAnswer;
	}

}
