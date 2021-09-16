package com.codepath.recyclerviewlab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.recyclerviewlab.models.Article;
import com.codepath.recyclerviewlab.networking.CallbackResponse;
import com.codepath.recyclerviewlab.networking.NYTimesApiClient;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class ArticleResultFragment extends Fragment {

    private NYTimesApiClient client = new NYTimesApiClient();
    private ArticleResultsRecyclerViewAdapter adapter;
    private ContentLoadingProgressBar progressSpinner;
    private RecyclerView recyclerView;
    Context context;
    private String savedQuery;

    private EndlessRecyclerViewScrollListener scrollListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleResultFragment() {

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        SearchView item = (SearchView) menu.findItem(R.id.action_search).getActionView();
        item.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadNewArticlesByQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_result_list, container, false);

        context = getContext();

        recyclerView = view.findViewById(R.id.list);
        adapter = new ArticleResultsRecyclerViewAdapter();
        progressSpinner = view.findViewById(R.id.progress);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadArticlesByPage(page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadNewArticlesByQuery(String query) {
        Log.d("ArticleResultFragment", "loading articles for query " + query);
        Toast.makeText(getContext(), "Loading articles for \'" + query + "\'", Toast.LENGTH_SHORT).show();

        savedQuery = query;
        progressSpinner.setVisibility(ProgressBar.VISIBLE);

        client.getArticlesByQuery(new CallbackResponse<List<Article>>() {
            @Override
            public void onSuccess(List<Article> models) {
                Log.d("ArticleResultFragment", "Successfully loaded articles");
                ArticleResultsRecyclerViewAdapter adapter = (ArticleResultsRecyclerViewAdapter) recyclerView.getAdapter();
                adapter.setNewArticles(models);
                adapter.notifyDataSetChanged();
                progressSpinner.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ArticleResultFragment", "Failure loading articles " + error.getMessage());
            }
        }, query);
    }

    private void loadArticlesByPage(final int page) {
        Log.d("ArticleResultFragment", "loading articles for query " + savedQuery);
        Toast.makeText(getContext(), "Loading articles for \'" + savedQuery + "\'", Toast.LENGTH_SHORT).show();

        client.getArticlesByQuery(new CallbackResponse<List<Article>>() {
            @Override
            public void onSuccess(List<Article> models) {
                Log.d("ArticleResultFragment", "Successfully loaded articles");
                ArticleResultsRecyclerViewAdapter adapter = (ArticleResultsRecyclerViewAdapter) recyclerView.getAdapter();
                adapter.addArticles(models);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ArticleResultFragment", "Failure loading articles " + error.getMessage());
            }
        }, savedQuery);
    }
}
