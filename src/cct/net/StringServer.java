/**
 * ##StringServer##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package cct.net;

import java.io.IOException;
import java.net.Socket;

import processing.core.*;
import processing.net.Client;
import processing.net.Server;

//TODO: Overload the client functionality of the server so that all clients are StringClients

/**
 * This is a template class and can be used to start a new processing library or
 * tool. Make sure you rename this class as well as the name of the example
 * package 'template' to your own library or tool naming convention.
 * 
 * @example Hello
 * 
 *          (the tag @example followed by the name of an example included in
 *          folder 'examples' will automatically include the example in the
 *          javadoc.)
 * 
 */

public class StringServer extends GenericServer<StringClient> {

	public StringClient[] clients;

	public final static String VERSION = "##library.prettyVersion##";

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public StringServer(PApplet theParent, int port) {
		super(theParent, port, StringClient.class);
	}

	// the last index used for available. can't just cycle through
	// the clients in order from 0 each time, because if client 0 won't
	// shut up, then the rest of the clients will never be heard from.
	//int lastAvailable = -1;

	/**
	 * ( begin auto-generated from Server_available.xml )
	 * 
	 * Returns the next client in line with a new message.
	 * 
	 * ( end auto-generated )
	 * 
	 * ( end auto-generated )
	 * 
	 * @webref server
	 * @usage application
	 */
	/*public StringClient available() {
		synchronized (clients) {
			int index = lastAvailable + 1;
			if (index >= clientCount)
				index = 0;

			for (int i = 0; i < clientCount; i++) {
				int which = (index + i) % clientCount;
				StringClient client = clients[which];
				if (client.available() > 0) {
					lastAvailable = which;
					return client;
				}
			}
		}
		return null;
	}

	//TODO: make the Clients into StringClients
	public void run() {
		while (Thread.currentThread() == thread) {
			try {
				Socket socket = server.accept();
				StringClient client = new StringClient(parent, socket);
				synchronized (clients) {
					addClient(client);
					if (serverEventMethod != null) {
						try {
							serverEventMethod.invoke(parent, new Object[] {
									this, client });
						} catch (Exception e) {
							System.err
									.println("Disabling serverEvent() for port "
											+ port);
							e.printStackTrace();
							serverEventMethod = null;
						}
					}
				}
			} catch (IOException e) {
				// errorMessage("run", e);
				e.printStackTrace();
				thread = null;
			}
			try {
				Thread.sleep(8);
			} catch (InterruptedException ex) {
			}
		}
	}*/

	/**
	 * write a string of data followed by the '\f' character
	 * 
	 * @param data
	 *            string to write to the clients
	 */
	public void writeString(String data) {
		StringBuffer s = new StringBuffer();
		s.append(data);
		s.append('\f');
		super.write(s.toString());
	}

}
