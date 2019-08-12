package com.example.blockbreaker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NicknameDialog extends AppCompatDialogFragment {
    private EditText EditTextnickname;
    private NicknameDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.nicknamedialog,null);
        builder.setView(view)
                .setTitle("Write a nickname")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickname = EditTextnickname.getText().toString();
                        listener.applyTexts(nickname);
                    }
                });
        EditTextnickname= view.findViewById(R.id.edit_nickname);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (NicknameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
                    "Must implement NicknameDialogListerner");
        }
    }

    public interface NicknameDialogListener{
        void applyTexts(String nickname);
    }
}
