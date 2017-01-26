function start() {
    cm.sendYesNo("The plane will be taking off soon, Will you leave now? You will have buy the plane ticket again to come in here.");
}

function action(mode, type, selection) {
    if(mode == -1) {
        cm.dispose();
        return;
    }
    if(mode == 1)
        status++;
    if(mode == 0) 
        cm.sendOk("Please hold on for a sec, and plane will be taking off. Thanks for your patience.");
    if(status == 0)
        cm.sendYesNo("The plane will be taking off soon, Will you leave now? You will have buy the plane ticket again to come in here.");
    else if(status == 1)
        cm.sendNext("I have already told you the ticket is not refunable~ hope to see you again~");
    else if(status == 2) {
        cm.warp();
        cm.dispose(103000000);
    } else {
        cm.sendOk("You have make a bug! Please report this to http://www.valhalladev.com");
        cm.dispose();
    }
}