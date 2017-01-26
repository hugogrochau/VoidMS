
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
	cm.sendSimple ("Welcome to the All In One Shop!\n\r Please pick a category.#e#d" +
               "#k\r\n#L80##rMagician Equips" +
                 "#k\r\n#L81##rThief Equips" +
                 "#k\r\n#L82##rWarrior Equips" +
                 "#k\r\n#L83##rArcher Equips" +
                 "#k\r\n#L84##rPirate Equips" +
                 "#k\r\n#L85##rCommon Equips" +
                 "#k\r\n#L86##rScrolls, Pots, ETC" +
			"#k\r\n#L87##rCash Items" +
		 "#k\r\n#L88##rBuy NX Cash");
	  } else if (selection == 80) {
               cm.sendSimple ("Pick a Category #e#d" +
			"#k\r\n#L0##rMagican Shoes" +
			"#k\r\n#L1##rMagican Overalls" +
			"#k\r\n#L2##rMagican Gloves" +
			"#k\r\n#L3##rMagican Hats" +
			"#k\r\n#L4##rMagican Shields" +
			"#k\r\n#L5##rMagican and Common Wands" +
			"#k\r\n#L6##rMagican and Common Staffs");
	  } else if (selection == 81) {
               cm.sendSimple ("Pick a Category #e#d" +
			"#k\r\n#L7##bThief Shoes" +
			"#k\r\n#L8##bThief Bottoms" +
			"#k\r\n#L9##bThief Tops" +
			"#k\r\n#L10##bThief Overalls" +
			"#l\r\n#L11##bThief Gloves" +
			"#l\r\n#L12##bThief Hats" +
			"#l\r\n#L13##bThief Shields" +
			"#l\r\n#L14##bThief and Common Daggers" +
			"#k\r\n#L15##bThief and Common Claws" +
			"#k\r\n#L16##bThief Throwing Stars");
	  } else if (selection == 82) {
               cm.sendSimple ("Pick a Category #e#d" +
			"#k\r\n#L17##dWarrior Shoes" +
			"#k\r\n#L18##dWarrior Bottom" +
			"#k\r\n#L19##dWarrior Top" +
			"#k\r\n#L20##dWarrior Overalls" +
			"#k\r\n#L21##dWarrior Gloves" +
			"#k\r\n#L22##dWarrior Hats" +
			"#k\r\n#L23##dWarrior Shields" +
			"#k\r\n#L24##dWarrior and Common One-Handed Axes" +
			"#k\r\n#L25##dWarrior and Common Two-Handed Axes" +
			"#k\r\n#L26##dWarrior and Common One-Handed BWs" +
			"#k\r\n#L27##dWarrior and Common Two-Handed BWs" +
			"#k\r\n#L28##dWarrior and Common One-Handed Swords" +
			"#k\r\n#L29##dWarrior and Common Two-Handed Swords" +
			"#k\r\n#L30##dWarrior and Common Spears" +
			"#k\r\n#L31##dWarrior and Common Pole Arms");
	  } else if (selection == 83) {
               cm.sendSimple ("Pick a Category #e#d" +
			"#k\r\n#L32##gArcher Shoes" +
			"#k\r\n#L33##gArcher Overalls" +
			"#k\r\n#L34##gArcher Gloves" +
			"#k\r\n#L35##gArcher Hats" +
			"#k\r\n#L36##gArcher and Common Bows" +
			"#k\r\n#L37##gArcher and Common Crossbows" +
			"#k\r\n#L38##gArcher Arrows");
	  } else if (selection == 84) {
               cm.sendSimple ("Pick a Category #e#d" +
			"#k\r\n#L76##bPirate Hats" +
			"#k\r\n#L71##bPirate Weapons" +
			"#k\r\n#L72##bPirate Bullets and Capsules" +
			"#k\r\n#L73##bPirate Overalls" +
			"#k\r\n#L74##bPirate Gloves" +
			"#k\r\n#L75##bPirate Shoes");
	  } else if (selection == 85) {
               cm.sendSimple ("Pick a Category #e#d" +
			"#k\r\n#L39#Maple Weapons" +
			"\r\n#L40#Earrings" +
			"\r\n#L41#Necklaces and Face Accessories" +
			"\r\n#L42#Capes" +
			"\r\n#L43#Common Shoes" +
			"\r\n#L44#Common Hats" +
			
			"\r\n#L46#Common Overalls" +
			"\r\n#L47#Common Shields" +
			"\r\n#L48#Level 0 Weapons");
	  } else if (selection == 86) {
               cm.sendSimple ("Pick a Category #e#d" +
			
			"\r\n#L51#Buffs and Potions" +
			"\r\n#L52#Boss Pieces and Rocks" +
			"\r\n#L57#Chairs" +
			"\r\n#L53#Mounts" +
			"\r\n#L54#All scrolls!");
	  } else if (selection == 87) {
               cm.sendSimple ("Pick a Category #e#d" +
			"\r\n#L55#All Pets" +
			"\r\n#L56#All Pet Equips, Pet Food, Scrolls, Books" +
			"\r\n#L59#Cash Capes" +
			"\r\n#L60#Cash Gloves" +
			"\r\n#L61#Cash Shields" +
			"\r\n#L62#Rings" +
			
			"\r\n#L64#Emotion" +
			"\r\n#L65#Cash Earrings" +
			"\r\n#L66#Eye Accessories" +
			"\r\n#L67#All Accessories" +
			"\r\n#L68#Effects" +
			"\r\n#L69#Cash Throwing Stars" +
			"\r\n#L70#Messengers");
	  } else if (selection == 0) {
		cm.openShop (10000);
		cm.dispose();
	  } else if (selection == 1) {
		cm.openShop (10001);
		cm.dispose();
	  } else if (selection == 2) {
		cm.openShop (10002);
		cm.dispose();
	  } else if (selection == 3) {
		cm.openShop (10003);
		cm.dispose();
	  } else if (selection == 4) {
		cm.openShop (10004);
		cm.dispose();
	  } else if (selection == 5) {
		cm.openShop (10005);
		cm.dispose();
	  } else if (selection == 6) {
		cm.openShop (10006);
		cm.dispose();
	  } else if (selection == 7) {
		cm.openShop (10007);
		cm.dispose();
	  } else if (selection == 8) {
		cm.openShop (10008);
		cm.dispose();
	  } else if (selection == 9) {
		cm.openShop (10009);
		cm.dispose();
	  } else if (selection == 10) {
		cm.openShop (10010);
		cm.dispose();
	  } else if (selection == 11) {
		cm.openShop (10011);
		cm.dispose();
	  } else if (selection == 12) {
		cm.openShop (10012);
		cm.dispose();
	  } else if (selection == 13) {
		cm.openShop (10013);
		cm.dispose();
	  } else if (selection == 14) {
		cm.openShop (10014);
		cm.dispose();
	  } else if (selection == 15) {
		cm.openShop (10015);
		cm.dispose();
	  } else if (selection == 16) {
		cm.openShop (10038);
		cm.dispose();
	  } else if (selection == 17) {
		cm.openShop (10016);
		cm.dispose();
	  } else if (selection == 18) {
		cm.openShop (10017);
		cm.dispose();
	  } else if (selection == 19) {
		cm.openShop (10018);
		cm.dispose();
	  } else if (selection == 20) {
		cm.openShop (10019);
		cm.dispose();
	  } else if (selection == 21) {
		cm.openShop (10020);
		cm.dispose();
	  } else if (selection == 22) {
		cm.openShop (10021);
		cm.dispose();
	  } else if (selection == 23) {
		cm.openShop (10022);
		cm.dispose();
	  } else if (selection == 24) {
		cm.openShop (10023);
		cm.dispose();
	  } else if (selection == 25) {
		cm.openShop (10024);
		cm.dispose();
	  } else if (selection == 26) {
		cm.openShop (10025);
		cm.dispose();
	  } else if (selection == 27) {
		cm.openShop (10026);
		cm.dispose();
	  } else if (selection == 28) {
		cm.openShop (10027);
		cm.dispose();
	  } else if (selection == 29) {
		cm.openShop (10028);
		cm.dispose();
	  } else if (selection == 30) {
		cm.openShop (10029);
		cm.dispose();
	  } else if (selection == 31) {
		cm.openShop (10030);
		cm.dispose();
	  } else if (selection == 32) {
		cm.openShop (10031);
		cm.dispose();
	  } else if (selection == 33) {
		cm.openShop (10032);
		cm.dispose();
	  } else if (selection == 34) {
		cm.openShop (10033);
		cm.dispose();
	  } else if (selection == 35) {
		cm.openShop (10034);
		cm.dispose();
	  } else if (selection == 36) {
		cm.openShop (10035);
		cm.dispose();
	  } else if (selection == 37) {
		cm.openShop (100320);
		cm.dispose();
	  } else if (selection == 38) {
		cm.openShop (10037);
		cm.dispose();
	  } else if (selection == 39) {
		cm.openShop (10051);
		cm.dispose();
	  } else if (selection == 40) {
		cm.openShop (10039);
		cm.dispose();
	  } else if (selection == 41) {
		cm.openShop (10040);
		cm.dispose();
	  } else if (selection == 42) {
		cm.openShop (10041);
		cm.dispose();
	  } else if (selection == 43) {
		cm.openShop (10042);
		cm.dispose();
	  } else if (selection == 44) {
		cm.openShop (10043);
		cm.dispose();
	  } else if (selection == 45) {
		cm.openShop (10044);
		cm.dispose();
	  } else if (selection == 46) {
		cm.openShop (10045);
		cm.dispose();
	  } else if (selection == 47) {
		cm.openShop (10046);
		cm.dispose();
	  } else if (selection == 48) {
		cm.openShop (10047);
		cm.dispose();
	  } else if (selection == 49) {
		cm.openShop (10048);
		cm.dispose();
	  } else if (selection == 50) {
		cm.openShop (10048);
		cm.dispose();
	  } else if (selection == 51) {
		cm.openShop (10049);
		cm.dispose();
	  } else if (selection == 52) {
		cm.openShop (10050);
		cm.dispose();
	  } else if (selection == 53) {
		cm.openShop (10052);
		cm.dispose();
	  } else if (selection == 54) {
		cm.openShop (10053);
		cm.dispose();
	  } else if (selection == 55) {
		cm.openShop (10054);
		cm.dispose();
	  } else if (selection == 56) {
		cm.openShop (10055);
		cm.dispose();
	  } else if (selection == 57) {
		cm.openShop (10056);
		cm.dispose();
	  } else if (selection == 58) {
		cm.openShop (10057);
		cm.dispose();
                } else if (selection == 59) {
		cm.openShop (10058);
		cm.dispose();
                } else if (selection == 60) {
		cm.openShop (10059);
		cm.dispose();
                } else if (selection == 61) {
		cm.openShop (10060);
		cm.dispose();
                } else if (selection == 62) {
		cm.openShop (10061);
		cm.dispose();
                } else if (selection == 63) {
		cm.openShop (10062);
		cm.dispose();
                } else if (selection == 64) {
		cm.openShop (10063);
		cm.dispose();
                } else if (selection == 65) {
		cm.openShop (10064);
		cm.dispose();
                } else if (selection == 66) {
		cm.openShop (10065);
		cm.dispose();
                } else if (selection == 67) {
		cm.openShop (10066);
		cm.dispose();
                } else if (selection == 68) {
		cm.openShop (10067);
		cm.dispose();
                } else if (selection == 69) {
		cm.openShop (10068);
		cm.dispose();
                } else if (selection == 70) {
		cm.openShop (10069);
		cm.dispose();
                } else if (selection == 71) {
		cm.openShop (13035);
		cm.dispose();
                } else if (selection == 72) {
		cm.openShop (13001);
		cm.dispose();
                } else if (selection == 73) {
		cm.openShop (13002);
		cm.dispose();
                } else if (selection == 74) {
		cm.openShop (13003);
		cm.dispose();
                } else if (selection == 75) {
		cm.openShop (13004);
		cm.dispose();
                } else if (selection == 76) {
		cm.openShop (13005);
		cm.dispose();
	  } else if (selection == 88) {
                cm.sendSimple ("How much NX Cash would you like to buy? \n\r\n #L89#10,000 NX for 50,000,000 Mesos\n\r\n#L90#25,000 NX for 125,000,000 Mesos\n\r\n#L91#60,000 NX for 300,000,000 Mesos");
                } else if (selection == 89) {
		if (cm.getPlayer().getMeso() < 50000000) {
                   cm.sendOk ("Please check to see if you have enough #rMesos#k");
                   cm.dispose();
                     } else {
                   cm.gainMeso (-50000000);
                   cm.modifyNx(10000);
                   cm.sendOk("Enjoy your NX Cash!");
                   cm.dispose();
                   }
                } else if (selection == 90) {
		if (cm.getPlayer().getMeso() < 125000000) {
                   cm.sendOk ("Please check to see if you have enough #rMesos#k");
                   cm.dispose();
                     } else {
                   cm.gainMeso (-1250000000);
                   cm.modifyNx(25000);
                   cm.sendOk("Enjoy your NX Cash!");
                   cm.dispose();
                   }
                } else if (selection == 91) {
		if (cm.getPlayer().getMeso() < 300000000) {
                   cm.sendOk ("Please check to see if you have enough #rMesos#k");
                   cm.dispose();
                     } else {
                   cm.gainMeso (-300000000);
                   cm.modifyNx(60000);
                   cm.sendOk("Enjoy your NX Cash!");
                   cm.dispose();
                   }
                } else if (selection == 92) {
		if (cm.getPlayer().getMeso() < 200000000) {
                   cm.sendOk ("Please check to see if you have enough #rMesos#k");
                   cm.dispose();
                     } else {
                   cm.gainMeso (-200000000);
                   cm.modifyNX(40000,4);
                   cm.dispose();
                   }
                } else if (selection == 93) {
		if (cm.getPlayer().getMeso() < 250000000) {
                   cm.sendOk ("Please check to see if you have enough #rMesos#k");
                   cm.dispose();
                     } else {
                   cm.gainMeso (-250000000);
                   cm.modifyNX(50000,4);
                   cm.dispose();
                   }
      }
}
}