/* 
 *      Coded by: LilSmilee
 *     Use: Introductionary NPC
 */ 
var status = 0;
	
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status > 1 && mode == 0) 
        status--;
    else if (mode == 1 && status < 2)
        status++;
    else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendNext("#e#rWelcome #b#h ##r#e! This is VoidMS, the environment with excitement. Where you can see true colors in every different way.");	    
    } else if (status == 1) { 
        cm.sendSimple("#eWhat do you want to do?\r\n\r\n#b#L1#Check Out Rates and Website\r\n#L3#Read the FAQ & Other Information\r\n#L4#Get another Wizet Hat!\r\n\#L5#See the 10 last players registered!");
    } else if (status == 2) {
        if (selection == 0) {
            cm.sendPrev("#eHello #b#h ##e#k! This server now has v95 Equipments! Rares are being sort out at the moment. \r\n \r\n #rUpdates \r\n#k-There is a new Vote Exchanger! Talk to Helena in henesys to check out the prizes\r\n-New Donor NPC! Check out Mr.Moneybags in the FM or Henesys to see the gift list\r\n-Added 2 new games to the casino, Minesweeper and the Gemz game");
	 	    
        } else if (selection == 1) {
            cm.sendPrev(" #e#kOur rates are, #r500xExp#k|#r50xMeso#k|#r15xDrop.\r\n #kOur website is at #bhttp://voidms.com \r\n #kOur community is at #bhttp://voiddev.com/forum");
	   
        } else if (selection == 2) {
            cm.sendPrev("#r#eUnder Construction"); // CONSTRUCT THIS BITCH PLZ
        /*cm.sendPrev("#e#rHats \r\n#v1002784##v1002863##v1002921##v1002920##v1003001##v1002844##v1002978##v1002998##v1002999##v1002891##v1002890##v1002889##v1002888##v1002887##v1002892##v1002893##v1002847##v1002849##v1002839##v1002995##v1002969##v1002598##v1002774##v1000031##v1001046##v1000032##v1001047##v1000030##v1001045##v1002609##v1002776##v1002777##v1002778##v1002779##v1002780##v1002790##v1002791##v1002792##v1002793##v1002794##v1002843##v1002842##v1002902##v1002936# \r\n \r\n Tops \r\n #v1042164##v1042150##v1042169##v1042168##v1042165##v1042173##v1042170##v1042149##v1042156# \r\n \r\n Overalls \r\n #v1050138##v1051158##v1050139##v1051159##v1052091##v1052092##v1052093##v1052142# \r\n \r\n Bottoms \r\n #v1062098##v1062104##v1062106##v1062112##v1062119##v1062108##v1062091##v1062075##v1062076# \r\n \r\n Shoes \r\n #v1072407##v1072437##v1072427##v1072428##v1072429##v1072430##v1072431##v1072432##v1072373##v1072355##v1072356##v1072357##v1072358##v1072359##v1072361##v1072362##v1072363##v1072364##v1072365##v1072330##v1072330##v1072333##v1072334##v1072341##v1072349##v1072281##v1072282##v1072283##v1072331# \r\n \r\n Gloves \r\n #v1082233##v1082272##v1082261##v1082263##v1082255##v1082252##v1082245##v1082232##v1082234##v1082235##v1082236##v1082237##v1082238##v1082239##v1082240##v1082241##v1082242##v1082243# \r\n \r\n Capes \r\n #v1102211##v1102174##v1102229##v1102224##v1102223##v1102184##v1102186##v1102214##v1102228##v1102170##v1102209##v1102230##v1102187##v1102188##v1102088##v1102090##v1102233# \r\n \r\n Accessory \r\n #v1022070##v1012137##v1022068##v1022072##v1012076##v1012077##v1012078##v1012079##v1012084##v1012098##v1012074##v1022058##v1032055##v1022067# \r\n \r\n Weapons \r\n #v1702260##v1702256##v1702257##v1702258##v1702259##v1702263##v1702209##v1702203##v1702223##v1702190##v1702204##v1302131##v1432056##v1432066##v1482046##v1302095##v1302098##v1302099##v1302100##v1302101##v1332099##v1382080##v1472100##v1432056##v1432061##v1492037##v1492048##v1492065##v1702177##v1702118##v1702119##v1702120##v1702115##v1702136##v1702149##v1702150##v1702168##v1702166##v1702221##v1702125##v1702237##v1702200##v1702224##v1302061##v1452088##v1472087##v1442088##v1442087##v1302087##v1302103##v1302104##v1302115##v1382077##v1432063##v1432064##v1492047#");*/
        } else if (selection == 3) {
            cm.sendPrev("#e#kHello #b#h ##k, need help? Maybe these FAQs and details will answer your question(s). \r\n \r\nQ: What are the commands in the server? \r\n#rA: You can check by using the command @help \r\n \r\n#kQ: Who are the staff members of this server? \r\n#rA: The command @stafflist will answer that. \r\n \r\n#kQ: How or where can i buy NX? \r\n#rA: Use the command @shop and at the very bottom click Buy NX Cash. \r\n \r\n#kQ: May I become a GM? \r\n#rA: Apply/Create an application for GM position at the forums. \r\n \r\n#kQ: Where can I train? \r\n#rA: Look around in the free market rooms. There are many different monsters spawning there. Use the command @spawner or talk to shane to access our noob - pro spawner. If they are too strong for you, try finding a better map with @spinel. \r\n \r\n #kQ: I need help where the gm mang? \r\n#rA: You can contact a GM by using the command @gmmsg [Message here]. \r\n \r\n#kQ: How do I get rares? \r\n#rA: Win them from events, buy them from others, or gach it with the Gachapon Statue located at @casino(You'll need 1 Blue Wish Ticket to gach it). \r\n \r\n#kQ: Where can I buy weapons & equipments? \r\n#rA: The command @shop sells almost everything. You can also purchase cash items in the cash shop. \r\n\r\n\r\n\r\n\#kOther Information:\r\n--------------------------\r\n\r\n#r-Our currencies are Snail Stamps and Slime Stamps. Snail Stamps are 1,000,000,000 mesos. 1 Slime Stamp = 10 Snail Stamps. \r\n\r\n-We have a very own customized super rebirths system. User the command @srb for more information. \r\n\r\n-You can gamble your Yellow Wish Tickets at the casino. You can access it by using the command @casino or by talking to the NPC Guon in henesys.\r\n\r\n-You can start a drop event by talking to De Lang in henesys. \r\n\r\n-You can PVP(Player Vs Player) by using the command @pvp or talking to the NPC Tombstone in henesys. \r\n\r\n-If you like to do Jump Quests, use the command @jq or talk to NPC Banquet Master in henesys.\r\n-You can use '@buff set' to select personalized buffs then use them with '@buffme'.\r\n\r\n#k#eHave Fun!");     	    
        } else if (selection == 4) {
            if (cm.itemQuantity(1002140) < 1) {	
                cm.gainItem(1002140, 1);
                cm.sendPrev("Here you Go, Don't Lose it this time.");
            } else {
                cm.sendPrev("#e#rYou already have one");
            }	
        } else if (selection == 5) {
            cm.sendPrev("#e" + cm.getLatestPlayers());
        }
    } 
}  

