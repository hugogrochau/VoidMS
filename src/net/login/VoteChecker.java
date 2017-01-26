package net.login;

import database.DatabaseConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * GTOP Vote Checker
 * @author AskHugo
 * MIT Licensed
 */
public class VoteChecker implements Runnable {

    private final String[] details = {
        "GTOPID",
        "GTOP_PASSWORD"
    };
    private final boolean silent = false;

    public void run() {
        log("Updating Vote Logs");
        Document doc = makeDoc("http://www.gtop100.com/report1.php?siteid=" + details[0] + "&pass=" + details[1]);
        if (doc != null) {
            NodeList entries = doc.getElementsByTagName("entry");
            for (int i = 0; i < entries.getLength(); i++) {
                Node entryNode = entries.item(i);
                if (entryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element entryElement = (Element) entryNode;
                    Element ipElement = (Element) entryElement.getElementsByTagName("ip").item(0);
                    String ip = ((Node) ipElement.getChildNodes().item(0)).getNodeValue().trim();
                    Element timeElement = (Element) entryElement.getElementsByTagName("time").item(0);
                    long time = Long.parseLong(((Node) timeElement.getChildNodes().item(0)).getNodeValue().trim());
                    if (!voteCounted(ip, time)) {
                        logVote(ip, time);
                        countVote(ip);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd/MM");
                        log("IP: " + ip + " voted at: " + sdf.format(new Date(time)));
                    }
                }
            }
        } else {
            log("Your GTOP details are wrong");
        }
    }

    public static Document makeDoc(String site) {
        try {
            URL url = new URL(site);
            InputStream is = url.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(is);
            is.close();
            d.getDocumentElement().normalize();
            return d;
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            return null;
        } catch (SAXException saxe) {
            saxe.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    public static boolean voteCounted(String ip, long time) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT `id` FROM `gtopvotes` WHERE `ip`=? AND `timestamp`=?");
            ps.setString(1, ip);
            ps.setLong(2, time);
            ResultSet rs = ps.executeQuery();
            boolean counted = rs.next();
            rs.close();
            ps.close();
            return counted;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean logVote(String ip, long time) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO `gtopvotes` (`ip`,`timestamp`) VALUES (?,?)");
            ps.setString(1, ip);
            ps.setLong(2, time);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean countVote(String ip) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE `accounts` "
                                                      + "SET `votepoints` = `votepoints` + 1 "
                                                      + "WHERE `lastknownip`= ? "
                                                      + "ORDER BY `lastlogin` DESC LIMIT 1");
            ps.setString(1, ip);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    private void log(String msg) {
        if (!silent) {
            System.out.println("[VoteChecker] " + msg);
        }
    }
}
