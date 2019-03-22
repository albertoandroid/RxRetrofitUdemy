package com.androiddesdecero.rxretrofitudemy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.androiddesdecero.rxretrofitudemy.adapter.RepositoryAdapter;
import com.androiddesdecero.rxretrofitudemy.api.WebService;
import com.androiddesdecero.rxretrofitudemy.model.GitHubRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hu.akarnokd.rxjava2.math.MathObservable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RxRetrofitActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RepositoryAdapter adapter;
    private List<GitHubRepo> gitHubRepos;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retrofit);
        setUpView();
        //sinRx();
        //conRx();
        //conRxLambda();
        //conRxOrdenarInverso();
        //conRxFiltrarLenguaje();
        //conRxFiltrarLenguajeLambda();
        //conRxFiltrarLenguajeLambdaMasOperadores();
        //conRxOrdenarPorEstrellas();
        conRxAverageEstrellas();
    }

    private void conRxAverageEstrellas(){
        Observable<Integer> observable = WebService
                .getInstance()
                .createService()
                .getReposForUserRx("JakeWharton")
                .toObservable()
                .flatMapIterable(e->e)
                .map(e->e.getStargazers_count());

        MathObservable.averageDouble(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e->Log.d("TAG1", "average: " + e)
                );

        MathObservable.max(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e->Log.d("TAG1", "max: " + e)
                );

        MathObservable.min(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e->Log.d("TAG1", "min: " + e)
                );
    }

    private void conRxOrdenarPorEstrellas(){
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("JakeWharton")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .flatMapIterable(e->e)
                        .toSortedList((o1,o2)->o1.getStargazers_count() - o2.getStargazers_count())
                        .subscribe(
                                e->adapter.setData(e),
                                error->Log.d("TAG1", "error: " + error.getMessage())
                        )
        );
    }

    private void conRxFiltrarLenguajeLambdaMasOperadores(){
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("JakeWharton")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .flatMapIterable(e->e)
                        .filter(e->e.getLanguage()!=null && e.getLanguage().equals("Java"))
                        //.take(3)
                        //.first(new GitHubRepo())
                        //.elementAt(3)
                        .skip(3)
                        .subscribe(
                                e->{
                                    gitHubRepos.add(e);
                                    adapter.setData(gitHubRepos);
                                },
                                error->Log.d("TAG1", "error: " + error.getMessage())
                        )
        );
    }

    private void conRxFiltrarLenguajeLambda(){
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("JakeWharton")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .flatMapIterable(e->e)
                        .filter(e->e.getLanguage()!=null && e.getLanguage().equals("Java"))
                        .subscribe(
                                e->{
                                    gitHubRepos.add(e);
                                    adapter.setData(gitHubRepos);
                                },
                                error->Log.d("TAG1", "error: " + error.getMessage())
                        )
        );
    }

    private void conRxFiltrarLenguaje(){
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("JakeWharton")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .flatMap(new Function<List<GitHubRepo>, ObservableSource<GitHubRepo>>() {
                    @Override
                    public ObservableSource<GitHubRepo> apply(List<GitHubRepo> gitHubRepos) throws Exception {
                        return Observable.fromIterable(gitHubRepos);
                    }
                })
                .filter(new Predicate<GitHubRepo>() {
                    @Override
                    public boolean test(GitHubRepo gitHubRepo) throws Exception {
                        if(gitHubRepo.getLanguage()!=null && gitHubRepo.getLanguage().equals("Kotlin")){
                            return true;
                        }
                        return false;
                    }
                })
                .subscribe(
                        e->{
                            gitHubRepos.add(e);
                            adapter.setData(gitHubRepos);
                        },
                        error->Log.d("TAG1", "error: " + error.getMessage())
                )
        );

    }

    private void conRxOrdenarInverso(){
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("JakeWharton")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<GitHubRepo>, List<GitHubRepo>>() {
                    @Override
                    public List<GitHubRepo> apply(List<GitHubRepo> gitHubRepos) {
                        Collections.sort(gitHubRepos, new Comparator<GitHubRepo>() {
                            @Override
                            public int compare(GitHubRepo o1, GitHubRepo o2) {
                                return o2.getName().compareTo(o1.getName());
                            }
                        });
                        return gitHubRepos;
                    }
                })
                .subscribe(
                        e->adapter.setData(e),
                        error->Log.d("TAG1", "error: " + error.getMessage())
                )
        );

    }

    private void conRx(){
        compositeDisposable.add(
            WebService
                .getInstance()
                .createService()
                .getReposForUserRx("JakeWharton")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<GitHubRepo>>() {
                            @Override
                            public void accept(List<GitHubRepo> gitHubRepos) throws Exception {
                                adapter.setData(gitHubRepos);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("TAG1", "Error " + throwable.getMessage());
                            }
                        }
                )
        );
    }

    private void conRxLambda(){
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("JakeWharton")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                e->adapter.setData(e),
                                error->Log.d("TAG1", "Error " + error.getMessage())
                        )
        );
    }

    private void sinRx(){
        Call<List<GitHubRepo>> call = WebService
                .getInstance()
                .createService()
                .getReposForUser("JakeWharton");

        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                gitHubRepos = response.body();
                adapter.setData(gitHubRepos);
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                Log.d("TAG1", "Error " + t.getMessage());
            }
        });
    }

    private void setUpView(){
        compositeDisposable = new CompositeDisposable();
        gitHubRepos = new ArrayList<>();
        adapter = new RepositoryAdapter(gitHubRepos);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
