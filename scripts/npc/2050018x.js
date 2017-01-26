/*

//            cm.getPlayer().ban("Declined ToS", false);   // ProNess<3
        cm.dispose();
        return;
    }
    if (status == 1)
        cm.sendAcceptDecline("You cannot enter the server until you accept the rules and regulations. Make sure to read and accept the rules, if you decline the Terms Of Service, you will be banned.\r\n\r\n #rPlease accept the Rules and Regulations of #bLaststory#r#r \r\n\r\n\ #b1.#r Any user must respect all staff members; threatening, harassing, cursing, or any verbal insult towards staff members is a top offence. \r\n\r\n\#b2.#r Any user who was caught with any unauthorized 3rd party programs that directly/indirectly interfere with the game play of the server which allows the user to gain an advantage in anyway will result in a ban. \r\n\r\n\#b3. #rLastStory#r carries no responsibility of your account's security, if you give out your password; you are putting your account in jeopardy of being stolen. \r\n\r\n\#b4. #rYou obey the game rules, found on our website at the rules page; caught breaking any rule may result in any sort of penalty. \r\n\r\n\#b5. #rIn any case of any user's access being removed from the server; staff members have the right to restore or not to restore anyone's account/data of their free will. \r\n\r\n\#b6. #rYou are responsible for rollbacks. YOU are responsible                           to use @save.                                                                                                                                                               #bChoose Wisely#k.");
    else if (status == 2)
        cm.sendYesNo("Thank you for accepting. Would You Like Max Skills ?");
    else if (status == 3) {
        if (max)
            cm.getChar().maxAllSkills();
        cm.warp(100000000);
        cm.gainMeso(100000000);
        cm.getPlayer().modifyCSPoints(1, 25000);
        cm.sendOk("Good luck! 25,000 Nx has been added to your account!");
        cm.dispose();
    }
}*/