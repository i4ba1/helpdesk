package id.co.knt.helpdesk.api.utilities;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class MACAddr {

	public static byte[] getMacAddress() throws Exception{
		InetAddress address = InetAddress.getLocalHost();
		NetworkInterface nwi = NetworkInterface.getByInetAddress(address);
		byte mac[] = nwi.getHardwareAddress();
		return mac;
	}
}
