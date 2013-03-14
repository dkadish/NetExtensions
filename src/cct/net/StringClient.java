package cct.net;

import java.io.IOException;
import java.net.Socket;

import processing.core.PApplet;
import processing.net.Client;

public class StringClient extends GenericClient {

	public final static String VERSION = "##library.prettyVersion##";

	private String tempString = null;

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 * @throws IOException
	 */
	public StringClient(PApplet theParent, String host, int port)
			throws IOException {
		super(theParent, host, port);
	}

	public StringClient(PApplet theParent, Socket socket) throws IOException {
		super(theParent, socket);
	}

	@Override
	public int available() {
		if (tempString == null) {
			tempString = super.readString();// super.readStringUntil('\f');
		} else {
			String tmp = super.readString();
			if( tmp != null )
				tempString = tempString.concat(tmp);
		}

		if (tempString != null) {
			int i = tempString.indexOf('\f');
			if (i != -1) {
				/*String returnString = tempString.substring(0, i);
				if( i == tempString.length() ){
					tempString = null;
				} else {
					tempString = tempString.substring(i);
				}
				
				return returnString.length();*/
				return i;
			}
		}
		
		return 0;
	}

	public String readString() {
		String str = null;
		
		int i = available();
		if (i > 0) {
			str = tempString.substring(0, i);
			if( i == tempString.length() - 1)
				tempString = null;
			else
				tempString = tempString.substring(i);
		}

		return str;
	}

	public void writeString(String data) {
		StringBuffer s = new StringBuffer();
		s.append(data);
		s.append('\f');
		super.write(s.toString().getBytes());
	}

}
