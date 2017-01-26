package net.login.handler;

import client.MapleCharacter;
import client.MapleClient;
import java.security.NoSuchAlgorithmException;
import net.MaplePacketHandler;
import net.login.LoginWorker;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.KoreanDateUtil;
import java.util.Calendar;

public class LoginPasswordHandler implements MaplePacketHandler {

    @Override
    public boolean validateState(MapleClient c) {
        return !c.isLoggedIn();
    }

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {

        String login = slea.readMapleAsciiString();
        String pwd = slea.readMapleAsciiString();
        c.setAccountName(login);
        int loginok = 0;
        boolean ipBan = c.hasBannedIP();
        boolean macBan = c.hasBannedMac();
        try {
            loginok = c.login(login, pwd, ipBan || macBan);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        Calendar tempbannedTill = c.getTempBanCalendar();
        if (loginok == 0 && (ipBan || macBan)) {
            loginok = 3;
            if (macBan) {
                String[] ipSplit = c.getSession().getRemoteAddress().toString().split(":");
                MapleCharacter.ban(ipSplit[0], "Enforcing account ban, account " + login, false);
            }
        }
        if (loginok == 3) {
            c.getSession().write(MaplePacketCreator.getPermBan(c.getBanReason()));
            if (macBan) {
                c.getSession().write(MaplePacketCreator.serverNotice(1, "You are MAC banned. Please post an unban request at http://forum.voidms.com"));
            }
            return;
        } else if (loginok == 7) {
            c.getSession().write(MaplePacketCreator.getLoginFailed(loginok));
            return;
        } else if (loginok != 0) {
            if (macBan) {
                c.getSession().write(MaplePacketCreator.serverNotice(1, "You are MAC banned. Please post an unban request at http://forum.voidms.com"));
            } else {
                c.getSession().write(MaplePacketCreator.getLoginFailed(loginok));
            }
            return;
        } else if (tempbannedTill.getTimeInMillis() != 0) {
            long tempban = KoreanDateUtil.getTempBanTimestamp(tempbannedTill.getTimeInMillis());
            byte reason = c.getBanReason();
            c.getSession().write(MaplePacketCreator.getTempBan(tempban, reason));
            return;
        }
        if (c.isGm()) {
            LoginWorker.getInstance().registerGMClient(c);
        } else {
            LoginWorker.getInstance().registerClient(c);
            System.out.println("Registered");
        }
    }
}
