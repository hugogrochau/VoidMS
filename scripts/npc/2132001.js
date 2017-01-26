var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 2 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) { 
		cm.sendSimple("Hello #r#h ##k, and welcome to the Chaos Item Upgrade Shop. What would you like?\r\n\r\n#d#L0#What are Chaos Items?#r(Important to Read)#l\r\n#d#L2#Special Drops#l\r\n#L1#I want to get or upgrade my Chaos Items#l\r\n#L3#I want you to make me a Chaos Scroll#l");
	} else if (selection == 0) {
		cm.sendOk("Chaos Items are a special group of items that I crafted, that can be upgraded to get more powerful. You can upgrade them by bringing me several different items and #dChaos Scrolls#k. I hear Chaos Scrolls are available at the Vote Point Shop run by Helena right when you enter Henesys. These items were made to be Chaos Scrolled to become more powerful with Weapon Attack. There is no limit to how many times you can ask for my service and complete my tasks to obtain them. I am here to craft all day. \r\n#rI will always select the lowest grade item in your inventory first. REMEMBER THAT!.");
		cm.dispose();
	} else if (selection == 1) {
		cm.sendSimple("What type of item would you like crafted or upgraded?\r\n#bMake sure the item you want to upgrade is unequipped before you choose the selection.\r\n\r\n#r#L10#Chaos Bandanas#l\r\n#L20#Chaos Bow-ties#l\r\n#L30#Chaos Masks (Theif Only Equip)#l\r\n#L40#Chaos Glasses#l\r\n#L50#Chaos Shoes#l\r\n#L60#Chaos Overalls#l\r\n#L70#Chaos Gloves#l\r\n#L80#Chaos Earrings#l\r\n#L90#Chaos Capes#l");

        } else if (selection == 2) {
                cm.sendOk("Certain items you may not know where to find. \r\nI know of a few special ones that drop.\r\n\r\n#b#i4031185# - Dark Nependeath\r\n#i4031783# - Bubblings\r\n#i4031784# - Cube Slimes\r\n#i4031524# - Jr.Grupins and Grupins\r\n\r\n#kHopefully this helps you a bit on your huntings.");
        } else if (selection == 3) {
                cm.sendSimple("I've learned this technique from the old Chaos Lord, but I'm going to need some items. Get me these and I will see if I can piece it together.\r\n\r\n#i4000298# 1000 Old Papers\r\n#i4011008# 20 Lidiums\r\n#i4002000# 5 Snail Stamps\r\n\r\n#d#L5#Got the stuff right here#l\r\n#L9#Ill come back later#l");
        } else if (selection == 5) {
                if (cm.itemQuantity(4000298) >= 1000 && cm.itemQuantity(4011008) >= 20 && cm.itemQuantity(4002000) >= 5) {
                        cm.loseItem(4000298, 1000);
                        cm.loseItem(4011008, 20);
                        cm.loseItem(4002000, 5);
                        cm.gainItem(2049100);
                        cm.sendOk("Enjoy your Chaos Scroll.");
                        cm.dispose();
                } else {
                        cm.sendOk("You dont seem to have the right items. Mind checking your stuff over again?");
                        cm.dispose();
                }
 // ================================================== Hats


	} else if (selection == 10) {
		if (cm.haveItem(1002781, 1)) {
			cm.sendSimple("I see you have gotten the first #dChaos Bandana#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1002781# Chaos Bandana Level 1\r\n#i4000285# 500 Red Belts\r\n#i4032011# 200 Soiled Rags\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L12#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1002782, 1)) {
			cm.sendSimple("I see you have gotten the second #dChaos Bandana#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1002782# Chaos Bandana Level 2\r\n#i4000048# 1000 Jr.Yeti Skins\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L13#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1002783, 1)) {
			cm.sendSimple("I see you have gotten the third #dChaos Bandana#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1002783# Chaos Bandana Level 3\r\n#i4000284# 1000 Yellow Belts\r\n#i4000162# 1000 Flamboyant Scale Skin\r\n#i2049100# 2 Chaos Scrolls#k\r\n\r\n#d#L14#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1002864, 1)) {
			cm.sendSimple("I see you have gotten the first #dAdvanced Chaos Bandana#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1002864# Advanced Chaos Bandana Level 1\r\n#i4000225# 2000 Kimono Pieces\r\n#i2049100# 2 Chaos Scrolls#k\r\n\r\n#d#L15#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1002865, 1)) {
			cm.sendSimple("I see you have gotten the second #dAdvanced Chaos Bandana#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1002865# Advanced Chaos Bandana Level 2\r\n#i4000035# 1500 Tablecloths\r\n#i4000183# 1500 Ink Bottles\r\n#i2049100# 3 Chaos Scrolls#k\r\n\r\n#d#L16#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have one of my #dChaos Bandanas#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4000155# 500 Seal Skins\r\n#i4000035# 500 Tablecloths\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L11#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ================================================== Bow-ties


	} else if (selection == 20) {
		if (cm.haveItem(1122002, 1)) {
			cm.sendSimple("I see you have gotten the first #dChaos Bow-tie#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1122002# Chaos Bow-Tie Level 1\r\n#i4000021# 200 Leather\r\n#i4000380# 50 Pink Essence\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L22#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1122004, 1)) {
			cm.sendSimple("I see you have gotten the second #dChaos Bow-tie#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1122004# Chaos Bow-Tie Level 2\r\n#i4000021# 300 Leather\r\n\r\n#i4031185# 50 Pots of Honey\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L23#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1122003, 1)) {
			cm.sendSimple("I see you have gotten the third #dChaos Bow-tie#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1122003# Chaos Bow-Tie Level 3\r\n#i4000021# 400 Leather\r\n#i4000382# 50 Blue Essences\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L24#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1122006, 1)) {
			cm.sendSimple("I see you have gotten the fourth #dChaos Bow-tie#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1122006# Chaos Bow-Tie Level 4\r\n#i4000021# 500 Leather\r\n\r\n#i4000384# 50 Black Essences\r\n#i2049100# 2 Chaos Scrolls#k\r\n\r\n#d#L25#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have one of my #dChaos Bow-ties#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4000021# 100 Leather\r\n#i4000232# 50 Kentasarus Flame\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L21#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ================================================== Masks


	} else if (selection == 30) {
		if (cm.haveItem(1012187, 1)) {
			cm.sendSimple("I see you have gotten the first #dChaos Mask#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1012187# Chaos Mask Level 1\r\n#i4000021# 200 Leathers\r\n#i4000379# 50 Green Essence\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L32#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1012188, 1)) {
			cm.sendSimple("I see you have gotten the second #dChaos Mask#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1012188# Chaos Mask Level 2\r\n#i4000021# 300 Leathers\r\n#i4031783# 50 Blue Mysterious Liquids\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L33#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1012189, 1)) {
			cm.sendSimple("I see you have gotten the third #dChaos Mask#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1012189# Chaos Mask Level 3\r\n#i4000021# 400 Leathers\r\n#i4031784# 50 Purple Mysterious Liquids\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L34#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1012190, 1)) {
			cm.sendSimple("I see you have gotten the fourth #dChaos Mask#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i1012190# Chaos Mask Level 4\r\n#i4000021# 500 Leathers\r\n#i4000381# 50 White Essence\r\n#i2049100# 2 Chaos Scrolls#k\r\n\r\n#d#L35#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have one of my #dChaos Masks#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4000021# 100 Leathers\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L31#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ================================================== Glasses


	} else if (selection == 40) {
		if (cm.haveItem(1022113, 1)) {
			cm.sendSimple("I see you have gotten the first pair of #dChaos Glasses#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i1022113# Chaos Glasses Level 2\r\n#i4000332# 250 Sand\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L42#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1022105, 1)) {
			cm.sendSimple("I see you have gotten the second pair of #dChaos Glasses#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i1022105# Chaos Glasses Level 2\r\n#i4000330# 250 Cactus Thorns\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L43#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1022106, 1)) {
			cm.sendSimple("I see you have gotten the third pair of #dChaos Glasses#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i1022106# Chaos Glasses Level 3\r\n#i4000101# 200 Yellow Toy Blocks\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L44#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1022107, 1)) {
			cm.sendSimple("I see you have gotten the fourth pair of #dChaos Glasses#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i1022107# Chaos Glasses Level 4\r\n#i4000078# 500 Jr.Cerebes Teeth\r\n#i2049100# 2 Chaos Scrolls#k\r\n\r\n#d#L45#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have a pair of my #dChaos Glasses#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4000129# 250 Lazy Buffy Marbles\r\n#i2049100# 1 Chaos Scroll#k\r\n\r\n#d#L41#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ================================================== Shoes


	} else if (selection == 50) {
		if (cm.haveItem(1072427, 1)) {
			cm.sendSimple("I see you have gotten the first pair of #dChaos Shoes#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i4032031# 50 Lucky Charms\r\n#i2049100# 1 Chaos Scroll\r\n#i1072427# Chaos Shoes Level 1\r\n\r\n#d#L52#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1072428, 1)) {
			cm.sendSimple("I see you have gotten the second pair of #dChaos Shoes#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i4000102# 150 Blue Toy Blocks\r\n#i2049100# 1 Chaos Scroll\r\n\#i1072428# Chaos Shoes Level 2\r\n\r\n#d#L53#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1072429, 1)) {
			cm.sendSimple("I see you have gotten the third pair of #dChaos Shoes#k. Would you like to upgrade tthem? If so you will need the following:\r\n\r\n#r#i4000264# 200 Rexton Leathers\r\n#i2049100# 1 Chaos Scroll\r\n\#i1072429# Chaos Shoes Level 3\r\n\r\n#d#L54#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1072431, 1)) {
			cm.sendSimple("I see you have gotten the fourth pair of #dChaos Shoes#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i4032030# 100 Stretchy Materials\r\n#i2049100# 2 Chaos Scrolls\r\n#i1072431# Chaos Shoes Level 4\r\n\r\n#d#L55#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1072432, 1)) {
			cm.sendSimple("I see you have gotten the fifth pair of #dChaos Shoes#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i4000273# 300 Old Neck Bones\r\n#i2049100# 3 Chaos Scrolls\r\n#i1072432# Chaos Shoes Level 5\r\n\r\n#d#L56#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have a pair of my #dChaos Shoes#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4001105# 100 Tough Dragon Skins\r\n#i2049100# 1 Chaos Scroll\r\n\r\n#d#L51#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ================================================== Overalls


	} else if (selection == 60) {
		if (cm.haveItem(1052264, 1)) {
			cm.sendSimple("I see you have gotten the first #dChaos Overall#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000064# 100 Crow Feathers\r\n#i4000052# 50 White Pang Tails\r\n#i4000051# 50 Hector Tails\r\n#i2049100# 1 Chaos Scroll\r\n#i1052264# Chaos Overall Level 1\r\n\r\n#d#L62#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1052265, 1)) {
			cm.sendSimple("I see you have gotten the second #dChaos Overall#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000064# 200 Crow Feathers\r\n#i4000052# 100 White Pang Tails\r\n#i4000051# 100 Hector Tails\r\n#i2049100# 1 Chaos Scroll\r\n#i1052265# Chaos Overall Level 2\r\n\r\n#d#L63#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1052266, 1)) {
			cm.sendSimple("I see you have gotten the third #dChaos Overall#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000064# 500 Crow Feathers\r\n#i4000052# 250 White Pang Tails\r\n#i4000051# 250 Hector Tails\r\n#i2049100# 2 Chaos Scrolls\r\n#i1052266# Chaos Overall Level 3\r\n\r\n#d#L64#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have one of my #dChaos Overalls#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4000064# 50 Crow Feathers\r\n#i4000052# 25 White Pang Tails\r\n#i4000051# 25 Hector Tails\r\n#i2049100# 1 Chaos Scroll\r\n\r\n#d#L61#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ================================================== Gloves


	} else if (selection == 70) {
		if (cm.haveItem(1082146, 1)) {
			cm.sendSimple("I see you have gotten the first #dChaos Glove#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000244# 250 Dragon Spirits\r\n#i2049100# 4 Chaos Scrolls\r\n#i1082146# Chaos Gloves Level 1\r\n\r\n#d#L72#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1082148, 1)) {
			cm.sendSimple("I see you have gotten the second #dChaos Glove#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000030# 500 Dragon Skins\r\n#i2049100# 6 Chaos Scrolls\r\n#i1082148# Chaos Gloves Level 2\r\n\r\n#d#L73#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1082147, 1)) {
			cm.sendSimple("I see you have gotten the third #dChaos Glove#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000082# 100 Zombies Lost Gold Teeth\r\n#i2049100# 8 Chaos Scrolls\r\n#i1082147# Chaos Gloves Level 3\r\n\r\n#d#L74#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1082145, 1)) {
			cm.sendSimple("I see you have gotten the fourth #dChaos Glove#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000068# 200 Fierrys Tentacle\r\n#i2049100# 10 Chaos Scrolls\r\n#i1082145# Chaos Gloves Level 4\r\n\r\n#d#L75#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have one of my #dChaos Gloves#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4032003# 100 Phantom Seeds\r\n#i2049100# 2 Chaos Scrolls\r\n\r\n#d#L71#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ================================================== Earrings


	} else if (selection == 80) {
		if (cm.haveItem(1032065, 1)) {
			cm.sendSimple("I see you have gotten the first pair of #dChaos Earrings#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i4032005# 100 Typhons Feathers\r\n#i2049100# 1 Chaos Scroll\r\n#i1032065# Chaos Earrings Level 1\r\n\r\n#d#L82#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1032066, 1)) {
			cm.sendSimple("I see you have gotten the second pair of #dChaos Earrings#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i4000238# 100 Harps Purple Tail Feather\r\n#i2049100# 1 Chaos Scroll\r\n#i1032066# Chaos Earrings Level 2\r\n\r\n#d#L83#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1032067, 1)) {
			cm.sendSimple("I see you have gotten the third pair of #dChaos Earrings#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i4001006# 50 Flaming Feathers\r\n#i2049100# 1 Chaos Scroll\r\n#i1032067# Chaos Earrings Level 3\r\n\r\n#d#L84#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1032068, 1)) {
			cm.sendSimple("I see you have gotten the fourth pair of #dChaos Earrings#k. Would you like to upgrade them? If so you will need the following:\r\n\r\n#r#i4031524# 100 Silk Feathers\r\n#i2049100# 2 Chaos Scrolls\r\n#i1032068# Chaos Earrings Level 4\r\n\r\n#d#L85#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have one of my #dChaos Earrings#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4003005# 100 Soft Feathers\r\n#i2049100# 1 Chaos Scroll\r\n\r\n#d#L81#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ================================================== Capes


	} else if (selection == 90) {
		if (cm.haveItem(1102205, 1)) {
			cm.sendSimple("I see you have gotten the first #dChaos Cape#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000171# 200 Tiger Skins\r\n#i4000152# 200 Thanatos Straps\r\n#i4000151# 200 Gatekeepers Armbands\r\n#i2049100# 1 Chaos Scroll\r\n#i1102205# Chaos Cape Level 1\r\n\r\n#d#L92#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else if (cm.haveItem(1102206, 1)) {
			cm.sendSimple("I see you have gotten the second #dChaos Cape#k. Would you like to upgrade it? If so you will need the following:\r\n\r\n#r#i4000171# 300 Tiger Skins\r\n#i4000152# 300 Thanatos Straps\r\n#i4000151# 300 Gatekeepers Armbands\r\n#i2049100# 1 Chaos Scroll\r\n#i1102206# Chaos Cape Level 2\r\n\r\n#d#L93#I have the items, I want to upgrade.#l\r\n#L9#I need more time.#l");

		} else {
			cm.sendSimple("You dont seem to have one of my #dChaos Capes#k. I can make you one if you want. If you want one, you will need the following:\r\n\r\n#r#i4000171# 100 Tiger Skins\r\n#i4000152# 100 Thanatos Straps\r\n#i4000263# 100 Red Shells\r\n#i2049100# 1 Chaos Scroll\r\n\r\n#d#L91#I have the items.#l\r\n#L9#I need more time.#l");
		}

// ========================================== Upgrades
// ========================================== Hats

	} else if (selection == 9) {
			cm.sendOk("Alright. Take your time and talk to me again when you need something.");
			cm.dispose();
	} else if (selection == 11) {
		if (cm.itemQuantity(4000155) >= 500 && cm.itemQuantity(4000035) >= 500 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000155, 500);
			cm.loseItem(4000035, 500);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1002781, cm.getP(), 1000, 1000, 1000, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 7);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Bandana Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 12) {
		if (cm.itemQuantity(1002781) >= 1 && cm.itemQuantity(4000285) >= 500 && cm.itemQuantity(4032011) >= 200 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000285, 500);
			cm.loseItem(4032011, 200);
                        cm.loseItem(1002781, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1002782, cm.getP(), 1100, 1100, 1100, 1100, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 7);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Bandana Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 13) {
		if (cm.itemQuantity(1002782) >= 1 && cm.itemQuantity(4000048) >= 1000 &&  cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000048, 1000);
			cm.loseItem(2049100, 1);
                        cm.loseItem(1002782, 1);
			cm.makeCustomItem(1002783, cm.getP(), 1200, 1200, 1200, 1200, 0, 0, 0, 0, 0, 0, 0, 0, 15, 15, 7);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Bandana Level 3#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 14) {
		if (cm.itemQuantity(1002783) >= 1 && cm.itemQuantity(4000284) >= 1000 && cm.itemQuantity(4000162) >= 1000 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4000284, 1000);
			cm.loseItem(4000162, 1000);
                        cm.loseItem(1002783, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1002864, cm.getP(), 1300, 1300, 1300, 1300, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 7);
			cm.reloadChar();
                        cm.sendOk("Here is your #dAdvanced Chaos Bandana Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 15) {
		if (cm.itemQuantity(1002864) >= 1 && cm.itemQuantity(4000225) >= 2000 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4000225, 2000);
                        cm.loseItem(1002864, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1002865, cm.getP(), 1400, 1400, 1400, 1400, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 7);
			cm.reloadChar();
                        cm.sendOk("Here is your #dAdvanced Chaos Bandana Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 16) {
		if (cm.itemQuantity(1002865) >= 1 && cm.itemQuantity(4000183) >= 1500 && cm.itemQuantity(4000035) >= 1500 && cm.itemQuantity(2049100) >= 3 && !cm.isFull()) {
			cm.loseItem(4000183, 1500);
			cm.loseItem(4000035, 1500);
                        cm.loseItem(1002865, 1);
			cm.loseItem(2049100, 3);
			cm.makeCustomItem(1002866, cm.getP(), 1500, 1500, 1500, 1500, 0, 0, 200, 200, 0, 0, 0, 0, 30, 30, 7);
			cm.reloadChar();
                        cm.sendOk("Here is your #dMaster Chaos Bandana #k. Congratulations, you have completely upgraded the hats.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 21) {
		if (cm.itemQuantity(4000021) >= 100 && cm.itemQuantity(4000232) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000021, 100);
			cm.loseItem(4000232, 50);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1122002, cm.getP(), 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Bow-Tie Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 22) {
		if (cm.itemQuantity(1122002) >= 1 && cm.itemQuantity(4000021) >= 200 && cm.itemQuantity(4000380) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000021, 200);
			cm.loseItem(4000380, 50);
                        cm.loseItem(1122002, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1122004, cm.getP(), 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Bow-Tie Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 23) {
		if (cm.itemQuantity(1122004) >= 1 && cm.itemQuantity(4000021) >= 300 && cm.itemQuantity(4031185) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000021, 300);
			cm.loseItem(4031185, 50);
                        cm.loseItem(1122004, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1122003, cm.getP(), 30, 30, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Bow-Tie Level 3#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 24) {
		if (cm.itemQuantity(1122003) >= 1 && cm.itemQuantity(4000021) >= 400 && cm.itemQuantity(4000382) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000021, 400);
			cm.loseItem(4000382, 50);
                        cm.loseItem(1122003, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1122006, cm.getP(), 40, 40, 40, 40, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Bow-Tie Level 4#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 25) {
		if (cm.itemQuantity(1122006) >= 1 && cm.itemQuantity(4000021) >= 500 && cm.itemQuantity(4000384) >= 50 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4000021, 500);
			cm.loseItem(4000384, 50);
                        cm.loseItem(1122006, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1122005, cm.getP(), 50, 50, 50, 50, 0, 0, 200, 200, 0, 0, 0, 0, 5, 5, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dMaster Chaos Bow-Tie#k. Congratulations, you have completely upgraded the Bow-Ties.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 31) {
		if (cm.itemQuantity(4000021) >= 100 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000021, 100);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1012187, cm.getP(), 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Mask Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 32) {
		if (cm.itemQuantity(1012187) >= 1 && cm.itemQuantity(4000021) >= 200 && cm.itemQuantity(4000379) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000021, 200);
			cm.loseItem(4000379, 50);
                        cm.loseItem(1012187, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1012188, cm.getP(), 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Mask Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 33) {
		if (cm.itemQuantity(1012188) >= 1 && cm.itemQuantity(4000021) >= 300 && cm.itemQuantity(4031783) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000021, 300);
			cm.loseItem(4031783, 50);
                        cm.loseItem(1012188, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1012189, cm.getP(), 15, 15, 15, 15, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Mask Level 3#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 34) {
		if (cm.itemQuantity(1012189) >= 1 && cm.itemQuantity(4000021) >= 400 && cm.itemQuantity(4031784) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000021, 400);
			cm.loseItem(4031784, 50);
                        cm.loseItem(1012189, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1012190, cm.getP(), 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Mask Level 4#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 35) {
		if (cm.itemQuantity(1012190) >= 1 && cm.itemQuantity(4000021) >= 500 && cm.itemQuantity(4000381) >= 50 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4000021, 500);
			cm.loseItem(4000381, 50);
                        cm.loseItem(1012190, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1012191, cm.getP(), 25, 25, 25, 25, 0, 0, 200, 200, 0, 0, 0, 0, 5, 5, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dMaster Chaos Mask#k. Congratulations, you have completely upgraded the Masks.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 41) {
		if (cm.itemQuantity(4000129) >= 250 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000129, 250);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1022113, cm.getP(), 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 3);
			cm.reloadChar();
                        cm.sendOk("Here are your #dChaos Glasses Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 42) {
		if (cm.itemQuantity(1022113) >= 1 && cm.itemQuantity(4000332) >= 250 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000332, 250);
                        cm.loseItem(1022113, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1022105, cm.getP(), 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 3);
			cm.reloadChar();
                        cm.sendOk("Here are your #dChaos Glasses Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 43) {
		if (cm.itemQuantity(1022105) >= 1 && cm.itemQuantity(4000330) >= 200 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000330, 200);
                        cm.loseItem(1022105, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1022106, cm.getP(), 15, 15, 15, 15, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3);
			cm.reloadChar();
                        cm.sendOk("Here are your #dChaos Glasses Level 3#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 44) {
		if (cm.itemQuantity(1022106) >= 1 && cm.itemQuantity(4000101) >= 200 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000101, 200);
                        cm.loseItem(1022106, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1022107, cm.getP(), 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 3);
			cm.reloadChar();
                        cm.sendOk("Here are your #dChaos Glasses Level 4#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 45) {
		if (cm.itemQuantity(1022107) >= 1 && cm.itemQuantity(4000078) >= 500 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4000078, 500);
                        cm.loseItem(1022107, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1022112, cm.getP(), 25, 25, 25, 25, 0, 0, 200, 200, 0, 0, 0, 0, 5, 5, 3);
			cm.reloadChar();
                        cm.sendOk("Here are your #dMaster Chaos Glasses#k. Congratulations. You have completely upgraded the glasses.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 51) {
		if (cm.itemQuantity(4001105) >= 100 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4001105, 100);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1072427, cm.getP(), 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Shoe Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 52) {
		if (cm.itemQuantity(1072427) >= 1 && cm.itemQuantity(4032031) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4032031, 50);
                        cm.loseItem(1072427, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1072428, cm.getP(), 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Shoe Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 53) {
		if (cm.itemQuantity(1072428) >= 1 && cm.itemQuantity(4000102) >= 150 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000102, 150);
                        cm.loseItem(1072428, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1072429, cm.getP(), 30, 30, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Shoe Level 3#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 54) {
		if (cm.itemQuantity(1072429) >= 1 && cm.itemQuantity(4000264) >= 200 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000264, 200);
                        cm.loseItem(1072429, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1072431, cm.getP(), 40, 40, 40, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Shoe Level 4#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 55) {
		if (cm.itemQuantity(1072431) >= 1 && cm.itemQuantity(4032030) >= 100 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4032030, 100);
                        cm.loseItem(1072431, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1072432, cm.getP(), 50, 50, 50, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Shoe Level 5#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 56) {
		if (cm.itemQuantity(1072432) >= 1 && cm.itemQuantity(4000273) >= 300 && cm.itemQuantity(2049100) >= 3 && !cm.isFull()) {
			cm.loseItem(4000273, 300);
                        cm.loseItem(1072432, 1);
			cm.loseItem(2049100, 3);
			cm.makeCustomItem(1072430, cm.getP(), 60, 60, 60, 60, 0, 0, 200, 200, 0, 0, 0, 0, 5, 5, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dMaster Chaos Shoe#k. Congratulations. You have completely upgraded the shoes.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 61) {
		if (cm.itemQuantity(4000064) >= 50 && cm.itemQuantity(4000052) >= 25 && cm.itemQuantity(4000051) >= 25 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000064, 50);
			cm.loseItem(4000052, 25);
			cm.loseItem(4000051, 25);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1052264, cm.getP(), 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 10);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Overall Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 62) {
		if (cm.itemQuantity(1052264) >= 1 && cm.itemQuantity(4000064) >= 100 && cm.itemQuantity(4000052) >= 50 && cm.itemQuantity(4000051) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000064, 100);
			cm.loseItem(4000052, 50);
			cm.loseItem(4000051, 50);
                        cm.loseItem(1052264, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1052265, cm.getP(), 40, 40, 40, 40, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 10);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Overall Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 63) {
		if (cm.itemQuantity(1052265) >= 1 && cm.itemQuantity(4000064) >= 200 && cm.itemQuantity(4000052) >= 100 && cm.itemQuantity(4000051) >= 100 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000064, 200);
			cm.loseItem(4000052, 100);
			cm.loseItem(4000051, 100);
                        cm.loseItem(1052265, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1052266, cm.getP(), 60, 60, 60, 60, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 10);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Overall Level 3#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 64) {
		if (cm.itemQuantity(1052266) >= 1 && cm.itemQuantity(4000064) >= 500 && cm.itemQuantity(4000052) >= 250 && cm.itemQuantity(4000051) >= 250 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4000064, 500);
			cm.loseItem(4000052, 250);
			cm.loseItem(4000051, 250);
                        cm.loseItem(1052266, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1052267, cm.getP(), 80, 80, 80, 80, 0, 0, 200, 200, 0, 0, 0, 0, 4, 4, 10);
			cm.reloadChar();
                        cm.sendOk("Here is your #dMaster Chaos Overall#k. Congratulations. You have completely upgraded the overalls.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 71) {
		if (cm.itemQuantity(4032003) >= 100 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4032003, 100);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1082146, cm.getP(), 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 15, 15, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Glove Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 72) {
		if (cm.itemQuantity(1082146) >= 1 && cm.itemQuantity(4000244) >= 250 && cm.itemQuantity(2049100) >= 4 && !cm.isFull()) {
			cm.loseItem(4000244, 250);
                        cm.loseItem(1082146, 1);
			cm.loseItem(2049100, 4);
			cm.makeCustomItem(1082148, cm.getP(), 40, 40, 40, 40, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Glove Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 73) {
		if (cm.itemQuantity(1082148) >= 1 && cm.itemQuantity(4000030) >= 500 && cm.itemQuantity(2049100) >= 6 && !cm.isFull()) {
			cm.loseItem(4000030, 500);
                        cm.loseItem(1082148, 1);
			cm.loseItem(2049100, 6);
			cm.makeCustomItem(1082147, cm.getP(), 60, 60, 60, 60, 0, 0, 0, 0, 0, 0, 0, 0, 45, 45, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Glove Level 3#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 74) {
		if (cm.itemQuantity(1082147) >= 1 && cm.itemQuantity(4000082) >= 100 && cm.itemQuantity(2049100) >= 8 && !cm.isFull()) {
			cm.loseItem(4000082, 100);
                        cm.loseItem(1082147, 1);
			cm.loseItem(2049100, 8);
			cm.makeCustomItem(1082145, cm.getP(), 80, 80, 80, 80, 0, 0, 0, 0, 0, 0, 0, 0, 60, 60, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Glove Level 4#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 75) {
		if (cm.itemQuantity(1082145) >= 1 && cm.itemQuantity(4000068) >= 200 && cm.itemQuantity(2049100) >= 10 && !cm.isFull()) {
			cm.loseItem(4000068, 200);
                        cm.loseItem(1082145, 1);
			cm.loseItem(2049100, 10);
			cm.makeCustomItem(1082149, cm.getP(), 100, 100, 100, 100, 0, 0, 200, 200, 0, 0, 0, 0, 75, 75, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dMaster Chaos Glove#k. Congratulations. You have completely upgraded the gloves.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 81) {
		if (cm.itemQuantity(4003005) >= 100 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4003005, 100);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1032065, cm.getP(), 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Earring Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 82) {
		if (cm.itemQuantity(1032065) >= 1 && cm.itemQuantity(4032005) >= 100 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4032005, 100);
                        cm.loseItem(1032065, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1032066, cm.getP(), 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Earring Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 83) {
		if (cm.itemQuantity(1032066) >= 1 && cm.itemQuantity(4000238) >= 100 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000238, 100);
                        cm.loseItem(1032066, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1032067, cm.getP(), 15, 15, 15, 15, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Earring Level 3#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 84) {
		if (cm.itemQuantity(1032067) >= 1 && cm.itemQuantity(4001006) >= 50 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4001006, 50);
                        cm.loseItem(1032067, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1032068, cm.getP(), 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Earring Level 4#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 85) {
		if (cm.itemQuantity(1032068) >= 1 && cm.itemQuantity(4031524) >= 100 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4031524, 100);
                        cm.loseItem(1032068, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1032069, cm.getP(), 25, 25, 25, 25, 0, 0, 200, 200, 0, 0, 0, 0, 5, 5, 3);
			cm.reloadChar();
                        cm.sendOk("Here is your #dMaster Chaos Earring#k. Congratulations. You have completely upgraded the earrings.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 91) {
		if (cm.itemQuantity(4000171) >= 100 && cm.itemQuantity(4000152) >= 100 && cm.itemQuantity(4000263) >= 100 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000171, 100);
			cm.loseItem(4000152, 100);
			cm.loseItem(4000263, 100);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1102205, cm.getP(), 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Cape Level 1#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 92) {
		if (cm.itemQuantity(1102205) >= 1 && cm.itemQuantity(4000171) >= 200 && cm.itemQuantity(4000152) >= 200 && cm.itemQuantity(4000151) >= 200 && cm.itemQuantity(2049100) >= 1 && !cm.isFull()) {
			cm.loseItem(4000171, 200);
			cm.loseItem(4000152, 200);
			cm.loseItem(4000151, 200);
                        cm.loseItem(1102205, 1);
			cm.loseItem(2049100, 1);
			cm.makeCustomItem(1102206, cm.getP(), 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dChaos Cape Level 2#k. Enjoy.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        } else if (selection == 93) {
		if (cm.itemQuantity(1102206) >= 1 && cm.itemQuantity(4000171) >= 300 && cm.itemQuantity(4000152) >= 300 && cm.itemQuantity(4000151) >= 300 && cm.itemQuantity(2049100) >= 2 && !cm.isFull()) {
			cm.loseItem(4000171, 300);
			cm.loseItem(4000152, 300);
			cm.loseItem(4000151, 300);
                        cm.loseItem(1102206, 1);
			cm.loseItem(2049100, 2);
			cm.makeCustomItem(1102207, cm.getP(), 30, 30, 30, 30, 0, 0, 200, 200, 0, 0, 0, 0, 9, 9, 5);
			cm.reloadChar();
                        cm.sendOk("Here is your #dMaster Chaos Cape#k. Congratulations. You have completely upgraded the capes.");
			cm.dispose();
	} else {
			cm.sendOk("You do not seem to have the correct items or amount. Please check your inventory twice.");
			cm.dispose();
	}
        }
	}
}