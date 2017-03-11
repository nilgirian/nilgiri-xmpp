package net.nilgiri.xmpp;

final class XMPPGramQueue extends AbstractQueue<XMPPGram>
{
	public final static String REVISION = "$Revision: 1.1 $";

	final static int READ_BUF_LEN = MUD.READ_BUF_LEN * 2;
	final static int WRITE_BUF_LEN = MUD.WRITE_BUF_LEN * 2;

	XMPPGramQueue()
	{
	}

	@Override
	public final void push(byte[] array)
	{
		throw new XMException("Oops..");
	}

	@Override
	public final byte[] poll()
	{
		throw new XMException("Oops..");
	}

	@Override
	public final int readBufLen()
	{
		return READ_BUF_LEN;
	}

	@Override
	public final int writeBufLen()
	{
		return WRITE_BUF_LEN;
	}
}
