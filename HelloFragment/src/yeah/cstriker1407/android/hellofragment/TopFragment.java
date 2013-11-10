package yeah.cstriker1407.android.hellofragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TopFragment extends ListFragment
{
	private static final String[] WEEKS = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	// 初始化的选择位置
	int mCurCheckPosition = 0;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		// 设置要显示的数据
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.activity_list_item, android.R.id.text1, WEEKS));
		showDetails(mCurCheckPosition);
	}

	private void showDetails(int index)
	{
		// fragment的管理器
		FragmentManager fm = getFragmentManager();
		/*  */
		MiddleFragment details = (MiddleFragment) fm.findFragmentById(R.id.framelayout);

		if (details == null || details.getShowIndex() != index)
		{
			// 设置为单选模式
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			// 指定条目被选中
			getListView().setItemChecked(index, true);

			details = MiddleFragment.newInstance(index);
			// 新建DetailFragment的实例
			FragmentTransaction ft = fm.beginTransaction();
			// 替换FrameLayout为DetailFragment 类似于 事务
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			// 将得到的fragment 替换当前的viewGroup内容，add则不替换会依次累加
			
			/* 这里是将fragment替换detail里的内容，也就是说detail是个父容器，因此在上面的findfragmentbyid就可以以detail为父容器来查找地下是否有fragment */
			ft.replace(R.id.framelayout, details);
			// 提交
			ft.commit();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		showDetails(position);
	}

}
