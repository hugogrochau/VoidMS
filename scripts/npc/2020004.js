//1072349, 1002736, 1452060, 1442068, 1382060, 1022060, 1012106, 1432048, 1012076, 1012077, 1012078, 1012079, 1022058, 1302089, 1302088, 1302090, 1302091, 1302092, 1302094, 2041200, 1122001, 1122002, 1122003, 1122004, 1122005, 1122006, 1082246, 1072330, 1072333, 1072334, 1072341, 1062076, 1012084, 1002723, 4080000, 4080001, 4080002, 4080003, 4080004, 4080005, 4080006, 4080007, 4080008, 4080009, 4080010, 4080011

var item = Array();
var slimenum = 999999; // Number of Item needed
var youritem = 4001013;  // Item Needed

function start() {
    var text = "Npc has been disabled.";
    for(var i = 0; i < item.length ; i++)
    text += "#L" + i + "##t" + item[i] + "##l\r\n";
    cm.sendSimple(text);
}
function action(mode, type, selection) {
    if(cm.haveItem(youritem, slimenum)) {
        cm.gainItem(item[selection], 1);
        cm.gainItem(youritem, -slimenum);
        cm.sendOk("#bYou have recieved your prize.");
        cm.dispose();
} else {
        cm.sendSimple("Sad isnt it.");
    }
    cm.dispose();
}  