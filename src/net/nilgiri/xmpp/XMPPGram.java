package net.nilgiri.xmpp;

final class XMPPGram
{
	public final static String REVISION = "$Revision: 1.3 $";

	final String message;
	final String to;

	XMPPGram(String to, CharSequence message)
	{
		this.to = to;
		this.message = message.toString();
	}
}
