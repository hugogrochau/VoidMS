var status = -1;
var txt;
function start() {
    cm.sendSimple("#eWelcome to the All In One Shop!\r\nPlease choose a category#n#b" +
        "\r\n#L0#Job Items" +
        "\r\n#L1#Common Items" +
        "\r\n#L2#Cash Items");
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        txt = "Please choose a category#b\r\n";
        switch (selection) {
            case 0:
                txt += "\r\n#L0#Magician Equips" +
                "\r\n#L1#Thief Equips" +
                "\r\n#L2#Warrior Equips" +
                "\r\n#L3#Archer Equips" +
                "\r\n#L4#Pirate Equips";
                break;
            case 1:
                break;
            case 2:
                txt += "\r\n#L14000#Accessory" +
                "\r\n#L14001#Cap" +
                "\r\n#L14002#Cape" +
                "\r\n#L14003#Coat (tops)" +
                "\r\n#L14004#Glove" +
                "\r\n#L14005#Longcoat (overall)" +
                "\r\n#L14006#Pants" +
                "\r\n#L14007#PetEquip" +
                "\r\n#L14008#Ring" +
                "\r\n#L14009#Shield" +
                "\r\n#L14010#Shoes" +
                "\r\n#L14011#TamingMob" +
                "\r\n#L14012#Weapon";
                break;
        }
        cm.sendSimple(txt);
    } else if (status == 1) {
        if (selection > 100) {
            cm.openShop(selection);
            cm.getP().dropMessage(selection);
            cm.dispose();
        } else {
            txt = "Please choose a category#b\r\n";
            switch (selection) {
                case 1:
                    txt += "\r\n#L0#Magican Shoes" +
                    "\r\n#L1#Magican Overalls" +
                    "\r\n#L2#Magican Gloves" +
                    "\r\n#L3#Magican Hats" +
                    "\r\n#L4#Magican Shields" +
                    "\r\n#L5#Magican Wands" +
                    "\r\n#L6#Magican Staffs";
                    break;
            }
        }
    }
}


