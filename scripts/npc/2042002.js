/* * * * * * * * * * * * * * * \
 *         Q&A Event NPC        *
 *  By Hugo of MadStory/VoidMS  *
 *      gugubro1@gmail.com      *
 *         madstory.org         *
 *          voidms.com          *
\ * * * * * * * * * * * * * * */

var status = 0;
var option = 0;
var item = 4002000;
var question;
var answer;
var id;
var inventory;
var prize;
var quantity = 1;
var send;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    var p = cm.getPlayer();
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
            var txt = "#eHello #h #! I am the Q&A npc. Here you can submit or answer questions. What would you like to do?\r\n\r\n#b#L0#Answer a question\r\n#L1#Submit a new question\r\n#L2#Read more information";
            if (cm.getP().isGM()) {
                txt += "\r\n\r\n#r#L3#DELETE A QUESTION!#k";
            }
            cm.sendSimple(txt);
        } else if (status == 1) {
            if (selection == 0) {
                cm.sendSimple("#ePick a question to view its information and answer it.\r\n\r\n" + p.listQuestions() + "\r\n\r\n#L13337##bSubmit a new question");
            } else if (selection == 1) {
                if (cm.itemQuantity(item) > 0) {
                    cm.sendGetText("#ePlease enter the #rquestion#k that you would like to submit.");
                    option = 1;
                } else {
                    cm.sendOk("#eYou don't have a #i" + item + "#.");
                    cm.dispose();
                }
            } else if (selection == 2) {
                cm.sendOk("#e-It costs one #i " + item + "# to submit a new question.\r\n\r\n-It costs 100,000,000 to answer a question.\r\n\r\n-You can not ask personal question i.e. a question that someone who doesn't know you wouldn't know the answer to (your items will be deleted if so)\r\n\r\n-You can pick any item as prize.\r\n\r\n-If you pick an item with stats, the stats will be wiped off. This will be updated in the near future.\r\n\r\n-If you answer a question correctly, it will tell the whole server.\r\n\r\n-If you submit a new question, it will also tell the whole server.\r\n\r\n-You can't answer your own questions.");
                cm.dispose();
            } else if (selection == 3) {
                cm.sendSimple("#ePick a question to #rdelete#k:\r\n\r\n#b" + p.listQuestions());
                option = 2;
            }
        } else if (status == 2) {
            if (option == 0) {
                if (selection == 13337) {
                    status = 0;
                    action(1, 0, 1);
                } else {
                    id = selection;
                    cm.sendGetText(p.getQuestion(selection) + "\r\n\r\nAnswer the question:\r\n#rThis will cost 100,000,000 mesos");
                }
            } else if (option == 1) {
                question = cm.getText();
                if (cm.getText().length > 255) {
                    cm.sendOk("#eQuestion is too long. Make sure to keep it under 255 characters. Characters #r" + question.length + "#k.");
                    status = -1;
                } else {
                    cm.sendGetText("#eNow enter the #ranswer to the question");
                }
            } else if (option == 2) {
                id = selection;
                cm.sendYesNo("#eAre you sure you want to delete this question?\r\n\r\n"  + p.getQuestion(selection));
            } else {
                cm.dispose();
                return;
            }
        } else if (status == 3) {
            if (option == 0) {
                if (p.getMeso() >= 100000000) {
                    if (p.checkAnswer(id, cm.getText()) == true) {
                        cm.sendOk("#e#gCongratulations #h #, you won the prize!");
                    } else {
                        cm.sendOk("#e#rToo bad. Wrong answer.");
                        cm.gainMeso(-100000000);
                    }
                    cm.dispose();
                } else {
                    cm.sendOk("#e#rYou don't have 100,000,000 mesos");
                }
            } else if (option == 1) {
                answer = cm.getText();
                if (cm.getText().length > 45) {
                    cm.sendOk("#eAnswer is too long. Make sure to keep it under 45 characters. Characters #r" + answer.length + "#k.");
                    status = -1;
                } else {
                    cm.sendSimple("#eNow, to choose a prize. First pick an #rinventory#k:\r\n\r\n#b#L0#Equip#l\r\n#L1#Use#l\r\n#L2#Set-up#l\r\n#L3#ETC#l\r\n#L4#Cash");
                }
            } else if (option == 2) {
                cm.getP().setAnswered(id);
                cm.sendOk("#r#eQuestion Deleted!");
                cm.dispose();
                return;
            } else {
                cm.dispose();
                return;
            }
        } else if (status == 4) {
            inventory = selection;
            send = "#eNow choose the #ritem#k you'd like to give away:\r\n";
            if (selection == 0) {
                cm.sendSimple(send+cm.EquipList(p.getClient()));
            } else if (selection == 1) {
                cm.sendSimple(send+cm.UseList(p.getClient()));
            } else if (selection == 2) {
                cm.sendSimple(send+cm.SetupList(p.getClient()));
            } else if (selection == 3) {
                cm.sendSimple(send+cm.ETCList(p.getClient()));
            } else if (selection == 4) {
                cm.sendSimple(send+cm.CashList(p.getClient()));
            }
        } else if (status == 5) {
            if (inventory == 0) {
                prize = cm.getEquipId(selection);
                status = 5;
                action(1, 0, 1);
            } else {
                send = "#eNow choose the #rquantity#k of the item you want to give away#b";
                if (inventory == 1) {
                    prize = cm.getUseId(selection);
                    for (i = 1; i <= p.getUseQuantity(selection); i++) {
                        send += "\r\n#L" + i + "#" + i + "#l";
                    }
                } else if (inventory == 2) {
                    prize = cm.getSetupId(selection);
                    for (i = 1; i <= p.getSetupQuantity(selection); i++) {
                        send += "\r\n#L" + i + "#" + i + "#l";
                    }
                } else if (inventory == 3) {
                    prize = cm.getETCId(selection);
                    for (i = 1; i <= p.getEtcQuantity(selection); i++) {
                        send += "\r\n#L" + i + "#" + i + "#l";
                    }
                } else if (inventory == 4) {
                    prize = cm.getCashId(selection);
                    for (i = 1; i <= p.getCashQuantity(selection); i++) {
                        send += "\r\n#L" + i + "#" + i + "#l";
                    }
                }
                cm.sendSimple(send);
            }
        } else if (status == 6) {
            quantity = selection;
            cm.sendYesNo("#eAre you sure you want to register this question?\r\n\r\n#rInformation#k:\r\nQuestion: #b" + question + "#k\r\nAnswer: #b" + answer + "#k\r\nprize: #i" + prize + "#\r\nQuantity: #b" + selection + "\r\n\r\n#rThis will cost one #i " + item + "##k");
        } else if (status == 7) {
            if (cm.itemQuantity(item) > 0) {
                cm.loseItem(item, 1);
                cm.loseItem(prize, quantity);
                p.addQuestion(p.getName(), question, answer, prize, quantity);
                p.dropMessage(p.getName() + ":" + question + ":" + answer + ":" + prize + ":" + quantity);
                cm.sendOk("#e#gQuestion successfully added!");
            } else {
                cm.sendOk("#eYou don't have one #i" + item + "#");
            }
            cm.dispose();
        }
    }
}
