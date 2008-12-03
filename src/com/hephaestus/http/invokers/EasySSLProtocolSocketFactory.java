package com.hephaestus.http.invokers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

public class EasySSLProtocolSocketFactory implements
		SecureProtocolSocketFactory {
	
	private SSLContext sslcontext;

	public EasySSLProtocolSocketFactory() {
		super();
	}
	
    private static SSLContext createEasySSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL"); //$NON-NLS-1$
            context.init(
              null, 
              new TrustManager[] {new EasyX509TrustManager(null)}, 
              null);
            return context;
        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
            throw new HttpClientError(e.toString());
        }
    }
    
    private SSLContext getSSLContext() {
        if (this.sslcontext == null) {
            this.sslcontext = createEasySSLContext();
        }
        return this.sslcontext;
    }

	public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
			throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
                socket,
                host,
                port,
                autoClose
            );
	}

	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
                host,
                port
            );
	}

	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
			throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
                host,
                port,
                clientHost,
                clientPort);
	}

	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null"); //$NON-NLS-1$
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
            return socket;
        }
	}
	
    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().equals(EasySSLProtocolSocketFactory.class));
    }

    public int hashCode() {
        return EasySSLProtocolSocketFactory.class.hashCode();
    }

}
