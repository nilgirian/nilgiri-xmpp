package net.nilgiri.xmpp;

interface MUDConnectionInterface
{
	public final static String REVISION = "$Revision: 1.2 $";

	/** The HOST is always localhost. This is mostly for security. You can
		easily trust automatic logins from the same machine. The MUD is assumed
		to check for a localhost login when performing automatic logins from the
		XMPP client. However, if an outside party can also run daemons on the same
		machine, this security does not work so well.
		*/
	public final static String HOST = "localhost";

	/** How big of chunks to process over the TCP wire. Generally this should be
		good for most MUDs.
		*/
	public final static int READ_BUF_LEN = 2048;
	public final static int WRITE_BUF_LEN = 2048;

	/** The name of the MUD.
		*/
	String name();

	/** Port of the MUD.
	 */
	int port();

	/** Shared secret for automatically logging in a buddy into MUD.
	 */
	String loginSecret();

	/** URL of the MUD.
		*/
	String URL();

	/** What this MUD login this buddy maps to.
		@return the MUD name login, or "unknownIMClient" if none found.
		*/
	String buddyLogin(String buddy);

	/** Check to see if this buddy is trusted enough for special commands.
		*/
	boolean trusted(String buddy);

	/** Check to see if this buddy has admin priveledges.
		*/
	boolean admin(String buddy);

	/** Order the MUD to send its current log
		*/
	void maillog();

	/** Order the MUD to stop running.
	 */
	void halt();

	/** Order the MUD to resume after halt.
		*/
	void unhalt();

	/** Order the MUD to reboot.
		*/
	void reboot();

	/** Order the MUD to report its status.
		*/
	String info();

	/** Order the MUD to report its load.
		*/
	String load();

}
