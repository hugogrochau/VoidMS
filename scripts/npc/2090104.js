function start() {
    cm.sendSimple("Welcome! I am the VoidMS plastic surgeon. What gender are you? \r\n\r\n#L0#Male\r\n#L1#Female");
}

function action(mode, type, selection) {
    if (selection == 0) {
        cm.openNpc(9900000);
    } else if (selection == 1) {
        cm.openNpc(9900001);
    }
    cm.dispose();
}


