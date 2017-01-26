function start() {
cm.sendOk("#eUnder Construction");
cm.dispose();
}
/*var status = 0;
var rate = 2;

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
            cm.sendSimple ("#eHello #d#h ##k. I'm #rShadrian#k the Collector.\r\nI need some ETC's from monsters around \r\nVoidMS to add to my collection.\r\nSo would you mind bringing me some? I will Reward you.#n\r\n\r\n#b#L0#What do I get if I bring some?#l\r\n#L1#I have some etc here. Which ones do you take?#l");
        } else if (selection == 0) {
            cm.sendOk("Well, If you go around collecting ETC's for me around Madstory, I'll reward you with a couple of #rKarma Points#k. Depending on the type of ETC will depend on how much Madness I give you. I only take stacks of #b250#k and #b500#k so remember that. If your up for the challenge or have some ETC's to give me, come back and talk to me again.");
            cm.dispose();
        } else if (selection == 1) {
            cm.sendSimple("What region is your ETC's from?\r\n\r\n#L2##bVictoria Island#l\r\n#L3#Masteria/Haunted House/Crimson Woodkeep#l\r\n#L4#Aqua Road#l\r\n#L5#Ludus Lake#l\r\n#L6#Mu Lung Garden/Ariant#l\r\n#L7#Minar Forest#l\r\n#L8#El Nath and Orbis#l");
        } else if (selection == 2) {
            cm.sendSimple("These are the ETC's im looking for. Have any?\r\n\r\n#L10##i4000029##l#L11##i4000044##l#L12##i4000177##l#L13##i4000197##l#L14##i4000033##l\r\n#L15##i4000041##l#L16##i4000023##l#L17##i4000036##l#L18##i4000039##l#L19##i4000067##l");
        } else if (selection == 3) {
            cm.sendSimple("These are the ETC's im looking for. Have any?\r\n\r\n#L20##i4000400##l#L21##i4000399##l#L22##i4032011##l#L23##i4032031##l#L24##i4032012##l\r\n#L25##i4032004##l#L26##i4032005##l#L27##i4032008##l#L28##i4032006##l#L29##i4032010##l");
        } else if (selection == 4) {
            cm.sendSimple("These are the ETC's im looking for. Have any?\r\n\r\n#L30##i4000162##l#L31##i4000163##l#L32##i4000154##l#L33##i4000155##l#L34##i4000179##l\r\n#L35##i4000153##l#L36##i4000182##l#L37##i4000181##l#L38##i4000159##l#L39##i4000183##l");
        } else if (selection == 5) {
            cm.sendSimple("These are the ETC's im looking for. Have any?\r\n\r\n#L40##i4021009##l#L41##i4000172##l#L42##i4000132##l#L43##i4000173##l#L44##i4000147##l\r\n#L45##i4000169##l#L46##i4000131##l#L47##i4000105##l#L48##i4000119##l#L49##i4000118##l");
        } else if (selection == 6) {
            cm.sendSimple("These are the ETC's im looking for. Have any?\r\n\r\n#L50##i4000326##l#L51##i4000328##l#L52##i4000297##l#L53##i4000277##l#L54##i4000331##l\r\n#L55##i4000287##l#L56##i4000285##l#L57##i4000289##l#L58##i4000295##l#L59##i4000325##l");
        } else if (selection == 7) {
            cm.sendSimple("These are the ETC's im looking for. Have any?\r\n\r\n#L60##i4000234##l#L61##i4000242##l#L62##i4000270##l#L63##i4000263##l#L64##i4000240##l\r\n#L65##i4000261##l#L66##i4000237##l#L67##i4000264##l#L68##i4000271##l#L69##i4000226##l");
        } else if (selection == 8) {
            cm.sendSimple("These are the ETC's im looking for. Have any?\r\n\r\n#L70##i4000082##l#L71##i4000063##l#L72##i4000074##l#L73##i4000053##l#L74##i4000056##l\r\n#L75##i4000076##l#L76##i4000060##l#L77##i4000010##l#L78##i4000070##l#L79##i4000054##l");
        } else if (selection == 10) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L110##b250#l\r\n#L210#500#l");
        } else if (selection == 11) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L111##b250#l\r\n#L211#500#l");
        } else if (selection == 12) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L112##b250#l\r\n#L212#500#l");
        } else if (selection == 13) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L113##b250#l\r\n#L213#500#l");
        } else if (selection == 14) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L114##b250#l\r\n#L214#500#l");
        } else if (selection == 15) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L115##b250#l\r\n#L215#500#l");
        } else if (selection == 16) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L116##b250#l\r\n#L216#500#l");
        } else if (selection == 17) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L117##b250#l\r\n#L217#500#l");
        } else if (selection == 18) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L118##b250#l\r\n#L218#500#l");
        } else if (selection == 19) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L119##b250#l\r\n#L219#500#l");
        } else if (selection == 20) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L120##b250#l\r\n#L220#500#l");
        } else if (selection == 21) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L121##b250#l\r\n#L221#500#l");
        } else if (selection == 22) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L122##b250#l\r\n#L222#500#l");
        } else if (selection == 23) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L123##b250#l\r\n#L223#500#l");
        } else if (selection == 24) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L124##b250#l\r\n#L224#500#l");
        } else if (selection == 25) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L125##b250#l\r\n#L225#500#l");
        } else if (selection == 26) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L126##b250#l\r\n#L226#500#l");
        } else if (selection == 27) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L127##b250#l\r\n#L227#500#l");
        } else if (selection == 28) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L128##b250#l\r\n#L228#500#l");
        } else if (selection == 29) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L129##b250#l\r\n#L229#500#l");
        } else if (selection == 30) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L130##b250#l\r\n#L230#500#l");
        } else if (selection == 31) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L131##b250#l\r\n#L231#500#l");
        } else if (selection == 32) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L132##b250#l\r\n#L232#500#l");
        } else if (selection == 33) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L133##b250#l\r\n#L233#500#l");
        } else if (selection == 34) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L134##b250#l\r\n#L234#500#l");
        } else if (selection == 35) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L135##b250#l\r\n#L235#500#l");
        } else if (selection == 36) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L136##b250#l\r\n#L236#500#l");
        } else if (selection == 37) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L137##b250#l\r\n#L237#500#l");
        } else if (selection == 38) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L138##b250#l\r\n#L238#500#l");
        } else if (selection == 39) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L139##b250#l\r\n#L239#500#l");
        } else if (selection == 40) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L140##b250#l\r\n#L240#500#l");
        } else if (selection == 41) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L141##b250#l\r\n#L241#500#l");
        } else if (selection == 42) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L142##b250#l\r\n#L242#500#l");
        } else if (selection == 43) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L143##b250#l\r\n#L243#500#l");
        } else if (selection == 44) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L144##b250#l\r\n#L244#500#l");
        } else if (selection == 45) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L145##b250#l\r\n#L245#500#l");
        } else if (selection == 46) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L146##b250#l\r\n#L246#500#l");
        } else if (selection == 47) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L147##b250#l\r\n#L247#500#l");
        } else if (selection == 48) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L148##b250#l\r\n#L248#500#l");
        } else if (selection == 49) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L149##b250#l\r\n#L249#500#l");
        } else if (selection == 50) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L150##b250#l\r\n#L250#500#l");
        } else if (selection == 51) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L151##b250#l\r\n#L251#500#l");
        } else if (selection == 52) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L152##b250#l\r\n#L252#500#l");
        } else if (selection == 53) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L153##b250#l\r\n#L253#500#l");
        } else if (selection == 54) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L154##b250#l\r\n#L254#500#l");
        } else if (selection == 55) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L155##b250#l\r\n#L255#500#l");
        } else if (selection == 56) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L156##b250#l\r\n#L256#500#l");
        } else if (selection == 57) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L157##b250#l\r\n#L257#500#l");
        } else if (selection == 58) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L158##b250#l\r\n#L258#500#l");
        } else if (selection == 59) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L159##b250#l\r\n#L259#500#l");
        } else if (selection == 60) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L160##b250#l\r\n#L260#500#l");
        } else if (selection == 61) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L161##b250#l\r\n#L261#500#l");
        } else if (selection == 62) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L162##b250#l\r\n#L262#500#l");
        } else if (selection == 63) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L163##b250#l\r\n#L263#500#l");
        } else if (selection == 64) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L164##b250#l\r\n#L264#500#l");
        } else if (selection == 65) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L165##b250#l\r\n#L265#500#l");
        } else if (selection == 66) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L166##b250#l\r\n#L266#500#l");
        } else if (selection == 67) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L167##b250#l\r\n#L267#500#l");
        } else if (selection == 68) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L168##b250#l\r\n#L268#500#l");
        } else if (selection == 69) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L169##b250#l\r\n#L269#500#l");
        } else if (selection == 70) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L170##b250#l\r\n#L270#500#l");
        } else if (selection == 71) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L171##b250#l\r\n#L271#500#l");
        } else if (selection == 72) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L172##b250#l\r\n#L272#500#l");
        } else if (selection == 73) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L173##b250#l\r\n#L273#500#l");
        } else if (selection == 74) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L174##b250#l\r\n#L274#500#l");
        } else if (selection == 75) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L175##b250#l\r\n#L275#500#l");
        } else if (selection == 76) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L176##b250#l\r\n#L276#500#l");
        } else if (selection == 77) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L177##b250#l\r\n#L277#500#l");
        } else if (selection == 78) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L178##b250#l\r\n#L278#500#l");
        } else if (selection == 79) {
            cm.sendSimple("How many of this ETC do you have for me?\r\n#L179##b250#l\r\n#L279#500#l");
        } else if (selection == 110) {
            if (cm.haveItem(4000029,250)) {
                cm.gainItem(4000029,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 210) {
            if (cm.haveItem(4000029,500)) {
                cm.gainItem(4000029,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 111) {
            if (cm.haveItem(4000044,250)) {
                cm.gainItem(4000044,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 211) {
            if (cm.haveItem(4000044,500)) {
                cm.gainItem(4000044,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 112) {
            if (cm.haveItem(4000177,250)) {
                cm.gainItem(4000177,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 212) {
            if (cm.haveItem(4000177,500)) {
                cm.gainItem(4000177,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 113) {
            if (cm.haveItem(4000197,250)) {
                cm.gainItem(4000197,-250);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 213) {
            if (cm.haveItem(4000197,500)) {
                cm.gainItem(4000197,-500);
                cm.gainKarma(7 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 114) {
            if (cm.haveItem(4000033,250)) {
                cm.gainItem(4000033,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 214) {
            if (cm.haveItem(4000033,500)) {
                cm.gainItem(4000033,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 115) {
            if (cm.haveItem(4000041,250)) {
                cm.gainItem(4000041,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 215) {
            if (cm.haveItem(4000041,500)) {
                cm.gainItem(4000041,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 116) {
            if (cm.haveItem(4000023,250)) {
                cm.gainItem(4000023,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 216) {
            if (cm.haveItem(4000023,500)) {
                cm.gainItem(4000023,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 117) {
            if (cm.haveItem(4000036,250)) {
                cm.gainItem(4000036,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 217) {
            if (cm.haveItem(4000036,500)) {
                cm.gainItem(4000036,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 118) {
            if (cm.haveItem(4000039,250)) {
                cm.gainItem(4000039,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 218) {
            if (cm.haveItem(4000039,500)) {
                cm.gainItem(4000039,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 119) {
            if (cm.haveItem(4000067,250)) {
                cm.gainItem(4000067,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 219) {
            if (cm.haveItem(4000067,500)) {
                cm.gainItem(4000067,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 120) {
            if (cm.haveItem(4000400,250)) {
                cm.gainItem(4000400,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 220) {
            if (cm.haveItem(4000400,500)) {
                cm.gainItem(4000400,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 121) {
            if (cm.haveItem(4000399,250)) {
                cm.gainItem(4000399,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 221) {
            if (cm.haveItem(4000399,500)) {
                cm.gainItem(4000399,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 122) {
            if (cm.haveItem(4032011,250)) {
                cm.gainItem(4032011,-250);
                cm.gainKarma(4 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 222) {
            if (cm.haveItem(4032011,500)) {
                cm.gainItem(4032011,-500);
                cm.gainKarma(9 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 123) {
            if (cm.haveItem(4032031,250)) {
                cm.gainItem(4032031,-250);
                cm.gainKarma(4 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 223) {
            if (cm.haveItem(4032031,500)) {
                cm.gainItem(4032031,-500);
                cm.gainKarma(9 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 124) {
            if (cm.haveItem(4032012,250)) {
                cm.gainItem(4032012,-250);
                cm.gainKarma(6 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 224) {
            if (cm.haveItem(4032012,500)) {
                cm.gainItem(4032012,-500);
                cm.gainKarma(13 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 125) {
            if (cm.haveItem(4032004,250)) {
                cm.gainItem(4032004,-250);
                cm.gainKarma(7 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 225) {
            if (cm.haveItem(4032004,500)) {
                cm.gainItem(4032004,-500);
                cm.gainKarma(15 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 126) {
            if (cm.haveItem(4032005,250)) {
                cm.gainItem(4032005,-250);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 226) {
            if (cm.haveItem(4032005,500)) {
                cm.gainItem(4032005,-500);
                cm.gainKarma(7 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 127) {
            if (cm.haveItem(4032008,250)) {
                cm.gainItem(4032008,-250);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 227) {
            if (cm.haveItem(4032008,500)) {
                cm.gainItem(4032008,-500);
                cm.gainKarma(7 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 128) {
            if (cm.haveItem(4032006,250)) {
                cm.gainItem(4032006,-250);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 228) {
            if (cm.haveItem(4032006,500)) {
                cm.gainItem(4032006,-500);
                cm.gainKarma(7 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 129) {
            if (cm.haveItem(4032010,250)) {
                cm.gainItem(4032010,-250);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 229) {
            if (cm.haveItem(4032010,500)) {
                cm.gainItem(4032010,-500);
                cm.gainKarma(11 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 130) {
            if (cm.haveItem(4000162,250)) {
                cm.gainItem(4000162,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 230) {
            if (cm.haveItem(4000162,500)) {
                cm.gainItem(4000162,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 131) {
            if (cm.haveItem(4000163,250)) {
                cm.gainItem(4000163,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 231) {
            if (cm.haveItem(4000163,500)) {
                cm.gainItem(4000163,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 132) {
            if (cm.haveItem(4000154,250)) {
                cm.gainItem(4000154,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 232) {
            if (cm.haveItem(4000154,500)) {
                cm.gainItem(4000154,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 133) {
            if (cm.haveItem(4000155,250)) {
                cm.gainItem(4000155,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 233) {
            if (cm.haveItem(4000155,500)) {
                cm.gainItem(4000155,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 134) {
            if (cm.haveItem(4000179,250)) {
                cm.gainItem(4000179,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 234) {
            if (cm.haveItem(4000179,500)) {
                cm.gainItem(4000179,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 135) {
            if (cm.haveItem(4000153,250)) {
                cm.gainItem(4000153,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 235) {
            if (cm.haveItem(4000153,500)) {
                cm.gainItem(4000153,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 136) {
            if (cm.haveItem(4000182,250)) {
                cm.gainItem(4000182,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 236) {
            if (cm.haveItem(4000182,500)) {
                cm.gainItem(4000182,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 137) {
            if (cm.haveItem(4000181,250)) {
                cm.gainItem(4000181,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 237) {
            if (cm.haveItem(4000181,500)) {
                cm.gainItem(4000181,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 138) {
            if (cm.haveItem(4000159,250)) {
                cm.gainItem(4000159,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 238) {
            if (cm.haveItem(4000159,500)) {
                cm.gainItem(4000159,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 139) {
            if (cm.haveItem(4000183,250)) {
                cm.gainItem(4000183,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 239) {
            if (cm.haveItem(4000183,500)) {
                cm.gainItem(4000183,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 140) {
            if (cm.haveItem(4021009,250)) {
                cm.gainItem(4021009,-250);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 240) {
            if (cm.haveItem(4021009,500)) {
                cm.gainItem(4021009,-500);
                cm.gainKarma(7 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 141) {
            if (cm.haveItem(4000172,250)) {
                cm.gainItem(4000172,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 241) {
            if (cm.haveItem(4000172,500)) {
                cm.gainItem(4000172,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 142) {
            if (cm.haveItem(4000132,250)) {
                cm.gainItem(4000132,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 242) {
            if (cm.haveItem(4000132,500)) {
                cm.gainItem(4000132,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 143) {
            if (cm.haveItem(4000173,250)) {
                cm.gainItem(4000173,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 243) {
            if (cm.haveItem(4000173,500)) {
                cm.gainItem(4000173,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 144) {
            if (cm.haveItem(4000147,250)) {
                cm.gainItem(4000147,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 244) {
            if (cm.haveItem(4000147,500)) {
                cm.gainItem(4000147,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 145) {
            if (cm.haveItem(4000169,250)) {
                cm.gainItem(4000169,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 245) {
            if (cm.haveItem(4000169,500)) {
                cm.gainItem(4000169,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 146) {
            if (cm.haveItem(4000131,250)) {
                cm.gainItem(4000131,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 246) {
            if (cm.haveItem(4000131,500)) {
                cm.gainItem(4000131,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 147) {
            if (cm.haveItem(4000105,250)) {
                cm.gainItem(4000105,-250);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 247) {
            if (cm.haveItem(4000105,500)) {
                cm.gainItem(4000105,-500);
                cm.gainKarma(7 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 148) {
            if (cm.haveItem(4000119,250)) {
                cm.gainItem(4000119,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 248) {
            if (cm.haveItem(4000119,500)) {
                cm.gainItem(4000119,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 149) {
            if (cm.haveItem(4000118,250)) {
                cm.gainItem(4000118,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 249) {
            if (cm.haveItem(4000118,500)) {
                cm.gainItem(4000118,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 150) {
            if (cm.haveItem(4000326,250)) {
                cm.gainItem(4000326,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 250) {
            if (cm.haveItem(4000326,500)) {
                cm.gainItem(4000326,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 151) {
            if (cm.haveItem(4000328,250)) {
                cm.gainItem(4000328,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 251) {
            if (cm.haveItem(4000328,500)) {
                cm.gainItem(4000328,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 152) {
            if (cm.haveItem(4000297,250)) {
                cm.gainItem(4000297,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 252) {
            if (cm.haveItem(4000297,500)) {
                cm.gainItem(4000297,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 153) {
            if (cm.haveItem(4000277,250)) {
                cm.gainItem(4000277,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 253) {
            if (cm.haveItem(4000277,500)) {
                cm.gainItem(4000277,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 154) {
            if (cm.haveItem(4000331,250)) {
                cm.gainItem(4000331,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 254) {
            if (cm.haveItem(4000331,500)) {
                cm.gainItem(4000331,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 155) {
            if (cm.haveItem(4000287,250)) {
                cm.gainItem(4000287,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 255) {
            if (cm.haveItem(4000287,500)) {
                cm.gainItem(4000287,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 156) {
            if (cm.haveItem(4000285,250)) {
                cm.gainItem(4000285,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 256) {
            if (cm.haveItem(4000285,500)) {
                cm.gainItem(4000285,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 157) {
            if (cm.haveItem(4000289,250)) {
                cm.gainItem(4000289,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 257) {
            if (cm.haveItem(4000289,500)) {
                cm.gainItem(4000289,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 158) {
            if (cm.haveItem(4000295,250)) {
                cm.gainItem(4000295,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 258) {
            if (cm.haveItem(4000295,500)) {
                cm.gainItem(4000295,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 159) {
            if (cm.haveItem(4000325,250)) {
                cm.gainItem(4000325,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 259) {
            if (cm.haveItem(4000325,500)) {
                cm.gainItem(4000325,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 160) {
            if (cm.haveItem(4000234,250)) {
                cm.gainItem(4000234,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 260) {
            if (cm.haveItem(4000234,500)) {
                cm.gainItem(4000234,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 161) {
            if (cm.haveItem(4000242,250)) {
                cm.gainItem(4000242,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 261) {
            if (cm.haveItem(4000242,500)) {
                cm.gainItem(4000242,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 162) {
            if (cm.haveItem(4000270,250)) {
                cm.gainItem(4000270,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 262) {
            if (cm.haveItem(4000270,500)) {
                cm.gainItem(4000270,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 163) {
            if (cm.haveItem(4000263,250)) {
                cm.gainItem(4000263,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 263) {
            if (cm.haveItem(4000263,500)) {
                cm.gainItem(4000263,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 164) {
            if (cm.haveItem(4000240,250)) {
                cm.gainItem(4000240,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 264) {
            if (cm.haveItem(4000240,500)) {
                cm.gainItem(4000240,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 165) {
            if (cm.haveItem(4000261,250)) {
                cm.gainItem(4000261,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 265) {
            if (cm.haveItem(4000261,500)) {
                cm.gainItem(4000261,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 166) {
            if (cm.haveItem(4000237,250)) {
                cm.gainItem(4000237,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 266) {
            if (cm.haveItem(4000237,500)) {
                cm.gainItem(4000237,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 167) {
            if (cm.haveItem(4000264,250)) {
                cm.gainItem(4000264,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 267) {
            if (cm.haveItem(4000264,500)) {
                cm.gainItem(4000264,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 168) {
            if (cm.haveItem(4000271,250)) {
                cm.gainItem(4000271,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 268) {
            if (cm.haveItem(4000271,500)) {
                cm.gainItem(4000271,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 169) {
            if (cm.haveItem(4000226,250)) {
                cm.gainItem(4000226,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 269) {
            if (cm.haveItem(4000226,500)) {
                cm.gainItem(4000226,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 170) {
            if (cm.haveItem(4000082,250)) {
                cm.gainItem(4000082,-250);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 270) {
            if (cm.haveItem(4000082,500)) {
                cm.gainItem(4000082,-500);
                cm.gainKarma(11 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 171) {
            if (cm.haveItem(4000063,250)) {
                cm.gainItem(4000063,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 271) {
            if (cm.haveItem(4000063,500)) {
                cm.gainItem(4000063,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 172) {
            if (cm.haveItem(4000074,250)) {
                cm.gainItem(4000074,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 272) {
            if (cm.haveItem(4000074,500)) {
                cm.gainItem(4000074,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 173) {
            if (cm.haveItem(4000053,250)) {
                cm.gainItem(4000053,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 273) {
            if (cm.haveItem(4000053,500)) {
                cm.gainItem(4000053,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 174) {
            if (cm.haveItem(4000056,250)) {
                cm.gainItem(4000056,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 274) {
            if (cm.haveItem(4000056,500)) {
                cm.gainItem(4000056,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 175) {
            if (cm.haveItem(4000076,250)) {
                cm.gainItem(4000076,-250);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 275) {
            if (cm.haveItem(4000076,500)) {
                cm.gainItem(4000076,-500);
                cm.gainKarma(7 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 176) {
            if (cm.haveItem(4000060,250)) {
                cm.gainItem(4000060,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 276) {
            if (cm.haveItem(4000060,500)) {
                cm.gainItem(4000060,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 177) {
            if (cm.haveItem(4000010,250)) {
                cm.gainItem(4000010,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 277) {
            if (cm.haveItem(4000010,500)) {
                cm.gainItem(4000010,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 178) {
            if (cm.haveItem(4000070,250)) {
                cm.gainItem(4000070,-250);
                cm.gainKarma(1 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 278) {
            if (cm.haveItem(4000070,500)) {
                cm.gainItem(4000070,-500);
                cm.gainKarma(3 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 179) {
            if (cm.haveItem(4000054,250)) {
                cm.gainItem(4000054,-250);
                cm.gainKarma(2 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else if (selection == 279) {
            if (cm.haveItem(4000054,500)) {
                cm.gainItem(4000054,-500);
                cm.gainKarma(5 * rate);
                cm.sendOk("Thank you for the Items. Come back if you have more!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have that many... Get lost you liar!");
                cm.dispose();
            }
        } else
            cm.dispose();
        return;
    }
}*/