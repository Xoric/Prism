package xoric.prism.data.meta;

public enum MetaType
{
	COMMON,
	TOC /* file table */,
	SHADER,
	DEVELOP /* contains attachments and targetFile */,
	ANIM_D /* animation-names for ModelCreator etc. */,
	MODEL_G,
	URGENCY, /* message urgency */
	COLLECTION, /* sprite collection */
	GRID /* sprite grid */
	// max index: 255
}
