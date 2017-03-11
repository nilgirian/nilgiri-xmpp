package net.nilgiri.xmpp;

final class Facebook implements XMPPConnectionInterface
{
	public final static String REVISION = "$Revision: 1.1 $";

	public final String host() { return "chat.facebook.com"; }
	public final int port() { return 5222; }
	public final String proxy() { return "chat.facebook.com"; }
	public final String username() { return "-EMAIL@ADRESS.COM-"; }
	public final String password() { return "-MYPASSWORD-"; }
	public final String status() { return "http://nilgiri.net"; }
	public final boolean SASLAuth() { return true; }
}
