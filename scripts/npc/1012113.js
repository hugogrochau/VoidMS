function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("Hello #h #! I'm #rTommy#k. \r\nI'm the gamemaster of this game: Hit and Run! \r\n #L0#Sounds cool, how do you play it?#l \r\n#L1#I want to play a practice round!#l \r\n#L2#I want to leave.#l");
        } else if (selection == 0) { 
            cm.sendPrev("Hit and Run is a game played in 2 teams. \r\nThere are #bAttackers#k, you guys, and 1 #bDefender#k, the GM. \r\nThe game is played in a special map, which has some good hiding spots in it. \r\nIn this map the Attackers will have #r5 minutes#k to break the defender's #gReactor#k and loot the flag it drops. \r\n\r\nIf you can drop the #i4001025# on your own platform, you win the game.\r\nBut if he can kill the attackers by tagging them and prevent you all to break the #gReactor#k for 5 mins, the GM wins! \r\n\r\n#e#bComing soon\r\nThis game (and maybe some others), will be used to gain Guild Points for your guild! So start practicing your hit and run skills as you will need them to increase the glory of your guild!#k#n ");
        }
        else if (selection == 1) {
            if (cm.isLeader) {
                cm.sendOk("If you got your party of 6 people ready, contact a GM and ask him politely to challenge you in a game of Hit and Run");
                cm.dispose();
            }else{
                cm.sendOk("You need a 6 man party to challenge a GM, get a party and make use @staffonline to find any active GM's then whisper them!"); 
                cm.dispose();
			}
		} else if (selection == 2) { 
				cm.warp(100000200);
				cm.dispose();
        
            }
        }
    }
  