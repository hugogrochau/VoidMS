/* Item destroyer
 */

importPackage(Packages.java.util);
importPackage(Packages.client);
importPackage(Packages.server);

var status = 0;
var targets = new Array();
var operation = -1;
var sendTarget;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
			if (cm.getP().getGMLevel() > 3) {
            cm.sendSimple("What do you want to do?#b\r\n" +
                "#L0#Destroy item#l\r\n" +
                "#L1#Trade my item#l#k");
			} else {
				cm.sendOk("Tehee");
				cm.dispose();
            }
        } else if (status == 1) {
            operation = selection;
            if (selection == 0) {
                cm.sendYesNo("I will destroy the items in each of your first slots. Are you ready?");
            } else if (selection == 1) {
                var toSend = "Who shall receive the item in your first equip inventory slot?#b";
                var iter = cm.getPlayer().getMap().getCharacters().iterator();
                var i = 0;
                targets = new Array();
                while (iter.hasNext()) {
                    var curChar = iter.next();
                    toSend += "\r\n#L" + i + "#" + curChar.getName() + "#l";
                    targets[i] = curChar;
                    i++;
                }
                toSend += "#k";
                cm.sendSimple(toSend);
            }
        } else if (status == 2) {
            if (operation == 0) {
                //MapleInventoryManipulator.removeFromSlot(cm.getC(), MapleInventoryType.UNDEFINED, 1, 1, true);
                MapleInventoryManipulator.removeFromSlot(cm.getC(), MapleInventoryType.EQUIP, 1, 1, true);
                MapleInventoryManipulator.removeFromSlot(cm.getC(), MapleInventoryType.USE, 1, 1, true);
                MapleInventoryManipulator.removeFromSlot(cm.getC(), MapleInventoryType.SETUP, 1, 1, true);
                MapleInventoryManipulator.removeFromSlot(cm.getC(), MapleInventoryType.ETC, 1, 1, true);
                MapleInventoryManipulator.removeFromSlot(cm.getC(), MapleInventoryType.CASH, 1, 1, true);
				cm.sendOk("They are destroyed. See you next time.")
                cm.dispose();
            } else if (operation == 1) {
                sendTarget = targets[selection];
				text = cm.getText();
                cm.sendYesNo("I will send the item in your first equip inventory slot to " + sendTarget.getName() + ". Are you ready?");
            }
        } else if (status == 3) {
            if (operation == 1) {
                var item = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(1).copy();
                MapleInventoryManipulator.removeFromSlot(cm.getC(), MapleInventoryType.EQUIP, 1, 1, true);
                MapleInventoryManipulator.addFromDrop(sendTarget.getClient(), item, "Sent to " + sendTarget.getName() + "using Billy");
                cm.sendOk(sendTarget.getName() + " has received the item. See you next time.");
                cm.dispose();
            }
        }
    }
}	