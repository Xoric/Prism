package xoric.prism.data.meta;

public enum MetaType
{
	COMMON,
	TOC /* file table */,
	SHADER,
	DEVELOP /* contains attachments and targetFile */,
	ANIM_D /* animation-names for ModelCreator etc. */,
	MODEL_G,
	URGENCY /* message urgency */
	// max index: 255
}
