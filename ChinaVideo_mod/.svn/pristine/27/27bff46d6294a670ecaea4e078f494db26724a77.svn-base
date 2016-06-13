package com.zhipu.chinavideo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import com.zhipu.chinavideo.R;

public class SmilyParse {
	private Context mContext;
	private Pattern mPattern;
	private String[] mResArrayText;
	private Map<String, Integer> mResToIcons;
	private List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
	private Map<String, Integer> gif_map = new HashMap<String, Integer>();

	public SmilyParse(Context context) {
		mContext = context;
		mResArrayText = context.getResources().getStringArray(
				Smily.DEFAULT_SMILY_TEXT);
		mResToIcons = buileResToDrawableMap();
		mPattern = buildPattern();
		data = buileMap();
	}

	private HashMap<String, Integer> buileResToDrawableMap() {

		if (mResArrayText.length != Smily.DEFAULT_SMILY_ICONS.length) {
			throw new IllegalStateException("length is Illegal");
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < mResArrayText.length; i++) {
			map.put(mResArrayText[i], Smily.DEFAULT_SMILY_ICONS[i]);
		}
		return map;
	}

	static class Smily {
		public static final int DEFAULT_SMILY_TEXT = R.array.default_smiley_texts;
		private static final int[] DEFAULT_SMILY_ICONS = { R.drawable.p0,
				R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
				R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8,
				R.drawable.p9, R.drawable.p10, R.drawable.p11, R.drawable.p12,
				R.drawable.p13, R.drawable.p14, R.drawable.p15, R.drawable.p16,
				R.drawable.p17, R.drawable.p18, R.drawable.p19, R.drawable.p20,
				R.drawable.p21, R.drawable.p22, R.drawable.p23, R.drawable.p24,
				R.drawable.p25, R.drawable.p26, R.drawable.p27, R.drawable.p28,
				R.drawable.p29, R.drawable.p30, R.drawable.p31, R.drawable.p32,
				R.drawable.p33, R.drawable.p34, R.drawable.p35, R.drawable.p36,
				R.drawable.p37, R.drawable.p38, R.drawable.p39, R.drawable.p40,
				R.drawable.p41, R.drawable.p42, R.drawable.p43, R.drawable.p44,
				R.drawable.p45, R.drawable.p46, R.drawable.p47, R.drawable.p48,
				R.drawable.p49, R.drawable.p50, R.drawable.p51, R.drawable.p52,
				R.drawable.p53, R.drawable.p54, R.drawable.p55, R.drawable.p56,
				R.drawable.p57, R.drawable.p58, R.drawable.p59, R.drawable.p60,
				R.drawable.p61, R.drawable.p62, R.drawable.p63, R.drawable.p64,
				R.drawable.p65, R.drawable.p66, R.drawable.p67, R.drawable.p68,
				R.drawable.p69, R.drawable.p70, R.drawable.p71, R.drawable.p72,
				R.drawable.p73, R.drawable.p74, R.drawable.p75, R.drawable.p76,
				R.drawable.p77, R.drawable.p78, R.drawable.p79, R.drawable.p80,
				R.drawable.p81, R.drawable.p82, R.drawable.p83, R.drawable.p84,
				R.drawable.p85, R.drawable.p86, R.drawable.p87, R.drawable.p88,
				R.drawable.p89, R.drawable.p90,

				R.drawable.d0, R.drawable.d1, R.drawable.d2, R.drawable.d3,
				R.drawable.d4, R.drawable.d5, R.drawable.d6, R.drawable.d7,
				R.drawable.d8, R.drawable.d9, R.drawable.d10, R.drawable.d11,
				R.drawable.d12, R.drawable.d13, R.drawable.d14, R.drawable.d15,
				R.drawable.d16, R.drawable.d17, R.drawable.d18, R.drawable.d19,
				R.drawable.d20, R.drawable.d21, R.drawable.d22, R.drawable.d23,
				R.drawable.d24, R.drawable.d25, R.drawable.d26, R.drawable.d28,
				R.drawable.d29, R.drawable.d30, R.drawable.d31, R.drawable.d32,
				R.drawable.d33, R.drawable.d34, R.drawable.d35, R.drawable.d36,
				R.drawable.d37, R.drawable.d38, R.drawable.d39,

				R.drawable.m0, R.drawable.m1, R.drawable.m2, R.drawable.m3,
				R.drawable.m4, R.drawable.m5, R.drawable.m6, R.drawable.m7,
				R.drawable.m8, R.drawable.m9, R.drawable.m10, R.drawable.m11,
				R.drawable.m12, R.drawable.m13, R.drawable.m14, R.drawable.m15,
				R.drawable.m16, R.drawable.m17, R.drawable.m18, R.drawable.m19,
				R.drawable.m20, R.drawable.m21, R.drawable.m22, R.drawable.m23,
				R.drawable.m24, R.drawable.m25,

				R.drawable.t1, R.drawable.t2, R.drawable.t3, R.drawable.t4,
				R.drawable.t5, R.drawable.t6, R.drawable.t7, R.drawable.t8,
				R.drawable.t9, R.drawable.t10, R.drawable.t11, R.drawable.t12,
				R.drawable.t13, R.drawable.t14, R.drawable.t15, R.drawable.t16,
				R.drawable.t17, R.drawable.t18, R.drawable.t19, R.drawable.t20,
				R.drawable.t21, R.drawable.t22, R.drawable.t23, R.drawable.t24,
				R.drawable.t25, R.drawable.t26, R.drawable.t27, R.drawable.t28,
				R.drawable.t29, R.drawable.t30, R.drawable.t31, R.drawable.t32,
				R.drawable.t33, R.drawable.t34, R.drawable.t35, R.drawable.t36,
				R.drawable.t37, R.drawable.t38,

				R.drawable.bff, R.drawable.bf2, R.drawable.bf3, R.drawable.bf4,
				R.drawable.bf5, R.drawable.bf6, R.drawable.bf7, R.drawable.bf8,
				R.drawable.bf9, R.drawable.bf10, R.drawable.bf11,
				R.drawable.bf12, R.drawable.bf13, R.drawable.bf14,
				R.drawable.bf15, R.drawable.bf16, R.drawable.bf17,
				R.drawable.bf18, R.drawable.bf19, R.drawable.bf20,
				R.drawable.bf21, R.drawable.bf22, R.drawable.bf23,
				R.drawable.bf24, R.drawable.bf25,

				R.drawable.gzt1, R.drawable.gzt2, R.drawable.gzt3,
				R.drawable.gzt4, R.drawable.gzt5, R.drawable.gzt6,
				R.drawable.gzt7, R.drawable.gzt8, R.drawable.gzt9,
				R.drawable.gzt10, R.drawable.gzt11, R.drawable.gzt12,
				R.drawable.gzt13, R.drawable.gzt14, R.drawable.gzt15,
				R.drawable.gzt16, R.drawable.gzt17, R.drawable.gzt18,
				R.drawable.gzt19, R.drawable.gzt20, R.drawable.gzt21,
				R.drawable.gzt22, R.drawable.gzt23, R.drawable.gzt24,
				R.drawable.gzt25,

				R.drawable.meml1, R.drawable.meml2, R.drawable.meml3,
				R.drawable.meml4, R.drawable.meml5, R.drawable.meml6,
				R.drawable.meml7, R.drawable.meml8, R.drawable.meml9,
				R.drawable.meml10, R.drawable.meml11, R.drawable.meml12,
				R.drawable.meml13, R.drawable.meml14, R.drawable.meml15,
				R.drawable.meml16, R.drawable.meml17, R.drawable.meml18,
				R.drawable.meml19, R.drawable.meml20, R.drawable.meml21,
				R.drawable.meml22, R.drawable.meml23,

				R.drawable.pdd1, R.drawable.pdd2, R.drawable.pdd3,
				R.drawable.pdd4, R.drawable.pdd5, R.drawable.pdd6,
				R.drawable.pdd7, R.drawable.pdd8, R.drawable.pdd9,
				R.drawable.pdd10, R.drawable.pdd11, R.drawable.pdd12,
				R.drawable.pdd13, R.drawable.pdd14, R.drawable.pdd15,
				R.drawable.pdd16, R.drawable.pdd17, R.drawable.pdd18,
				R.drawable.pdd19, R.drawable.pdd20, R.drawable.pdd21,
				R.drawable.pdd22, R.drawable.pdd23, R.drawable.pdd24,
				R.drawable.pdd25,

				R.drawable.yct1, R.drawable.yct2, R.drawable.yct3,
				R.drawable.yct4, R.drawable.yct5, R.drawable.yct6,
				R.drawable.yct7, R.drawable.yct8, R.drawable.yct9,
				R.drawable.yct10, R.drawable.yct11, R.drawable.yct12,
				R.drawable.yct13, R.drawable.yct14, R.drawable.yct15,
				R.drawable.yct16, R.drawable.yct17, R.drawable.yct18,
				R.drawable.yct19, R.drawable.yct20, R.drawable.yct21,
				R.drawable.yct22, R.drawable.yct23, R.drawable.yct24,
				R.drawable.yct25,

				R.drawable.xj1, R.drawable.xj2, R.drawable.xj3, R.drawable.xj4,
				R.drawable.xj6, R.drawable.xj7, R.drawable.xj8, R.drawable.xj9,
				R.drawable.xj12, R.drawable.xj13, R.drawable.xj14,
				R.drawable.xj15, R.drawable.xj17, R.drawable.xj19,
				R.drawable.xj21, R.drawable.xj22, R.drawable.xj23,
				R.drawable.xj24, R.drawable.xj25, R.drawable.xj26,
				R.drawable.xj27, R.drawable.xj28, R.drawable.xj29,
				R.drawable.xj30, R.drawable.xj31, R.drawable.xj32,
				R.drawable.xj33, R.drawable.xj35, R.drawable.xj36,
				R.drawable.xj37, R.drawable.xj38, R.drawable.xj39,
				R.drawable.xj41, R.drawable.xj42, R.drawable.xj44,
				R.drawable.xj45, R.drawable.xj46, R.drawable.xj47,
				R.drawable.xj48, R.drawable.xj49, R.drawable.xj51,
				R.drawable.xj52, R.drawable.xj53, R.drawable.xj54,
				R.drawable.xj55, R.drawable.xj56, R.drawable.xj57,
				R.drawable.xj58, R.drawable.xj59,

				R.drawable.qbl2, R.drawable.qbl3, R.drawable.qbl4,
				R.drawable.qbl6, R.drawable.qbl7, R.drawable.qbl8,
				R.drawable.qbl9, R.drawable.qbl10, R.drawable.qbl11,
				R.drawable.qbl12, R.drawable.qbl13, R.drawable.qbl14,
				R.drawable.qbl15, R.drawable.qbl16, R.drawable.qbl17,
				R.drawable.qbl19, R.drawable.qbl20, R.drawable.qbl21,
				R.drawable.qbl22, R.drawable.qbl23, R.drawable.qbl24,
				R.drawable.qbl26, R.drawable.qbl27, R.drawable.qbl28,
				R.drawable.qbl32, R.drawable.qbl33, R.drawable.qbl34,
				R.drawable.qbl35, R.drawable.qbl38, R.drawable.qbl40,
				R.drawable.qbl42, R.drawable.qbl43, R.drawable.qbl46,
				R.drawable.qbl47, R.drawable.qbl48, R.drawable.qbl51,
				R.drawable.qbl52, R.drawable.qbl53, R.drawable.qbl56,
				R.drawable.qbl57, R.drawable.qbl58, R.drawable.qbl60,
				R.drawable.qbl61, R.drawable.qbl63, R.drawable.qbl66,
				R.drawable.qbl68, R.drawable.qbl69, R.drawable.qbl70,
				R.drawable.qbl71, R.drawable.qbl72, R.drawable.qbl73,
				R.drawable.qbl74, R.drawable.qbl76, R.drawable.qbl77,
				R.drawable.qbl78, R.drawable.qbl79, R.drawable.qbl80,
				R.drawable.qbl81, R.drawable.qbl82, R.drawable.qbl83,

		};
	}

