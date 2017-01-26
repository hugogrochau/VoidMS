 function start() {
	cm.sendOk("Under Construction");
	cm.dispose();
}/*

importPackage(Packages.client);



var status = 0;
var chance1 = Math.floor(Math.random()*200+1);
var chance2 = Math.floor(Math.random()*50);
var chance3 = (Math.floor(Math.random()*20)+1);
var chance4 = Math.floor(Math.random()*2+1);
var itemchance = chance1 + chance2 + chance3 * chance4;
var itemamount = Math.floor(Math.random()*50+1);
var stirge = Array(1060127,1102176,1061149,1302088,1102177,1060128,1061150,1102178,
                   1302089,1060129,1061151,1102179,1102180,1102181,1102182,1302090,
                   1302091,1060130,1060131,1061152,1061153,1302092,1061154,1060132,
                   1302094,1102183,1060133,1061155);
var stirgerandom = Math.round(Math.random()*stirge);
var timeless = Array(1372035,1372036,1372037,1372038,1372039,1372040,1372041,1372042,
                      1382045,1382046,1382047,1382048,1382049,1382050,1382051,1382052);
var timerandom = Math.round(Math.random()*timeless);
var realtime = Array(1302081,1312037,1322060,1402046,1412033,1422037,1482023);
var rtrandom = Math.round(Math.random()*realtime);
var scrolls = Array(2044503,2044703,2044603,2043303,2040807,2040806,2043103,2043203,2043003,2040506,
2044403,2040903,2040709,2040710,2040711,2044303,2043803,2040403,2044103,2044203,2044003,2043703); // 22
var ranscrolls = Math.round(Math.random()*scrolls);



function start() {
	status = -1;
	action(1, 0, 0);
}


function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 2 && mode == 0) {
			cm.sendOk("See you next time, when you try your luck here~!");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			if (cm.isFull() && cm.isUseFull() && cm.isSetupFull && cm.isEtcFull) {
				cm.sendOk("Please make atleast one space in all your inventories.");
				cm.dispose();
			} else {
				cm.sendNext("Welcome to the VoidMS Gachapon! I work using Green Wish Tickets! If you have one, please click next.");
			}
		} else if (status == 1) {
			if (cm.haveItem(4031544)) {
			cm.sendYesNo("I see you have a ticket of mine, do you wish to use it?");
			}
			else if (!cm.haveItem(4031544)) {
			cm.sendOk("You dont have any #bGreen Wish Tickets#k.");
			cm.dispose();
			}
		}
		else if (status == 2) {
			cm.loseItem(4031544, 1);
                        
			if ((itemchance >= 1) && (itemchance <= 20)) {
			cm.gainOneItem(2000004, itemamount);
			}
			else if ((itemchance >= 21) && (itemchance <= 40)) {
			cm.gainOneItem(2020012, itemamount);
			}
			else if ((itemchance >= 41) && (itemchance <= 50)) {
			cm.gainOneItem(2000005, itemamount);
			}
			else if ((itemchance >= 51) && (itemchance <= 60)) {
			cm.gainOneItem(2030007, itemamount);
			}
			else if ((itemchance >= 61) && (itemchance <= 70)) {
			cm.gainOneItem(2022027, itemamount);
			}
			else if (itemchance == 71) {
			cm.gainOneItem(2040001, 1);
			}
			else if (itemchance == 72) {
			cm.gainOneItem(2041002, 1);
			}
			else if (itemchance == 73) {
			cm.gainOneItem(stirge[stirgerandom], 1);
			}
			else if (itemchance == 74) {
			cm.gainOneItem(2040702, 1);
			}
			else if (itemchance == 75) {
			cm.gainOneItem(2043802, 1);
			}
			else if (itemchance == 76) {
			cm.gainOneItem(2040402, 1);
			}
			else if (itemchance == 77) {
			cm.gainOneItem(stirge[stirgerandom], 1);
			}
			else if (itemchance == 78) {
			cm.gainOneItem(1302022, 1);
			}
			else if (itemchance == 79) {
			cm.gainOneItem(1302022, 1);
			}	
			else if (itemchance == 80) {
			cm.gainOneItem(1322026, 1);
			}	
			else if (itemchance == 81) {
			cm.gainOneItem(1302026, 1);
			}
			else if (itemchance == 82) {
			cm.gainOneItem(1442017, 1);
			}
			else if (itemchance == 83) {
			cm.gainOneItem(realtime[rtrandom], 1);
			}	
			else if (itemchance == 84) {
			cm.gainOneItem(1102043, 1);
			}
			else if (itemchance == 85) {
			cm.gainOneItem(1442016, 1);
			}
			else if (itemchance == 86) {
			cm.gainOneItem(stirge[stirgerandom], 1);
			}
			else if (itemchance == 87) {
			cm.gainOneItem(1302027, 1);
			}	
			else if (itemchance == 88) {
			cm.gainOneItem(realtime[rtrandom], 1);
			}
			else if (itemchance == 89) {
			cm.gainOneItem(1322025, 1);
			}
			else if (itemchance == 90) {
			cm.gainOneItem(1312012, 1);
			}
			else if (itemchance == 91) {
			cm.gainOneItem(1302022, 1);
			}
			else if (itemchance == 92) {
			cm.gainOneItem(timeless[timerandom], 1);
			}
			else if (itemchance == 93) {
			cm.gainOneItem(1302028, 1);
			}
			else if (itemchance == 94) {
			cm.gainOneItem(1372002, 1);
			}
			else if (itemchance == 95) {
			cm.gainOneItem(1002033, 1);
			}
			else if (itemchance == 96) {
			cm.gainOneItem(1092022, 1);
			}
			else if (itemchance == 97) {
			cm.gainOneItem(1302021, 1);
			}
			else if (itemchance == 98) {
			cm.gainOneItem(timeless[timerandom], 1);
			}
			else if (itemchance == 99) {
			cm.gainOneItem(1102042, 1);
			}
			else if (itemchance == 100) {
			cm.gainOneItem(1322024, 1);
			}
			else if (itemchance == 101) {
			cm.gainOneItem(1082148, 1);
			}
			else if (itemchance == 102) {
			cm.gainOneItem(1002012, 1);
			}
			else if (itemchance == 103) {
			cm.gainOneItem(scrolls[ranscrolls], 1);
			}
			else if (itemchance == 104) {
			cm.gainOneItem(1322022, 1);
			}
			else if (itemchance == 105) {
			cm.gainOneItem(1002020, 1);
			}
			else if (itemchance == 106) {
			cm.gainOneItem(1302013, 1);
			}
			else if (itemchance == 107) {
			cm.gainOneItem(1082146, 1);
			}
			else if (itemchance == 108) {
			cm.gainOneItem(timeless[timerandom], 1);
			}
			else if (itemchance == 109) {
			cm.gainOneItem(1002096, 1);
			}
			else if (itemchance == 110) {
			cm.gainOneItem(1302017, 1);
			}
			else if (itemchance == 111) {
			cm.gainOneItem(1442012, 1);
			}
			else if (itemchance == 112) {
			cm.gainOneItem(1322010, 1);
			}
			else if (itemchance == 113) {
			cm.gainOneItem(scrolls[ranscrolls], 1);
			}
			else if (itemchance == 114) {
			cm.gainOneItem(1442018, 1);
			}
			else if (itemchance == 115) {
			cm.gainOneItem(1092011, 1);
			}
			else if (itemchance == 116) {
			cm.gainOneItem(timeless[timerandom], 1);
			}
			else if (itemchance == 117) {
			cm.gainOneItem(1302003, 1);
			}
			else if (itemchance == 118) {
			cm.gainOneItem(1432001, 1);
			}
			else if (itemchance == 119) {
			cm.gainOneItem(1312011, 1);
			}
			else if (itemchance == 120) {
			cm.gainOneItem(1002088, 1);
			}
			else if (itemchance == 121) {
			cm.gainOneItem(1041020, 1);
			}
			else if (itemchance == 122) {
			cm.gainOneItem(4000313, 1);
			}
			else if (itemchance == 123) {
			cm.gainOneItem(1442004, 1);
			}
			else if (itemchance == 124) {
			cm.gainOneItem(1422008, 1);
			}
			else if (itemchance == 125) {
			cm.gainOneItem(1302056, 1);
			}
			else if (itemchance == 126) {
			cm.gainOneItem(timeless[timerandom], 1);
			}
			else if (itemchance == 127) {
			cm.gainOneItem(1382001, 1);
			}
			else if (itemchance == 128) {
			cm.gainOneItem(1041053, 1);
			}
			else if (itemchance == 129) {
			cm.gainOneItem(1060014, 1);
			}
			else if (itemchance == 130) {
			cm.gainOneItem(1050053, 1);
			}
			else if (itemchance == 131) {
			cm.gainOneItem(1051032, 1);
			}
			else if (itemchance == 132) {
			cm.gainOneItem(1050073, 1);
			}
			else if (itemchance == 133) {
			cm.gainOneItem(1061036, 1);
			}
			else if (itemchance == 134) {
			cm.gainOneItem(1002253, 1);
			}
			else if (itemchance == 135) {
			cm.gainOneItem(1002034, 1);
			}
			else if (itemchance == 136) {
			cm.gainOneItem(timeless[timerandom], 1);
			}
			else if (itemchance == 137) {
			cm.gainOneItem(1050067, 1);
			}
			else if (itemchance == 138) {
			cm.gainOneItem(1051052, 1);
			}
			else if (itemchance == 139) {
			cm.gainOneItem(1002072, 1);
			}
			else if (itemchance == 140) {
			cm.gainOneItem(1002144, 1);
			}
			else if (itemchance == 141) { 
			cm.gainOneItem(1051054, 1);
			}
			else if (itemchance == 142) { 
			cm.gainOneItem(1050069, 1);
			}
			else if (itemchance == 143) { 
			cm.gainOneItem(1372007, 1);
			}
			else if (itemchance == 144) { 
			cm.gainOneItem(scrolls[ranscrolls], 1);
			}
			else if (itemchance == 145) { 
			cm.gainOneItem(1050074, 1);
			}
			else if (itemchance == 146) { 
			cm.gainOneItem(1002254, 1);
			}
			else if (itemchance == 147) {
			cm.gainOneItem(1002274, 1);
			}
			else if (itemchance == 148) { 
			cm.gainOneItem(1002218, 1);
			}
			else if (itemchance == 149) { 
			cm.gainOneItem(1051055, 1);
			}
			else if (itemchance == 150) { 
			cm.gainOneItem(1382010, 1);
			}
			else if (itemchance == 151) { 
			cm.gainOneItem(1002246, 1);
			}
			else if (itemchance == 152) { 
			cm.gainOneItem(1050039, 1);
			}
			else if (itemchance == 153) { 
			cm.gainOneItem(1382007, 1);
			}
			else if (itemchance == 154) { 
			cm.gainOneItem(1372000, 1);
			}
			else if (itemchance == 155) { 
			cm.gainOneItem(1002013, 1);
			}
			else if (itemchance == 156) { 
			cm.gainOneItem(1050072, 1);
			}
			else if (itemchance == 157) { 
			cm.gainOneItem(1002036, 1);
			}
			else if (itemchance == 158) { 
			cm.gainOneItem(1002243, 1);
			}
			else if (itemchance == 159) { 
			cm.gainOneItem(1372008, 1);
			}
			else if (itemchance == 160) { 
			cm.gainOneItem(1382008, 1);
			}
			else if (itemchance == 161) { 
			cm.gainOneItem(1382011, 1);
			}
			else if (itemchance == 162) { 
			cm.gainOneItem(1092021, 1);
			}
			else if (itemchance == 163) { 
			cm.gainOneItem(1051034, 1);
			}
			else if (itemchance == 164) { 
			cm.gainOneItem(1050047, 1);
			}
			else if (itemchance == 165) { 
			cm.gainOneItem(1040019, 1);
			}
			else if (itemchance == 166) { 
			cm.gainOneItem(1041031, 1);
			}
			else if (itemchance == 167) { 
			cm.gainOneItem(1051033, 1);
			}
			else if (itemchance == 168) { 
			cm.gainOneItem(1002153, 1);
			}
			else if (itemchance == 169) { 
			cm.gainOneItem(1002252, 1);
			}
			else if (itemchance == 170) { 
			cm.gainOneItem(1051024, 1);
			}
			else if (itemchance == 171) { 
			cm.gainOneItem(1002153, 1);
			}
			else if (itemchance == 172) { 
			cm.gainOneItem(1050068, 1);
			}
			else if (itemchance == 173) { 
			cm.gainOneItem(1382003, 1);
			}
			else if (itemchance == 174) { 
			cm.gainOneItem(1382006, 1);
			}
			else if (itemchance == 175) { 
			cm.gainOneItem(1050055, 1);
			}
			else if (itemchance == 176) { 
			cm.gainOneItem(1051031, 1);
			}
			else if (itemchance == 177) { 
			cm.gainOneItem(1050025, 1);
			}
			else if (itemchance == 178) { 
			cm.gainOneItem(1002155, 1);
			}
			else if (itemchance == 179) { 
			cm.gainOneItem(1002245, 1);
			}
			else if (itemchance == 180) { 
			cm.gainOneItem(13720013, 1);
			}
			else if (itemchance == 181) { 
			cm.gainOneItem(1452004, 1);
			}
			else if (itemchance == 182) { 
			cm.gainOneItem(1452023, 1);
			}
			else if (itemchance == 183) { 
			cm.gainOneItem(1060057, 1);
			}
			else if (itemchance == 184) { 
			cm.gainOneItem(1040071, 1);
			}
			else if (itemchance == 185) { 
			cm.gainOneItem(1002137, 1);
			}
			else if (itemchance == 186) { 
			cm.gainOneItem(1462009, 1);
			}
			else if (itemchance == 187) { 
			cm.gainOneItem(1452017, 1);
			}
			else if (itemchance == 188) { 
			cm.gainOneItem(1040025, 1);
			}
			else if (itemchance == 189) { 
			cm.gainOneItem(1041027, 1);
			}
			else if (itemchance == 190) { 
			cm.gainOneItem(1452005, 1);
			}
			else if (itemchance == 191) { 
			cm.gainOneItem(1452007, 1);
			}
			else if (itemchance == 192) { 
			cm.gainOneItem(1061057, 1);
			}
			else if (itemchance == 193) { 
			cm.gainOneItem(1472006, 1);
			}
			else if (itemchance == 194) { 
			cm.gainOneItem(1472019, 1);
			}
			else if (itemchance == 195) { 
			cm.gainOneItem(1060084, 1);
			}
			else if (itemchance == 196) { 
			cm.gainOneItem(1472028, 1);
			}
			else if (itemchance == 197) { 
			cm.gainOneItem(1002179, 1);
			}
			else if (itemchance == 198) { 
			cm.gainOneItem(1082074, 1);
			}
			else if (itemchance == 199) { 
			cm.gainOneItem(1332015, 1);
			}
			else if (itemchance == 200) { 
			cm.gainOneItem(1432001, 1);
			}
			else if (itemchance == 201) { 
			cm.gainOneItem(1060071, 1);
			}
			else if (itemchance == 202) { 
			cm.gainOneItem(1472007, 1);
			}
			else if (itemchance == 203) { 
			cm.gainOneItem(1472002, 1);
			}
			else if (itemchance == 204) { 
			cm.gainOneItem(1051009, 1);
			}
			else if (itemchance == 205) { 
			cm.gainOneItem(1061037, 1);
			}
			else if (itemchance == 206) { 
			cm.gainOneItem(1332016, 1);
			}
			else if (itemchance == 207) { 
			cm.gainOneItem(1302022, 1);
			}
			else if (itemchance == 208) { 
			cm.gainOneItem(1472020, 1);
			}
			else if ((itemchance >= 209) && (itemchance <= 215)) { 
			cm.gainOneItem(1102084, 1);
			}
			else if ((itemchance >= 216) && (itemchance <= 221)) { 
			cm.gainOneItem(1102086, 1);
			}
			else if ((itemchance >= 222) && (itemchance <= 228)) { 
			cm.gainOneItem(1102042, 1);
			}
			else if ((itemchance >= 228) && (itemchance <= 240)) { 
			cm.gainOneItem(1032026, 1);
			}


			cm.dispose();
		}
	}
}*/