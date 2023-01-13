package com.auro.application.home.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.databinding.FriendsBoardItemLayoutBinding;
import com.auro.application.databinding.FriendsInviteItemLayoutBinding;
import com.auro.application.home.data.model.FriendsLeaderBoardModel;
import com.auro.application.home.presentation.view.viewholder.InviteItemViewHolder;
import com.auro.application.home.presentation.view.viewholder.LeaderBoardItemViewHolder;

import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter {
    List<FriendsLeaderBoardModel> list;
    CommonCallBackListner commonCallBackListner;


    public LeaderBoardAdapter(List<FriendsLeaderBoardModel> list, CommonCallBackListner commonCallBackListner) {
        this.list = list;
        this.commonCallBackListner = commonCallBackListner;
    }


    public void setDataList(List<FriendsLeaderBoardModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE:
                FriendsBoardItemLayoutBinding friendsBoardItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.friends_board_item_layout, viewGroup, false);
                return new LeaderBoardItemViewHolder(friendsBoardItemLayoutBinding);


            case AppConstant.FriendsLeaderBoard.LEADERBOARD_INVITE_TYPE:
                FriendsInviteItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.friends_invite_item_layout, viewGroup, false);
                return new InviteItemViewHolder(binding);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = list.get(position).getViewType();

        switch (viewType) {

            case AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE:
                ((LeaderBoardItemViewHolder) holder).bindUser(list.get(position),list, position,commonCallBackListner);
                break;


            case AppConstant.FriendsLeaderBoard.LEADERBOARD_INVITE_TYPE:

                ((InviteItemViewHolder) holder).bindUser(list.get(position) ,position);
                break;


        }
    }


    @Override
    public int getItemViewType(int position) {

        switch (list.get(position).getViewType()) {

            case AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE:
                return AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE;

            case AppConstant.FriendsLeaderBoard.LEADERBOARD_INVITE_TYPE:
                return AppConstant.FriendsLeaderBoard.LEADERBOARD_INVITE_TYPE;

            case AppConstant.FriendsLeaderBoard.TRANSACTIONS_ADAPTER:
                return AppConstant.FriendsLeaderBoard.TRANSACTIONS_ADAPTER;

            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
