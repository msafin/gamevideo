package com.sharegogo.video.activity;

import com.sharegogo.video.game.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameDialogFragment extends DialogFragment{
	private int mTitleId;
	private int mContentId;
	private int mPositiveId;
	private int mNegativeId;
	private OnClickListener mListener;
	
	public GameDialogFragment(int titleId,int contentId,int positiveId,int negativeId,OnClickListener listener)
	{
		mTitleId = titleId;
		mContentId = contentId;
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
        
        icon.setImageResource(R.drawable.ic_help);
        title.setText(mTitleId);
        content.setText(mContentId);
        
        builder.setView(view)
               .setPositiveButton(mPositiveId, mListener)
               .setNegativeButton(mNegativeId,mListener);
        
        return builder.create();
	}
}
