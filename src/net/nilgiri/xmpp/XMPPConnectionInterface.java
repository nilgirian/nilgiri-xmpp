package net.nilgiri.xmpp;

import org.jivesoftware.smack.XMPPConnection;

interface XMPPConnectionInterface
{
	public final static String REVISION = "$Revision: 1.2 $";

	String host();
	int port();
	String proxy();
	String username();
	String password();
	String status();
	boolean SASLAuth();
}
