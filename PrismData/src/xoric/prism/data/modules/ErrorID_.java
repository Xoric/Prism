package xoric.prism.data.modules;

@Deprecated
public enum ErrorID_
{
	COMMON("unknown error"),
	INVALID_LOGIN("invalid login"),
	ATTACHMENT_NOT_FOUND("attachment not found"),
	FILE_NOT_FOUND("file not found"),
	WRITE_ERROR("write error"),
	INVALID_INDEX("invalid index"),
	READ_ERROR("read error"),
	COMPILE_ERROR("compile error"),
	PATH_NOT_FOUND("path not found"),
	UNEXPECTED_LINE("unexpected line"),
	CORRUPT_LINE("corrupt line"),
	META_BLOCK_MISSING("MetaBlock missing"),
	META_LINE_MISSING("MetaLine missing"),
	COMPRESSION_ERROR("compression error"),
	CORRUPT_META_BLOCK("MetaBlock corrupt"),
	CANNOT_CREATE_DIRECTORY("cannot create directory"),
	DIRECTORY_NOT_EMPTY("directory is not empty");

	private final String description;

	private ErrorID_(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
}
