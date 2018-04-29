package com.example.asus.mydlnaapplicationone;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.mydlnaapplicationone.SSDP.SsdpConstants;

/**
 * Created by asus on 2018/4/29.
 */

public class IdentityVerificationDialog extends AlertDialog implements View.OnClickListener {

    EditText edittextPassword;
    Button buttonCancel,buttonConnect;
    Context context;
    OnConnectButtonListener connectButtonListener;

    public  IdentityVerificationDialog(Context context,OnConnectButtonListener connectButtonListener)
    {
        super(context);
        this.context = context;
        this.connectButtonListener = connectButtonListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_identityverification);
        edittextPassword = findViewById(R.id.edittext_identityverification_password);
        //pompt keybord.
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        this.setCancelable(false);
        buttonCancel = findViewById(R.id.button_identityverification_cancel);
        buttonConnect=findViewById(R.id.button_identityverification_connect);
        buttonCancel.setOnClickListener(this);
        buttonConnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_identityverification_cancel:
                connectButtonListener.getPassword(SsdpConstants.IDENTIFYVERIFICATION_CANCEL);
                break;
            case R.id.button_identityverification_connect:
                connectButtonListener.getPassword(edittextPassword.getText().toString());
                break;
            default:
                break;
        }
        this.dismiss();

    }

    public interface OnConnectButtonListener{
        void getPassword(String password);
    }

}
