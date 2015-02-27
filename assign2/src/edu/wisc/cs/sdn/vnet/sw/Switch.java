package edu.wisc.cs.sdn.vnet.sw;

import net.floodlightcontroller.packet.Ethernet;
import edu.wisc.cs.sdn.vnet.Device;
import edu.wisc.cs.sdn.vnet.DumpFile;
import edu.wisc.cs.sdn.vnet.Iface;
import java.util.*;
import net.floodlightcontroller.packet.MACAddress;
import java.util.concurrent.locks.*;
/**
 * @author Aaron Gember-Jacobson
 */
public class Switch extends Device
{	
	/**
	 * Creates a router for a specific host.
	 * @param host hostname for the router
	 */
        Map<MACAddress, Map<Iface, Long>> SwitchTable;
    
	public Switch(String host, DumpFile logfile)
	{
		super(host,logfile);
		SwitchTable = new HashMap<MACAddress,Map<Iface, Long>>();
		//(new  SwitchEntryThread()).start();
	}

	/**
	Method to broadcast a packet on all interfaces except the one on which it arrived
	*/


        public void broadcastPacket(Ethernet etherPacket, Iface inIface) {
	    System.out.println("Switch :: " + getHost()+"BROADCASTING!!");
	    for(Map.Entry<String, Iface> entry: getInterfaces().entrySet()) {
		if(entry.getValue() != inIface) {
		    sendPacket(etherPacket, entry.getValue());
		}
	    }
	}		
	/**
	 * Handle an Ethernet packet received on a specific interface.
	 * @param etherPacket the Ethernet packet that was received
	 * @param inIface the interface on which the packet was received
	 */
        
	public void handlePacket(Ethernet etherPacket, Iface inIface)
	{
		System.out.println("*** -> Received packet: " +
                etherPacket.toString().replace("\n", "\n\t"));
		
		/********************************************************************/
		/* TODO: Handle packets                                             */
		
		// Check if Source MAC is mapped else create an entry
		if(SwitchTable.containsKey(etherPacket.getSourceMAC())) {
		    
		    Iterator srcentryit = SwitchTable.get(etherPacket.getSourceMAC()).entrySet().iterator();
		    Map.Entry srctimeoutentry = (Map.Entry) srcentryit.next(); // Get the time field in ms
		    System.out.println("Time Elapsed :: " + (System.currentTimeMillis() - (long) srctimeoutentry.getValue())/1000);
		    SwitchTable.get(etherPacket.getSourceMAC()).put(inIface, System.currentTimeMillis());
		    
		    // Check if Dest Mac is mapped else broadcast the packet
		    if(SwitchTable.containsKey(etherPacket.getDestinationMAC())) {
			System.out.println("Destination lookup" +SwitchTable.get(etherPacket.getDestinationMAC()).keySet());
			Set destInt = SwitchTable.get(etherPacket.getDestinationMAC()).keySet();
			Iterator it = destInt.iterator();
			Iterator destentryit = SwitchTable.get(etherPacket.getDestinationMAC()).entrySet().iterator();
			Map.Entry desttimeoutentry = (Map.Entry) destentryit.next(); // Get the time field in ms
			System.out.println("Time Elapsed :: " + (System.currentTimeMillis() - (long) desttimeoutentry.getValue())/1000);
			if((System.currentTimeMillis() - (long) desttimeoutentry.getValue())/1000 <=15) {
			    sendPacket(etherPacket, (Iface) it.next());
			}
			else {
			    System.out.println("Time Elapsed !!!!!! ");
			    SwitchTable.remove(etherPacket.getDestinationMAC());
			    broadcastPacket(etherPacket, inIface);
			}
		    }
		    else {
			broadcastPacket(etherPacket, inIface);
		    }
		}
		else {
		    SwitchTable.put(etherPacket.getSourceMAC(), new HashMap<Iface, Long>()); 
		    SwitchTable.get(etherPacket.getSourceMAC()).put(inIface, System.currentTimeMillis());
		    
		    if(SwitchTable.containsKey(etherPacket.getDestinationMAC())) {
			System.out.println("Destination lookup" +SwitchTable.get(etherPacket.getDestinationMAC()).keySet());
			Set destInt = SwitchTable.get(etherPacket.getDestinationMAC()).keySet();
			Iterator it = destInt.iterator();
			Iterator destentryit = SwitchTable.get(etherPacket.getDestinationMAC()).entrySet().iterator();
			Map.Entry desttimeoutentry = (Map.Entry) destentryit.next(); // Get the time field in ms
			System.out.println("Time Elapsed :: " + (System.currentTimeMillis() - (long) desttimeoutentry.getValue())/1000);
			if((System.currentTimeMillis() - (long) desttimeoutentry.getValue())/1000 <=15) {
			    sendPacket(etherPacket, (Iface) it.next());
			}
			else {
			    SwitchTable.remove(etherPacket.getDestinationMAC());
			    broadcastPacket(etherPacket, inIface);
			}
		    }
		    else {
			//SwitchTable.remove(etherPacket.getDestinationMAC());
			broadcastPacket(etherPacket, inIface);
		    }
		}
		
		
		Iterator iter = SwitchTable.entrySet().iterator();
		while(iter.hasNext()) {
		    Map.Entry entry = (Map.Entry)iter.next(); 
		    Iterator itert = SwitchTable.get(entry.getKey()).entrySet().iterator();
		    Map.Entry timeentryMap = (Map.Entry) itert.next();
		    System.out.println("Switch:: " + getHost() +"MAP Output: " + entry.getKey() + ":" + entry.getValue() + "Time :" + timeentryMap.getValue());	
       	
		}
		    /********************************************************************/
	}
}    

