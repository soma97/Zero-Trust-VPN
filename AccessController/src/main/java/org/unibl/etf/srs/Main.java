package org.unibl.etf.srs;

import org.unibl.etf.srs.auth.UserAuthManager;

public class Main {

    public static void main(String[] args) {
        UserAuthManager.getInstance().startAccessStateChecker();
    }
}
