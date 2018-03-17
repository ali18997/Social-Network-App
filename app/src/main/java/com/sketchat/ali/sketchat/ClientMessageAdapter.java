package com.sketchat.ali.sketchat;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ALI on 12/22/2017.
 */

public class ClientMessageAdapter  extends RecyclerView.Adapter<ClientMessageAdapter.MessageViewHolder>{

    private List<Messages> mMessageList;

    ViewGroup previous;

    public ClientMessageAdapter (List <Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        previous = parent;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);

        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView messageText2;
        public TextView messageName;
        public CircleImageView profileImage;


        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            messageText2 = (TextView) view.findViewById(R.id.message_text_layout2);
            messageName = (TextView) view.findViewById(R.id.message_user_name);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);

        }
    }



    @Override
    public void onBindViewHolder (MessageViewHolder viewHolder, int i) {



        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();

        if (from_user.equals("client")) {

            viewHolder.messageText2.setBackgroundResource(R.drawable.message_text_background2);
            viewHolder.messageText2.setTextColor(Color.BLACK);
            viewHolder.messageText2.setText(c.getMessage());
            viewHolder.messageText2.setVisibility(View.VISIBLE);
            viewHolder.messageText.setVisibility(View.INVISIBLE);
            viewHolder.profileImage.setVisibility(View.INVISIBLE);
            viewHolder.messageName.setVisibility(View.INVISIBLE);

        } else {

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
            viewHolder.messageText.setText(c.getMessage().substring(c.getMessage().indexOf(")") + 1));
            viewHolder.messageText.setVisibility(View.VISIBLE);
            viewHolder.messageText2.setVisibility(View.INVISIBLE);
            viewHolder.profileImage.setVisibility(View.VISIBLE);
            viewHolder.messageName.setVisibility(View.VISIBLE);
            viewHolder.messageName.setText(c.getMessage().substring(c.getMessage().indexOf("(") + 1, c.getMessage().indexOf(")")));


        }




    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

}
