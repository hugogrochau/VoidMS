/*importPackage(Packages.server); 
importPackage(Packages.client); 
var item=Array( 
[50, 100, [[200, 4000001], [100, 4000009]]], 
[75, 125, [[250, 4000023], [125, 4000030]]], 
[100, 150, [[250, 4000058], [200, 4000059]]], 
[100, 200, [[175, 4000232], [175, 4000233]]], 
[150, 200, [[250, 4000042], [300, 4000014]]], 
[200, 250, [[300, 4000022], [300, 4000025]]], 
[250, 300, [[300, 4000268], [300, 4000269], [300, 4000270]]]); 
// syntax: [(increase on dex/str/int/luk), (increase in Wep Attack), [[item 1 amount, item 1 ID], [item 2 amount, item 2 ID], (this is optional) [item3 amount, item 3 id]] 
var itemid=Math.floor(Math.random()*item.length); 
var st =-1; 
var equip; 
var equip2; 
var se; 
function removeItems(){ 
    for (var i = 0;  i<item[itemid][2].length; i++){ 
        cm.gainItem(item[itemid][2][i][1], -item[itemid][2][i][0]); 
    } 
} 
function haveItem(){ 
    var b=true; 
    for (var i = 0;  i<item[itemid][2].length; i++){ 
        if (!cm.haveItem(item[itemid][2][i][1],item[itemid][2][i][0])){ 
            b=false; 
            break; 
        } 
    } 
    return b; 
} 
function getItems(){ 
    var derp =""; 
    for (var i =0;i<item[itemid][2].length;i++){ 
        derp+=item[itemid][2][i][0]+" #i"+item[itemid][2][i][1]+"# #t"+item[itemid][2][i][1]+"#\r\n"; 
    } 
    return derp; 
} 
function start(){ 
    cm.sendYesNo("Hey I can upgrade one of your items by "+item[itemid][0]+" on all stats and "+item[itemid][1]+" on yourWep Attack. However, you'll need the following items: \r\n"+getItems()); 
} 
function action(m,t,s){ 
    if (m<1) 
        cm.dispose(); 
    else{ 
        st++; 
        if (st==0){ 

            if (haveItem()){ 
                cm.sendSimple("Pick an item to update.\r\n"+cm.EquipList(cm.getClient())); 
            }else { 
                cm.sendOk("You don't have these items: "+getItems()); 
                cm.dispose(); 
            } 
        } else if (st==1){ 
            se=s; 
            var gain = item[itemid][0]; 
            var gain2 = item[itemid][1]; 
            equip=MapleItemInformationProvider.getInstance().getEquipById(cm.getPlayer().getInventory(MapleInventoryType.getByType(1)).getItem(s).getItemId()); 
            equip2 = cm.getPlayer().getInventory(MapleInventoryType.getByType(1)).getItem(s); 
            equip.setDex(equip2.getDex()+gain); 
            equip.setStr(equip2.getDex()+gain); 
            equip.setInt(equip2.getInt()+gain); 
            equip.setLuk(equip2.getLuk()+gain); 
            equip.setWatk(equip2.getWatk()+gain2); 
            cm.sendYesNo("Are you sure you want to upgrade your #r#t"+equip.getItemId()+"#?"); 
        } else if (st==2){ 
            MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.getByType(1), se, 1, true); 
            MapleInventoryManipulator.addFromDrop(cm.getClient(), equip); 
            removeItems(); 
            cm.dispose(); 
        } 
    } 
}  
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if(status == 0){
            cm.sendGetText("#eHello #h #! I can give you any item you wish. \r\n\r\n#rType the correct & exact name of the item or else you'll DC! ");
        }else if(status == 1){
            if (cm.getText() == "" || !isNaN(cm.getText()) ||  cm.getText() == "a" || cm.getText() == "b" || cm.getText() == "c" || cm.getText() == "d" || cm.getText() == "e" || cm.getText() == "f" || cm.getText() == "g" || cm.getText() == "h" || cm.getText() == "i" || cm.getText() == "j" || cm.getText() == "k" || cm.getText() == "l" || cm.getText() == "m" || cm.getText() == "n" || cm.getText() == "o" || cm.getText() == "p" || cm.getText() == "q" || cm.getText() == "r" || cm.getText() == "s" || cm.getText() == "t" || cm.getText() == "u" || cm.getText() == "v" || cm.getText() == "w" || cm.getText() == "x" || cm.getText() == "y" || cm.getText() == "z"|| cm.getText() == "aa"|| cm.getText() == "ab"|| cm.getText() == "ac"|| cm.getText() == "ad"|| cm.getText() == "ae"|| cm.getText() == "af"|| cm.getText() == "ag"|| cm.getText() == "ah"|| cm.getText() == "ai"|| cm.getText() == "aj"|| cm.getText() == "ak"|| cm.getText() == "al"|| cm.getText() == "am"|| cm.getText() == "an"|| cm.getText() == "ao"|| cm.getText() == "ap"|| cm.getText() == "aq"|| cm.getText() == "ar"|| cm.getText() == "as"|| cm.getText() == "at"|| cm.getText() == "au"|| cm.getText() == "av"|| cm.getText() == "aw"|| cm.getText() == "ax"|| cm.getText() == "ay"|| cm.getText() == "az") {
                cm.sendOk("#e#rSorry, you did not type in the correct & exact name of the item. Please try again."); 
                cm.dispose(); 
                return; 
            } else {
                cm.sendSimple(cm.searchItem(cm.getText()));
            }
        }else if(status == 2){
            cm.gainItem(selection);
            cm.dispose();
        }
    }
}*/
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
                        cm.sendSimple("Hello! I can trade your #eBox of Presents#n for #eYellow Wish Tickets#n. Each Yellow Wish Ticket costs one Box of Presents.\r\nHow many Boxes of Presents would you like to trade?\r\n\r\n#b#L0#I want to trade 1 #i4031137##l\r\n#L1#I want to trade 10 #i4031137##l\r\n#L2#I want to trade all my #i4031137##l");
                } else if (selection == 0) {                   
                if (cm.itemQuantity(4031137) < 1) {
                        cm.sendOk("You do not have 1 #i4031137#!");
                        cm.dispose();
                } else {
                        cm.gainItem(4031137, -1);
                        cm.gainItem(4031543, 1);
                        cm.sendOk("Here is your #i4031543#!");
                        cm.dispose();
                }
                } else if (selection == 1) {
                if (cm.itemQuantity(4031137) < 10) {
                        cm.sendOk("You do not have 10 #i4031137#!");
                        cm.dispose();
                } else {
                        cm.gainItem(4031137, -10);
                        cm.gainItem(4031543, 10);
                        cm.sendOk("Here are your #i4031543#!");
                        cm.dispose();
                }
                } else if (selection == 2) {
                        var bop = cm.itemQuantity(4031137);
                        if (bop > 0) {
                                cm.gainItem(4031543, bop);
                                cm.gainItem(4031137, -bop);
                        } else {
                                cm.sendOk("You do not have any #i4031137#.");
                                cm.dispose();
                                }
                        }
                }
        }