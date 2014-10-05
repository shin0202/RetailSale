package com.example.retailsale.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.retailsale.MainActivity;
import com.example.retailsale.PhotoPlayer;
import com.example.retailsale.R;

public class BrowserFragment extends Fragment implements OnItemClickListener
{
//	private Context context;
	private int albumCount = 0;
	private PhotosAdapterView photosAdapterView;
	private List<Integer> photos = new ArrayList<Integer>();
	private GridView photoGrid;

//	public BrowserFragment(Context context)
//	{
//		this.context = context;
//	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		MainActivity mainActivity = (MainActivity) activity;
		// value = mainActivity.getBrowserData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.browser_tab, container, false);
		photoGrid = (GridView) view.findViewById(R.id.browser_tab_files_grid);
		photoGrid.setOnItemClickListener(this);
		LinearLayout albums = (LinearLayout) view.findViewById(R.id.albums);
		albumCount = 0;
		for (int j = 0; j < 3; j++)
		{
			albums.addView(insertPhoto("path"));
		}
		for (int i = 0; i < 50; i++)
		{
			photos.add(i);
		}
		photosAdapterView = new PhotosAdapterView(getActivity(), photos);
		photoGrid.setAdapter(photosAdapterView);
		return view;
	}

	View insertPhoto(String path)
	{
		// Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
		int layoutDp = (int) getResources().getDimension(R.dimen.scrollview_layout_size);
		int imgDp = (int) getResources().getDimension(R.dimen.scrollview_img_size);
		LinearLayout layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(layoutDp, layoutDp));
		layout.setGravity(Gravity.CENTER);
		ImageView imageView = new ImageView(getActivity());
		imageView.setLayoutParams(new LayoutParams(imgDp, imgDp));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// imageView.setImageBitmap(bm);
		imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.album));
		imageView.setTag(albumCount);
		albumCount++;
		imageView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Log.d("Test", "albumCount " + v.getTag());
				int photoCount = 0;
				photos.clear();
				photosAdapterView = null;
				switch ((Integer) v.getTag())
				{
				case 0:
					photoCount = 50;
					break;
				case 1:
					photoCount = 30;
					break;
				case 2:
					photoCount = 10;
					break;
				}
				Log.d("Test", "photoCount " + photoCount);
				for (int i = 0; i < photoCount; i++)
				{
					photos.add(i);
				}
				photosAdapterView = new PhotosAdapterView(getActivity(), photos);
				photoGrid.setAdapter(photosAdapterView);
			}
		});
		layout.addView(imageView);
		return layout;
	}

	public class PhotosAdapterView extends BaseAdapter
	{
		public static final int BASE_INDEX = 1000;
		private List<Integer> photos;
		private Context context;
		// Views
		private LayoutInflater layoutInflater;
		private ViewTag viewTag;

		public PhotosAdapterView(Context context, List<Integer> photos)
		{
			this.context = context;
			this.photos = photos;
			layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount()
		{
			return photos.size();
		}

		@Override
		public Object getItem(int position)
		{
			return photos.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = layoutInflater.inflate(R.layout.cell_of_browser_tab, null);
				viewTag = new ViewTag((ImageView) convertView.findViewById(R.id.browser_photo));
				convertView.setTag(viewTag);
			}
			else
			{
				viewTag = (ViewTag) convertView.getTag();
			}
			convertView.setId(BASE_INDEX + position);
			return convertView;
		}

		class ViewTag
		{
			ImageView showPhoto;

			public ViewTag(ImageView showPhoto)
			{
				this.showPhoto = showPhoto;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intent = new Intent(getActivity(), PhotoPlayer.class);
		startActivity(intent);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
//		TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
//		txtResult.setText(value);
	}
}
