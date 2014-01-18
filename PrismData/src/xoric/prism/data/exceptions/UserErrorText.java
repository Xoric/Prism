package xoric.prism.data.exceptions;

public enum UserErrorText
{
	LOCAL_GAME_FILE_CAUSED_PROBLEM("A local game file has caused a problem."),
	DEVELOP_FILE_CAUSED_PROBLEM("A development file has caused a problem."),
	INTERNAL_PROBLEM("An internal error occured."),
	WRITE_ERROR("An error occurred while trying to write a file."),
	READ_ERROR("An error occurred while trying to read from a file."),
	COULD_NOT_CREATE_DIRECTORY("Directory could not be created."),
	SHADER_PROBLEM("There was a problem with a shader."),
	FILE_NOT_FOUND("File not found.");

	private final String text;

	private UserErrorText(String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		return text;
	}
}
