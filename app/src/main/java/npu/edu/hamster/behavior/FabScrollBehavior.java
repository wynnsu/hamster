package npu.edu.hamster.behavior;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

/**
 * Created by su153 on 2/27/2017.
 */

public class FabScrollBehavior extends FloatingActionButton.Behavior {
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed < 0 && child.getVisibility() == View.GONE)
            child.show();
        if (coordinatorLayout.getScrollY() > target.getHeight() - 20)
            child.hide();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }
}
