package xoric.prism.data.exceptions;

public interface IInfoLayer
{
	public void setUplink(IInfoLayer uplink);

	public void addExceptionInfoTo(PrismException e);
}
