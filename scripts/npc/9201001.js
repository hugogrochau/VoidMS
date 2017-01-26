var pids = [1112001, 1112002, 1112003, 1112005, 1112006]
var fids = [1112800, 1112801, 1112802];
var id;
var text;
var status = -1;
var price = 10;
var vprice = 5;

function start() {
    text = "#eHello #h #, I can make you some cool effect rings for " + price + " #i4002000# and #r" + vprice + " Vote Points#k. Which one would you like?\r\n\r\nFriendship Rings:#n#b";
    for (i=0;i<fids.length;i++)
        text += "\r\n#L" + fids[i] + "##i" + fids[i] + "#";
    text += "#l\r\n\r\n#k#eCouple Rings:#n#b"
    for (i=0;i<pids.length;i++)
        text += "\r\n#L" + pids[i] + "##i" + pids[i] + "#";
    cm.sendSimple(text);
}

function action(m,t,s) {
    status++;
    if (m == 1) {
        if (status == 0) {
            id = s;
            cm.sendGetText("Please type the name of the person you would like you make a #i" + id + "# ring with:");
        } else if (status == 1) {
            var name = cm.getText();
            if (name != null) {
                if (cm.getP().votePoints() >= vprice) {
                    if (cm.itemQuantity(4002000) >= price) {
                        if (cm.createRing(name, id)) {
                            cm.sendOk("You have created a #i" + id + "# ring with #r" + name);
                            cm.gainItem(4002000, -price);
                            cm.getP().changeVotePoints(-vprice);
                        } else {
                            cm.sendOk("#e" + name + " either doesn't exist or is not in the same channel as you");
                        }
                    } else {
                        cm.sendOk("#eYou do not have #r" + price + " #i4002000#");
                    }
                } else {
                    cm.sendOk("#eYou do not have #r" + vprice + " vote points");
                }
            }
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}