/*function start() { chatez, If you read this. Disabling this, waiting for your reply to see If you're fine with me using chairs as gachapon statue
cm.sendOk("#rCurrently Disabled");
cm.dispose();
}*/
/*var wui = 0;
var mounts = [1902014,1902011,1902017,1902015,1902016,1902019,1902020,1902021,1902022,1902023,1902024,1902025,1902027,1902028,1902031,1902032,1902033,1902034,1902035,1902036,1902037,1902038,1902039,1902040,1902041,1902042,1902043,1902044,1902045,1902046,1902050,1902056];
var saddles =[1912010,1912007,1912011,1912011,1912011,1912012,1912013,1912014,1912015,1912016,1912017,1912018,1912020,1912021,1912024,1912025,1912026,1912027,1912028,1912029,1912030,1912031,1912032,1912033,1912034,1912035,1912036,1912037,1912038,1912039,1912050,1912049];


var test = "";

function start() {
    for (var i = 0; i < mounts.length; i++) {
test += "\r\n#L" + i + "##v" + mounts[i] + "# for 50 #v4031137#.";
}

if (cm.isFull()) {
	cm.sendOk("Make some room before you lose your items.");
} else {
   cm.sendSimple("Hello #e#h #.#n  I'm the VoidMS Mount Exchanger! You can exchange Box' of Presents for mounts here! You also get the appropriate saddle with your purchase.  \r Which mount would you want?\r"+test);
   }


}

function action(mode, type, selection) {
if (mounts[selection] > 0){
    if(cm.itemQuantity(4031137) >= 50){
        cm.gainItem(mounts[selection],1);
        cm.gainItem(saddles[selection],1);
        cm.loseItem(4031137, 50);
        cm.sendOk("Enjoy!");
    }else{
        var have = cm.itemQuantity(4031137);
        var need = 50-have;
        cm.sendOk("You do not have enough #v4031137# to buy this chair. You need #e"+need+"#n more.");
    }
}
}*/
function start() {
	cm.sendOk("#eRemoved");
	cm.dispose();
}