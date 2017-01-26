/*
@	Author : Raz
@
@	NPC = Yellow Balloon
@	Map = Hidden-Street <Stage 3>
@	NPC MapId = 922010300
@	Function = LPQ - 3rd Stage
@
*/


var status = 0;
var party;
var preamble;
var gaveItems;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {

         
         if (mode == -1) {
		cm.dispose();//ExitChat
	}else if (mode == 0){
		cm.dispose();//No
	}else{		    //Regular Talk
		if (mode == 1)
			status++;
		else
			status--;
		var eim = cm.getChar().getEventInstance(); 
		var nthtext = "3rd";


                 if (status == 0) {
		 party = eim.getPlayers();
                 preamble = eim.getProperty("leader" + nthtext + "preamble");
		 gaveItems = eim.getProperty("leader" + nthtext + "gaveItems");
                        if (preamble == null) {
                                cm.sendNext("Hi. Welcome to the " + nthtext + " stage.");
                                eim.setProperty("leader" + nthtext + "preamble","done");
                                cm.dispose();
                        }else{
		 if(!isLeader()){
		 if(gaveItems == null){
		 cm.sendOk("Please tell your #bParty-Leader#k to come talk to me");
		 cm.dispose();
		 }else{
		  cm.sendOk("Hurry, goto the next stage, the portal is open!");
		  cm.dispose();
		 }
		}else{
		if(gaveItems == null){
		if(cm.itemQuantity(4001022) >= 32){
		cm.sendOk("Good job! You have collected all 32 #b#t4001022#'s#k\r\n You may continue to the next stage!");
		cm.loseItem(4001022,15);
                cm.givePartyExp(20000, eim.getPlayers());
		}else{
		cm.sendOk("Sorry you don't have all 32 #b#t4001022#'s#k");
		cm.dispose();
		}
		}else{
		cm.sendOk("Hurry, goto the next stage, the portal is open!");
		cm.dispose();
		}
		}}
		}else if (status == 1){
		cm.warpParty(922010400);
		eim.setProperty("3stageclear","true");
		eim.setProperty("leader" + nthtext + "gaveItems","done");
		cm.dispose();
		}            
          }
     }
     
     
function isLeader(){
if(cm.getParty() == null){
return false;
}else{
return cm.isLeader();
}
}