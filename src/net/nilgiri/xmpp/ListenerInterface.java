package net.nilgiri.xmpp;

interface ListenerInterface
{
	public final static String REVISION = "$Revision: 1.1 $";
	//sending messages
	abstract void send(String mesg);
}
