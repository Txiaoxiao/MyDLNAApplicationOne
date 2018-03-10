package com.example.asus.mydlnaapplicationone.SSDP;

/**
 * Created by asus on 2018/3/10.
 */

public class SsdpSearchMessage {


    static final String HOST = "Host: " + SsdpConstants.ADDRESS + ":" + SsdpConstants.SEND_PORT;

    static final String MAN = "Man: \"ssdp:discover\"";

    static final String NEWLINE = "\r\n"

            ;

    int mMX = 3; /* seconds to delay response */

    String mST; /* Search target */

    public SsdpSearchMessage(String searchTarget) {

        mST = searchTarget;

    }

    public int getmMX() {

        return mMX;

    }

    public void setmMX(int mMX) {

        this.mMX = mMX;

    }

    public String getmST() {

        return mST;

    }

    public void setmST(String mST) {

        this.mST = mST;

    }

    @Override

    public String toString() {

        StringBuilder content = new StringBuilder();

        content.append(SsdpConstants.SL_MSEARCH).append(NEWLINE);

        content.append(MAN).append(NEWLINE);

        content.append("Mx: " + mMX).append(NEWLINE);

        content.append(HOST).append(NEWLINE);

        content.append(mST).append(NEWLINE);

        content.append(NEWLINE);

        return content.toString();

    }


}
