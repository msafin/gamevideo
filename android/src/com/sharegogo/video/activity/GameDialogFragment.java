package com.sharegogo.video.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharegogo.video.game.R;

public class GameDialogFragment extends DialogFragment{
	private int mIcon;
	private int mTitleId;
	private String mContent;
	private int mPositiveId;
	private int mNegativeId;
	private OnClickListener mListener;
	
	public GameDialogFragment(int icon,int titleId,String content,int positiveId,int negativeId,OnClickListener listener)
	{
		mIcon = icon;
		mTitleId = titleId;
		mContent = content;
		mPositiveId = positiveId;
		mNegativeId = negativeId;
		mListener = listener;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_fragment, null);
        ImageView icon = (ImageView)view.findViewById(android.R.id.icon);
        TextView title = (TextView)view.findViewById(android.R.id.title);
        TextView content = (TextView)view.findViewById(R.id.dialog_content);
        
        icon.setImageResource(mIcon);
        title.setText(mTitleId);
        if(mContent != null)
        {
        	content.setText(mContent);
        }
        
        builder.setView(view)
               .setPositiveButton(mPositiveId, mListener)
               .setNegativeButton(mNegativeId,mListener);
        
        return builder.create();
	}
}
