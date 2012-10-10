/*
 * Copyright (C) 2012 Tobias Brunner
 * Hochschule fuer Technik Rapperswil
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.  See <http://www.fsf.org/copyleft/gpl.txt>.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 */

package org.strongswan.android.logic;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkManager
{
	public NetworkManager()
	{
	}

	/**
	 * Function that retrieves a local address of the given family.
	 *
	 * @param ipv4 true to return an IPv4 address, false for IPv6
	 * @return string representation of an IPv4 address, or null if none found
	 */
	public String getLocalAddress(boolean ipv4)
	{
		try
		{
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			if (en == null)
			{	/* no interfaces at all */
				return null;
			}
			while (en.hasMoreElements())
			{
				NetworkInterface intf = en.nextElement();
				if (intf.isLoopback() || !intf.isUp() ||
					intf.getName().startsWith("tun"))
				{
					continue;
				}
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				while (enumIpAddr.hasMoreElements())
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (inetAddress.isLoopbackAddress())
					{
						continue;
					}
					if ((ipv4 && inetAddress instanceof Inet4Address) ||
						(!ipv4 && inetAddress instanceof Inet6Address))
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch (SocketException ex)
		{
			ex.printStackTrace();
			return null;
		}
		return null;
	}
}
