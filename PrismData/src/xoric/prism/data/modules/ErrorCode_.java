package xoric.prism.data.modules;

@Deprecated
public class ErrorCode_
{
	private final int code;

	public ErrorCode_(ModuleID_ moduleID, ActorID_ actorID, ErrorID_ errorID)
	{
		this.code = calc(moduleID, actorID, errorID);
	}

	public ErrorCode_(ActorID_ actorID, ErrorID_ errorID)
	{
		ModuleID_ m = ModuleID_.GENERIC;
		this.code = calc(m, actorID, errorID);
	}

	public ErrorCode_(IActor_ actor, ErrorID_ errorID)
	{
		ModuleID_ m = ModuleID_.GENERIC;
		this.code = calc(m, actor.getActorID(), errorID);
	}

	public ErrorCode_(int code)
	{
		this.code = code;
	}

	public String toHexString()
	{
		return "0X" + Integer.toHexString(code).toUpperCase();
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

	public ModuleID_ getModuleID()
	{
		int i = getModule();
		return ModuleID_.values()[i];
	}

	public ActorID_ getActorID()
	{
		int i = getActor();
		return ActorID_.values()[i];
	}

	public ErrorID_ getErrorID()
	{
		int i = getError();
		return ErrorID_.values()[i];
	}

	public static int calc(ModuleID_ moduleID, ActorID_ actorID, ErrorID_ errorID)
	{
		int code = (moduleID.ordinal() << 24) | (actorID.ordinal() << 12) | errorID.ordinal();
		return code;
	}
}
