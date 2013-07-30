package com.kapp.singlo.ui;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kapp.singlo.R;

public class Mylesson_Adapter extends ArrayAdapter<Mylesson_List_Data> {
	
	private ArrayList<Mylesson_List_Data> items;
	
	LinearLayout likestar;	
	ImageView inlike;
	
    public Mylesson_Adapter(Context context, int textViewResourceId, ArrayList<Mylesson_List_Data> items) {
        super(context, textViewResourceId, items);
        this.items = items; 
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
             
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.mylesson_item, null);
        }
        Mylesson_List_Data Mylesson_List_Data = items.get(position);
 
        if (Mylesson_List_Data != null) {
            
            //ImageView ind = (ImageView)v.findViewById(R.id.ImageView_1);
        	WebView ind = (WebView)v.findViewById(R.id.webView1); 
            TextView ind_01 = (TextView)v.findViewById(R.id.TextView_01);
            TextView ind_02 = (TextView)v.findViewById(R.id.TextView_02);
            TextView ind_03 = (TextView)v.findViewById(R.id.TextView_03); 
            TextView ind_04 = (TextView)v.findViewById(R.id.TextView_04);  
            inlike = (ImageView)v.findViewById(R.id.ImageView_9);                
             
           
            //ind.setImageResource(Contents_List_Data.getImage_ID());
            //ind.setImageBitmap(Contents_List_Data.getImage_url());
            //ind.setPadding(10, 10, 10, 10);
            //	WebSettings set = ind.getSettings();
            //	set.setLoadWithOverviewMode(true);
            //	set.setUseWideViewPort(true);
            //ind.loadUrl(Contents_List_Data.getImage_URL());          
            ind.loadDataWithBaseURL(null, Mylesson_Adapter.getImageHtmlCode(Mylesson_List_Data.getImage_URL()), "text/html", "utf-8", null);
            ind.setBackgroundResource(R.anim.shape);
            ind.setPadding(1, 1, 1, 1);             
            //ind.setBackgroundResource(R.drawable.hospital1_imgbg);            
            ind_01.setText(Mylesson_List_Data.getTop_Titles());            
            ind_02.setText(Mylesson_List_Data.getMain_Titles());
            ind_03.setText(Mylesson_List_Data.getSub_Titles());  
            ind_04.setText(Mylesson_List_Data.getText_Titles()); 
            inlike.setImageResource(Mylesson_List_Data.getImage_PL());
            
            //focus disable      
            ind.setFocusable(false);            
            ind.setClickable(false);
            ind.setLongClickable(false);
            ind.setFocusableInTouchMode(false);
            ind.setHorizontalScrollBarEnabled(false);
            ind.setVerticalScrollBarEnabled(false);
            ind.setScrollBarStyle(v.SCROLLBARS_INSIDE_OVERLAY);
            
            ind_01.setFocusable(false);
            ind_02.setFocusable(false);
            ind_03.setFocusable(false);
            ind_04.setFocusable(false); 
            
            likestar = (LinearLayout)v.findViewById(R.id.likestar);   
            likestar.setOnClickListener(likestarOnClickListener);
            likestar.setTag(position);
            
        }
 
        return v;
    }    
    
    public static String getImageHtmlCode(String _imageURL){
		StringBuffer sb = new StringBuffer("<HTML>");
		sb.append("<HEAD>");
		sb.append("<style type='text/css'>");
		sb.append("body {");
		sb.append("margin-left: 0px;");
		sb.append("margin-top: 0px;");
		sb.append("margin-right: 0px;");
		sb.append("margin-bottom: 0px;");
		sb.append("}");
		sb.append("</style>");  
		sb.append("</HEAD>");
		sb.append("<BODY>");		
		sb.append("<img width=\"100%\" height=\"100%\" src=\""+_imageURL +"\"/>");
		sb.append("</BODY>");
		sb.append("</HTML>");
		return sb.toString();
    }
    
    private OnClickListener likestarOnClickListener=new OnClickListener(){

		public void onClick(View v) {
			ImageView imglike = (ImageView)v.findViewById(R.id.ImageView_9); 
			
			int pos = (Integer) v.getTag();
			
			Log.d("pos", String.valueOf(pos));			
			
				
		}		
	};
	
	
	
		
	
}
 
class Mylesson_List_Data {
	//private Bitmap Image_url;
    //private int Image_ID;
	private String Image_URL;
    private String Top_Titles;
    private String Main_Titles;    
    private String Sub_Titles;
    private String Text_Titles;
    private int Image_PL;    
 
    public Mylesson_List_Data(String Image_URL, String Top_Titles, String Main_Titles, String Sub_Titles, String Text_Titles, int Image_PL) {
    	//this.setImage_url(Image_url);
        //this.setImage_ID(Image_ID);
    	this.setImage_URL(Image_URL);
        this.setTop_Titles(Top_Titles);
        this.setMain_Titles(Main_Titles);       
        this.setSub_Titles(Sub_Titles);
        this.setText_Titles(Text_Titles);
        this.setImage_PL(Image_PL);
    }
    
    public String getImage_URL() {
        return Image_URL;
    }
    
    private void setImage_URL(String image_URL) {
		// TODO Auto-generated method stub
		Image_URL = image_URL;
	}


	public String getTop_Titles() {
        return Top_Titles;
    }
 
    public void setTop_Titles(String top_titles) {
    	Top_Titles = top_titles;
    }
 
    public String getMain_Titles() {
        return Main_Titles;
    }
 
    public void setMain_Titles(String main_titles) {
        Main_Titles = main_titles;
    }
     
    public String getSub_Titles() {
        return Sub_Titles;
    }
 
    public void setSub_Titles(String sub_titles) {
       Sub_Titles = sub_titles;
    }
    
    public String getText_Titles() {
        return Text_Titles;
    }
 
    public void setText_Titles(String text_titles) {
       Text_Titles = text_titles;
    }
    
    public int getImage_PL() {
        return Image_PL;
    }
 
    public void setImage_PL(int image_PL) {
        Image_PL = image_PL;
    }   
     
}