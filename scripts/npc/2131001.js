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
		cm.sendSimple("Hello #r#h ##k, and welcome to the Chaos Item Upgrade Shop. What would you like?\r\n\r\n#d#L0#What are Chaos Items?#l\r\n#L1#I want to Get or upgrade my Chaos Item#l");
	}
	}
}