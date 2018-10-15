package com.sansi.stellarWiFi.util;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/14 10:31
 *          类说明
 */
final public class ListViewCompat {
    /**
     * 局部更新数据，调用一次getView()方法；Google推荐的做法
     *
     * @param listView 要更新的listview
     * @param position 要更新的位置
     */
    public static void notifyDataSetChanged(ListView listView, int position) {
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();
        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = listView.getChildAt(position - firstVisiblePosition);
            ListAdapter adapter = listView.getAdapter();
            if (adapter != null) adapter.getView(position, view, listView);
        }

    }

}
