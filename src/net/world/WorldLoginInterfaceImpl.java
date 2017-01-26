package net.world;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import net.channel.remote.ChannelWorldInterface;
import net.world.guild.MapleGuildCharacter;
import net.world.remote.WorldLoginInterface;

public class WorldLoginInterfaceImpl extends UnicastRemoteObject implements WorldLoginInterface {

    private static final long serialVersionUID = -4965323089596332908L;

    public WorldLoginInterfaceImpl() throws RemoteException {
        super(0, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
    }

    public Properties getDatabaseProperties() throws RemoteException {
        return WorldServer.getInstance().getDbProp();
    }

    public Properties getWorldProperties() throws RemoteException {
        return WorldServer.getInstance().getWorldProp();
    }

    public boolean isAvailable() throws RemoteException {
        return true;
    }

    public Map<Integer, Integer> getChannelLoad() throws RemoteException {
        Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
        for (ChannelWorldInterface cwi : WorldRegistryImpl.getInstance().getAllChannelServers()) {
            ret.put(cwi.getChannelId(), cwi.getConnected());
        }
        return ret;
    }

    @Override
    public void deleteGuildCharacter(MapleGuildCharacter mgc) throws RemoteException {
        WorldRegistryImpl wr = WorldRegistryImpl.getInstance();
        // Ensure it's loaded on world server.
        wr.setGuildMemberOnline(mgc, false, -1);

        if (mgc.getGuildRank() > 1) // Not leader.
        {
            wr.leaveGuild(mgc);
        } else {
            wr.disbandGuild(mgc.getGuildId());
        }
    }

    public boolean isConnected(String accname) throws RemoteException {
        for (ChannelWorldInterface cwi : WorldRegistryImpl.getInstance().getAllChannelServers()) {
            if (cwi.accountConnected(accname)) {
                return true;
            }
        }//TODO WorldServer.getInstance().
        return false;
    }
}
