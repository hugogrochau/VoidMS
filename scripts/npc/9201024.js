/*var wui = 0;
var chairs = [3010036,3010127,3010126,3010128,3010096,3010095,3010110,3010108,3010097,3010069,3010118,3010116
,3010092,3010080,3010111,3010106,3010073,3010070,3010099,3010099,3010085,3010071,3010068
,3010064,3010062,3010060,3010058,3010057,3010047,3010046,3010045,3010055,3010049,3010021
,3012005,3010028,3010017,3010016,3010072, 3010086,3010088,3017950,3010094,3012006,3017003,3010091,3013000,3017002,3017001,3011518,3010116
,3010078,3010079,3012008,3010112,3013006,3013030,3011006,3011004,3011003,3011002,3011001,3010077,3010093,3010074
,3010075,3012011,3012010,3010137,3010152,3010120,3010117,3010115,3010109,3010107,3010101,3010034,3010035,3010038,3010037];
var test = "";

function start() {
    for (var i = 0; i < chairs.length; i++) {
        test += "\r\n#L" + i + "##v" + chairs[i] + "# for 25 #v4031137#";
    }
    test += "\r\n\r\n#r#L1337#Buy the first 100 chairs for 1500 #v4031137##l #e\r\n\r\n(Make sure you have 100 spots in your set-up inventory)"

    if (cm.isSetupFull()) {
        cm.sendOk("Make some room before you lose your items.");
    } else {
        cm.sendSimple("Hello #e#h #.#n  I'm the VoidMS Chair Exchanger! You can exchange Box' of Presents for chairs here! You can get these presents from Big Puff Daddy in FM1.\r Which chair would you want?\r"+test);
    }
}

function action(mode, type, selection) {
    if (selection == 1337) {        
        if (cm.itemQuantity(4031137) > 1499) {
            for (var i = 0; i  < 102; i++){
                cm.gainItem(chairs[i], 1);
            } 
            cm.loseItem(4031137, 1500);
        } else {
            var need = 1000-cm.itemQuantity(4031137);
            cm.sendOk("You do not have enough #v4031137# to buy this chair. You need #e"+need+"#n more.");            
        }
    } else {
        if (chairs[selection] > 0){
            if(cm.itemQuantity(4031137) >= 25){
                cm.gainItem(chairs[selection],1);
                cm.loseItem(4031137, 25);                
            } else {
                var have = cm.itemQuantity(4031137);
                var need = 25-have;
                cm.sendOk("You do not have enough #v4031137# to buy this chair. You need #e"+need+"#n more.");                
            }
        }
    }
    cm.dispose();
}*/
function start() {
	cm.sendOk("#eRemoved.");
	cm.dispose();
}