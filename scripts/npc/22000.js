//Multi-Purpose NPC
//Author: Moogra
 


importPackage(Packages.client);
importPackage(Packages.scripting.npc);

var npcid = Array(9000020, 9200000, 2003, 1061008, 2010007, 2010008, 9010009, 1052017, 22000, 2131000, 2040030, 2110000);
var selected = 1;

function start() {
    cm.sendSimple("Hi there! What do you want to do?#b\r\n\r\n#L0#Travel Around the Maple World\r\n#L1#Job Advance\r\n#L2#Reset Stats\r\n#L3#Visit the All in One Shop\r\n#L8#Open the Wish Ticket Exchanger\r\n#L9#Open the Vote Point Exchanger\r\n#L10#Open the Items Vendor\r\n#L11#Create a Guild Hideout\r\n#L4#Create or Edit a Guild");
}

function action(m, t, s) {
    if (s == 8) {
        cm.openNpc(2111004);        
    } else if (s == 4){
        cm.warp(200000301);
    } else {
        if(npcid[s] > 0){
            cm.openNpc(npcid[s]);
        } else {
            cm.dispose();
        }
    }
}