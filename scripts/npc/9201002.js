/**
  High Priest John - 9201002.js
-- Original Author --------------------------------------------------------------------------------
	Jvlaple
-- Modified by -----------------------------------------------------------------------------------
	XoticMS.
---------------------------------------------------------------------------------------------------
 **/
importPackage(Packages.client);
importPackage(Packages.server);

var status;
var minLevel = 10;
var maxLevel = 200;
var mySelection = -1;
var rings = Array(1112001, 1112002, 1112003, 1112005, 1112006);

importPackage(Packages.tools);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (type == 1 && mode == 0)
            cm.sendOk("You're not reading to be married yet huh ? Well then I shall make you suck my balls :D");
        cm.dispose();
        return;
    }
	
    if (cm.getPlayer().getMapId() == 680000000) {
        if (status == 0) {
            cm.sendSimple("Hello #h #,\r\n#bWhat would you like to do today ?#b\r\n#L0#I want to get married!#l\r\n#L1#I want to see my friends wedding!#l\r\n#L2#I want to trade a Premium wedding ticket for 5 invitations!#l\r\n#L3#I want to buy a Premium Wedding Ticket for 25,000,000 Mesos!#l\r\n#L4#I would like to obtain a wedding permit.#l");
        } else if (status == 1) {
            if (selection == 0) {
                if (cm.getParty() == null) { // no party
                    cm.sendOk("You want to get married? Get in a party with your partner!");
                    cm.dispose();
                } else if (!cm.isLeader()) { // not party leader
                    cm.sendOk("Please ask your partner to talk to me.");
                    cm.dispose();
                } else {
                    var party = cm.getParty().getMembers();
                    var mapId = cm.getPlayer().getMapId();
                    var levelValid = 0;
                    var genderRight = 0;
                    var alreadyMarried = 0;
                    for (var i = 0; i < party.size(); i++) {
                        var pPlayer = party.get(i);
                        if (pPlayer.getLevel() >= minLevel && pPlayer.getLevel() <= maxLevel)
                            levelValid += 1;
                        if (pPlayer.getGender() == 0) {
                            genderRight += 1;
                        } else if (pPlayer.getGender() == 1) {
                            genderRight += 2;
                        }
                        if (pPlayer.isMarried() == 1) {
                            alreadyMarried += 1;
                        }
                    }
                    if (party.size() == 2) {
                        if (party.get(0).getGender() == 0) { // leader.
                            if (levelValid == 2 || cm.partyMembersInMap() == 2) {
                                if (genderRight == 3) {
                                    if (alreadyMarried == 0) {
                                        if (cm.getMeso() >= 0) {
                                            // Kick it into action.  Slate says nothing here, just warps you in.
                                            var em;
                                            if (em == null) {
                                                cm.sendOk("Marriage does not work. We are trying to fix this problem.");
                                            } else {
                                                // Begin the Wedding o.O
                                                em.startInstance(cm.getParty(), cm.getPlayer().getMap());
                                                party = cm.getPlayer().getEventInstance().getPlayers();
                                                cm.broadcastMessage(5, hname + " and " + wname + "'s Wedding is going to be started in Cathedral at Channel " + cm.getC().getChannel() +".");
                                                var eim = cm.getChar().getEventInstance();
                                                eim.setProperty("husband", party.get(0).getName());
                                                eim.setProperty("wife", party.get(1).getName());
                                            }
                                        } else
                                            cm.sendOk("You don't have the items required. Sorry");
                                    } else
                                        cm.sendOk("You're already married. Get lost");
                                } else
                                    cm.sendOk("Our server does not support gay/lesbian(s)");
                            } else
                                cm.sendOk("You guys need to be in the same map and be atleast level "+minLevel+" to "+maxLevel);
                        } else
                            cm.sendOk("Please make your husband leader");
                    } else
                        cm.sendOk("Get in a party with you and ONLY your wife.");
                    cm.dispose();
                }
            } else if (selection == 1) {
                if (cm.haveItem(5251100, 1)) {
                    cm.sendGetText("Please enter the name of one of the members of the Wedding.");
                } else {
                    cm.sendOk("Looks like the couple who you want to watch haven't given you an Invitation Ticket yet.");
                    cm.dispose();
                }
            } else if (selection == 2) {
                if (cm.haveItem(5251003, 1)) {
                    cm.gainItem(5251003, -1);
                    cm.gainItem(5251100,5);
                } else {
                    cm.sendOk("You don't have a Premium Wedding Ticket.");
                }
                cm.dispose();
            } else if (selection == 3) {
                if (cm.getMeso() >= 25000000) {
                    cm.gainMeso(-25000000);
                    cm.gainItem(5251003, 1);
                } else {
                    cm.sendOk("You don't have enough mesos.");
                }
                cm.dispose();
            } else if (selection == 4) {
                if (cm.getPlayer().getMarriageQuestLevel() == 50) {
                    cm.sendNext("Please go and visit Mom and Dad in their house. They live somewhere in Henesys Hunting Ground II.");
                    cm.getPlayer().addMarriageQuestLevel();
                } else if (cm.getPlayer().getMarriageQuestLevel() == 53) {
                    if (cm.haveItem(4031373, 1)) {
                        cm.sendNext("Great, Heres my permission.");
                        cm.removeAll(4031373);
                        cm.gainItem(4031374, 1);
                        cm.getPlayer().setMarriageQuestLevel(100);
                    } else {
                        cm.sendNext("You haven't got Mom and Dad's blessing.");
                    }
                } else {
                    cm.sendNext("I don't know what your'e talking about.");
                }
                cm.dispose();
            }
        } else if (status == 2) {
            var chr = cm.getCharByName(cm.getText());
            if (chr != null) {
                if (chr.getMapId() == 680000200) {
                    var eim = chrr.getEventInstance();
                    eim.registerPlayer(cm.getPlayer());
                } else {
                    cm.sendOk("The wedding you would like to attend has not started.");
                }
            } else
                cm.sendOk("Player was not found");
            cm.dispose();
        }
    } else if (cm.getPlayer().getMapId() == 680000210) {
        var eim = cm.getPlayer().getEventInstance();

        var husbandName = eim.getProperty("husband");
        var wifeName = eim.getProperty("wife");

        var husband = cm.getCharByName(husbandName);
        var wife = cm.getCharByName(wifeName);
			
        var id = cm.getPlayer().getId();
            
        var hclicked = eim.getProperty("hclicked");
        var wclicked = eim.getProperty("hclicked");

        var otherChar = husband == cm.getPlayer() ? wife : husband;
			
        if (husband != null && wife != null) {
            if (status == 0) {
                if (id  != husband.getId() && id  != husband.getId()) {
                    cm.sendOk("You are not getting married!");
                    cm.dispose();
                } else if (cm.getPlayer().isMarried() > 0) {
                    cm.sendOk("You have already been married.");
                    cm.dispose();
                } else if (hclicked == 1 && husbandName.equals(cm.getPlayer().getName())) {
                    cm.sendOk("You've already accepted to marry your wife, ask your wife to accept now");
                    cm.dispose();
                } else if (wclicked == 1 && wifeName.equals(cm.getPlayer().getName())) {
                    cm.sendOk("You've already accepted to marry your husband, ask your husband to accept now");
                    cm.dispose();
                } else {
                    cm.sendYesNo("Do you wish to marry your partner?\r\n\r\nThis will be a final desision.");
                }
            } else if (status == 1) {
                if (husband == cm.getPlayer())
                    eim.setProperty("hclicked", 1);
                else if (wife == cm.getPlayer())
                    eim.setProperty("wclicked", 1);
                else {
                    cm.sendOk("WTF ?");
                    cm.dispose();
                }

                if (eim.getProperty("hclicked") == 1 && eim.getProperty("hclicked") == 1) {
                    if (!cm.createMarriage(otherChar.getName())) {
                        cm.sendOk("The system cannot find your partner.");
                        cm.dispose();
                        return;
                    }
                    cm.broadcastMessage(5, "Congratulations to "+husbandName+" and "+wifeName+". Let these newly weds know that you're greatful and spam them ! :)");
                    cm.removeAll(4031374);
                    MapleInventoryManipulator.removeById(otherChar.getClient(), MapleInventoryType.USE, 4031374, otherChar.getItemQuantity(4031374, false), false, false);
                    if (!cm.makeRing(otherChar.getName(), Math.floor(Math.random() * rings.length))) {
                        cm.sendOk("The system could not find your partner.");
                    }
                    cm.dispose();
                }
            }
        }
    }
}