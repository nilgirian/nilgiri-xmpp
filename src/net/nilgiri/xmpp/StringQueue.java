package net.nilgiri.xmpp;

final class StringQueue extends AbstractQueue<String>
{
	public final static String REVISION = "$Revision: 1.1 $";
	//we need to make sure that we have enough in the buffer for two frames
	final static int READ_BUF_LEN = MUD.READ_BUF_LEN * 2;
	final static int WRITE_BUF_LEN = MUD.WRITE_BUF_LEN * 2;

	//for queuing the bytes from the channel, and chopping into standard form:
	// <text>\n
	StringQueue()
	{
	}

	@Override
	public final void push(byte array[])
	{
		enqueue(new String(array));
	}

	public final void push(Object obj)
	{
		enqueue(new String(obj.toString().getBytes()));
	}

	@Override
	public final byte[] poll()
	{
		String string = dequeue();
		if (string == null)
		{
			return null;
		}
		return string.getBytes();
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
