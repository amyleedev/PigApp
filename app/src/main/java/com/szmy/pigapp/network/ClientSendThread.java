package com.szmy.pigapp.network;

import android.util.Log;

import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientSendThread{

	private Socket mSocket;
	private String mStrToSend;
	
	public synchronized void start(Socket socket, String str0) {
		this.mSocket = socket;
		this.mStrToSend = str0;
		
		try {
			OutputStreamWriter ost0=new OutputStreamWriter(mSocket.getOutputStream());
			ost0.write(mStrToSend+"\n");
			ost0.flush();
			Log.d("mStrToSend = " +mStrToSend, "++++++++++++++++++++++++++" +
					"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch(Exception e){
			Log.d("cannot send to ","++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
	}
}
