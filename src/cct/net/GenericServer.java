/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
 Server - basic network server implementation
 Part of the Processing project - http://processing.org

 Copyright (c) 2004-2007 Ben Fry and Casey Reas
 The previous version of this code was developed by Hernando Barragan

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General
 Public License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 Boston, MA  02111-1307  USA
 */

package cct.net;

import processing.core.*;
import processing.net.Client;
import sun.org.mozilla.javascript.ast.ArrayLiteral;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * ( begin auto-generated from Server.xml )
 * 
 * A server sends and receives data to and from its associated clients (other
 * programs connected to it). When a server is started, it begins listening for
 * connections on the port specified by the <b>port</b> parameter. Computers
 * have many ports for transferring data and some are commonly used so be sure
 * to not select one of these. For example, web servers usually use port 80 and
 * POP mail uses port 110.
 * 
 * ( end auto-generated )
 * 
 * @webref net
 * @usage application
 * @brief The server class is used to create server objects which send and
 *        receives data to and from its associated clients (other programs
 *        connected to it).
 * @instanceName server any variable of type Server
 */
public class GenericServer<T extends GenericClient> implements Runnable {
	PApplet parent;
	Method serverEventMethod;
	Class<T> clientClass;

	Thread thread;
	ServerSocket server;
	int port;

	/** Number of clients currently connected. */
	public int clientCount;
	/** Array of client objects, useful length is determined by clientCount. */
	public ArrayList<T> clients;

	/**
	 * @param parent
	 *            typically use "this"
	 * @param port
	 *            port used to transfer data
	 */
	public GenericServer(PApplet parent, int port, Class<T> clientClazz) {
		this.parent = parent;
		this.port = port;
		this.clientClass = clientClazz;

		try {
			server = new ServerSocket(this.port);
			// clients = new Vector();
			clients = new ArrayList<T>();

			thread = new Thread(this);
			thread.start();

			parent.registerMethod("dispose", this);

			// reflection to check whether host applet has a call for
			// public void serverEvent(Server s, Client c);
			// which is called when a new guy connects
			try {
				serverEventMethod = parent.getClass().getMethod("serverEvent",
						new Class[] { GenericServer.class, Client.class });
			} catch (Exception e) {
				// no such method, or an error.. which is fine, just ignore
			}

		} catch (IOException e) {
			e.printStackTrace();
			thread = null;
			// errorMessage("<init>", e);
		}
	}

	/**
	 * ( begin auto-generated from Server_disconnect.xml )
	 * 
	 * Disconnect a particular client.
	 * 
	 * ( end auto-generated )
	 * 
	 * ( end auto-generated )
	 * 
	 * @webref server:server
	 * @param client
	 *            the client to disconnect
	 */
	public void disconnect(T client) {
		// client.stop();
		client.dispose();
		clients.remove(client);
		/*
		 * int index = clientIndex(client); if (index != -1) {
		 * removeIndex(index); }
		 */
	}

	/*
	 * protected void removeIndex(int index) { clientCount--; // shift down the
	 * remaining clients for (int i = index; i < clientCount; i++) {
	 * clients.set(i, clients.get(i + 1)); } // mark last empty var for garbage
	 * collection clients.set(clientCount, null); }
	 * 
	 * protected void addClient(T client) { clientCount++; clients.add(client);
	 * }
	 * 
	 * protected int clientIndex(T client) { for (int i = 0; i < clientCount;
	 * i++) { if (clients.get(i) == client) { return i; } } return -1; }
	 */

	static public String ip() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	// the last index used for available. can't just cycle through
	// the clients in order from 0 each time, because if client 0 won't
	// shut up, then the rest of the clients will never be heard from.
	int lastAvailable = -1;

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
	public T available() {
		synchronized (clients) {
			int index = lastAvailable + 1;
			if (index >= clients.size())
				index = 0;

			ListIterator<T> i = clients.listIterator(index);
			while (i.hasNext()) {
				T client = i.next();
				if (client.available() > 0) {
					lastAvailable = i.previousIndex();
					return client;
				}
			}

			/*
			 * for (int i = 0; i < clientCount; i++) { int which = (index + i) %
			 * clientCount; T client = clients.get(which); if
			 * (client.available() > 0) { lastAvailable = which; return client;
			 * } }
			 */
		}
		return null;
	}

	/**
	 * ( begin auto-generated from Server_stop.xml )
	 * 
	 * Disconnects all clients and stops the server.
	 * 
	 * ( end auto-generated )
	 * 
	 * <h3>Advanced</h3>
	 * <p/>
	 * Use this to shut down the server if you finish using it while your applet
	 * is still running. Otherwise, it will be automatically be shut down by the
	 * host PApplet using dispose(), which is identical.
	 * 
	 * @webref server
	 * @usage application
	 */
	public void stop() {
		dispose();
	}

	/**
	 * Disconnect all clients and stop the server: internal use only.
	 */
	public void dispose() {
		thread = null;

		if (clients != null) {
			for (int i = 0; i < clientCount; i++) {
				disconnect(clients.get(i));
			}
			clientCount = 0;
			clients = null;
		}

		try {
			if (server != null) {
				server.close();
				server = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (Thread.currentThread() == thread) {
			try {
				Socket socket = server.accept();
				T client = T.getInstance(clientClass, parent, socket);
				synchronized (clients) {
					clients.add(client);
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
	}

	/**
	 * ( begin auto-generated from Server_write.xml )
	 * 
	 * Writes a value to all the connected clients. It sends bytes out from the
	 * Server object.
	 * 
	 * ( end auto-generated )
	 * 
	 * @webref server
	 * @brief Writes data to all connected clients
	 * @param data
	 *            data to write
	 */
	public void write(int data) { // will also cover char
		int index = 0;
		ListIterator<T> i = clients.listIterator();
		while (i.hasNext()) {
			T client = i.next();
			// try{
			client.write(data);
			// } catch( SocketException e){
			//
			// }
			if (!client.active()) {
				i.remove();
			}
		}
	}

	public void write(byte data[]) {
		int index = 0;
		ListIterator<T> i = clients.listIterator();
		while (i.hasNext()) {
			T client = i.next();
			client.write(data);
			if (!client.active()) {
				i.remove();
			}
		}
	}

	public void write(String data) {
		int index = 0;
		ListIterator<T> i = clients.listIterator();
		while (i.hasNext()) {
			T client = i.next();
			client.write(data);
			if (!client.active()) {
				i.remove();
			}
		}
	}

	/**
	 * General error reporting, all corralled here just in case I think of
	 * something slightly more intelligent to do.
	 */
	// public void errorMessage(String where, Exception e) {
	// parent.die("Error inside Server." + where + "()", e);
	// //System.err.println("Error inside Server." + where + "()");
	// //e.printStackTrace(System.err);
	// }
}