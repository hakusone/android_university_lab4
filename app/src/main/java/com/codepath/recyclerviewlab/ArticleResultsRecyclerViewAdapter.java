package com.codepath.recyclerviewlab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.recyclerviewlab.models.Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleResultsRecyclerViewAdapter extends RecyclerView.Adapter<ArticleResultsRecyclerViewAdapter.ViewHolder> {
    private List<Article> articles = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.fragment_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(bookView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleResultsRecyclerViewAdapter.ViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.article_headline.setText(article.headline.main);
        holder.article_pub_date.setText(article.publishDate);
        holder.article_snippet.setText(article.snippet);
    }

    public void setNewArticles(List<Article> models) {
        articles.clear();
        articles.addAll(models);
    }

    public void addArticles(List<Article> models) {
        articles.addAll(models);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    // View lookup cache
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView article_pub_date;
        public TextView article_headline;
        public TextView article_snippet;

        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            article_pub_date = (TextView)itemView.findViewById(R.id.article_pub_date);
            article_headline = (TextView)itemView.findViewById(R.id.article_headline);
            article_snippet = (TextView)itemView.findViewById(R.id.article_snippet);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    clickListener.onItemClick(itemView, getBindingAdapterPosition());
                }
            });
        }
    }
}
