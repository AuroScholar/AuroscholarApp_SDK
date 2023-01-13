package com.auro.application.home.presentation.view.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.StartQuizItemLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;

import java.util.List;

public class ChapterSelectAdapter extends RecyclerView.Adapter<ChapterSelectAdapter.SubjectHolder> {

    List<QuizResModel> list;
    CommonCallBackListner commonCallBackListner;
    Context mcontext;

    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private final int checkedPosition = 0;

    public ChapterSelectAdapter(Context mcontext, List<QuizResModel> list, CommonCallBackListner commonCallBackListner) {
        this.list = list;
        this.commonCallBackListner = commonCallBackListner;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ChapterSelectAdapter.SubjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        StartQuizItemLayoutBinding languageItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.start_quiz_item_layout, viewGroup, false);
        return new ChapterSelectAdapter.SubjectHolder(languageItemLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ChapterSelectAdapter.SubjectHolder holder, int position) {
        holder.bindUser(list.get(position), position, commonCallBackListner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubjectHolder extends RecyclerView.ViewHolder {
        StartQuizItemLayoutBinding binding;

        public SubjectHolder(@NonNull StartQuizItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindUser(QuizResModel model, int position, CommonCallBackListner commonCallBackListner) {

            try {
                if (!TextUtil.isEmpty(model.getName())) {
                    binding.RpChapter.setText(model.getName());
                } else {
                    binding.RpChapter.setText("Quiz " + (position + 1));
                }
                int score = 0;
                if (model.getAttempt() > 0) {
                    if (!TextUtil.isEmpty(model.getScoreallpoints())) {
                        String[] scoreArray = model.getScoreallpoints().split(",");
                        if (scoreArray.length > 0) {
                            score = ConversionUtil.INSTANCE.convertStringToInteger(scoreArray[scoreArray.length - 1]);
                        }
                    }
                }
                binding.RpScore.setText("Score: " + score);
                String str = String.format("%02d", (position + 1));
                binding.serial.setText(str);

                if (model.getAttempt() == 3) {
                    binding.buttonStart.setVisibility(View.GONE);
                }

                if (model.getAttempt() > 0) {
                    binding.imageView7.setImageDrawable(AuroApp.getAppContext().getDrawable(R.drawable.restart_bg));
                    if (model.getAttempt() == 1) {
                        binding.RPTextView12.setText("Retake 1");
                        binding.RPTextView12.setTextSize(TypedValue.COMPLEX_UNIT_SP, mcontext.getResources().getDimension(R.dimen._3sdp));
                    }
                    if (model.getAttempt() == 2) {
                        binding.RPTextView12.setText("Retake 2");
                        binding.RPTextView12.setTextSize(TypedValue.COMPLEX_UNIT_SP, mcontext.getResources().getDimension(R.dimen._3sdp));
                    }
                }

                enableDisbaleQuiz(binding, position);

                binding.buttonStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (commonCallBackListner != null) {
                            // list.get(position).setSubjectPos(subjectPos);
                            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.START_QUIZ_BUTON, list.get(position)));
                        }
                    }
                });


            }catch (Exception e)
            {
                AppLogger.e("ChapterSelectionAdapter-",e.getMessage());
            }
        }
    }

    public void setData(List<QuizResModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    private void enableDisbaleQuiz(StartQuizItemLayoutBinding binding, int position) {
        if (position > 0) {
            QuizResModel model = list.get(position - 1);
            if (model.getAttempt() > 0) {
                binding.buttonStart.setEnabled(true);
            } else {
                binding.buttonStart.setEnabled(false);
                binding.serial.setTextColor(AuroApp.getAppContext().getColor(R.color.light_grey));
                binding.RpScore.setTextColor(AuroApp.getAppContext().getColor(R.color.light_grey));
                binding.RpChapter.setTextColor(AuroApp.getAppContext().getColor(R.color.ash_grey));
                binding.imageView7.setImageDrawable(AuroApp.getAppContext().getDrawable(R.drawable.quiz_lock_image));
                binding.RPTextView12.setText("");
            }
        }
    }

}