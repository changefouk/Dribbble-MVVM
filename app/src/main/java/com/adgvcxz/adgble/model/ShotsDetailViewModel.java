package com.adgvcxz.adgble.model;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;

import com.adgvcxz.adgble.R;
import com.adgvcxz.adgble.adapter.TopMarginSelector;
import com.adgvcxz.adgble.binding.ItemView;
import com.adgvcxz.adgble.binding.MutliItemViewSelector;
import com.adgvcxz.adgble.content.Shot;
import com.adgvcxz.adgble.util.RxUtil;
import com.adgvcxz.adgble.util.Util;
import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * zhaowei
 * Created by zhaowei on 2016/10/25.
 */

public class ShotsDetailViewModel extends BaseRecyclerViewModel {

    public final Shot shotItemViewModel;
    public final ObservableInt translationY = new ObservableInt();
    public final ObservableInt firstItemTop = new ObservableInt();
    public final ObservableInt elevation = new ObservableInt();

    public final ItemView imageItemView = ItemView.of(BR.item, R.layout.item_shot_large_without_info);
    public final ItemView headerItemView = ItemView.of(BR.item, R.layout.item_shot_detail_header);

    public final MutliItemViewSelector itemView = MutliItemViewSelector.newInstance((position, item) -> position == 0 ? headerItemView : imageItemView);

    public ShotsDetailViewModel(Activity activity, Shot model) {
        int topMargin = activity.getResources().getDimensionPixelSize(R.dimen.detail_toolbar_height);
        final int margin = topMargin;
        shotItemViewModel = model;
        int temp = activity.getResources().getDimensionPixelSize(R.dimen.detail_toolbar_height) - Util.getActionBarHeight(activity) * 2;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            temp -= Util.getStatusBarHeight(activity);
//        }
        int height = temp;
        this.topMarginSelector = new ObservableField<>((TopMarginSelector) (view, position) -> position == 0 ? margin : 0);
        float density = activity.getResources().getDisplayMetrics().density;
        RxUtil.toObservableInt(firstItemTop).filter(integer -> integer != -1).map(integer -> -integer + topMargin)
                .map(integer -> integer > height ? height : integer)
                .filter(integer -> integer != translationY.get()).subscribe(integer -> {
            translationY.set(integer);
            elevation.set((int) (8 * integer * density / height));
        });
        for (int i = 0; i < 10; i++) {
            items.add(shotItemViewModel);
        }
    }

    @Override
    public Observable<List> request(int page) {
        return null;
    }
}
