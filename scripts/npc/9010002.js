/* * * * * * * * * * * * * * * *\
 *      Super Rebirth NPC      *
 *  By Hugo of MadStory/VoidMS *
 *      gugubro1@gmail.com     *
 *         madstory.org        *
 *          voidms.com         *
\* * * * * * * * * * * * * * * */

var status = 0;
var stat = 0;
var srb = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    } else {
        if (mode == 0 && type == 1) {
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            if (cm.isFull()) {
                cm.sendOk("You dont have room in your #rEquip Inventory#k.\r\nMake sure to make space before you SRB.");
                cm.dispose();
            } else {
                cm.sendSimple("#eHey #h #, I am the #rSuper Rebirth#k NPC. I can exchange your stats for srb rings and upgrate the ones you already have. What do you want to do?\r\n\r\n#b#L1#SRB1\r\n#L2#SRB2\r\n#L3#SRB3\r\n#L4#SRB4#l");
            }
        } else if (status == 1) {
            srb = selection;
            if (selection == 4) {
                status = 1;
                action(1, 0, 0);
            } else {
                cm.sendSimple("#ePlease choose which stat you would like to SRB.\r\n\r\b#b#L1#STR\r\n#L2#DEX\r\n#L3#INT\r\n#L4#LUK#l");
            }
        } else if (status == 2) {
            stat = selection;
            if (srb == 1) {
                cm.sendYesNo("#eThis will take #r20,000#k AP off a selected stat and give you a #bSRB1#K ring with #r10,000#k of the same selected stat. This will also cost #r1#k vote points and #r1#k #i4002000#.\r\n\r\n#rAre you sure you want to do this?#k");
            } else if (srb == 2) {
                cm.sendYesNo("#eThis will take #rfour#k #bSRB1#k rings and replace it with a #bSRB2#k ring with #r20,000#k of the same stat. This will also cost #r2#k vote points and #r2#k #i4002000#.\r\n\r\n#rAre you sure you want to do this?#k");
            } else if (srb == 3) {
                cm.sendYesNo("#eThis will take #rfour#k #bSRB2#k rings and replace it with a #bSRB3#k ring with #r30,000#k of the same stat. This will also cost #r3#k vote points and #r3#k #i4002000#.\r\n\r\n#rAre you sure you want to do this?#k");
            } else if (srb == 4) {
                cm.sendYesNo("#eThis will take #rone of each#k different #bSRB3#k ring and exchange it for a #bSRB4#k ring with with #r30,000#k of all stats and #r50#k Weapon Attack. This will also cost #r4#k vote points and #r4#k #i4002000#.\r\n\r\n#rAre you sure you want to do this?#k");
            }
        } else if (status == 3) {
            var p = cm.getPlayer();
            if (srb == 1) {
                var statup = new java.util.ArrayList();
                if (p.votePoints() > 0 && cm.itemQuantity(4002000) > 0) {
                    var str = p.getStr();
                    var dex = p.getDex();
                    var Int = p.getInt();
                    var luk = p.getLuk();
                    if (stat == 1) {// STR
                        if (str >= 20000) {
                            str = str - 20000;
                            cm.makeItem(1112119, p, 10000, 0, 0, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have #r20,000#k STR");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 2) {// DEX
                        if (dex >= 20000) {
                            dex = dex - 20000;
                            cm.makeItem(1112206, p, 0, 10000, 0, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have #r20,000#k DEX");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 3) {// INT
                        if (Int >= 20000) {
                            Int = Int - 20000;
                            cm.makeItem(1112117, p, 0, 0,  10000, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have #r20,000#k INT");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 4) {// LUK
                        if (luk >= 20000) {
                            luk = luk - 20000;
                            cm.makeItem(1112207, p, 0, 0, 0, 10000, 0);
                        } else {
                            cm.sendOk("#eYou don't have #r20,000#k LUK");
                            cm.dispose();
                            return;
                        }
                    }
                    p.changeVotePoints(-1);
                    cm.loseItem(4002000, 1);
                    statup.add(new Packages.tools.Pair(Packages.client.MapleStat.STR, java.lang.Integer.valueOf(str)));
                    statup.add (new Packages.tools.Pair(Packages.client.MapleStat.LUK, java.lang.Integer.valueOf(luk)));
                    statup.add(new Packages.tools.Pair(Packages.client.MapleStat.DEX, java.lang.Integer.valueOf(dex)));
                    statup.add(new Packages.tools.Pair(Packages.client.MapleStat.INT, java.lang.Integer.valueOf(Int)));
                    p.setDex(dex);
                    p.setStr(str);
                    p.setInt(Int);
                    p.setLuk(luk);
                    p.getClient().getSession().write(Packages.tools.MaplePacketCreator.updatePlayerStats(statup));
                } else {
                    cm.sendOk("#eYou either do not have #r1#k #i4002000# or less than #r1#k vote point.");
                    cm.dispose();
                    return;
                }
            } else if (srb == 2) {
                if (p.votePoints() > 1 && cm.itemQuantity(4002000) > 1) {
                    if (stat == 1) { // STR
                        if (cm.itemQuantity(1112119) > 3) {
                            cm.loseItem(1112119, 4);
                            cm.makeItem(1112120, p, 20000, 0, 0, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have at least #rfour#k #i1112119#\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 2) { // DEX
                        if (cm.itemQuantity(1112206) > 3) {
                            cm.loseItem(1112206, 4);
                            cm.makeItem(1112214, p, 0, 20000, 0, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have at least #rfour#k #i1112206#\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 3) { // INT
                        if (cm.itemQuantity(1112117) > 3) {
                            cm.loseItem(1112117, 4);
                            cm.makeItem(1112232, p, 0, 0, 20000, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have at least #rfour#k #i1112206#\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 4) { // LUK
                        if (cm.itemQuantity(1112207) > 3) {
                            cm.loseItem(1112207, 4);
                            cm.makeItem(1112215, p, 0, 0, 0, 20000, 0);
                        } else {
                            cm.sendOk("#eYou don't have at least #rfour#k #i1112207#\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                            cm.dispose();
                            return;
                        }
                    }
                    p.changeVotePoints(-2);
                    cm.loseItem(4002000, 2);
                } else {
                    cm.sendOk("#eYou either do not have #r2#k #i4002000# or less than #r2 vote points.");
                    cm.dispose();
                    return;
                }
            } else if (srb == 3) {
                if (p.votePoints() > 2 && cm.itemQuantity(4002000) > 2) {
                    if (stat == 1) { // STR
                        if (cm.itemQuantity(1112120) > 3) {
                            cm.loseItem(1112120, 4);
                            cm.makeItem(1112230, p, 30000, 0, 0, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have at least #rfour#k #i1112120#\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 2) { // DEX
                        if (cm.itemQuantity(1112214) > 3) {
                            cm.loseItem(1112214, 4);
                            cm.makeItem(1112211, p, 0, 30000, 0, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have at least #rfour#k #i1112214#\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 3) { // INT
                        if (cm.itemQuantity(1112232) > 3) {
                            cm.loseItem(1112232, 4);
                            cm.makeItem(1112122, p, 0, 0, 30000, 0, 0);
                        } else {
                            cm.sendOk("#eYou don't have at least #rfour#k #i1112232#\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                            cm.dispose();
                            return;
                        }
                    } else if (stat == 4) { // LUK
                        if (cm.itemQuantity(1112215) > 3) {
                            cm.loseItem(1112215, 4);
                            cm.makeItem(1112106, p, 0, 0, 0, 30000, 0);

                        } else {
                            cm.sendOk("#eYou don't have at least #rfour#k #i1112215#\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                            cm.dispose();
                            return;
                        }
                    }
                    p.changeVotePoints(-3);
                    cm.loseItem(4002000, 3);
                } else {
                    cm.sendOk("#eYou either do not have #r3#k #i4002000# or less than #r3 vote points.");
                    cm.dispose();
                    return;
                }
            } else if (srb == 4) {
                if (p.votePoints() > 3 && cm.itemQuantity(4002000) > 3) {
                    if (cm.itemQuantity(1112230) > 0 && cm.itemQuantity(1112211) > 0 && cm.itemQuantity(1112122) > 0 && cm.itemQuantity(1112106) > 0) {
                        cm.loseItem(1112230, 1);
                        cm.loseItem(1112211, 1);
                        cm.loseItem(1112122, 1);
                        cm.loseItem(1112106, 1);
                        cm.makeItem(1112103, p, 30000, 30000, 30000, 30000, 50);
                        p.changeVotePoints(-4);
                        cm.loseItem(4002000, 4);
                    } else {
                        cm.sendOk("#eYou don't have at least #rone of each#k different #bSRB3#k ring\r\n\r\n#rMake sure you have it in your inventory, not equipped.");
                        cm.dispose();
                        return;
                    }
                } else {
                    cm.sendOk("#eYou either do not have #r4#k #i4002000# or less than #r4 vote points.");
                    cm.dispose();
                    return;
                }
            }
            //cm.reloadChar();
            cm.sendOk("#eYou have successfully Super Rebirthed!");
            cm.dispose();
        }
    }
}