package com.szmy.pigapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActivityTickling extends BaseActivity {
	private ListView mListView;
	private FeedbackAgent mAgent;
	private Conversation mComversation;
	private Context mContext;
	private ReplyAdapter adapter;
	private Button sendBtn;
	private EditText inputEdit;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private final int VIEW_TYPE_COUNT = 2;
	private final int VIEW_TYPE_USER = 0;
	private final int VIEW_TYPE_DEV = 1;
	private final String TAG = ActivityTickling.class.getName();

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tickling);
		mContext = this;
		initView();
		mAgent = new FeedbackAgent(this);
		mComversation = new FeedbackAgent(this).getDefaultConversation();
		adapter = new ReplyAdapter();
		mListView.setAdapter(adapter);
		sync();

	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.fb_reply_list);
		sendBtn = (Button) findViewById(R.id.fb_send_btn);
		inputEdit = (EditText) findViewById(R.id.fb_send_content);
		// ����ˢ�����
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fb_reply_refresh);
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = inputEdit.getText().toString();
				inputEdit.getEditableText().clear();
				if (!TextUtils.isEmpty(content)) {
					// ��������ӵ��Ự�б�
					mComversation.addUserReply(content);
					// ˢ����ListView
					mHandler.sendMessage(new Message());
					// ���ͬ��
					sync();
				}
			}
		});

		// ����ˢ��
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				sync();
			}
		});
	}

	// ���ͬ��
	private void sync() {

		mComversation.sync(new SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> replyList) {
			}

			@Override
			public void onReceiveDevReply(List<Reply> replyList) {
				// SwipeRefreshLayoutֹͣˢ��
				mSwipeRefreshLayout.setRefreshing(false);
				// ������Ϣ��ˢ��ListView
				mHandler.sendMessage(new Message());
				// �����û���µĻظ���ݣ��򷵻�
				if (replyList == null || replyList.size() < 1) {
					return;
				}
			}
		});
		// ����adapter��ˢ��ListView
		adapter.notifyDataSetChanged();
	}

	// adapter
	class ReplyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mComversation.getReplyList().size();
		}

		@Override
		public Object getItem(int arg0) {
			return mComversation.getReplyList().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public int getViewTypeCount() {
			// ���ֲ�ͬ��Tiem����
			return VIEW_TYPE_COUNT;
		}

		@Override
		public int getItemViewType(int position) {
			// ��ȡ�����ظ�
			Reply reply = mComversation.getReplyList().get(position);
			if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
				// �����߻ظ�Item����
				return VIEW_TYPE_DEV;
			} else {
				// �û��������ظ�Item����
				return VIEW_TYPE_USER;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			// ��ȡ�����ظ�
			Reply reply = mComversation.getReplyList().get(position);
			if (convertView == null) {
				// ���Type�����������ز�ͬ��Item����
				if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
					// �����ߵĻظ�
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.custom_fb_dev_reply, null);
				} else {
					// �û��ķ������ظ�
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.custom_fb_user_reply, null);
				}

				// ����ViewHolder����ȡ����View
				holder = new ViewHolder();
				holder.replyContent = (TextView) convertView
						.findViewById(R.id.fb_reply_content);
				holder.replyProgressBar = (ProgressBar) convertView
						.findViewById(R.id.fb_reply_progressBar);
				holder.replyStateFailed = (ImageView) convertView
						.findViewById(R.id.fb_reply_state_failed);
				holder.replyData = (TextView) convertView
						.findViewById(R.id.fb_reply_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// ������������
			// ����Reply������
			holder.replyContent.setText(reply.content);
			// ��AppӦ�ý��棬���ڿ����ߵ�Reply����statusû������
			if (!Reply.TYPE_DEV_REPLY.equals(reply.type)) {
				// ���Reply��״̬������replyStateFailed��״̬
				if (Reply.STATUS_NOT_SENT.equals(reply.status)) {
					holder.replyStateFailed.setVisibility(View.VISIBLE);
				} else {
					holder.replyStateFailed.setVisibility(View.GONE);
				}

				// ���Reply��״̬������replyProgressBar��״̬
				if (Reply.STATUS_SENDING.equals(reply.status)) {
					holder.replyProgressBar.setVisibility(View.VISIBLE);
				} else {
					holder.replyProgressBar.setVisibility(View.GONE);
				}
			}

			// �ظ���ʱ����ݣ��������QQ����Reply֮�����100000ms��չʾʱ��
			if ((position + 1) < mComversation.getReplyList().size()) {
				Reply nextReply = mComversation.getReplyList()
						.get(position + 1);
				if (nextReply.created_at - reply.created_at > 100000) {
					Date replyTime = new Date(reply.created_at);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					holder.replyData.setText(sdf.format(replyTime));
					holder.replyData.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}

		class ViewHolder {
			TextView replyContent;
			ProgressBar replyProgressBar;
			ImageView replyStateFailed;
			TextView replyData;
		}
	}
}
