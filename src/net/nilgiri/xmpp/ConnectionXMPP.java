package net.nilgiri.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import java.util.HashMap;

final class ConnectionXMPP extends Thread
{
	public final static String REVISION = "$Revision: 1.6 $";
	
	XMPPConnection _connection;
	XMPPGramQueue enqueue = new XMPPGramQueue();
	private final MUDConnectionInterface _mudserver;
	private final HashMap<String,Buddy> _buddylist = new HashMap<String,Buddy>();

	ConnectionXMPP(XMPPConnectionInterface server,
			MUDConnectionInterface mudserver) throws XMPPException
	{
		ConnectionConfiguration config = new ConnectionConfiguration(
				server.host(),
				server.port(),
				server.proxy());
		config.setSASLAuthenticationEnabled(server.SASLAuth());
		_connection = new XMPPConnection(config);
		_connection.connect();
		_connection.login(server.username(), server.password());

		PacketListener listener = new PacketListener()
		{
			ConnectionXMPP connection;

			@Override
			public void processPacket(Packet p)
			{
				String name = p.getFrom();
				if (name != null)
				{
					if (p instanceof Message)
					{
						Message msg = (Message) p;
						String from = msg.getFrom();
						StringBuilder buf = new StringBuilder();
						int len = from.length();
						for (int i = 0; i < len; i++)
						{
							char c = from.charAt(i);
							if (c != '/')
							{
								buf.append(c);
							}
							else
							{
								break;
							}
						}
						name = buf.toString();
						Buddy buddy = _buddylist.get(name);
						if (buddy == null)
						{
							buddy = new Buddy(name, enqueue, ConnectionXMPP.this);
							_buddylist.put(name, buddy);
						}
						buddy.recvim(msg.getBody());
					}
					else
					{
						System.out.println(name + "|> "+p.toString());
					}
				}
				else
				{
					System.out.println("|> "+p.toString());
				}
			}
		};
		_connection.addPacketListener(listener, null);
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus("http://nilgiri.net/");
		_connection.sendPacket(presence);
		_mudserver = mudserver;
	}

	public final MUDConnectionInterface mudserver()
	{
		return _mudserver;
	}

	public final void send(String to, String text)
	{
		Message msg = new Message(to, Message.Type.chat);
		msg.setBody(text);
		_connection.sendPacket(msg);
	}

	public final void run()
	{
		while (true)
		{
			XMPPGram gram = enqueue.dequeue();
			if (gram != null)
			{
				send(gram.to, gram.message);
			}
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch (InterruptedException e)
			{
				throw new XMException(e);
			}
		}
	}

	public final void who(StringBuilder buf)
	{
		for (Buddy buddy : _buddylist.values())
		{
			buf.append(' ');
			buf.append(buddy.name);
			buf.append('\n');
		}
	}

	public final static int SLEEP_TIME = 500;
}
