var status;
var boxes = [4000422, 4000423, 4000424, 4000425, 4031168, 4031169]; // Boxes to choose from
var prize = [[4000436, 4000436, 4000436, 4000436, 4000436], [2340000, 2340000, 2340000], [2041200, 2041200, 4031545]]; // Add your prizes here
var rand = Math.floor(Math.random()*100);

function start() {
    status = -1;
    action(1, 0, 0);
}


function action(mode, type, selection) {
    if (mode == -1) {
            cm.dispose();
        }else{
    if (status >= 5 && mode == 0) {
            cm.sendOk("See you next time!.");
            cm.dispose();
            return;                    
        }
    
        if (mode == 1) {
            status++;
        }else{
            status--;
        }
    if (status == 0) {
        if (cm.haveItem(4031544, 1)) { // You choose what item.  
            cm.sendNext("Step right up and test your luck. See if you can win a Blue Wish Ticket by guessing the correct box!");
        }else{
            cm.sendOk("You don't have a Green Wish Ticket.");
            cm.dispose();
        }
    } else if (status == 1) {
            cm.sendNext("Do you feel lucky?");
    } else if (status == 2) {
        for(var i = 0, text = "Choose one of the prize below.\r\n\r\n"; i < boxes.length; text += "\t\t#L" + i + "##i" + boxes[i] + "##l" + ((i+1) % 2 == 0 ? "\r\n" : ""), i++);
        cm.sendSimple(text);
    } else if (status == 3) {
        cm.sendYesNo("Do you want to choose #i" + boxes[selection] + "#?");
    } else if (status == 4) {
        var rand2;
        if ((rand >= 1) && (rand <= 50)) {
            rand2 = Math.floor(Math.random() * prize[0].length);
        } else if ((rand >= 51) && (rand <= 90)) {
            rand2 = Math.floor(Math.random() * prize[1].length);
        }else{
            rand2 = Math.floor(Math.random() * prize[2].length);
            }
            cm.gainItem(4031544, -1); // You choose what item, same as above
            cm.gainItem([rand >= 1 && rand <= 50 ? prize[0][rand2] : rand >= 51 && rand <= 90 ? prize[1][rand2] : prize[2][rand2]]);
            cm.sendOk("You have won a #v" + [rand >= 1 && rand <= 50 ? prize[0][rand2] : rand >= 51 && rand <= 90 ? prize[1][rand2] : prize[2][rand2]] + "#");
            cm.dispose();
        }
    }
}