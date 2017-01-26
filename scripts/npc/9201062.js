/* * * * * * * * * * * * * * * \
*        ItemID Checker        *
*   By Hugo of MadStory/VoidMS *
*      gugubro1@gmail.com      *
*         madstory.org         *
*          voidms.com          *
\ * * * * * * * * * * * * * * */

var thing = 0;
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 2 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("#eWhich inventory is your item in?#n#b\r\n#L0#Equip#l\r\n#L1#Use#l\r\n#L2#Set-up#l\r\n#L3#ETC#l\r\n#L4#Cash#l");
        } else if (status == 1) {
            string = "#ePick the item:\r\n#n";
            if (selection == 0) {                
                thing = 0;
                cm.sendSimple(string+cm.EquipList(cm.getC()));
            } else if (selection == 1) {
                thing = 1;
                cm.sendSimple(string+cm.UseList(cm.getC()));
            } else if (selection == 2) {
                thing = 2;
                cm.sendSimple(string+cm.SetupList(cm.getC()));
            } else if (selection == 3) {
                thing = 3;
                cm.sendSimple(string+cm.ETCList(cm.getC()));
            } else if (selection == 4) {
                thing = 4;
                cm.sendSimple(string+cm.CashList(cm.getC()));
            }
        } else if (status == 2) {
            var say = "#eThe item id for #i";
            var say2 = "# is #n#r";
            switch (thing) {
                case 0:
                    cm.sendOk(say + cm.getPlayer().getEquipId(selection) + say2 + cm.getPlayer().getEquipId(selection));
                    break;
                case 1:
                    cm.sendOk(say + cm.getPlayer().getUseId(selection) + say2 + cm.getPlayer().getUseId(selection));
                    break;
                case 2:
                    cm.sendOk(say + cm.getPlayer().getSetupId(selection) + say2 + cm.getPlayer().getSetupId(selection));
                    break;
                case 3:
                    cm.sendOk(say + cm.getPlayer().getETCId(selection) + say2 + cm.getPlayer().getETCId(selection));
                    break;
                case 4:
                    cm.sendOk(say + cm.getPlayer().getCashId(selection) + say2 + cm.getPlayer().getCashId(selection));
                    break;
            }
            cm.dispose();
        }
    }
}
    

