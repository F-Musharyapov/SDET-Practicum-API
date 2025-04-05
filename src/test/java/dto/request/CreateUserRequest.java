package dto.request;

import java.util.List;

public class CreateUserRequest{
	private boolean verified;
	private String title;
	private List<Integer> importantNumbers;
	private Addition addition;

	public boolean isVerified(){
		return verified;
	}

	public String getTitle(){
		return title;
	}

	public List<Integer> getImportantNumbers(){
		return importantNumbers;
	}

	public Addition getAddition(){
		return addition;
	}
}