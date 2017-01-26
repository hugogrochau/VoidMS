function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    selected = selection;
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
			cm.sendOk("Bye!");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
		cm.sendNext("Hello! I am #rodsmk's #kcity warper!");
		}
		else if (stauts == 1) {
		cm.sendSimple("Pick your destination! \r\n\r\n#b#L0#Henesys#l\r\n#L1#Perion#l\r\n#L2#Kerning City#l\r\n#L3#Lith Harbor#l\r\n#L4#Ellinia#l\r\n#L5#Sleepywood#l\r\n#L6#Nautilus#l\r\n#L7#Florina Beach#l\r\n#L8#Amherst#l\r\n#L9#Southperry#l\r\n#L10#Orbis#l\r\n#L11#El Nath#l\r\n#L12#Aquarium#l\r\n#L13#Ludibrium#l\r\n#L14#Omega Sector#l\r\n#L15#Mu Lung#l\r\n#L16#Herb Town#l\r\n#L17#Leafre#l\r\n#L18#Ariant#l\r\n#L19#Singapore CBD#l\r\n#L20#Boat Quay Town#l\r\n#L21#Mushroom Shrine (Zipangu)#l\r\n#L22#Showa Town#l);
		else if (selection == 0) {
		cm.warp(100000000);
		cm.dispose();
		}
		else if (selection == 1) {
		cm.warp(102000000);
		cm.dispose();
		}
		else if (selection == 2) {
		cm.warp(103000000);
		cm.dispose();
		}
		else if (selection == 3) {
		cm.warp(104000000);
		cm.dispose();
		}
		else if (selection == 4) {
		cm.warp(101000000);
		cm.dispose();
		}
		else if (selection == 5) {
		cm.warp(105040300);
		cm.dispose();
		}
		else if (selection == 6) {
		cm.warp(120000000);
		cm.dispose();
		}
		else if (selection == 7) {
		cm.warp(110000000);
		cm.dispose();
		}
		else if (selection == 8) {
		cm.warp(1010000);
		cm.dispose();
		}
		else if (selection == 9) {
		cm.warp(60000);
		cm.dispose();
		}
		else if (selection == 10) {
		cm.warp(200000000);
		cm.dispose();
		}
		else if (selection == 11) {
		cm.warp(211000000);
		cm.dispose();
		}
		else if (selection == 12) {
		cm.warp(230000000);
		cm.dispose();
		}
		else if (selection == 13) {
		cm.warp(220000000);
		cm.dispose();
		}
		else if (selection == 14) {
		cm.warp(221000000);
		cm.dispose();
		}
		else if (selection == 15) {
		cm.warp(250000000);
		cm.dispose();
		}
		else if (selection == 16) {
		cm.warp(251000000);
		cm.dispose();
		}
		else if (selection == 17) {
		cm.warp(240000000);
		cm.dispose();
		}
		else if (selection == 18) {
		cm.warp(260000000);
		cm.dispose();
		}
		else if (selection == 19) {
		cm.warp(540000000);
		cm.dispose();
		}
		else if (selection == 20) {
		cm.warp(541000000);
		cm.dispose();
		}
		else if (selection == 21) {
		cm.warp(800000000);
		cm.dispose();
		}
		else if (selection == 22) {
		cm.warp(801000000);
		cm.dispose();
			}
		}
	}	
}

