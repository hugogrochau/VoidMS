/*var maps = new Array(100000000, 105040310);

function start() {
    status = 1;
    cm.sendYesNo("Congratulations on finishing the Jump Quest! Here, you deserve 2 #bKarma levels!");
}

function action(mode, type, selection) {
    if (status == 1) {
        status = 2;
        var warpText = "Do you want to do the Deep Forest of Patience JQ or get out of here?";
        for (var i = 0; i < maps.length; i++)
            warpText += "\r\n#L" + i + "##m" + maps[i] + "##l";
        cm.sendSimple(warpText);
    } else if (status == 2) {
        cm.warp(maps[selection], 0);
	cm.gainKarma(2);
        cm.dispose();
    }
}	*/

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode,type,selection)
{
	cm.finishJQ();
	cm.dispose();
}