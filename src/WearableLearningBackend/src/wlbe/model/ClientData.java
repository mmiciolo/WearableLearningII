package wlbe.model;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

import wlbe.modules.Server;

public class ClientData {
	
	private Server serverModule;
	private AsynchronousServerSocketChannel serverSocket;
	private AsynchronousSocketChannel clientSocket;
	private ByteBuffer buffer;
	private boolean isRead;
	private int gameInstanceId;
    
    public ClientData() {
    	
    }
    
    public ClientData(Server serverModule, AsynchronousServerSocketChannel serverSocket, AsynchronousSocketChannel clientSocket, boolean isRead, int gameInstanceId) {
    	this.serverModule = serverModule;
    	this.serverSocket = serverSocket;
    	this.clientSocket = clientSocket;
    	this.buffer = ByteBuffer.allocate(65536);
    	this.isRead = isRead;
    	this.gameInstanceId = gameInstanceId;
    }
    
    public void setServerModule(Server serverModule) {
    	this.serverModule = serverModule;
    }
    
    public void setServerSocket(AsynchronousServerSocketChannel serverSocket) {
    	this.serverSocket = serverSocket;
    }
    
    public void setClientSocket(AsynchronousSocketChannel clientSocket) {
    	this.clientSocket = clientSocket;
    }
    
    public void setBuffer(ByteBuffer buffer) {
    	this.buffer = buffer;
    }
    
    public void setIsRead(boolean isRead) {
    	this.isRead = isRead;
    }
    
    public Server getServerModule() {
    	return this.serverModule;
    }
    
    public AsynchronousServerSocketChannel getServerSocket() {
    	return this.serverSocket;
    }
    
    public AsynchronousSocketChannel getClientSocket() {
    	return this.clientSocket;
    }
    
    public ByteBuffer getBuffer() {
    	return this.buffer;
    }
    
    public boolean getIsRead() {
    	return this.isRead;
    }
    
    public int getGameInstanceId() {
    	return this.gameInstanceId;
    }
}
