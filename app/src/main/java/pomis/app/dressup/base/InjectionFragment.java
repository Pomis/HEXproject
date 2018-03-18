package pomis.app.dressup.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pomis.app.dressup.App;
import pomis.app.dressup.data.Repository;


public class InjectionFragment extends Fragment {

//    protected Realm realm;
    protected Repository api;
//    protected SettingsPrefs prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        realm = getRealmInstance(getContext());
//        prefs = getPrefsInstance(getContext());
        api = App.getApi();
    }

    protected <T> Observable<T> async(Observable<List<T>> observable) {
        return observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::fromIterable);
    }

    protected <T> Single<T> async(Single<T> single) {
        return single
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected Completable async(Completable completable) {
        return completable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

}