package com.paypassword;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private RelativeLayout relRoot;
    private TextView tvPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PayPasswordDialog dialog=new PayPasswordDialog(MainActivity.this,R.style.mydialog);
                dialog.setDialogClick(new PayPasswordDialog.DialogClick() {
                    @Override
                    public void doConfirm(String password) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,password,Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initView() {
        relRoot = (RelativeLayout) findViewById(R.id.rel_root);
        tvPay = (TextView) findViewById(R.id.tv_pay);
    }
}
