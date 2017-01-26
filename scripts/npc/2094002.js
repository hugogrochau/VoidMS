var status = 0;
function start () {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	selected = selection
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.sendSimple("I am a...\r\n#b#L0#Warrior#l\r\n#L1#Magician#l\r\n#L2#Bowman#l\r\n#L3#Thief#l\r\n#L4#Pirate#l");
						
return;
		}
		if (mode == 1)
			status ++;
		else
			status --;
		if (status == 0) {
			cm.sendSimple("#eHello, #h #! I was hired by the Staff of VoidMS to give you suggestions on certain skills to save on your Keyboard! I suggest you talk to me when you are Level 200, so you can have the maximum amount of skills!\r\n\r\n Please pick your job, to see what you should save.\r\n I am a...\r\n#b#L0#Warrior#l\r\n#L1#Magician#l\r\n#L2#Bowman#l\r\n#L3#Thief#l\r\n#L4#Pirate#l");
			} else if (selection == 0) {
				cm.sendSimple("#eOh, you picked Warrior. Wise choice.\r\n\r\nI am a...\r\n#b#L5#Hero#l\r\n#L6#Dark Knight#l\r\n#L7#Paladin#l");
            } else if (selection == 1) {
                cm.sendSimple("#eOh, you picked Magician. Wise choice.\r\n\r\nI am a...\r\n#b#L8#Bishop#l\r\n#L9#Arch Mage (Ice/Lightning)#l\r\n#L10#Arch Mage (Fire/Poison)#l");
            } else if (selection == 2) {
                cm.sendSimple("#eOh, you picked Bowman. Wise choice.\r\n\r\nI am a...\r\n#b#L11#Bowmaster#l\r\n#L12#Marksman#l");
            } else if (selection == 3) {
                cm.sendSimple("#eOh, you picked Thief. Wise choice.\r\n\r\nI am a...\r\n#b#L13#Night Lord#l\r\n#L14#Shadower#l");
            } else if (selection == 4) {
                cm.sendSimple("#eOh, you picked Pirate. Wise choice.\r\n\r\nI am a...\r\n#b#L15#Buccaneer#l\r\n#L16#Corsair#l");
            } else if (selection == 5) {
                cm.sendPrev("#eIf you are a Hero, some skills you should keep are\r\n\r\n#s1201004# - Sword Booster\r\n#s1111002# - Combo Attack\r\n#s1121008# - Brandish\r\n#s1121006# - Rush\r\n#s1121002# - Power Stance");
            } else if (selection == 6) {
                cm.sendPrev("#eIf you are a Dark Knight, some skills you should keep are\r\n\r\n#s1301004# - Spear Booster\r\n#s9101008# - Hyper Body\r\n#s1311006# - Dragon Roar");
            } else if (selection == 7) {
                cm.sendPrev("#eIf you are a Paladin, some skills you should keep are\r\n\r\n#s1201004# - Sword Booster\r\n#s1201005# - Blunt Weapon Booster\r\n#s1211003# - Fire Charge: Sword\r\n#s1211004# - Flame Charge: Blunt Weapon\r\n#s1211005# - Ice Charge: Sword\r\n#s1211006# - Blizzard Charge: Blunt Weapon\r\n#s1211007# - Thunder Charge: Sword\r\n#s1211008# - Lightning Charge: Blunt Weapon\r\n#s1221003# - Holy Charge: Sword\r\n#s1221004# - Divine Charge: Blunt Weapon\r\n#s1221011# - Heaven's Hammer");
            } else if (selection == 8) {
                cm.sendPrev("#eIf you are a Bishop, some skills you should keep are\r\n\r\n#s2301001# - Teleport\r\n#s2311003# - Holy Symbol\r\n#s2321004# - Infinity\r\n#s2321008# - Genesis");
			} else if (selection == 9) {
                cm.sendPrev("#eIf you are an Arch Mage(Ice/Lightning), some skills you should keep are\r\n\r\n#s2101002# - Teleport\r\n#s2321004# - Infinity\r\n#s2221007# - Blizzard");
            } else if (selection == 10) {
                cm.sendPrev("#eIf you are an Arch Mage(Fire/Poison), some skills you should keep are\r\n\r\n#s2201002# - Teleport\r\n#s2121004# - Infinity\r\n#s2121007# - Meteor Shower");
            } else if (selection == 11) {
                cm.sendPrev("#eIf you are a Bowmaster, some skills you should keep are\r\n\r\n#s3101002# - Bow Booster\r\n#s3101004# - Soul Arrow: Bow\r\n#s3121002# - Sharp Eyes\r\n#s3121004# - Hurricane");
			} else if (selection == 12) {
                cm.sendPrev("#eIf you are a Marksman, some skills you should keep are\r\n\r\n#s3201002# - Crossbow Booster\r\n#s3201004# - Shoul Arrow: Crossbow\r\n#s3221002# - Sharp Eyes");
            } else if (selection == 13) {
                cm.sendPrev("#eIf you are a Night Lord, some skills you should keep are\r\n\r\n#s4001003# - Dark Sight\r\n#s4101003# - Claw Booster\r\n#s4201003# - Haste\r\n#s4111002# - Shadow Partner\r\n#s4111006# - Flash Jump\r\n#s4121007# - Triple Throw\r\n#s4121006# - Shadow Stars");
            } else if (selection == 14) {
                cm.sendPrev("#eIf you are a Shadower, some skills you should keep are\r\n\r\n#s4001003# - Dark Sight\r\n#s4201002# - Dagger Booster\r\n#s4201003# - Haste\r\n#s4201005# - Savage Blow\r\n#s4221007# - Boomerang Step\r\n#s4221001# - Assassinate");
			} else if (selection == 15) {
                cm.sendPrev("#eIf you are a Buccaneer, some skills you should keep are\r\n\r\n#s5101006# - Knuckler Booster\r\n#s5121007# - Barrage\r\n#s5121001# - Dragon Strike\r\n#s5121010# - Time Leap");
            } else if (selection == 16) {
                cm.sendPrev("#eIf you are a Corsair, some skills you should keep are\r\n\r\n#s5201003# - Gun Booster\r\n#s5201005# - Wings\r\n#s5221003# - Aerial Strike\r\n#s5221004# - Rapid Fire");
      			}
		}
	}