	private List<Map<String, ?>> buileMap() {
		List<Map<String, ?>> listMap = new ArrayList<Map<String, ?>>();
		for (int i = 0; i < mResArrayText.length; i++) {
			HashMap<String, Object> entry = new HashMap<String, Object>();
			entry.put("icon", Smily.DEFAULT_SMILY_ICONS[i]);
			entry.put("text", mResArrayText[i]);
			listMap.add(entry);
		}
		return listMap;
	}

	private Pattern buildPattern() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for (int i = 0; i < mResArrayText.length; i++) {
			sb.append(Pattern.quote(mResArrayText[i]));
			sb.append('|');
		}
		sb.replace(sb.length() - 1, sb.length(), ")");
		return Pattern.compile(sb.toString());
	}

	public List<Map<String, ?>> getData() {
		return data;
	}

	public CharSequence compileStringToDisply(String text) {
		SpannableStringBuilder sb = new SpannableStringBuilder(text);
		Matcher m = mPattern.matcher(text);
		while (m.find()) {
			int resId = mResToIcons.get(m.group());
			sb.setSpan(new ImageSpan(mContext, resId), m.start(), m.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return sb;
	}

	public Map<String, Integer> getGif_map() {
		return gif_map;
	}

	public void setGif_map(Map<String, Integer> gif_map) {
		this.gif_map = gif_map;
	}

	public void initGif() {
		gif_map.put("[/狂笑]", R.drawable.p0);
		gif_map.put("[/大笑]", R.drawable.p1);
		gif_map.put("[/惊讶]", R.drawable.p2);
		gif_map.put("[/害羞]", R.drawable.p3);
		gif_map.put("[/偷笑]", R.drawable.p4);
		gif_map.put("[/抓狂]", R.drawable.p5);
		gif_map.put("[/大哭]", R.drawable.p6);
		gif_map.put("[/色]", R.drawable.p7);
		gif_map.put("[/坏笑]", R.drawable.p8);
		gif_map.put("[/发怒]", R.drawable.p9);
		gif_map.put("[/尴尬]", R.drawable.p10);
		gif_map.put("[/阴险]", R.drawable.p11);
		gif_map.put("[/鼓掌]", R.drawable.p12);
		gif_map.put("[/再见]", R.drawable.p13);
		gif_map.put("[/无语]", R.drawable.p14);
		gif_map.put("[/挖鼻]", R.drawable.p15);
		gif_map.put("[/顶]", R.drawable.p16);
		gif_map.put("[/胜利]", R.drawable.p17);
		gif_map.put("[/OK]", R.drawable.p18);
		gif_map.put("[/拜托]", R.drawable.p19);
		gif_map.put("[/囧]", R.drawable.p20);
		gif_map.put("[/淡定]", R.drawable.p21);
		gif_map.put("[/美女]", R.drawable.p22);
		gif_map.put("[/靓仔]", R.drawable.p23);
		gif_map.put("[/神马]", R.drawable.p24);
		gif_map.put("[/开心]", R.drawable.p25);
		gif_map.put("[/给力]", R.drawable.p26);
		gif_map.put("[/飞吻]", R.drawable.p27);
		gif_map.put("[/眨眼]", R.drawable.p28);
		gif_map.put("[/V5]", R.drawable.p29);
		gif_map.put("[/勾引]", R.drawable.p30);
		gif_map.put("[/泪汪汪]", R.drawable.p31);
		gif_map.put("[/骂]", R.drawable.p32);
		gif_map.put("[/炸弹]", R.drawable.p33);
		gif_map.put("[/菜刀]", R.drawable.p34);
		gif_map.put("[/帅]", R.drawable.p35);
		gif_map.put("[/审视]", R.drawable.p36);
		gif_map.put("[/无语]", R.drawable.p37);
		gif_map.put("[/无奈]", R.drawable.p38);
		gif_map.put("[/亲亲]", R.drawable.p39);
		gif_map.put("[/露大腿]", R.drawable.p40);
		gif_map.put("[/呵呵]", R.drawable.p41);
		gif_map.put("[/吐血]", R.drawable.p42);
		gif_map.put("[/媚眼]", R.drawable.p43);
		gif_map.put("[/愁人]", R.drawable.p44);
		gif_map.put("[/肿么了]", R.drawable.p45);
		gif_map.put("[/调戏]", R.drawable.p46);
		gif_map.put("[/抽]", R.drawable.p47);
		gif_map.put("[/哼哼]", R.drawable.p48);
		gif_map.put("[/鄙视]", R.drawable.p49);
		gif_map.put("[/围观]", R.drawable.p50);
		gif_map.put("[/激动]", R.drawable.p51);
		gif_map.put("[/口水]", R.drawable.p52);
		gif_map.put("[/热汗]", R.drawable.p53);
		gif_map.put("[/输了]", R.drawable.p54);
		gif_map.put("[/石化]", R.drawable.p55);
		gif_map.put("[/蔑视]", R.drawable.p56);
		gif_map.put("[/哭]", R.drawable.p57);
		gif_map.put("[/吐了]", R.drawable.p58);
		gif_map.put("[/太委屈]", R.drawable.p59);
		gif_map.put("[/捂脸]", R.drawable.p60);
		gif_map.put("[/捂左脸]", R.drawable.p61);
		gif_map.put("[/亲]", R.drawable.p62);
		gif_map.put("[/吻]", R.drawable.p63);
		gif_map.put("[/傻笑]", R.drawable.p64);
		gif_map.put("[/闭眼]", R.drawable.p65);
		gif_map.put("[/坏坏]", R.drawable.p66);
		gif_map.put("[/跳跳]", R.drawable.p67);
		gif_map.put("[/心碎]", R.drawable.p68);
		gif_map.put("[/红唇]", R.drawable.p69);
		gif_map.put("[/v5]", R.drawable.p70);
		gif_map.put("[/八字胡]", R.drawable.p71);
		gif_map.put("[/变脸]", R.drawable.p72);
		gif_map.put("[/吃货]", R.drawable.p73);
		gif_map.put("[/大笑1]", R.drawable.p74);
		gif_map.put("[/大笑2]", R.drawable.p75);
		gif_map.put("[/孤寂]", R.drawable.p76);
		gif_map.put("[/落叶]", R.drawable.p77);
		gif_map.put("[/哈皮]", R.drawable.p78);
		gif_map.put("[/惊恐]", R.drawable.p79);
		gif_map.put("[/囧1]", R.drawable.p80);
		gif_map.put("[/拉轰]", R.drawable.p81);
		gif_map.put("[/切克闹]", R.drawable.p82);
		gif_map.put("[/惬意]", R.drawable.p83);
		gif_map.put("[/热]", R.drawable.p84);
		gif_map.put("[/太极]", R.drawable.p85);
		gif_map.put("[/委屈]", R.drawable.p86);
		gif_map.put("[/献花]", R.drawable.p87);
		gif_map.put("[/笑]", R.drawable.p88);
		gif_map.put("[/真棒]", R.drawable.p89);
		gif_map.put("[/猪猪]", R.drawable.p90);

		gif_map.put("[/p跳舞]", R.drawable.d0);
		gif_map.put("[/p开火]", R.drawable.d1);
		gif_map.put("[/p眼神]", R.drawable.d2);
		gif_map.put("[/p唱歌]", R.drawable.d3);
		gif_map.put("[/p爱心]", R.drawable.d4);
		gif_map.put("[/p嗯嗯]", R.drawable.d5);
		gif_map.put("[/p叫]", R.drawable.d6);
		gif_map.put("[/p锤地]", R.drawable.d7);
		gif_map.put("[/p街舞]", R.drawable.d8);
		gif_map.put("[/p黑线]", R.drawable.d9);
		gif_map.put("[/p喷血]", R.drawable.d10);
		gif_map.put("[/p滚]", R.drawable.d11);
		gif_map.put("[/p走路]", R.drawable.d12);
		gif_map.put("[/p大笑]", R.drawable.d13);
		gif_map.put("[/p听歌]", R.drawable.d14);
		gif_map.put("[/p面壁]", R.drawable.d15);
		gif_map.put("[/p女神]", R.drawable.d16);
		gif_map.put("[/p鼓掌]", R.drawable.d17);
		gif_map.put("[/p运动]", R.drawable.d18);
		gif_map.put("[/p剪刀]", R.drawable.d19);
		gif_map.put("[/p经过]", R.drawable.d20);
		gif_map.put("[/p晕]", R.drawable.d21);
		gif_map.put("[/p早]", R.drawable.d22);
		gif_map.put("[/p叹气]", R.drawable.d23);
		gif_map.put("[/p吉他]", R.drawable.d24);
		gif_map.put("[/p坏笑]", R.drawable.d25);
		gif_map.put("[/p潜水]", R.drawable.d26);
		gif_map.put("[/p做梦]", R.drawable.d28);
		gif_map.put("[/p弹指]", R.drawable.d29);
		gif_map.put("[/p一指禅]", R.drawable.d30);
		gif_map.put("[/p女生]", R.drawable.d31);
		gif_map.put("[/p闪]", R.drawable.d32);
		gif_map.put("[/p我来了]", R.drawable.d33);
		gif_map.put("[/p无聊]", R.drawable.d34);
		gif_map.put("[/p太极]", R.drawable.d35);
		gif_map.put("[/p捏脸]", R.drawable.d36);
		gif_map.put("[/p梦话]", R.drawable.d37);
		gif_map.put("[/p登场]", R.drawable.d38);
		gif_map.put("[/p哟西]", R.drawable.d39);

		gif_map.put("[/y示爱]", R.drawable.m0);
		gif_map.put("[/y再见]", R.drawable.m1);
		gif_map.put("[/y伤心]", R.drawable.m2);
		gif_map.put("[/y钱]", R.drawable.m3);
		gif_map.put("[/y委屈]", R.drawable.m4);
		gif_map.put("[/y汗]", R.drawable.m5);
		gif_map.put("[/y爱心]", R.drawable.m6);
		gif_map.put("[/y吃货]", R.drawable.m7);
		gif_map.put("[/y黑线]", R.drawable.m8);
		gif_map.put("[/y帅哥]", R.drawable.m9);
		gif_map.put("[/y路过]", R.drawable.m10);
		gif_map.put("[/y不]", R.drawable.m11);
		gif_map.put("[/y不行]", R.drawable.m12);
		gif_map.put("[/y爱情]", R.drawable.m13);
		gif_map.put("[/y流鼻血]", R.drawable.m14);
		gif_map.put("[/y龇牙]", R.drawable.m15);
		gif_map.put("[/y晕]", R.drawable.m16);
		gif_map.put("[/y挖鼻]", R.drawable.m17);
		gif_map.put("[/y亲]", R.drawable.m18);
		gif_map.put("[/y嗨]", R.drawable.m19);
		gif_map.put("[/y人呢]", R.drawable.m20);
		gif_map.put("[/y洗澡]", R.drawable.m21);
		gif_map.put("[/y做坏事]", R.drawable.m22);
		gif_map.put("[/y美女]", R.drawable.m23);
		gif_map.put("[/y蔑视]", R.drawable.m24);
		gif_map.put("[/y大笑]", R.drawable.m25);

		gif_map.put("[/tBYE]", R.drawable.t1);
		gif_map.put("[/t无所谓]", R.drawable.t2);
		gif_map.put("[/t砖头]", R.drawable.t3);
		gif_map.put("[/t听歌]", R.drawable.t4);
		gif_map.put("[/t擦汗]", R.drawable.t5);
		gif_map.put("[/t晕]", R.drawable.t6);
		gif_map.put("[/t宝贝]", R.drawable.t7);
		gif_map.put("[/t运动]", R.drawable.t8);
		gif_map.put("[/t鄙视]", R.drawable.t9);
		gif_map.put("[/t偷吃]", R.drawable.t10);
		gif_map.put("[/t爱心]", R.drawable.t11);
		gif_map.put("[/t流泪]", R.drawable.t12);
		gif_map.put("[/t走你]", R.drawable.t13);
		gif_map.put("[/t瞌睡]", R.drawable.t14);
		gif_map.put("[/t烦死了]", R.drawable.t15);
		gif_map.put("[/t机器人]", R.drawable.t16);
		gif_map.put("[/t自豪]", R.drawable.t17);
		gif_map.put("[/t梦游]", R.drawable.t18);
		gif_map.put("[/t跳舞]", R.drawable.t19);
		gif_map.put("[/t无语]", R.drawable.t20);
		gif_map.put("[/t可爱]", R.drawable.t21);
		gif_map.put("[/t撞墙]", R.drawable.t22);
		gif_map.put("[/t开心]", R.drawable.t23);
		gif_map.put("[/t亲]", R.drawable.t24);
		gif_map.put("[/t烤串]", R.drawable.t25);
		gif_map.put("[/t吃惊]", R.drawable.t26);
		gif_map.put("[/t发疯]", R.drawable.t27);
		gif_map.put("[/t奋斗]", R.drawable.t28);
		gif_map.put("[/t虔诚]", R.drawable.t29);
		gif_map.put("[/t了结你]", R.drawable.t30);
		gif_map.put("[/t生日]", R.drawable.t31);
		gif_map.put("[/t睡觉]", R.drawable.t32);
		gif_map.put("[/t发抖]", R.drawable.t33);
		gif_map.put("[/t施法]", R.drawable.t34);
		gif_map.put("[/t困]", R.drawable.t35);
		gif_map.put("[/t亲亲]", R.drawable.t36);
		gif_map.put("[/t走你1]", R.drawable.t37);
		gif_map.put("[/t挖鼻]", R.drawable.t38);

		gif_map.put("[/f好的]", R.drawable.bff);
		gif_map.put("[/f演讲]", R.drawable.bf2);
		gif_map.put("[/f鬼脸]", R.drawable.bf3);
		gif_map.put("[/f超人]", R.drawable.bf4);
		gif_map.put("[/f口水]", R.drawable.bf5);
		gif_map.put("[/f呲牙]", R.drawable.bf6);
		gif_map.put("[/f打酱油]", R.drawable.bf7);
		gif_map.put("[/f发怒]", R.drawable.bf8);
		gif_map.put("[/f石头剪刀布]", R.drawable.bf9);
		gif_map.put("[/f大吼]", R.drawable.bf10);
		gif_map.put("[/f洒脱]", R.drawable.bf11);
		gif_map.put("[/f思考]", R.drawable.bf12);
		gif_map.put("[/f吐]", R.drawable.bf13);
		gif_map.put("[/f冷]", R.drawable.bf14);
		gif_map.put("[/f流泪]", R.drawable.bf15);
		gif_map.put("[/f头晕]", R.drawable.bf16);
		gif_map.put("[/f鼻血]", R.drawable.bf17);
		gif_map.put("[/f电锯]", R.drawable.bf18);
		gif_map.put("[/f雷劈]", R.drawable.bf19);
		gif_map.put("[/f问好]", R.drawable.bf20);
		gif_map.put("[/f你好]", R.drawable.bf21);
		gif_map.put("[/f鼻涕]", R.drawable.bf22);
		gif_map.put("[/f不知]", R.drawable.bf23);
		gif_map.put("[/f爱心]", R.drawable.bf24);
		gif_map.put("[/f巴掌]", R.drawable.bf25);

		gif_map.put("[/g好的]", R.drawable.gzt1);
		gif_map.put("[/g擦掌]", R.drawable.gzt2);
		gif_map.put("[/g哭]", R.drawable.gzt3);
		gif_map.put("[/g88]", R.drawable.gzt4);
		gif_map.put("[/g笑]", R.drawable.gzt5);
		gif_map.put("[/g打滚]", R.drawable.gzt6);
		gif_map.put("[/g努力]", R.drawable.gzt7);
		gif_map.put("[/g喇叭]", R.drawable.gzt8);
		gif_map.put("[/g报警]", R.drawable.gzt9);
		gif_map.put("[/g鼓掌]", R.drawable.gzt10);
		gif_map.put("[/g鞭打]", R.drawable.gzt11);
		gif_map.put("[/g扇]", R.drawable.gzt12);
		gif_map.put("[/g怕]", R.drawable.gzt13);
		gif_map.put("[/g无语]", R.drawable.gzt14);
		gif_map.put("[/g照镜]", R.drawable.gzt15);
		gif_map.put("[/g亲]", R.drawable.gzt16);
		gif_map.put("[/g零食]", R.drawable.gzt17);
		gif_map.put("[/g抱抱]", R.drawable.gzt18);
		gif_map.put("[/g兔子]", R.drawable.gzt19);
		gif_map.put("[/g坏笑]", R.drawable.gzt20);
		gif_map.put("[/g冷]", R.drawable.gzt21);
		gif_map.put("[/g发怒]", R.drawable.gzt22);
		gif_map.put("[/g飘走]", R.drawable.gzt23);
		gif_map.put("[/g捡钱]", R.drawable.gzt24);
		gif_map.put("[/g舌头]", R.drawable.gzt25);

		gif_map.put("[/m微笑]", R.drawable.meml1);
		gif_map.put("[/m胜利]", R.drawable.meml2);
		gif_map.put("[/m沮丧]", R.drawable.meml3);
		gif_map.put("[/m踩]", R.drawable.meml4);
		gif_map.put("[/m口水]", R.drawable.meml5);
		gif_map.put("[/m胜利2]", R.drawable.meml6);
		gif_map.put("[/m洗衣服]", R.drawable.meml7);
		gif_map.put("[/m拍]", R.drawable.meml8);
		gif_map.put("[/m哭]", R.drawable.meml9);
		gif_map.put("[/m叹气]", R.drawable.meml10);
		gif_map.put("[/m戳]", R.drawable.meml11);
		gif_map.put("[/m坏笑]", R.drawable.meml12);
		gif_map.put("[/m摇摆]", R.drawable.meml13);
		gif_map.put("[/m激光]", R.drawable.meml14);
		gif_map.put("[/m爱心]", R.drawable.meml15);
		gif_map.put("[/m大笑]", R.drawable.meml16);
		gif_map.put("[/m哈哈]", R.drawable.meml17);
		gif_map.put("[/m生气]", R.drawable.meml18);
		gif_map.put("[/m撒娇]", R.drawable.meml19);
		gif_map.put("[/m怒气]", R.drawable.meml20);
		gif_map.put("[/m眨眼]", R.drawable.meml21);
		gif_map.put("[/m悲伤]", R.drawable.meml22);
		gif_map.put("[/m生气2]", R.drawable.meml23);

		gif_map.put("[/d钱]", R.drawable.pdd1);
		gif_map.put("[/d心]", R.drawable.pdd2);
		gif_map.put("[/d不]", R.drawable.pdd3);
		gif_map.put("[/d大吼]", R.drawable.pdd4);
		gif_map.put("[/d激光]", R.drawable.pdd5);
		gif_map.put("[/d坏笑]", R.drawable.pdd6);
		gif_map.put("[/d丑]", R.drawable.pdd7);
		gif_map.put("[/d糊涂]", R.drawable.pdd8);
		gif_map.put("[/d便便]", R.drawable.pdd9);
		gif_map.put("[/d再见]", R.drawable.pdd10);
		gif_map.put("[/d鄙视]", R.drawable.pdd11);
		gif_map.put("[/d撒花]", R.drawable.pdd12);
		gif_map.put("[/d卖萌]", R.drawable.pdd13);
		gif_map.put("[/d来啊]", R.drawable.pdd14);
		gif_map.put("[/d天使]", R.drawable.pdd15);
		gif_map.put("[/d酷]", R.drawable.pdd16);
		gif_map.put("[/d扎]", R.drawable.pdd17);
		gif_map.put("[/d饮料]", R.drawable.pdd18);
		gif_map.put("[/d哭]", R.drawable.pdd19);
		gif_map.put("[/d鼓掌]", R.drawable.pdd20);
		gif_map.put("[/d羞]", R.drawable.pdd21);
		gif_map.put("[/d鬼怪]", R.drawable.pdd22);
		gif_map.put("[/d光明]", R.drawable.pdd23);
		gif_map.put("[/d爱心]", R.drawable.pdd24);
		gif_map.put("[/d哀求]", R.drawable.pdd25);

		gif_map.put("[/c掀桌]", R.drawable.yct1);
		gif_map.put("[/c吐]", R.drawable.yct2);
		gif_map.put("[/c大笑]", R.drawable.yct3);
		gif_map.put("[/c哭]", R.drawable.yct4);
		gif_map.put("[/c不甘]", R.drawable.yct5);
		gif_map.put("[/c棒]", R.drawable.yct6);
		gif_map.put("[/c压力]", R.drawable.yct7);
		gif_map.put("[/c狂吐]", R.drawable.yct8);
		gif_map.put("[/c奔跑]", R.drawable.yct9);
		gif_map.put("[/c刀]", R.drawable.yct10);
		gif_map.put("[/c抽烟]", R.drawable.yct11);
		gif_map.put("[/c热]", R.drawable.yct12);
		gif_map.put("[/c卖萌]", R.drawable.yct13);
		gif_map.put("[/c哀求]", R.drawable.yct14);
		gif_map.put("[/c抠鼻]", R.drawable.yct15);
		gif_map.put("[/c抛眉]", R.drawable.yct16);
		gif_map.put("[/c温泉]", R.drawable.yct17);
		gif_map.put("[/c送花]", R.drawable.yct18);
		gif_map.put("[/c泪奔]", R.drawable.yct19);
		gif_map.put("[/c睡觉]", R.drawable.yct20);
		gif_map.put("[/c冷]", R.drawable.yct21);
		gif_map.put("[/c汗]", R.drawable.yct22);
		gif_map.put("[/c酷]", R.drawable.yct23);
		gif_map.put("[/c黄牌]", R.drawable.yct24);
		gif_map.put("[/c88]", R.drawable.yct25);

		gif_map.put("[/x美腿]", R.drawable.xj1);
		gif_map.put("[/x媚娘]", R.drawable.xj2);
		gif_map.put("[/x中刀]", R.drawable.xj3);
		gif_map.put("[/x枣上好]", R.drawable.xj4);
		gif_map.put("[/x开枪]", R.drawable.xj6);
		gif_map.put("[/x跳]", R.drawable.xj7);
		gif_map.put("[/x扯裤]", R.drawable.xj8);
		gif_map.put("[/x打枪]", R.drawable.xj9);
		gif_map.put("[/x打枪]", R.drawable.xj10);
		gif_map.put("[/x打枪]", R.drawable.xj11);
		gif_map.put("[/x撒钱]", R.drawable.xj12);
		gif_map.put("[/x鼻血]", R.drawable.xj13);
		gif_map.put("[/x拍砖]", R.drawable.xj14);
		gif_map.put("[/x射击]", R.drawable.xj15);
		gif_map.put("[/x射击]", R.drawable.xj16);
		gif_map.put("[/x气功]", R.drawable.xj17);
		gif_map.put("[/x气功]", R.drawable.xj18);
		gif_map.put("[/x肌肉]", R.drawable.xj19);
		gif_map.put("[/x肌肉]", R.drawable.xj20);
		gif_map.put("[/x喝醉]", R.drawable.xj21);
		gif_map.put("[/x唱歌]", R.drawable.xj22);
		gif_map.put("[/x妩媚]", R.drawable.xj23);
		gif_map.put("[/x哭]", R.drawable.xj24);
		gif_map.put("[/x搞基]", R.drawable.xj25);
		gif_map.put("[/x西瓜]", R.drawable.xj26);
		gif_map.put("[/x炫富]", R.drawable.xj27);
		gif_map.put("[/x拔毛]", R.drawable.xj28);
		gif_map.put("[/x掉水里]", R.drawable.xj29);
		gif_map.put("[/x痛苦]", R.drawable.xj30);
		gif_map.put("[/x奥特曼]", R.drawable.xj31);
		gif_map.put("[/xV5]", R.drawable.xj32);
		gif_map.put("[/x苍蝇]", R.drawable.xj33);
		gif_map.put("[/x苍蝇]", R.drawable.xj34);
		gif_map.put("[/x抓2]", R.drawable.xj35);
		gif_map.put("[/x吓人]", R.drawable.xj36);
		gif_map.put("[/x苍蝇2]", R.drawable.xj37);
		gif_map.put("[/x拱前]", R.drawable.xj38);
		gif_map.put("[/x凹凸曼]", R.drawable.xj39);
		gif_map.put("[/x凹凸曼]", R.drawable.xj40);
		gif_map.put("[/x搞基2]", R.drawable.xj41);
		gif_map.put("[/x龟派气功2]", R.drawable.xj42);
		gif_map.put("[/x龟派气功2]", R.drawable.xj43);
		gif_map.put("[/x牛郎织女]", R.drawable.xj44);
		gif_map.put("[/x怕]", R.drawable.xj45);
		gif_map.put("[/x喷火]", R.drawable.xj46);
		gif_map.put("[/x碗上好]", R.drawable.xj47);
		gif_map.put("[/x耍帅]", R.drawable.xj48);
		gif_map.put("[/x多啦A梦]", R.drawable.xj49);
		gif_map.put("[/x多啦A梦]", R.drawable.xj50);
		gif_map.put("[/x球棒]", R.drawable.xj51);
		gif_map.put("[/x恐惧]", R.drawable.xj52);
		gif_map.put("[/x派钱]", R.drawable.xj53);
		gif_map.put("[/x溺水]", R.drawable.xj54);
		gif_map.put("[/x自拍]", R.drawable.xj55);
		gif_map.put("[/x偷笑]", R.drawable.xj56);
		gif_map.put("[/x洗澡]", R.drawable.xj57);
		gif_map.put("[/x弹]", R.drawable.xj58);
		gif_map.put("[/x送花]", R.drawable.xj59);

		gif_map.put("[/q抛眉]", R.drawable.qbl1);
		gif_map.put("[/q抛眉]", R.drawable.qbl2);
		gif_map.put("[/q扇扇]", R.drawable.qbl3);
		gif_map.put("[/q不知]", R.drawable.qbl4);
		gif_map.put("[/q不知]", R.drawable.qbl5);
		gif_map.put("[/q化石]", R.drawable.qbl6);
		gif_map.put("[/q乱跳]", R.drawable.qbl7);
		gif_map.put("[/q飞吻]", R.drawable.qbl8);
		gif_map.put("[/q不耐烦]", R.drawable.qbl9);
		gif_map.put("[/q烧香]", R.drawable.qbl10);
		gif_map.put("[/q害羞]", R.drawable.qbl11);
		gif_map.put("[/q很棒]", R.drawable.qbl12);
		gif_map.put("[/q哈欠]", R.drawable.qbl13);
		gif_map.put("[/q饱]", R.drawable.qbl14);
		gif_map.put("[/q叹气]", R.drawable.qbl15);
		gif_map.put("[/q发烧]", R.drawable.qbl16);
		gif_map.put("[/q饿]", R.drawable.qbl17);
		gif_map.put("[/q饿]", R.drawable.qbl18);
		gif_map.put("[/q晚安]", R.drawable.qbl19);
		gif_map.put("[/q偷笑]", R.drawable.qbl20);
		gif_map.put("[/q生日快乐]", R.drawable.qbl21);
		gif_map.put("[/q叉腰]", R.drawable.qbl22);
		gif_map.put("[/q嚎叫]", R.drawable.qbl23);
		gif_map.put("[/q变身]", R.drawable.qbl24);
		gif_map.put("[/q变身]", R.drawable.qbl25);
		gif_map.put("[/q擦泪]", R.drawable.qbl26);
		gif_map.put("[/q头痛]", R.drawable.qbl27);
		gif_map.put("[/qyes]", R.drawable.qbl28);
		gif_map.put("[/qyes]", R.drawable.qbl29);
		gif_map.put("[/qyes]", R.drawable.qbl30);
		gif_map.put("[/qyes]", R.drawable.qbl31);
		gif_map.put("[/q泪奔]", R.drawable.qbl32);
		gif_map.put("[/q啦啦]", R.drawable.qbl33);
		gif_map.put("[/q洗刷刷]", R.drawable.qbl34);
		gif_map.put("[/q骑马舞]", R.drawable.qbl35);
		gif_map.put("[/q骑马舞]", R.drawable.qbl36);
		gif_map.put("[/q骑马舞]", R.drawable.qbl37);
		gif_map.put("[/q没眼看]", R.drawable.qbl38);
		gif_map.put("[/q没眼看]", R.drawable.qbl39);
		gif_map.put("[/qNo]", R.drawable.qbl40);
		gif_map.put("[/qNo]", R.drawable.qbl41);
		gif_map.put("[/q拜拜]", R.drawable.qbl42);
		gif_map.put("[/q哭泣]", R.drawable.qbl43);
		gif_map.put("[/q哭泣]", R.drawable.qbl44);
		gif_map.put("[/q哭泣]", R.drawable.qbl45);
		gif_map.put("[/q失望]", R.drawable.qbl46);
		gif_map.put("[/q吐舌]", R.drawable.qbl47);
		gif_map.put("[/q摸头]", R.drawable.qbl48);
		gif_map.put("[/q摸头]", R.drawable.qbl49);
		gif_map.put("[/q摸头]", R.drawable.qbl50);
		gif_map.put("[/q远望]", R.drawable.qbl51);
		gif_map.put("[/q耶]", R.drawable.qbl52);
		gif_map.put("[/q飘过]", R.drawable.qbl53);
		gif_map.put("[/q飘过]", R.drawable.qbl54);
		gif_map.put("[/q飘过]", R.drawable.qbl55);
		gif_map.put("[/q打头]", R.drawable.qbl56);
		gif_map.put("[/q抱抱]", R.drawable.qbl57);
		gif_map.put("[/q悠闲]", R.drawable.qbl58);
		gif_map.put("[/q悠闲]", R.drawable.qbl59);
		gif_map.put("[/q减肥]", R.drawable.qbl60);
		gif_map.put("[/q嚎哭]", R.drawable.qbl61);
		gif_map.put("[/q嚎哭]", R.drawable.qbl62);
		gif_map.put("[/q送花]", R.drawable.qbl63);
		gif_map.put("[/q送花]", R.drawable.qbl64);
		gif_map.put("[/q送花]", R.drawable.qbl65);
		gif_map.put("[/q弹头]", R.drawable.qbl66);
		gif_map.put("[/q耻笑]", R.drawable.qbl67);
		gif_map.put("[/q耻笑]", R.drawable.qbl68);
		gif_map.put("[/q揉捏]", R.drawable.qbl69);
		gif_map.put("[/q拍手]", R.drawable.qbl70);
		gif_map.put("[/q飘走]", R.drawable.qbl71);
		gif_map.put("[/q口水]", R.drawable.qbl72);
		gif_map.put("[/q舔]", R.drawable.qbl73);
		gif_map.put("[/q呼啦圈]", R.drawable.qbl74);
		gif_map.put("[/q呼啦圈]", R.drawable.qbl75);
		gif_map.put("[/q捏]", R.drawable.qbl76);
		gif_map.put("[/q思考]", R.drawable.qbl77);
		gif_map.put("[/q走路1]", R.drawable.qbl78);
		gif_map.put("[/q写作业]", R.drawable.qbl79);
		gif_map.put("[/q噎住]", R.drawable.qbl80);
		gif_map.put("[/qHi]", R.drawable.qbl81);
		gif_map.put("[/q闻]", R.drawable.qbl82);
		gif_map.put("[/q敬礼]", R.drawable.qbl83);

	}
}
