package com.alandy.catrobot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alandy.catrobot.bean.ChatMessage;
import com.alandy.catrobot.bean.ChatMessage.Type;
import com.alandy.catrobot.utils.HttpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView mListMsgs;
	private EditText mInputMsg;
	private Button mSendMsg;
	private List<ChatMessage> mListDatas;
	private ChatMessageAdapter mAdatpter;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			// 等待接收，子线程完成数据的返回
			ChatMessage fromMessage = (ChatMessage) msg.obj;
			mListDatas.add(fromMessage);
			mAdatpter.notifyDataSetChanged();
			mListMsgs.setSelection(mListDatas.size()-1);
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
		initListener();
	}

	private void initView() {
		mListMsgs = (ListView) findViewById(R.id.listview_msgs);
		mInputMsg = (EditText) findViewById(R.id.et_input_msg);
		mSendMsg = (Button) findViewById(R.id.bt_send_msg);
	}

	private void initData() {
		mListDatas = new ArrayList<ChatMessage>();
		mListDatas.add(new ChatMessage("你好！小猫很高兴为你服务", Type.INCOMING,
				new Date()));
		mAdatpter = new ChatMessageAdapter(this, mListDatas);
		mListMsgs.setAdapter(mAdatpter);
	}

	private void initListener() {
		mSendMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String toMsg = mInputMsg.getText().toString();
				if (TextUtils.isEmpty(toMsg)) {
					Toast.makeText(MainActivity.this, "发送消息不能为空！", 0).show();
					return;
				}

				//把发送的信息封装到ChatMessage中
				ChatMessage toMessage = new ChatMessage();

				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);

				mListDatas.add(toMessage);
				//刷新界面
				mAdatpter.notifyDataSetChanged();
				//输入框数据清空
				mInputMsg.setText("");
				mListMsgs.setSelection(mListDatas.size() -1 );

				new Thread() {
					public void run() {
						ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
						System.out.println(fromMessage);
						Message m = Message.obtain();
						m.obj = fromMessage;
						mHandler.sendMessage(m);
					};
				}.start();
			}
		});
	}
}
