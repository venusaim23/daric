package com.alanai.daric.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alanai.daric.Models.Diary;
import com.alanai.daric.R;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private AdapterInterface listener;

    private Context context;
    private List<Diary> diaries;

    public interface AdapterInterface {
        void updateItems();
    }

    public DiaryAdapter(Context context, List<Diary> diaries) {
        this.context = context;
        this.diaries = diaries;

        if (context instanceof AdapterInterface)
            listener = (AdapterInterface) context;
        else
            throw new RuntimeException(context.toString() + " must implement AdapterInterface");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diary diary = diaries.get(position);
        holder.titleTV.setText(diary.getTitle());
        holder.timeTV.setText(diary.getDateTime());
        if (diary.isBookmark())
            holder.bookmarkImg.setImageResource(R.drawable.ic_round_bookmark);
        else
            holder.bookmarkImg.setImageResource(R.drawable.ic_round_bookmark_border);

        holder.contentTV.setText(diary.getContent());

        String tag = diary.getTag();
        if (tag == null || tag.isEmpty()) {
            holder.tagCard.setVisibility(View.GONE);
        } else {
            holder.tagCard.setVisibility(View.VISIBLE);
            holder.tagTV.setText(tag);
        }
    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    public void bookmarkDiary(int position) {
//        Diary diary = diaries.get(getBindingAdapterPosition());
//        if (diary.isBookmark()) {
//            diary.setBookmark(false);
//            bookmarkImg.setImageResource(R.drawable.ic_round_bookmark_border);
//        } else {
//            diary.setBookmark(true);
//            bookmarkImg.setImageResource(R.drawable.ic_round_bookmark);
//        }
//        diaries.set(getBindingAdapterPosition(), diary);
////                    notifyDataSetChanged();
//        notifyItemChanged(getBindingAdapterPosition());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTV;
        public ImageView bookmarkImg;
        public TextView timeTV;
        public TextView contentTV;
        public CardView tagCard;
        public TextView tagTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.title_note_tv);
            timeTV = itemView.findViewById(R.id.time_note_tv);
            bookmarkImg = itemView.findViewById(R.id.bookmark_img);
            contentTV = itemView.findViewById(R.id.content_tv);
            tagCard = itemView.findViewById(R.id.tag_card_note);
            tagTV = itemView.findViewById(R.id.tag_tv_note_item);

            bookmarkImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateBookmark();
                }
            });
        }

        private void updateBookmark() {
            Diary diary = diaries.get(getBindingAdapterPosition());
            if (diary.isBookmark()) {
                diary.setBookmark(false);
                bookmarkImg.setImageResource(R.drawable.ic_round_bookmark_border);
            } else {
                diary.setBookmark(true);
                bookmarkImg.setImageResource(R.drawable.ic_round_bookmark);
            }
            diaries.set(getBindingAdapterPosition(), diary);
//                    notifyDataSetChanged();
            listener.updateItems();
            notifyItemChanged(getBindingAdapterPosition());
        }
    }
}
