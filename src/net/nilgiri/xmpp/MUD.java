package net.nilgiri.xmpp;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;

final class MUD implements MUDConnectionInterface
{
	public final static String REVISION = "$Revision: 1.15 $";

	public final String name() { return "the Forgotten World"; }
	public final int port() { return 8888; }
	public final String loginSecret() { return "nTOC2MUD"; }
	public final String URL() { return "http://nilgiri.net"; }

	public final String buddyLogin(String buddy)
	{
		if (buddy.startsWith("nacino@gmail.com"))
		{
			return "sin";
		}
		if (buddy.startsWith("rjbogue@gmail.com"))
		{
			return "rivin";
		}
		if (buddy.startsWith("tomwoudwyk@gmail.com"))
		{
			return "loki";
		}
		if (buddy.startsWith("ahnissi@gmail.com>"))
		{
			return "ahnisi";
		}
		return "unknownIMclient";
	}

	public final boolean trusted(String buddy)
	{
		if (buddy.startsWith("nacino@gmail.com")
		|| buddy.startsWith("rjbogue@gmail.com")
		|| buddy.startsWith("tomwoudwyk@gmail.com")
		|| buddy.startsWith("ahnissi@gmail.com"))
		{
			return true;
		}
		return false;
	}

	public final boolean admin(String buddy)
	{
		if (buddy.startsWith("nacino@gmail.com")
		|| buddy.startsWith("rjbogue@gmail.com"))
		{
			return true;
		}
		return false;
	}

	public final void halt()
	{
		try
		{
			touch(FILE_HALT);
		}
		catch (IOException e)
		{
			throw new XMException(e);
		}
	}

	public final void maillog()
	{
		try
		{
			touch(FILE_MAILLOG);
		}
		catch (IOException e)
		{
			throw new XMException(e);
		}
	}

	public final void unhalt()
	{
		try
		{
			rm(FILE_HALT);
		}
		catch (IOException e)
		{
			throw new XMException(e);
		}
	}

	public final void reboot()
	{
		try
		{
			touch(FILE_REBOOT);
		}
		catch (IOException e)
		{
			throw new XMException(e);
		}
	}

	public final String info()
	{
		try
		{
			return mudinfo(CMD_STATUS);
		}
		catch (IOException e)
		{
			return null;
		}
	}

	public final String load()
	{
		try
		{
			return mudinfo(CMD_LOAD);
		}
		catch (IOException e)
		{
			return null;
		}
	}

	private final static void touch(String filename) throws IOException
	{
		FileWriter out = new FileWriter(filename, true);
		out.flush();
		out.close();
	}

	private final static void rm(String filename) throws IOException
	{
	}

	private final String mudinfo(String command) throws IOException
	{
		Socket socket = new Socket(HOST, port());
		InputStream si = socket.getInputStream();
		OutputStream so = socket.getOutputStream();
		so.write(command.getBytes());
		byte[] bytes = new byte[READ_BUF_LEN];
		si.read(bytes); //throw away first line
		so.write('\n'); //write empty line
		int len = si.read(bytes); //read this line
		assert len != READ_BUF_LEN; //do not exceed!!
		String buf = new String(bytes, 0, len);
		socket.close();
		return buf;
	}

	public final static void main(String args[]) throws IOException
	{
		Socket socket = new Socket("nilgiri.net", 8888);
		InputStream si = socket.getInputStream();
		OutputStream so = socket.getOutputStream();
		so.write(CMD_STATUS.getBytes());
		byte[] bytes = new byte[READ_BUF_LEN];
		si.read(bytes); //throw away first line
		so.write('\n'); //write empty line
		int len = si.read(bytes); //read this line
		assert len != READ_BUF_LEN; //do not exceed!!
		String buf = new String(bytes, 0, len);
		socket.close();
		System.out.println(buf);
	}

	private final static String CMD_STATUS = "INF_TOC2MUD_STATUS";
	private final static String CMD_LOAD = "INF_TOC2MUD_UPTIME";
	public final static String FILE_HALT = "stop";
	public final static String FILE_MAILLOG = "maillog";
	public final static String FILE_REBOOT = "disable";
	public final static String INFO_STATUS = "INF_TOC2MUD_STATUS";
	private final static int PORT = 8888;
}
