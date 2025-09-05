package com.scheartmed.im.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.scheartmed.im.R;
import com.scheartmed.im.event.BottomBarEvent;
import com.scheartmed.im.widget.emoji.EmojiBean;
import com.scheartmed.im.widget.emoji.EmojiUtils;
import com.scheartmed.im.widget.emoji.ExpressLayout;
import com.scheartmed.im.widget.morelayout.MoreAdapter;
import com.scheartmed.im.widget.morelayout.MoreLayoutItemBean;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class ChatUiHelper {
    private static final String SHARE_PREFERENCE_NAME = "com.chat.ui";
    private static final String SHARE_PREFERENCE_TAG = "soft_input_height";
    private Activity mActivity;
    private LinearLayout mContentLayout;//整体界面布局
    private RelativeLayout mBottomLayout;//底部布局
    private ExpressLayout mEmojiLayout;//表情布局
    private LinearLayout mAddLayout;//添加布局
    private Button mSendBtn;//发送按钮
    private View mAddButton;//加号按钮
    private Button mAudioButton;//录音按钮
    private ImageView mAudioIv;//录音图片


    private EditText mEditText;
    private InputMethodManager mInputManager;
    private SharedPreferences mSp;

    public ChatUiHelper() {

    }

    public static ChatUiHelper with(Activity activity) {
        ChatUiHelper mChatUiHelper = new ChatUiHelper();
        //   AndroidBug5497Workaround.assistActivity(activity);
        mChatUiHelper.mActivity = activity;
        mChatUiHelper.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mChatUiHelper.mSp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return mChatUiHelper;
    }
//
//    public static final int EVERY_PAGE_SIZE = 21;
//    private List<EmojiBean> mListEmoji;

//    public ChatUiHelper bindEmojiData() {
//        mListEmoji = EmojiDao.getInstance().getEmojiBean();
//        //  LogUtil.d("获取到的表情集合"+Arrays.asList(mListEmoji));
//        LinearLayout homeEmoji = mActivity.findViewById(R.id.home_emoji);
//        ViewPager vpEmoji = mActivity.findViewById(R.id.vp_emoji);
//        final IndicatorView indEmoji = mActivity.findViewById(R.id.ind_emoji);
//        LinearLayout.LayoutParams layoutParams12 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        LayoutInflater inflater = LayoutInflater.from(mActivity);
//        //将RecyclerView放至ViewPager中：
//        int pageSize = EVERY_PAGE_SIZE;
//        EmojiBean mEmojiBean = new EmojiBean();
//        mEmojiBean.setId(0);
//        mEmojiBean.setUnicodeInt(000);
//        int deleteCount = (int) Math.ceil(mListEmoji.size() * 1.0 / EVERY_PAGE_SIZE);//要显示的删除键的数量
//        LogUtil.d("" + deleteCount);
//        //添加删除键
//        for (int i = 1; i < deleteCount + 1; i++) {
//            if (i == deleteCount) {
//                mListEmoji.add(mListEmoji.size(), mEmojiBean);
//            } else {
//                mListEmoji.add(i * EVERY_PAGE_SIZE - 1, mEmojiBean);
//            }
//            LogUtil.d("添加次数" + i);
//
//        }
//
//
//        int pageCount = (int) Math.ceil((mListEmoji.size()) * 1.0 / pageSize);//一共的页数
//        List<View> viewList = new ArrayList<View>();
//        for (int index = 0; index < pageCount; index++) {
//            //每个页面创建一个recycleview
//            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.item_emoji_vprecy, vpEmoji, false);
//            recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 7));
//            EmojiAdapter entranceAdapter;
//            if (index == pageCount - 1) {
//                //最后一页的数据
//                List<EmojiBean> lastPageList = mListEmoji.subList(index * EVERY_PAGE_SIZE, mListEmoji.size());
//                entranceAdapter = new EmojiAdapter(lastPageList, index, EVERY_PAGE_SIZE);
//            } else {
//                entranceAdapter = new EmojiAdapter(mListEmoji.subList(index * EVERY_PAGE_SIZE, (index + 1) * EVERY_PAGE_SIZE), index, EVERY_PAGE_SIZE);
//            }
//            entranceAdapter.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    EmojiBean mEmojiBean = (EmojiBean) adapter.getData().get(position);
//                    if (mEmojiBean.getId() == 0) {
//                        //如果是删除键
//                        mEditText.dispatchKeyEvent(new KeyEvent(
//                                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
//                    } else {
//                        mEditText.append(((EmojiBean) adapter.getData().get(position)).getUnicodeInt());
//                    }
//
//
//                }
//            });
//            recyclerView.setAdapter(entranceAdapter);
//            viewList.add(recyclerView);
//        }
//        CommonVpAdapter adapter = new CommonVpAdapter(viewList);
//        vpEmoji.setAdapter(adapter);
//        indEmoji.setIndicatorCount(vpEmoji.getAdapter().getCount());
//        indEmoji.setCurrentIndicator(vpEmoji.getCurrentItem());
//        vpEmoji.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                indEmoji.setCurrentIndicator(position);
//            }
//        });
//        return this;
//    }


    //绑定整体界面布局
    public ChatUiHelper bindContentLayout(LinearLayout bottomLayout) {
        mContentLayout = bottomLayout;
        return this;
    }


    //绑定输入框
    public ChatUiHelper bindEditText(EditText editText) {
        mEditText = editText;
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && mBottomLayout.isShown()) {
                    clickEt();
                }
                return false;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditText.getText().toString().trim().length() > 0) {
                    mSendBtn.setVisibility(View.VISIBLE);
                    mAddButton.setVisibility(View.GONE);
                } else {
                    mSendBtn.setVisibility(View.GONE);
                    mAddButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return this;
    }

    private void clickEt() {
        lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
        hideBottomLayout(true);//隐藏表情布局，显示软件盘
        //软件盘显示后，释放内容高度
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                unlockContentHeightDelayed();
            }
        }, 200L);
    }

    //绑定底部布局
    public ChatUiHelper bindBottomLayout(RelativeLayout bottomLayout) {
        mBottomLayout = bottomLayout;
        return this;
    }


    //绑定表情布局
    public ChatUiHelper bindEmojiLayout(ExpressLayout emojiLayout) {
        mEmojiLayout = emojiLayout;
        mEmojiLayout.setOnExpressSelListener(new ExpressLayout.OnExpressSelListener() {
            @Override
            public void onEmojiSelect(EmojiBean emojiBean) {
                // 如果点击了表情,则添加到输入框中
                // 获取当前光标位置,在指定位置上添加表情图片文本
                int curPosition = mEditText.getSelectionStart();
                StringBuilder sb = new StringBuilder(mEditText.getText().toString());
                sb.insert(curPosition, emojiBean.getEmojiName());
                // 特殊文字处理,将表情等转换一下
                SpannableString spannableString = EmojiUtils.text2Emoji(mEmojiLayout.getContext(), sb.toString(), mEditText.getTextSize());
                mEditText.setText(spannableString);
                // 将光标设置到新增完表情的右侧
                mEditText.setSelection(curPosition + emojiBean.getEmojiName().length());
            }

            @Override
            public void onEmojiDelete() {
                // 调用系统的删除操作
                mEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
        return this;
    }

    //绑定添加布局
    public ChatUiHelper bindAddLayout(LinearLayout addLayout) {
        mAddLayout = addLayout;
//        mAddLayout.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideBottomLayout(false);//隐藏表情布局，显示软件盘
//                unlockContentHeightDelayed();
//            }
//        });
        return this;
    }

    private static final int MORE_PAGE_SIZE = 8;

    public ChatUiHelper bindMoreLayoutData(List<MoreLayoutItemBean> list, OnItemClickListener listener) {
        ViewPager vpMore = mActivity.findViewById(R.id.VpMore);
        final IndicatorView indMore = mActivity.findViewById(R.id.indMore);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        int pageCount = (int) Math.ceil((list.size()) * 1.0 / MORE_PAGE_SIZE);//一共的页数
        List<View> viewList = new ArrayList<View>();
        for (int index = 0; index < pageCount; index++) {
            //每个页面创建一个recycleview
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.item_emoji_vprecy, vpMore, false);
            recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));
            MoreAdapter entranceAdapter;
            if (index == pageCount - 1) {
                //最后一页的数据
                List<MoreLayoutItemBean> lastPageList = list.subList(index * MORE_PAGE_SIZE, list.size());
                entranceAdapter = new MoreAdapter(lastPageList, index, MORE_PAGE_SIZE);
            } else {
                entranceAdapter = new MoreAdapter(list.subList(index * MORE_PAGE_SIZE, (index + 1) * MORE_PAGE_SIZE), index, MORE_PAGE_SIZE);
            }
            if (listener != null) {
                entranceAdapter.setOnItemClickListener(listener);
            }
            recyclerView.setAdapter(entranceAdapter);
            viewList.add(recyclerView);
        }
        if (pageCount == 1) {
            indMore.setVisibility(View.GONE);
        }
        CommonVpAdapter adapter = new CommonVpAdapter(viewList);
        vpMore.setAdapter(adapter);
        indMore.setIndicatorCount(vpMore.getAdapter().getCount());
        indMore.setCurrentIndicator(vpMore.getCurrentItem());
        vpMore.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indMore.setCurrentIndicator(position);
            }
        });
        return this;
    }


    //绑定发送按钮
    public ChatUiHelper bindToSendButton(Button sendbtn) {
        mSendBtn = sendbtn;
        return this;
    }


    //绑定语音按钮点击事件
    public ChatUiHelper bindAudioBtn(RecordButton audioBtn) {
        mAudioButton = audioBtn;
        return this;
    }

    //绑定语音图片点击事件
    public ChatUiHelper bindAudioIv(ImageView audioIv) {
        mAudioIv = audioIv;
        audioIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果录音按钮显示
                if (mAudioButton.isShown()) {
                    hideAudioButton();
                    mEditText.requestFocus();
                    showSoftInput();
                } else {
                    dealClearFocusAndShowAudio();
                }
            }
        });

        // UIUtils.postTaskDelay(() -> mRvMsg.smoothMoveToPosition(mRvMsg.getAdapter().getItemCount() - 1), 50);
        return this;
    }

    private void dealClearFocusAndShowAudio() {
        mEditText.clearFocus();
        showAudioButton();
        hideEmotionLayout();
        hideMoreLayout();
    }

    private void hideAudioButton() {
        mAudioButton.setVisibility(View.GONE);
        mEditText.setVisibility(View.VISIBLE);
        mAudioIv.setImageResource(R.mipmap.ic_audio);
    }


    private void showAudioButton() {
        mAudioButton.setVisibility(View.VISIBLE);
        mEditText.setVisibility(View.GONE);
        mAudioIv.setImageResource(R.mipmap.ic_keyboard);
        if (mBottomLayout.isShown()) {
            hideBottomLayout(false);
        } else {
            hideSoftInput();
        }
    }


    //绑定表情按钮点击事件
    public ChatUiHelper bindToEmojiButton(ImageView emojiBtn) {
        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.clearFocus();
                if (!mEmojiLayout.isShown()) {
                    if (mAddLayout.isShown()) {
                        showEmotionLayout();
                        hideMoreLayout();
                        hideAudioButton();
                        return;
                    }
                } else if (mEmojiLayout.isShown() && !mAddLayout.isShown()) {
                    if (mBottomLayout.isShown()) {
                        dealClosePanel();
                    } else {
                        if (isSoftInputShown()) {//同上
                            dealShowPanel();
                        } else {
                            showBottomLayout();//两者都没显示，直接显示表情布局
                        }
                    }
                    return;
                }
                showEmotionLayout();
                hideMoreLayout();
                hideAudioButton();
                if (mBottomLayout.isShown()) {
                    dealClosePanel();
                } else {
                    if (isSoftInputShown()) {//同上
                        dealShowPanel();
                    } else {
                        showBottomLayout();//两者都没显示，直接显示表情布局
                    }
                }
            }
        });
        return this;
    }

    private void dealShowPanel() {
        lockContentHeight();
        showBottomLayout();
        unlockContentHeightDelayed();
    }


    //绑定底部加号按钮
    public ChatUiHelper bindToAddButton(View addButton) {
        mAddButton = addButton;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.clearFocus();
                hideAudioButton();
                if (mBottomLayout.isShown()) {
                    if (mAddLayout.isShown()) {
                        dealClosePanel();
                    } else {
                        showMoreLayout();
                        hideEmotionLayout();
                    }
                } else {
                    if (isSoftInputShown()) {//同上
                        hideEmotionLayout();
                        showMoreLayout();
                        dealShowPanel();
                    } else {
                        showMoreLayout();
                        hideEmotionLayout();
                        showBottomLayout();//两者都没显示，直接显示表情布局
                    }
                }
            }
        });
        return this;
    }

    private void dealClosePanel() {
        lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
        hideBottomLayout(true);//隐藏表情布局，显示软件盘
        unlockContentHeightDelayed();//软件盘显示后，释放内容高度
    }


    private void hideMoreLayout() {
        mAddLayout.setVisibility(View.GONE);
    }

    private void showMoreLayout() {
        mAddLayout.setVisibility(View.VISIBLE);
    }


    /**
     * 隐藏底部布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    public void hideBottomLayout(boolean showSoftInput) {
        if (mBottomLayout.isShown()) {
            mBottomLayout.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            } else {
                EventBus.getDefault().post(new BottomBarEvent(true));
            }
        }
    }

    private void showBottomLayout() {
        EventBus.getDefault().post(new BottomBarEvent(false));
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
//            softInputHeight = dip2Px(270);
            softInputHeight = mSp.getInt(SHARE_PREFERENCE_TAG, dip2Px(270));
        }
        hideSoftInput();
        mBottomLayout.getLayoutParams().height = softInputHeight;
        mBottomLayout.setVisibility(View.VISIBLE);
    }


    private void showEmotionLayout() {
        mEmojiLayout.setVisibility(View.VISIBLE);
    }

    private void hideEmotionLayout() {
        mEmojiLayout.setVisibility(View.GONE);
    }

    /**
     * 是否显示软件盘
     *
     * @return
     */
    public boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    public int dip2Px(int dip) {
        float density = mActivity.getApplicationContext().getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }


    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public boolean isShowBottomLayout(){
        return mBottomLayout.isShown();
    }


    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /*  *
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。*/
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        if (isNavigationBarExist(mActivity)) {
            softInputHeight = softInputHeight - getNavigationHeight(mActivity);
        }
        //存一份到本地
        if (softInputHeight > 0) {
            mSp.edit().putInt(SHARE_PREFERENCE_TAG, softInputHeight).apply();
        }
        return softInputHeight;
    }


    public void showSoftInput() {
        mEditText.requestFocus();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mEditText, 0);
            }
        });
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentLayout.getLayoutParams();
        params.height = mContentLayout.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    public void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentLayout.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }


    private static final String NAVIGATION = "navigationBarBackground";

    // 该方法需要在View完全被绘制出来之后调用，否则判断不了
    //在比如 onWindowFocusChanged（）方法中可以得到正确的结果
    public boolean isNavigationBarExist(@NonNull Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                vp.getChildAt(i).getContext().getPackageName();
                if (vp.getChildAt(i).getId() != View.NO_ID && NAVIGATION.equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                    return true;
                }
            }
        }
        return false;
    }


    public int getNavigationHeight(Context activity) {
        if (activity == null) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = 0;
        if (resourceId > 0) {
            //获取NavigationBar的高度
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }


}
