package yeah.cstriker1407.android.hellofragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MiddleFragment extends Fragment {
	private static final String[] WEEKS = new String[] { "Sunday", "Monday",
			"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	public static MiddleFragment newInstance(int index) {
		MiddleFragment df = new MiddleFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		df.setArguments(args);
		return df;
	}

	public int getShowIndex() {
		int index = getArguments().getInt("index", 0);
		return index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView tv = new TextView(getActivity());
		tv.setText(WEEKS[getShowIndex()]);
		return tv;
	}

}