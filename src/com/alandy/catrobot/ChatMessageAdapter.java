package com.alandy.catrobot;

import java.text.SimpleDateFormat;
import java.util.List;

import org.w3c.dom.Text;

import com.alandy.catrobot.bean.ChatMessage;
import com.alandy.catrobot.bean.ChatMessage.Type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {
	private List<ChatMessage> mListDatas;
	private LayoutInflater mInflater;
	public ChatMessageAdapter(Context context,
			List<ChatMessage> mListDatas) {
		mInflater = LayoutInflater.from(context);
		this.mListDatas = mListDatas;
	}

	@Override
	public int getCount() {
		return mListDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mListDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	/**
	 * 有两种布局，一种是接收，一种是发送
	 */
	@Override
	public int getViewTypeCount(){
		return 2;
	}
	
	//区分布局
	@Override
	public int getItemViewType(int position) {
		ChatMessage chatMessage = mListDatas.get(position);
		if (chatMessage.getType() == Type.INCOMING) {
			return 0;
		}
		return 1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMessage chatMessage = mListDatas.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			// 通过ItemType设置不同的布局
			if (getItemViewType(position) == 0) {
				//接收布局
				convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
				holder = new ViewHolder();
				holder.date =(TextView) convertView.findViewById(R.id.tv_form_msg_date);
				holder.msg = (TextView) convertView.findViewById(R.id.tv_from_msg_info);
				
			}else {
				//发送布局
				convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
				holder = new ViewHolder();
				holder.date = (TextView) convertView.findViewById(R.id.tv_to_msg_date);
				holder.msg = (TextView) convertView.findViewById(R.id.tv_to_msg_info);
			}
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		//设置数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		holder.date.setText(sdf.format(chatMessage.getDate()));
		holder.msg.setText(chatMessage.getMsg());
		
		return convertView;
	}
	
	private class ViewHolder{
		//时间
		TextView date;
		//内容
		TextView msg;
	}
}
