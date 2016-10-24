package com.example.hoangha.lab2;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hoangha.lab2.Adapter.ArticleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SearchRequest mSearchRequest;
    private ArticleApi mArticleApi;
    private ArticleAdapter mArticleAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private SearchView mSearchView;

    //List<Article> articles;

    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;
    @BindView(R.id.pdLoading)
    View pdLoading;
    @BindView(R.id.pdLoadMore)
    ProgressBar pdLoadMore;

    private interface Listener {
        void onResult(SearchResult searchResult);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpApi();
        setUpViews();
        search();
    }

    private void setUpViews() {
        mArticleAdapter = new ArticleAdapter();
        mArticleAdapter.setListener(new ArticleAdapter.Listener() {
            @Override
            public void OnLoadMore() {
                searchMore();
            }
        });

        mLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvArticle.setLayoutManager(mLayoutManager);
        rvArticle.setAdapter(mArticleAdapter);
    }

    private void setUpApi() {
        mSearchRequest = new SearchRequest();
        mArticleApi = RetrofitUtils.get().create(ArticleApi.class);
    }

    private void search() {
        mSearchRequest.resetPage();
        pdLoading.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                mArticleAdapter.setArticles(searchResult.getArticles());
            }
        });
    }
    private void searchMore() {
        mSearchRequest.nextPage();
        pdLoadMore.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                mArticleAdapter.addArticles(searchResult.getArticles());
            }
        });
    }


    private void fetchArticles(final Listener listener) {
        mArticleApi.search(mSearchRequest.toQueryMap()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                //articles = response.body().getArticles();
                listener.onResult(response.body());
                handleComplete();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("ops", t.getMessage());
            }
        });
    }

    private void handleComplete() {
        pdLoading.setVisibility(View.GONE);
        pdLoadMore.setVisibility(View.GONE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchRequest.setQuery(query);
//                mSearchRequest.setBeginDate("20160110");
//                mSearchRequest.setOrder("newest");
                search();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                Toast.makeText(this,"sort",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this,ConfigActivity.class);

                i.putExtra("data",mSearchRequest);
                startActivityForResult(i,13);

                return true;
            case R.id.action_search:
                Toast.makeText(this,"search",Toast.LENGTH_SHORT).show();
/*
                mSearchRequest.setQuery("art");
                mSearchRequest.setBeginDate("20160110");
                mSearchRequest.setOrder("oldest");
                search();*/

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 13)
            if(data.hasExtra("dataR")) {
                mSearchRequest = (SearchRequest) data.getExtras().getParcelable("dataR");
                //search();
            }
    }
}
