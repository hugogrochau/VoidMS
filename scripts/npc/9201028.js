var price = 60;
var id = 4031419;
var prizes = [1052172, 1002839, 1002699, 1702203];
var choice = -1;

function start() {
    var out = "#eMuhahahaha, happy halloween #h #! Do you have any\r\n#b#t" + id + "#s#k for me? I can give you some items for #r" + price + "#k #i" + id + "#\r\nWhich one would you like?#n\r\n\r\n#b";
    for (i=0;i<prizes.length;i++)
        out += "\r\n\r\n#L" + i + "##i"+ prizes[i] + "# #t" + prizes[i] + "#";
    cm.sendSimple(out);
}

function action(m,t,s){
    if (m == 1) {
        if (choice > -1) {
            if (!cm.isFull()) {
                if (cm.itemQuantity(id) >= price) {
                    cm.gainItem(id, -price);
                    cm.gainItem(prizes[choice], 1);
                    cm.sendOk("Here's your #i"+ prizes[choice] + "# #b#t" + prizes[choice] + "##k. Thanks for the #r" + price + " #b#t" + id + "#s#k");
                } else {
                    cm.sendOk("You only have #r" + cm.itemQuantity(id) + "#k out of the #r" + price + " #b#t" + id + "#s#k needed for a #i" + prizes[choice] + "# #b#t" + prizes[choice] +"#");
                }
            } else {
                cm.sendOk("#rYou do not have enough space in your #bEquip#r inventory");
            }
            cm.dispose();
        } else if (s > -1 && s < prizes.length) {
            cm.sendYesNo("#nAre you sure you want to spend #r" + price + " #b#t" + id + "#s#k on a #i" + prizes[s] + "# #b#t" + prizes[s] + "#");
            choice = s;
        }
    } else {
        cm.dispose();
    }
}