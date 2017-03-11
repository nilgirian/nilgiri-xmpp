package net.nilgiri.xmpp;

import java.io.IOException;

final class Buddy implements ListenerInterface
{
	public final static String REVISION = "$Revision: 1.15 $";
	final String name;
	final boolean trusted;
	final boolean admin;
	long t = 0;
	final XMPPGramQueue enqueue;
	ConnectionXMPP connection;
	ConnectionMUD mud = null;
	final MUDConnectionInterface mudserver;
	
	Buddy(String name, XMPPGramQueue enqueue, ConnectionXMPP connection)
	{
		this.name = name;
		this.mudserver = connection.mudserver();
		this.trusted = mudserver.trusted(name);
		this.admin = mudserver.admin(name);
		this.t = System.currentTimeMillis();
		this.enqueue = enqueue;
		this.connection = connection;
	}

	/** Recive a message from IM. The actually queuing is done in ConnectionAIM.
		This really means process the received IM.
		*/
	void recvim(CharSequence mesg)
	{
		boolean log = true;
		String message = mesg.toString();
		long t0 = System.currentTimeMillis();
		if (message.equalsIgnoreCase("#help"))
		{
			sendHelp();
		}
		else if (message.equalsIgnoreCase("#info"))
		{
			sendInfo();
		}
		else if (message.equalsIgnoreCase("#load"))
		{
			sendLoad();
		}
		else if (trusted && message.equalsIgnoreCase("#disconnect"))
		{
			sendDisconnect();
		}
		else if (trusted && message.equalsIgnoreCase("#connect"))
		{
			sendConnect();
		}
		else if (admin && message.equalsIgnoreCase("#maillog"))
		{
			mudserver.maillog();
		}
		else if (admin && message.equalsIgnoreCase("#halt"))
		{
			mudserver.halt();
		}
		else if (admin && message.equalsIgnoreCase("#unhalt"))
		{
			mudserver.unhalt();
		}
		else if (admin && message.equalsIgnoreCase("#reboot"))
		{
			mudserver.reboot();
		}
		else if (admin && message.equalsIgnoreCase("#who"))
		{
			StringBuilder buf = new StringBuilder();
			connection.who(buf);
			sendim(buf);
		}

		else if (admin && message.equalsIgnoreCase("#shutdown"))
		{
			System.err.println("Shutdown by "+name+".");
			throw new RuntimeException("shutdown xmpp");
		}
		else if (message.startsWith("#"))
		{
			sendim("I did not understand that # command.");
		}
		else if (mud != null && mud.connected())
		{
			log = false;
			sendmud(message);
		}
		else
		{
			if (t0 - t > TIME_RESEND_INTRO)
			{
				sendIntro();
			}
		}
		if (log)
		{
			StringBuilder buf = new StringBuilder(name)
				.append(">> ")
				.append(message);
			System.err.println(buf);
		}
		t = t0;
	}

	/** Send a message to IM. Queue a message to send out.
		*/
	void sendim(CharSequence message)
	{
		if (message == null)
		{
			return;
		}
		assert enqueue != null;
		enqueue.enqueue(new XMPPGram(name, message));
	}

	private void sendHelp()
	{
		sendim(
				"#help - this menu \r\n"+
				"#info - MUD information \r\n"+
				"#load - get MUD load information"
				);
		if (trusted)
		{
			sendim(
					"#connect - Connect to MUD \r\n"+
					"#disconnect - Disconnect from Mud \r\n");
		}
		if (admin)
		{
			sendim(
					"#maillog - mails the log to the admins\r\n"+
					"#halt - halt the MUD \r\n"+
					"#reboot - reboot the MUD \r\n"+
					"#unhalt - clear the halt of the MUD \r\n"+
					"#shutdown - shutdown the XMPP session \r\n"+
					"#who - see who is using XMPP client\r\n");
		}
	}

	private void sendInfo()
	{
		if (trusted)
		{
			if (mud == null || mud.connected() == false)
			{
				sendim("You are not connected.");
			}
			else
			{
				sendim("You are connected.");
			}
		}
		else
		{
			sendim("Visit us at: "+mudserver.URL());
		}
		String mesg = mudserver.info();
		if (mesg == null)
		{
			sendim("The MUD is currently down.");
		}
		sendim(mesg);
	}

	private void sendLoad()
	{
		String mesg = mudserver.load();
		if (mesg == null)
		{
			sendim("The MUD is currently down.");
		}
		sendim(mesg);
	}

	private void sendDisconnect()
	{
		assert trusted;
		if (mud == null)
		{
			sendim("You are already disconnected.");
			return;
		}
		mud.disconnect();
		mud = null;
		sendim("### you have been disconnected");
	}

	private void sendConnect()
	{
		assert trusted;
		try
		{
			if (mud == null)
			{
				mud = new ConnectionMUD(this);
			}
			if (mud.connected())
			{
				sendim("You are already connected.");
				return;
			}
			mud.connect();
			mud.start();
			if (mud.connected() == false)
			{
				sendim("Could not connect to MUD.");
			}
		}
		catch (IOException e)
		{
			throw new XMException(e);
		}
	}

	private void sendIntro()
	{
		sendim("I am the "+mudserver.name()+" XMPP bot. Type \"#help\" for more info, or visit us at "+mudserver.URL());
	}

	/** What to do with messages received from mud. */
	void recvmud(byte[] array)
	{
		StringBuilder buf = new StringBuilder(MUD.READ_BUF_LEN);
		int p = 0;
		//remove white space
		while (p != array.length)
		{
			char c = (char)array[p];
			p++;
			if (c == ' ' || c == '\n' || c == '\r')
			{
				continue;
			}
			buf.append(c);
			break;
		}
		//copy over rest
		boolean foundn = false;
		while (p != array.length)
		{
			char c = (char)array[p];
			if (c == '\r')
			{
				//skip over \r
				p++;
				continue;
			}
			else if (c == '\n')
			{
				if (foundn == false)
				{
					foundn = true;
					buf.append(c);
				}
				else
				{
					p++;
					continue;
				}
			}
			else if (c == '<')
			{
				int p1  = p+1;
				if (p1 < array.length)
				{
					if ((char)array[p1] == '>')
					{
						p++;
						p++;
						continue;
					}
				}
				buf.append(c);
			}
			else
			{
				foundn = false;
				buf.append(c);
			}
			p++;
		}
		String message = buf.toString();
		if (message.startsWith("The Angel of Death has come and gone.")) //XXX
		{
			return;
		}
		if (message.contains("Angel of Death"))
		{
			XMException.dump(array);
		}
		sendim(message);
	}

	/** For sending messages to mud. */
	void sendmud(String message)
	{
		assert mud.connected();
		mud.send(message);
	}

	@Override
	/** Assume for a disconnect message */
	public void send(String message)
	{
		sendim(message);
		mud = null;
	}

	final static long TIME_RESEND_INTRO = 60 * 60 * 24 * 1000;
}
