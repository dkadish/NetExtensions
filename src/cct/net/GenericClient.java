package cct.net;
import processing.core.*;
import processing.net.Client;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

/**
   * ( begin auto-generated from Client.xml )
   * 
   * A client connects to a server and sends data back and forth. If anything 
   * goes wrong with the connection, for example the host is not there or is 
   * listening on a different port, an exception is thrown.
   * 
   * ( end auto-generated )
 * @webref net
 * @brief The client class is used to create client Objects which connect to a server to exchange data. 
 * @instanceName client any variable of type Client
 * @usage Application
 * @see_external LIB_net/clientEvent
 */

public abstract class GenericClient extends Client{

    public GenericClient(PApplet parent, String host, int port) throws IOException {
		super(parent, host, port);
	}

    public GenericClient(PApplet parent, Socket socket) throws IOException {
		super(parent, socket);
	}
    
//    /**
//     * ( begin auto-generated from Client_write.xml )
//     * 
//     * Writes data to a server specified when constructing the client.
//     * 
//     * ( end auto-generated )
//     * @webref client:client
//     * @usage application
//     * @brief  	Writes bytes, chars, ints, bytes[], Strings
//     * @param data data to write
//     * @throws SocketException 
//     */
//    public void write(int data) throws SocketException{  // will also cover char
//      try {
//        output.write(data & 0xff);  // for good measure do the &
//        output.flush();   // hmm, not sure if a good idea
//
//      } catch (SocketException e){
//    	  throw e;
//      } catch (Exception e) { // null pointer or serial port dead
//        //errorMessage("write", e);
//        //e.printStackTrace();
//        //dispose();
//        //disconnect(e);
//        e.printStackTrace();
//        stop();
//      }
//    }
//
//    public void write(byte data[]) throws SocketException {
//      try {
//        output.write(data);
//        output.flush();   // hmm, not sure if a good idea
//
//      } catch (Exception e) { // null pointer or serial port dead
//        //errorMessage("write", e);
//        //e.printStackTrace();
//        //disconnect(e);
//        e.printStackTrace();
//        stop();
//      }
//    }
    
	public static <D extends GenericClient> D getInstance(Class<D> _class, PApplet parent, String host, int port)
    {
        try
        {
        	Class[] types = new Class[]{ PApplet.class, String.class, int.class };
        	Object[] args = new Object[]{parent, host, port};
            return _class.getConstructor(types).newInstance(args);
        }
        catch (Exception _ex)
        {
            _ex.printStackTrace();
        }
        return null;
    }

    public static <D extends GenericClient> D getInstance(Class<D> _class, PApplet parent, Socket socket)
    {
        try
        {
        	Class[] types = new Class[]{ PApplet.class, Socket.class};
        	Object[] args = new Object[]{parent, socket};
            return _class.getConstructor(types).newInstance(args);
        }
        catch (Exception _ex)
        {
            _ex.printStackTrace();
        }
        return null;
    }
}
