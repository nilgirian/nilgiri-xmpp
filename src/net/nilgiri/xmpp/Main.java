package net.nilgiri.xmpp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main
{
	public final static String REVISION = "$Revision: 1.6 $";

	public final static void start()
	{
	}

	public final static void main(String args[])
	{
		try
		{
			ConnectionXMPP xmpp = new ConnectionXMPP(new Gtalk(), new MUD());
			System.err.println("XMPP Client "+PROJECT_REVISION);
			xmpp.start();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public final static String PROJECT_REVISION;
	static
	{
		int major = 0;
		int minor = 0;

		String rstr = "Revision:";
		Pattern pattern = Pattern.compile("^\\$"+rstr+" (\\d*)\\.(\\d*) \\$$",
				Pattern.DOTALL);
		Matcher matcher;

		(matcher = pattern.matcher(AbstractConnection.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(AbstractQueue.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(Buddy.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(ConnectionMUD.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(ConnectionXMPP.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(Facebook.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(Gtalk.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(ListenerInterface.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(MUD.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(Main.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(QueueInterface.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(StringQueue.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(XMException.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(XMPPConnectionInterface.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(XMPPGram.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		(matcher = pattern.matcher(XMPPGramQueue.REVISION)).matches();
		major += Integer.valueOf(matcher.group(1));
		minor += Integer.valueOf(matcher.group(2));

		PROJECT_REVISION = "v"+major+"."+minor;
	}
}
