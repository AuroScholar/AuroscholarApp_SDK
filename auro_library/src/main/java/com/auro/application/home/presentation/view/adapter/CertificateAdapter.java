package com.auro.application.home.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FragmentCertificateBinding;
import com.auro.application.databinding.FragmentCertificateItemBinding;

import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.response.APIcertificate;
import com.auro.application.home.data.model.response.CertificateResModel;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;


public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.ViewHolder> {

    List<APIcertificate> mValues;
    Context mContext;
    FragmentCertificateItemBinding binding;
    CommonCallBackListner listner;

    public CertificateAdapter(Context context, List<APIcertificate> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner = listner;
    }

    public void updateList(ArrayList<APIcertificate> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        FragmentCertificateItemBinding binding;

        public ViewHolder(FragmentCertificateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

//        public void setData(APIcertificate resModel, int position) {
//            if (resModel.isSelect()) {
//                binding.selectImg.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_check));
//            } else {
//                binding.selectImg.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_uncheck));
//            }
//            ImageUtil.loadNormalImage(binding.certificateImg, resModel.getCertificateImage());
//
//        }

    }

    @Override
    public CertificateAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.fragment_certificate_item, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {
        // Vholder.setData(mValues.get(position), position);
//        Vholder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listner != null) {
//                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.ITEM_CLICK, mValues.get(position)));
//                }
//            }
//        });
        binding.sharelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Certificate shareable link "+mValues.get(position).getDownloalink());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                mContext.startActivity(shareIntent);
//               if (listner != null) {
//                   listner.commonEventListner(AppUtil.getCommonClickModel(position, Status.DOCUMENT_CLICK, mValues.get(position)));
//               }
                notifyDataSetChanged();
            }
        });
//        Vholder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                APIcertificate icertificate = mValues.get(position);
//              //  icertificate.setSelect(!icertificate.isSelect());
//                if (listner != null) {
//                    listner.commonEventListner(AppUtil.getCommonClickModel(position, Status.ITEM_LONG_CLICK, icertificate));
//                }
//                notifyDataSetChanged();
//                return false;
//            }
//        });
        binding.viewlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseintent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mValues.get(position).getCertificateviewlink()));
                mContext.startActivity(browseintent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



}
