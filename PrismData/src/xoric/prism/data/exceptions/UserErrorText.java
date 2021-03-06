package xoric.prism.data.exceptions;

public enum UserErrorText
{
	LOCAL_GAME_FILE_CAUSED_PROBLEM("A local game file has caused a problem."),
	LOCAL_GAME_FILE_MISSING("A local game file is missing."),
	DEVELOP_FILE_CAUSED_PROBLEM("A development file has caused a problem."),
	INTERNAL_PROBLEM("An internal error occured."),
	WRITE_ERROR("An error occurred while trying to write a file."),
	READ_ERROR("An error occurred while trying to read from a file."),
	COULD_NOT_CREATE_DIRECTORY("Directory could not be created."),
	SHADER_PROBLEM("There was a problem with a shader."),
	FILE_NOT_FOUND("File not found."),
	COMMUNICATION_ERROR("There was a problem with the communication between client and server."),
	PASSWORD_HASH("There was an error trying to encrypt your password. Your login attempt was therefore aborted."),
	ACCOUNT_COULD_NOT_BE_CREATED("Account could not be created."),
	ACCOUNT_ALREADY_EXISTS("This account name already exists."),
	ACCOUNT_LOGIN_FAILED("Login failed."),
	NETWORK_PROBLEM("An error occured while communicating with the server."),
	CONNECTION_PROBLEM("Cannot connect to server."),
	FRAME_BUFFER_ERROR("There was a problem allocating a frame buffer.");

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
