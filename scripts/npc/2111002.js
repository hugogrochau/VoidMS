/* * * * * * * * * * * * * * * \
*  By Hugo of MadStory/VoidMS  *
*      gugubro1@gmail.com      *
*         madstory.org         *
*          voidms.com          *
\ * * * * * * * * * * * * * * *//*
var thing = 0;
var status = 0;
var map = 0;
var mapsArray = new Array("100000000","910000000","100000001","100000002","100000006","100000100","100000101","100000102","100000103","100000104","100000105","100000200","100000201","100000202","100000203","101000000","101000001","101000002","101000200","101000300","102000000","102000001","102000002","102000003","103000000","103000001","103000002","103000003","103000004","103000005","103000006","103000100","104000000","104000001","104000002","104000003","105040300","105040400","105040401","105040402","110000000","200000000","200000001","200000002","200000100","200000300","200000301","200080101","211000000","211000001","211000100","211000101","211000102","211040401","220000000","220000001","220000002","220000003","220000004","220000005","220000006","220000100","220000300","220000301","220000302","220000303","220000304","220000305","220000306","220000307","220000400","221000000","221000001","221000100","221000200","221000300","221000400","221040401","221040402","222000000","222000001","222020000","230000000","230000001","230000002","230000003","230040401","230010201","230020201","240000000","240000001","240000002","240000003","240000004","240000005","240000006","240000100","250000000","250000001","250000002","250000003","250000100","251000000","251000100","251010404","260000000","260000100","260000200","260000300","260000301","260000302","260000303","670010000","680000000","680000001","680000002","680000003","680000004","600000000","600000001","600010001","600020000","682000000","682000306","682000401","800000000","801000000","801000300","801040000"); // put map ids here separated by commas


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
		if (c.getP().getGMLevel() >= 4) {
            var hello = "#eHello #h #, I am the drop event NPC. What would you like to do?#n\r\n\r\n#b#L0#I want to check the current drop event#l\r\n#L1#I would like to make a new drop event#l\r\n#L2#I would like to know more about drop events#l";
            if (cm.hasDropEvent() && cm.getPlayer().getName().equalsIgnoreCase(cm.getDropEventHost()) || cm.hasDropEvent() && cm.getPlayer().isGM()) hello += "\r\n\r\n\r\n#r#L3#Hoster control panel";
            cm.sendSimple(hello);
		} else {
			cm.sendOk("Back to the drawing board.");
			cm.dispose();
		}
        } else if (status == 1) {
            if (selection != 0 && cm.getC().getChannel() != 1) {
                cm.sendOk("#eYou can only host the drop event in channel one");
                cm.dispose();
                return;
            }
            if (selection == 0) {
                if (cm.hasDropEvent()) {
                    cm.sendOk("#eThe current drop event is hosted by#n #r" + cm.getDropEventHost() + "#k#e and the prize is #n\r\n#i" + cm.getDropEventItem() + "##b - #t" + cm.getDropEventItem() + "##k");
                } else {
                    cm.sendOk("#eThere isn't a drop event going on right now. #rGo make one!#k#n");
                }
                cm.dispose();
            } else if (selection == 1) {
                if (!cm.hasDropEvent()) {
                    var sendsimple = "#ePick a map for your item to drop:#n #b\r\n";
                    for (i = 0; i < mapsArray.length; i++) {
                        sendsimple += "#L" + i + "##m" + mapsArray[i] + "#\r\n";
                    }
                    sendsimple += "#k";
                    cm.sendSimple(sendsimple);
                } else {
                    cm.sendOk("#eThere is a drop event going on right now! #rGo participate#k and come back later to register another one.#n");
                    cm.dispose();
                }
            } else if (selection == 2) {
                cm.sendOk("#r#eIt cost 150,000,000 mesos to start a drop event.\r\n\r\n#k#eThe player that starts the drop event selects a map and a rare from his/her's inventory.\r\n\r\n#rThat rare will be dropped in the selected map. The first person to pick up the rare is the winner.#k\r\n\r\nThe event goes on until a player finds the item.\r\n\r\n#rIf no one finds the item after 60 minutes the event ends but the item stays in the map for the hoster to get back.");
                cm.dispose();
            } else if (selection == 3) {
                cm.sendSimple("#eWhat would you like to do?#n\r\n\r\n#r#L13371#Send a hint#l\r\n#L13372#View event info#l\r\n#L13373#End the event#l\r\n#L13374#Warp to your map#l");
            }
        } else if (status == 2) {
            if (selection == 13371) {
                if (cm.getDropEventHints() > 0) {
                    cm.sendGetText("#rHints left : " + cm.getDropEventHints() + "\r\n\r\n#k#eType in the hint you'd like to send:\r\n\r\n#n");

                } else {
                    cm.sendOk("#eYou have no hints left to use.#n");
                    cm.dispose();
                }
            } else if (selection == 13372) {
                if (cm.getPlayer().getName().equalsIgnoreCase(cm.getDropEventHost()) || cm.getPlayer().isGM()) {
                    cm.sendOk("#ePrize:#n\r\n\r\n#i" + cm.getDropEventItem() + "##b - #t" + cm.getDropEventItem() + "##k\r\n\r\n\r\n#eMap:#n #b#m" + cm.getDropEventMap() + "#");
                } else {
                    cm.dropMessage("#eYou are not the hoster of this event.#n");
                    cm.dispose();
                }
                cm.dispose();
            } else if (selection == 13373) {
                if (cm.getPlayer().getName().equalsIgnoreCase(cm.getDropEventHost())) {
                    cm.worldMessage(6, "[Drop Event]" + cm.getPlayer().getName() + " has ended his/her's drop event.");
                    cm.endDropEvent();
                    cm.sendOk("#e#rDrop event ended.#k#n");
                    cm.dispose();
                } else if (cm.getPlayer().isGM() && !cm.getPlayer().getName().equalsIgnoreCase(cm.getDropEventHost())){
                    cm.worldMessage(6, cm.getPlayer().getName() + " has ended " + cm.getDropEventHost() + "'s drop event.");
                    cm.endDropEvent();
                    cm.sendOk("#e#rDrop event ended.#k#n");
                    cm.dispose();
                } else {
                    cm.dropMessage("#eYou are not the hoster of this event.#n");
                    cm.dispose();
                }
            } else if (selection == 13374) {
                if (cm.getPlayer().getName().equalsIgnoreCase(cm.getDropEventHost()) || cm.getPlayer().isGM()) {
                    cm.warp(cm.getDropEventMap(), 0);
                } else {
                    cm.dropMessage("#eYou are not the hoster of this event.#n");
                    cm.dispose();
                }
            } else {
                map = mapsArray[selection];
                cm.sendSimple("#eWhich inventory is your item in?#n#b\r\n#L1#Equip#l\r\n#L4#Use#l\r\n#L3#Set-up#l\r\n#L2#ETC#l\r\n#L0#Cash#l");
            }
        } else if (status == 3) {
            var string = "#eNow pick a rare item as a prize#n:\r\n\r\n"
            if (selection == -1) {
                if (cm.getPlayer().getName().equalsIgnoreCase(cm.getDropEventHost()) || cm.getPlayer().isGM()) {
                    cm.worldMessage(6, "[Drop Event] Hint : " + cm.getText());
                    cm.useDropEventHint();
                } else {
                    cm.dropMessage("#eYou are not the hoster of this event.#n");
                }
                cm.dispose();
            } else if (selection == 0) {
                thing = 0;
                cm.sendSimple(string+cm.CashList(cm.getC()));
            } else if (selection == 1) {
                thing = 1;
                cm.sendSimple(string+cm.EquipList(cm.getC()));
            } else if (selection == 2) {
                thing = 2;
                cm.sendSimple(string+cm.ETCList(cm.getC()));
            } else if (selection == 3) {
                thing = 3;
                cm.sendSimple(string+cm.SetupList(cm.getC()));
            } else if (selection == 4) {
                thing = 4;
                cm.sendSimple(string+cm.UseList(cm.getC()));

            }
        } else if (status == 4) {
            if (cm.getMeso() >= 150000000) {
                cm.gainMeso(-150000000);
            } else {
                cm.sendOk("You need 150,000,000 mesos to start a drop event.");
                cm.dispose();
                return;
            }
            cm.setDropEvent(selection, map, thing);
            cm.sendOk("#eDrop event successfully registered.\r\n\r\nInformation:\r\n\r\nMap: #n#r#m" + cm.getDropEventMap() + "##k\r\n\r\n#ePrize: #n#b#i" + cm.getDropEventItem() + "# - #t" + cm.getDropEventItem() + "#");

            cm.dispose();
        }
    }
}
/*
function start() {
    cm.sendOk("#eI'm back on the drawing board. Please talk to me later");
    cm.dispose();
}*/