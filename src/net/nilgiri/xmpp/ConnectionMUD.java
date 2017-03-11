package net.nilgiri.xmpp;

import java.io.IOException;

final class ConnectionMUD extends AbstractConnection
{
	public final static String REVISION = "$Revision: 1.4 $";
	//private StringQueue _in = new StringQueue();
	private StringQueue _out = new StringQueue();
	private final Buddy buddy;

	ConnectionMUD(Buddy buddy) throws IOException
	{
		super(buddy.name,
				buddy.mudserver.HOST,
				buddy.mudserver.port(),
				buddy);
		this.buddy = buddy;
	}

	public final String name()
	{
		return buddy.name;
	}

	@Override
	public final void connect() throws IOException
	{
		super.connect();
		StringBuilder buf = new StringBuilder();
		buf.append(buddy.mudserver.loginSecret());
		buf.append(' ');
		buf.append(buddy.mudserver.buddyLogin(buddy.name));
		send(buf.toString());
	}

	private byte[] _buffer = new byte[MUD.READ_BUF_LEN];
	private int _offset = 0;
	private int _endpos = 0;
	//for receiving raw data from MUD.
	@Override
	public final void push(byte[] array)
	{
		buddy.recvmud(array);
	}

	@Override
	public final byte[] poll()
	{
		return _out.poll();
	}

	@Override
	public final boolean canPoll()
	{
		return _out.canPoll();
	}

	@Override
	public final int readBufLen()
	{
		return MUD.READ_BUF_LEN;
	}

	@Override
	public final int writeBufLen()
	{
		return MUD.WRITE_BUF_LEN;
	}

	@Override
	public final void clear()
	{
		//_in.clear();
		_out.clear();
	}

	public final void send(String mesg)
	{
		assert mesg != null;
		int len = mesg.length();
		assert len > 0;
		byte[] array = new byte[len+2];
		int i = 0;
		for (; i < len; i++)
		{
			array[i] = (byte)mesg.charAt(i);
		}
		array[i++] = '\n';
		array[i++] = 0;
		_out.push(array);
	}
}
