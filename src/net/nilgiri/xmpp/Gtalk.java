package net.nilgiri.xmpp;

final class Gtalk implements XMPPConnectionInterface
{
	public final static String REVISION = "$Revision: 1.3 $";

	public final String host() { return "talk.google.com"; }
	public final int port() { return 5222; }
	public final String proxy() { return "gmail.com"; }
	public final String username() { return "-USERNAME-"; }
	public final String password() { return "-PASSWORD-"; }
	public final String status() { return "http://nilgiri.net"; }
	public final boolean SASLAuth() { return false; }
}
