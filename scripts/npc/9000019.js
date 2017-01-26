/* Sera
 * Made by imPro of VoidMS
 */


var status = 0;
var train = false;
function start() {
    status = -1
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    } else {
        if (mode == 1)
            status ++;
        else if (mode == 0 && status != 0)
            status--;
        else
            status++;
        if (status == 0) {
            cm.sendYesNo("#e#v3991048##v3991030##v3991037##v3991028##v3991040##v3991038##v3991030#  #v3991045##v3991040# \r\n #v3991021##v3991014##v3991008##v3991003##v3991012##v3991018#\r\n\r\nWe are all glad to see you here! Here at VoidMS, we have a Starter Pack for all our new Players! It includes Naricain Elixirs, Power Elixirs, an NX look, and a Custom Mount! You can also get personalized weapons, based on the job you choose!\r\n\r\nNow, we know how hard those first Rebirths can get, so here at Void, we offer an exclusive training package to all of our new players! We will help you through your first 5 Rebirths of your MapleStory Life, through easy methods! However, we aren't going to force you to do so.\r\n\r\n#rWould you like to take part in our exclusive training package?");
        } else if (status == 1) {
            cm.sendSimple("Great! Before you go, pick a job!#b\r\n\r\n#L0#Beginner#l\r\n#L1#Warrior#l\r\n#L2#Magician#l\r\n#L3#Bowman#l\r\n#L4#Theif#l\r\n#L5#Pirate#l");
            if (mode == 1)
                train = true;
        } else if (status == 2) {
            if (selection == 0) {
                cm.gainItem(1302000);
            } else if (selection == 1) {
                cm.gainItem(1302027);
                cm.gainItem(1302006);
                cm.gainItem(1302071);
            } else if (selection == 2) {
                cm.gainItem(1372005);
                cm.gainItem(1382004);
                cm.gainItem(1382042);
            } else if (selection == 3) {
                cm.gainItem(1452002);
                cm.gainItem(1452027);
                cm.gainItem(1452048);
                cm.gainItem(2060000, 1000);
            } else if (selection == 4) {
                cm.gainItem(1472000);
                cm.gainItem(1472006);
                cm.gainItem(1472058);
                cm.gainItem(1332007);
                cm.gainItem(1332008);
                cm.gainItem(1332059);
                cm.gainItem(2070005, 1000)
            } else if (selection == 5) {
                cm.gainItem(1492000);
                cm.gainItem(1492002);
                cm.gainItem(1492004);
                cm.gainItem(1482000);
                cm.gainItem(1482002);
                cm.gainItem(1482004);
                cm.gainItem(2330000, 1000);
            }
            cm.gainItem(2000005, 100);
            cm.gainItem(2022282, 10);
            cm.gainItem(1002232, 1);
            cm.gainItem(1052047, 1);
            cm.gainItem(1002140, 1);
            cm.gainItem(1902040, 1);
            cm.gainItem(1912033, 1);
            cm.gainMeso(25000000);
            if (train){
                cm.warp(0, 0);
                for (i = 0; i < 10; i++)
                    cm.getP().levelUp();
                cm.fixExp();
            } else
                cm.warp(910000000, 0);
            cm.sendOk("Enjoy your journey!");
            cm.dispose();
        }
    }
}