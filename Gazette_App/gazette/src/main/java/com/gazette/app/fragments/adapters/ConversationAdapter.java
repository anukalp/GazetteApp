package com.gazette.app.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gazette.app.R;
import com.gazette.app.model.chat.Message;

import java.util.List;

/**
 * Created by Anil Gudigar on 11/24/2015.
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Message> messagesItems;


    public ConversationAdapter(Context context, List<Message> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;
        Log.i("Anil", "messagesItems :" + messagesItems.size());
    }

    public static class ViewHolderChatFromRight extends RecyclerView.ViewHolder {
        public LinearLayout mChatCard;

        public ViewHolderChatFromRight(View v) {
            super(v);
            mChatCard = (LinearLayout) v;
        }

    }

    @Override
    public int getItemViewType(int position) {
        Log.i("Anil", "ViewType :" + position);
        if (messagesItems.get(position).isSelf()) {
            return 0;
        } else {
            return 1;
        }
    }

    public static class ViewHolderChatFromLeft extends RecyclerView.ViewHolder {
        public LinearLayout mChatCard;

        public ViewHolderChatFromLeft(View v) {
            super(v);
            mChatCard = (LinearLayout) v;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("Anil", "ViewType :" + viewType);
        switch (viewType) {
            case 0:
                View rightView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_item_message_right, parent, false);
                ViewHolderChatFromRight rightViewHolder = new ViewHolderChatFromRight(rightView);
                return rightViewHolder;
            case 1:
                View leftView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_item_message_left, parent, false);
                ViewHolderChatFromLeft leftViewHolder = new ViewHolderChatFromLeft(leftView);
                return leftViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message m = messagesItems.get(position);
        TextView lblFrom = null;
        TextView txtMsg = null;
        if (messagesItems.get(position).isSelf()) {
            lblFrom = (TextView) ((ViewHolderChatFromRight) holder).mChatCard.findViewById(R.id.lblMsgFrom);
            txtMsg = (TextView) ((ViewHolderChatFromRight) holder).mChatCard.findViewById(R.id.txtMsg);
        } else {
            lblFrom = (TextView) ((ViewHolderChatFromLeft) holder).mChatCard.findViewById(R.id.lblMsgFrom);
            txtMsg = (TextView) ((ViewHolderChatFromLeft) holder).mChatCard.findViewById(R.id.txtMsg);
        }
        txtMsg.setText(m.getMessage());
        lblFrom.setText(m.getFromName());
    }

    @Override
    public int getItemCount() {
        return messagesItems.size();
    }


}
