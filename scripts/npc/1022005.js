/*
Karma NPC
Author: Chatez & Hugo

var status = 0;
var scrollId = Array(6,7,403,506,603,709,710,711,806,807,903,1024,1025,3003,3103,3203,3303,3703,3803,4003,4103,4203,4303,4403,4503,4603,4703);
var randomId = Math.floor(Math.random()*scrollId.length);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0)
            cm.dispose();
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0)
            if (cm.getKarma() > -11)
                cm.sendSimple ("So you want to use your Madness level in exchange for some benefits? What do you want to buy? \r\n#bYour Madness level is #r#e"+ cm.getKarma() + "\r\n\r\n#rITEMS -\r\n#d#L0##i1062007# (1000 Madness)#l #L1##i1042003# (1000 Madness)#l\r\n#L2##i1002140# (500 Madness)#l #L7##i1382060# (750 Madness)#l\r\n#L8##i1442068# (750 Madness)#l #L9##i1452060# (750 Madness)#l\r\n#L10##i1332077# (750 Madness)#l #L11##i1472072# (750 Madness)#l\r\n#L12##i1462052# (750 Madness)#l #L13##i1402048# (750 Madness)#l\r\n#L14##i2049100# (50 Madness)#l #L15##i4031348# (1500 Madness)#l\r\n#L3##i2040603#(Random Gm Scroll - 20 Madness)#l\r\n#L17##i2340000#(White Scroll - 10 Madness)#l\r\n#L18##i4031543#(Yellow Wish Ticket - 2 Madness)#l\r\n\r\n#rChaos Items (20 Slots and 10 Weapon Attack) -\r\n#d#L19##i1060026# (750 Madness)#l     #L20##i1061039# (750 Madness)#l\r\n#L21##i1102191# (500 Madness)    #L22##i1002492# (500 Madness)#l\r\n#L23##i1050127# (500 Madness)#l     #L24##i1051140# (500 Madness)#l\r\n#L25##i1072263# (500 Madness)#l");
            else {
                cm.sendOk ("You have less than -10 Madness! Get some more before you access this.");
                cm.dispose();
            }
        else if (status == 1) {
            switch(selection) {
                case 0:
                    if (cm.getKarma() > 999) {
                        cm.gainItem(1062007,1);
                        cm.gainKarma(-1000);
                    } else 
                        cm.sendSimple("Your Madness level is below 1000.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 1:
                    if (cm.getKarma() > 999) {
                        cm.gainItem(1042003,1);
                        cm.gainKarma(-1000);
                    } else 
                        cm.sendSimple("Your Madness level is below 1000.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 2:
                    if (cm.getKarma() > 499) {
                        cm.gainItem(1002140,1);
                        cm.gainKarma(-500);
                    } else
                        cm.sendSimple("Your Madness level is below 500.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 3:
                    if (cm.getKarma() > 19) {
                        cm.gainItem(scrollId[randomId]+2040000,1);
                        cm.gainKarma(-20);
                    } else
                        cm.sendSimple("Your Madness level is below 10.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 7:
		if (cm.getKarma() > 749) {
		    cm.gainItem(1382060,1);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 8:
		if (cm.getKarma() > 749) {
		    cm.gainItem(1442068);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 9:
		if (cm.getKarma() > 749) {
		    cm.gainItem(1452060);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 10:
		if (cm.getKarma() > 749) {
		    cm.gainItem(1332077);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 11:
		if (cm.getKarma() > 749) {
		    cm.gainItem(1472072);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 12:
		if (cm.getKarma() > 749) {
		    cm.gainItem(1462052);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 13:
		if (cm.getKarma() > 749) {
		    cm.gainItem(1402048);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 14:
		if (cm.getKarma() > 49) {
		    cm.gainItem(2049100);
		    cm.gainKarma(-50);
                    } else
                        cm.sendSimple("Your Madness level is below 50.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 15:
		if (cm.getKarma() > 1499) {
		    cm.gainItem(4031348);
		    cm.gainKarma(-1500);
                    } else
                        cm.sendSimple("Your Madness level is below 3000.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
                case 17:
		if (cm.getKarma() > 9) {
      		    cm.gainItem(2340000);
		    cm.gainKarma(-10);
                    } else
                        cm.sendSimple("Your Madness level is below 10.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
  		case 18:
		if (cm.getKarma() > 1) {
      		    cm.gainItem(4031543);
		    cm.gainKarma(-2);
                    } else
                        cm.sendSimple("Your Madness level is below 2.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
  		case 19:
		if (cm.getKarma() > 749) {
      		    cm.gainItem(1060026);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
  		case 20:
		if (cm.getKarma() > 749) {
      		    cm.gainItem(1061039);
		    cm.gainKarma(-750);
                    } else
                        cm.sendSimple("Your Madness level is below 750.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
  		case 21:
		if (cm.getKarma() > 499) {
      		    cm.gainItem(1102191);
		    cm.gainKarma(-500);
                    } else
                        cm.sendSimple("Your Madness level is below 500.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
  		case 22:
		if (cm.getKarma() > 499) {
      		    cm.gainItem(1002492);
		    cm.gainKarma(-500);
                    } else
                        cm.sendSimple("Your Madness level is below 500.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
  		case 23:
		if (cm.getKarma() > 499) {
      		    cm.gainItem(1050127);
		    cm.gainKarma(-500);
                    } else
                        cm.sendSimple("Your Madness level is below 500.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
  		case 24:
		if (cm.getKarma() > 499) {
      		    cm.gainItem(1051140);
		    cm.gainKarma(-500);
                    } else
                        cm.sendSimple("Your Madness level is below 500.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
  		case 25:
		if (cm.getKarma() > 499) {
      		    cm.gainItem(1072263);
		    cm.gainKarma(-500);
                    } else
                        cm.sendSimple("Your Madness level is below 500.\r\n#bYour Madness level is #r#e"+ cm.getKarma() + "");
                    break;
            }       
			
            cm.dispose();
        }
    }
}*/
function start(){
cm.sendOk("#e#rDo you like my wang?");
cm.dispose();
}