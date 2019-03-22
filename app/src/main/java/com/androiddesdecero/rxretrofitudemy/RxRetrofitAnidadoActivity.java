package com.androiddesdecero.rxretrofitudemy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androiddesdecero.rxretrofitudemy.R;
import com.androiddesdecero.rxretrofitudemy.adapter.ContributorAdapter;
import com.androiddesdecero.rxretrofitudemy.api.WebService;
import com.androiddesdecero.rxretrofitudemy.model.Contributor;
import com.androiddesdecero.rxretrofitudemy.model.GitHubRepo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxRetrofitAnidadoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable;
    private List<Contributor> contributors;
    private ContributorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retrofit_anidado);

        setUpView();
        //peticionesAnidadasServidorRx();
        //peticionesAnidadasServidorRxConLambda();
        peticionesAnidadasServidorRxConLambdaMejoras();

    }

    private void peticionesAnidadasServidorRxConLambdaMejoras(){
        compositeDisposable.add(WebService
                .getInstance()
                .createService()
                .getReposForUserRx("JakeWharton")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .flatMapIterable(e->e)
                .flatMap(e->WebService
                        .getInstance()
                        .createService()
                        .getReposContriForUserRx("JakeWharton", e.getName())
                        .subscribeOn(Schedulers.io())
                )
                .flatMapIterable(e->e)
                .filter(e->e.getContributions()>=300)
                .distinct(e->e.getLogin())
                .sorted((a,b)->b.getContributions() - a.getContributions())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e->{
                            contributors.add(e);
                            adapter.setData(contributors);
                        },
                        error->Log.d("TAG1", error.getMessage())
                )
        );
    }

    private void peticionesAnidadasServidorRxConLambda(){
        compositeDisposable.add(WebService
                .getInstance()
                .createService()
                .getReposForUserRx("JakeWharton")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .flatMapIterable(e->e)
                .flatMap(e->WebService
                                .getInstance()
                                .createService()
                                .getReposContriForUserRx("JakeWharton", e.getName())
                                .subscribeOn(Schedulers.io())
                )
                .flatMapIterable(e->e)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e->{
                            contributors.add(e);
                            adapter.setData(contributors);
                        },
                        error->Log.d("TAG1", error.getMessage())
                )
        );
    }

    private void setUpView(){
        compositeDisposable = new CompositeDisposable();
        contributors = new ArrayList<>();
        adapter = new ContributorAdapter(contributors);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);
        recyclerView.setAdapter(adapter);
    }

    private void peticionesAnidadasServidorRx(){
        compositeDisposable.add(WebService
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
                .flatMap(new Function<GitHubRepo, ObservableSource<List<Contributor>>>() {
                    @Override
                    public ObservableSource<List<Contributor>> apply(GitHubRepo gitHubRepo) throws Exception {
                        return WebService
                                .getInstance()
                                .createService()
                                .getReposContriForUserRx("JakeWharton", gitHubRepo.getName())
                                .subscribeOn(Schedulers.io());
                    }
                })
                .flatMap(new Function<List<Contributor>, ObservableSource<Contributor>>() {
                    @Override
                    public ObservableSource<Contributor> apply(List<Contributor> contributors) throws Exception {
                        return Observable.fromIterable(contributors);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e->{
                            contributors.add(e);
                            adapter.setData(contributors);
                        }
                )
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
