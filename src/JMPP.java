import java.util.*;
import java.io.*;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class JMPP implements MessageListener {

	XMPPConnection connection;

	public void login(String user, String pw) throws XMPPException {
		connection = new XMPPConnection("jabber.org");
		connection.connect();
		connection.login(user, pw);
	}

	public void sendMessage(String message, String to) throws XMPPException {
		Chat chat = connection.getChatManager().createChat(to, this);
		chat.sendMessage(message);
	}

	public void displayBuddyList() {
		Roster roster = connection.getRoster();
		Collection<RosterEntry> entries = roster.getEntries();

		System.out.println("***");
		System.out.println(entries.size() + " BuddyList:");
		for (RosterEntry r : entries) {
			System.out.println(r.getUser());
		}
		System.out.println("***");
	}

	public void disconnect() {
		connection.disconnect();
	}

	public void processMessage(Chat chat, Message message) {
		if (message.getType() == Message.Type.chat)
			System.out.println(chat.getParticipant() + ": "
					+ message.getBody());
	}

	public static void main(String args[]) throws XMPPException, IOException {
		JMPP jmpp = new JMPP();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg;

		XMPPConnection.DEBUG_ENABLED = false;

		System.out.println("Username:");
		String user = br.readLine();
		System.out.println("Password:");
		String pw = br.readLine();
		
		jmpp.login(user, pw);

		System.out.println("Enter contact email address:");
		String talkTo = br.readLine();
		System.out.println("All messages will be sent to " + talkTo + "\n\n");

		//Switch statements can only handle String as of Java7 so I opt not to use it
		while (true) {
			msg = br.readLine();
			if (msg.equals(EXIT_C)) {
				System.out.println("Good bye");
				break;
			}
			else if (msg.equals(DISPLAY_C)) {
				jmpp.displayBuddyList();
			}
			else {
				jmpp.sendMessage(msg, talkTo);
			}
		} 

		jmpp.disconnect();
	}
	
	static final String EXIT_C = "/exit";
	static final String ADD_C = "/add";
	static final String DISPLAY_C = "/display";

}
