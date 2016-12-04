package com.example.gemswin.screancasttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGridIcons extends BaseAdapter{
	  private Context mContext;
	  private final String[] web;
	  private final int[] Imageid; 

	    public CustomGridIcons(Context c, String[] web, int[] Imageid) {
	        mContext = c;
	        this.Imageid = Imageid;
	        this.web = web;
	    }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return web.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
		    View v;
		    if (convertView == null) {  // if it's not recycled, initialize some attributes
		        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(     Context.LAYOUT_INFLATER_SERVICE );
		        v = inflater.inflate(R.layout.grid_single, parent, false);
		    } else {
		        v = (View) convertView;
		    }
		    TextView text = (TextView)v.findViewById(R.id.grid_text);
		    text.setText(web[position]);
		    ImageView image = (ImageView)v.findViewById(R.id.grid_image);
		    image.setImageResource(Imageid[position]);
		    return v;
		}
}
