package com.scheartmed.im.widget.emoji;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;

import com.scheartmed.im.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmojiUtils {

    public static final String[] EMOJI_LIST={
            "😀", // 开心
            "😃", // 大笑
            "😄", // 笑脸
            "😁", // 露齿笑
            "😆", // 捧腹
            "😅", // 冷汗
            "🤣", // 笑翻
            "😂", // 笑哭
            "🙂", // 微笑
            "🙃", // 倒笑
            "😉", // 眨眼
            "😊", // 害羞
            "😇", // 天使
            "🥰", // 爱心眼
            "😍", // 心眼
            "🤩", // 星眼
            "😘", // 飞吻
            "😗", // 亲吻
            "😚", // 亲吻笑
            "😙", // 亲吻微笑
            "😋", // 调皮舔嘴
            "😛", // 调皮吐舌
            "😜", // 眨眼吐舌
            "🤪", // 疯狂脸
            "😝", // 尖叫吐舌
            "🤑", // 钱脸
            "🤗", // 拥抱
            "🤭", // 捂嘴笑
            "🤫", // 嘘
            "🤔", // 思考
            "🤐", // 闭嘴
            "🤨", // 斜眼
            "😐", // 平淡
            "😑", // 无语
            "😶", // 无口
            "😏", // 得意
            "😒", // 不满
            "🙄", // 翻白眼
            "😬", // 尴尬
            "🤥", // 撒谎
            "😌", // 放松
            "😔", // 沮丧
            "😪", // 困
            "🤤", // 流口水
            "😴", // 睡觉
            "😷", // 生病
            "🤒", // 发烧
            "🤕", // 受伤
            "🤢", // 恶心
            "🤮", // 呕吐
            "🤧", // 打喷嚏
            "🥵", // 热
            "🥶", // 冷
            "🥴", // 眩晕
            "😵", // 晕
            "🤯", // 爆炸头
            "🤠", // 牛仔帽
            "🥳", // 庆祝
            "😎", // 墨镜
            "🤓", // 书呆子
            "🧐", // 单眼镜
            "😕", // 困惑
            "😟", // 担心
            "🙁", // 难过
            "☹️", // 哭脸
            "😮", // 惊讶
            "😯", // 震惊
            "😲", // 张大嘴
            "😳", // 羞愧
            "🥺", // 恳求
            "😦", // 不悦
            "😧", // 痛苦
            "😨", // 害怕
            "😰", // 冷汗
            "😥", // 伤心
            "😢", // 哭
            "😭", // 哭泣
            "😱", // 惊恐
            "😖", // 烦恼
            "😣", // 折磨
            "😞", // 失望
            "😓", // 出汗
            "😩", // 疲惫
            "😫", // 累
            "🥱", // 打哈欠
            "😤", // 生气
            "😡", // 暴怒
            "😠", // 愤怒
            "🤬", // 骂人
            "😈", // 恶魔
            "👿", // 魔鬼
            "💀", // 骷髅
            "☠️", // 死亡
            "💩", // 屎
            "🤡", // 小丑
            "👹", // 鬼
            "👺", // 狐狸鬼
            "👻", // 幽灵
            "👽", // 外星人
            "👾", // 外星怪
            "🤖", // 机器人
            "🎃", // 南瓜
            "😺", // 笑猫
            "😸", // 开心猫
            "😹", // 笑哭猫
            "😻", // 爱心猫
            "😼", // 得意猫
            "😽", // 亲吻猫
            "🙀", // 惊吓猫
            "😿", // 哭猫
            "😾"  // 生气猫
    };

//    private static final int[] EMOJI_INDEX = {
//            R.drawable.d_hehe, // 呵呵
//            R.drawable.d_keai, // 可爱
//            R.drawable.d_taikaixin, // 太开心
//            R.drawable.d_guzhang, // 鼓掌
//            R.drawable.d_xixi, // 嘻嘻
//            R.drawable.d_haha, // 哈哈
//            R.drawable.d_xiaoku, // 笑哭
//            R.drawable.d_tiaopi, // 调皮
//            R.drawable.d_chanzui,// 馋嘴
//            R.drawable.d_heixian, // 黑线
//            R.drawable.d_han, // 汗
//            R.drawable.d_wabishi, // 挖鼻屎
//            R.drawable.d_heng, // 哼
//            R.drawable.d_nu, // 怒
//            R.drawable.d_kelian, // 可怜
//            R.drawable.d_liulei, // 流泪
//            R.drawable.d_daku, // 大哭
//            R.drawable.d_haixiu,// 害羞
//            R.drawable.d_aini, // 爱你
//            R.drawable.d_qinqin,// 亲亲
//            R.drawable.face_delete,// 删除键
//
//            R.drawable.d_doge, // doge
//            R.drawable.d_miao, // miao
//            R.drawable.d_yinxian, //阴险
//            R.drawable.d_touxiao,// 偷笑
//            R.drawable.d_ku, // 酷
//            R.drawable.d_sikao, // 思考
//            R.drawable.d_baibai, // 拜拜
//            R.drawable.d_bishi, // 鄙视
//            R.drawable.d_bizui, // 闭嘴
//            R.drawable.d_chijing, // 吃惊
//            R.drawable.d_dahaqi, // 打哈欠
//            R.drawable.d_dalian, // 打脸
//            R.drawable.d_ganmao, // 感冒
//            R.drawable.d_kun, // 困
//            R.drawable.d_zhouma, // 咒骂
//            R.drawable.d_shengbing,// 生病
//            R.drawable.d_shiwang, // 失望
//            R.drawable.d_shuai, // 衰
//            R.drawable.d_shuijiao, // 睡觉
//            R.drawable.d_tu, // 吐
//            R.drawable.face_delete,// 删除
//
//            R.drawable.d_weiqu, // 委屈
//            R.drawable.d_xu, // 嘘
//            R.drawable.d_yiwen, // 疑问
//            R.drawable.d_yun, // 晕
//            R.drawable.d_zuohengheng, // 左哼哼
//            R.drawable.d_youhengheng, // 右哼哼
//            R.drawable.d_zhuakuang,// 抓狂
//            R.drawable.d_zhutou, // 猪头
//            R.drawable.xinsui, // 心碎
//            R.drawable.l_xin, // 心
//            R.drawable.h_xihuanni, // 喜欢你
//            R.drawable.h_buyao, // 不要
//            R.drawable.h_bang, // 棒
//            R.drawable.h_lai,  // 来
//            R.drawable.h_ok,  // OK
//            R.drawable.h_quantou, // 拳头
//            R.drawable.h_ruo, // 弱
//            R.drawable.h_woshou, // 握手
//            R.drawable.h_shengli, //胜利
//            R.drawable.h_zan, // 赞
//
//            R.drawable.o_lazhu, // 蜡烛
//            R.drawable.o_liwu, // 礼物
//            R.drawable.o_dangao, // 蛋糕
//            R.drawable.o_feiji, // 飞机
//            R.drawable.o_ganbei, // 干杯
//            R.drawable.o_weiguan, // 围观
//            R.drawable.w_fuyun, // 云
//            R.drawable.w_taiyang, // 太阳
//            R.drawable.w_weifeng, // 微风
//            R.drawable.w_xiayu, // 下雨
//            R.drawable.w_yueliang, // 月亮
//    };



//    private static final String[] EMOJI_NAME = {
//            "[呵呵]",
//            "[可爱]",
//            "[太开心]",
//            "[鼓掌]",
//            "[嘻嘻]",
//            "[哈哈]",
//            "[笑哭]",
//            "[调皮]",
//            "[馋嘴]",
//            "[黑线]",
//            "[汗]",
//            "[挖鼻屎]",
//            "[哼]",
//            "[怒]",
//            "[可怜]",
//            "[流泪]",
//            "[大哭]",
//            "[害羞]",
//            "[爱你]",
//            "[亲亲]",
//            "[删除]",
//
//            "[doge]",
//            "[miao]",
//            "[阴险]",
//            "[偷笑]",
//            "[酷]",
//            "[思考]",
//            "[拜拜]",
//            "[鄙视]",
//            "[闭嘴]",
//            "[吃惊]",
//            "[打哈欠]",
//            "[打脸]",
//            "[感冒]",
//            "[困]",
//            "[咒骂]",
//            "[生病]",
//            "[失望]",
//            "[衰]",
//            "[睡觉]",
//            "[吐]",
//            "[删除]",
//
//            "[委屈]",
//            "[嘘]",
//            "[疑问]",
//            "[晕]",
//            "[左哼哼]",
//            "[右哼哼]",
//            "[抓狂]",
//            "[猪头]",
//            "[心碎]",
//            "[心]",
//            "[喜欢你]",
//            "[不要]",
//            "[棒]",
//            "[来]",
//            "[OK]",
//            "[拳头]",
//            "[弱]",
//            "[握手]",
//            "[胜利]",
//            "[赞]",
//            "[删除]",
//
//            "[蜡烛]",
//            "[礼物]",
//            "[蛋糕]",
//            "[飞机]",
//            "[干杯]",
//            "[围观]",
//            "[云]",
//            "[太阳]",
//            "[微风]",
//            "[下雨]",
//            "[月亮]",
//            "[删除]",
//    };

    private static List<EmojiBean> sEmojiBeans;
    private static Map<String,Integer> sEmojiMap;

    static {
        createEmojiList();
    }

    static List<EmojiBean> getEmojiBeans(){
        if (sEmojiBeans == null){
            createEmojiList();
        }
        return sEmojiBeans;
    }

    private static void createEmojiList(){
        sEmojiBeans = new ArrayList<>();
        sEmojiMap = new HashMap<>();
        EmojiBean emojiBean;
        for (int i = 0;i<EMOJI_LIST.length;i++){
            emojiBean = new EmojiBean();
//            emojiBean.setResIndex(EMOJI_INDEX[i]);
            emojiBean.setEmojiName(EMOJI_LIST[i]);
            sEmojiBeans.add(emojiBean);
//            sEmojiMap.put(EMOJI_NAME[i],EMOJI_INDEX[i]);
        }

    }

    /**
     * 从 Resource 中读取 Emoji 表情
     * @param res Resource
     * @param resId Emoji'id in Resource
     * @param reqWidth ImageView Width
     * @param reqHeight ImageView Height
     * @return Emoji with Bitmap
     */
    static Bitmap decodeBitmapFromRes(Resources res, int resId,
                                      int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 计算图片压缩比例
     * @param options Bitmap Decode Option
     * @param reqWidth ImageView Width
     * @param reqHeight ImageView Height
     * @return inSample Size value
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 单位转化
    static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static SpannableString text2Emoji(Context context,final String source,final float textSize) {
        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();
//        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
//        Pattern patternEmotion = Pattern.compile(regexEmotion);
//        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
//        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
//            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
//            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
//           Integer imgRes = getImgByName(key);
//            if (imgRes != 0) {
                // 压缩表情图片
                int size = (int) (textSize * 13.0f / 10.0f);
//                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
//                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
//                ImageSpan span = new ImageSpan(context, scaleBitmap);
//                spannableString.setSpan(span, start, start + key.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
        return spannableString;
    }

//    public static Integer getImgByName(String name){
//        return sEmojiMap.get(name);
//    }

}
