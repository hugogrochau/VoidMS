/* 
*
*	NimaKIN - v.62 // Did most as possible by hector - Got tired
*       
*/
var status = 0;
var beauty = 0;
var haircolor = Array();
var skin = Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
var hair = Array(34310, 31960, 32130, 33150, 31980, 33320, 34150, 34290, 33340, 30980, 32120);
var hairnew = Array();
var face = Array(21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21009, 21010, 21011, 21012, 21013, 21014, 21016, 21017, 21018, 21019, 21020, 21021, 21022, 21024, 21025, 21026, 21027, 21028, 21029, 21030, 21030, 21031, 21033, 21038, 20030, 20031, 20032, 20033, 20035, 20038, 20039, 20040);
var facenew = Array();
var colors = Array();

function start() {
    status = -1;
    action(1,0);
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
        if (status == 0) {
		if (cm.getP().getGMLevel() > 1) {
                cm.sendSimple("Hey there #h #, I can change your look. What would you like to change?\r\n#L0#Skin#l\r\n#L1#Hair#l\r\n#L2#Hair Color#l\r\n#L3#Eye#l\r\n#L4#Eye Color#l\r\n#L5#Make me Alien#l");
		} else { 
			cm.sendOk("Banana");
			cm.dispose();
		}
        } else if (status == 1) {
            if (selection == 0) {
                beauty = 1;
                cm.sendStyle("Pick one?", skin);
            } else if (selection == 1) {
                beauty = 2;
                hairnew = Array();
                for(var i = 0; i < hair.length; i++) {
                    hairnew.push(hair[i] + parseInt(cm.getPlayer().getHair() % 10));
                }
                cm.sendStyle("Pick one?", hairnew);
            } else if (selection == 2) {
                beauty = 3;
                haircolor = Array();
                var current = parseInt(cm.getPlayer().getHair()/10)*10;
                for(var i = 0; i < 8; i++) {
                    haircolor.push(current + i);
                }
                cm.sendStyle("Pick one?", haircolor);
            } else if (selection == 3) {
                beauty = 4;
                facenew = Array();
                for(var i = 0; i < face.length; i++) {
                    facenew.push(face[i] + cm.getPlayer().getFace() % 1000 - (cm.getPlayer().getFace() % 100));
                }
                cm.sendStyle("Pick one?", facenew);
            } else if (selection == 4) {
                beauty = 5;
                var current = cm.getPlayer().getFace() % 100 + 21000;
                colors = Array();
                colors = Array(current , current + 100, current + 200, current + 300, current +400, current + 500, current + 600, current + 700, current + 800);
                cm.sendStyle("Pick one?", colors);
            } else if (selection == 5) {
		cm.getP().setGender(2);
		cm.reloadChar();
		cm.sendOk("Done.");
	  }
        }
        else if (status == 2){
            if (beauty == 1){
                cm.setSkin(skin[selection]);
            }
            if (beauty == 2){
                cm.setHair(hairnew[selection]);
            }
            if (beauty == 3){
                cm.setHair(haircolor[selection]);
            }
            if (beauty == 4){
                cm.setFace(facenew[selection]);
            }
            if (beauty == 5){
                cm.setFace(colors[selection]);
            }
            cm.dispose();
        }
    }
}