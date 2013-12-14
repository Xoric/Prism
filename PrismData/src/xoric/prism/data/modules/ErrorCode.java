package xoric.prism.data.modules;

public class ErrorCode
{
	public static ModuleID defaultModuleID = ModuleID.GENERIC;

	private final int code;

	public ErrorCode(ModuleID moduleID, ActorID actorID, ErrorID errorID)
	{
		this.code = calc(moduleID, actorID, errorID);
	}

	public ErrorCode(ActorID actorID, ErrorID errorID)
	{
		this.code = calc(defaultModuleID, actorID, errorID);
	}

	public ErrorCode(IActor actor, ErrorID errorID)
	{
		this.code = calc(defaultModuleID, actor.getActorID(), errorID);
	}

	public ErrorCode(int code)
	{
		this.code = code;
	}

	public String toHexString()
	{
		return Integer.toHexString(code);
	}

	public int toInt()
	{
		return code;
	}

	public int getModule()
	{
		return (code >> 24) & 0xFF;
	}

	public int getActor()
	{
		return (code >> 12) & 0x3FF;
	}

	public int getError()
	{
		return code & 0x3FF;
	}

	public ModuleID getModuleID()
	{
		int i = getModule();
		return ModuleID.values()[i];
	}

	public ActorID getActorID()
	{
		int i = getActor();
		return ActorID.values()[i];
	}

	public ErrorID getErrorID()
	{
		int i = getError();
		return ErrorID.values()[i];
	}

	public static int calc(ModuleID moduleID, ActorID actorID, ErrorID errorID)
	{
		int code = (moduleID.ordinal() << 24) | (actorID.ordinal() << 12) | errorID.ordinal();
		return code;
	}
}