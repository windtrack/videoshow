package com.zhipu.chinavideo.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.entity.ActivityMsg;
import com.zhipu.chinavideo.entity.ChatMessage;

public class ParseMessage {
	private ActivityMsg msg;
	private Context context;
	private ChatMessage message;
	private ChatMessage primessage;
	private PagerSlidingTabStrip indicator;
	private String anchor_name;
	private SharedPreferences sharedPreferences;

	public ParseMessage(ActivityMsg msg, PagerSlidingTabStrip indicator,
			Context context, String anchor_name) {
		super();
		this.msg = msg;
		this.context = context;
		this.indicator = indicator;
		this.anchor_name = anchor_name;
		sharedPreferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
	}

	public ParseMessage() {
		super();
	}

	public ChatMessage getMessage() {
		message = new ChatMessage();
		primessage = new ChatMessage();
		if (Utils.isEmpty(msg.getMsg())) {
			return null;
		}
		String chat = msg.getMsg();
		try {
			JSONObject chat_object = new JSONObject(chat);
			// 公聊
			if (msg.getTid() == 2) {
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				String tname = Utils.uncodeParser(chat_object
						.getString("tname"));
				String tuid = chat_object.getString("tuid");
				String suid = chat_object.getString("suid");
				String s_stealth = chat_object.getString("s_stealth");
				String t_stealth = chat_object.getString("t_stealth");
				if (s_stealth.equals("1")) {
					sname = "神秘人";
				}
				if (t_stealth.equals("1")) {
					tname = "神秘人";
				}
				String text = Utils.uncodeParser(chat_object.getString("text"));
				String nickname = sharedPreferences.getString(APP.NICKNAME, "");
				if (tuid.length() < 2) {
					message.setSay(sname + " 说:");
				} else {
					message.setSay(sname + " 对 " + tname + " 说:");
				}

				// 判断是否是守护
				if (chat_object.has("g_level") && chat_object.has("g_type")
						&& !Utils.isEmpty(chat_object.getString("g_level"))
						&& !Utils.isEmpty(chat_object.getString("g_type"))
						&& Utils.isInteger(chat_object.getString("g_level"))
						&& Utils.isInteger(chat_object.getString("g_type"))) {
					int g_level = Integer.parseInt(chat_object
							.getString("g_level"));
					int g_type = Integer.parseInt(chat_object
							.getString("g_type"));
					// 年守护(名字和说话内容都是红色)
					if (g_level > 8) {
						message.setIs_shouhu("1");
					} else {
						// 银守护
						if (g_type == 1) {
							message.setIs_shouhu("2");
						} else if (g_type == 2) {
							message.setIs_shouhu("1");
							// 金守护
						} else {

						}
					}
				}
				message.setS_id(suid);
				message.setT_id(tuid);
				message.setTname(tname);
				message.setContent(text);
				message.setSname(sname);
				message.setTid(2);
				return message;
			} else if (msg.getTid() == 1) {
				// 私聊
				String nickname = sharedPreferences.getString(APP.NICKNAME, "");
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				String tname = Utils.uncodeParser(chat_object
						.getString("tname"));
				String tuid = chat_object.getString("tuid");
				String suid = chat_object.getString("suid");
				String text = Utils.uncodeParser(chat_object.getString("text"));
				String s_stealth = chat_object.getString("s_stealth");
				String t_stealth = chat_object.getString("t_stealth");

				if (s_stealth.equals("1")) {
					sname = "神秘人";
				}
				if (t_stealth.equals("1")) {
					tname = "神秘人";
				}
				// 判断是否是守护
				if (chat_object.has("g_level") && chat_object.has("g_type")
						&& !Utils.isEmpty(chat_object.getString("g_level"))
						&& !Utils.isEmpty(chat_object.getString("g_type"))
						&& Utils.isInteger(chat_object.getString("g_level"))
						&& Utils.isInteger(chat_object.getString("g_type"))) {
					int g_level = Integer.parseInt(chat_object
							.getString("g_level"));
					int g_type = Integer.parseInt(chat_object
							.getString("g_type"));
					// 年守护(名字和说话内容都是红色)
					if (g_level > 8) {
						message.setIs_shouhu("1");
					} else {
						// 银守护
						if (g_type == 1) {
							message.setIs_shouhu("2");
						} else if (g_type == 2) {
							message.setIs_shouhu("1");
							// 金守护
						} else {

						}
					}
				}
				primessage.setSay(sname + " 对 " + tname + " 说:");
				primessage.setContent(text);
				primessage.setSname(sname);
				primessage.setS_id(suid);
				primessage.setTname(tname);
				primessage.setT_id(tuid);
				primessage.setTid(2);
				return primessage;
			} else if (msg.getTid() == 7) {
				// PubChatFragment.StartGiftAnimation("中视娱乐", "5555", "icon");
				// 上线
				int onlinenum = chat_object.getInt("onlinenum");
				String user_type = chat_object.getString("user_type");
				String suid = chat_object.getString("suid");
				message.setUser_type(user_type);
				if (onlinenum <= 0) {
					onlinenum = 1;
				}
				indicator.getTextView(2).setText("观众(" + onlinenum + ")");
				String mountlist = "";
				if (chat_object.has("mountlist")) {
					mountlist = chat_object.getString("mountlist");
				}
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				String is_stealth = "0";
				if (chat_object.has("is_stealth")) {
					is_stealth = chat_object.getString("is_stealth");
					// Log.i("lvjian", "s_stealth----------->" + is_stealth);
					if (is_stealth.equals("1")) {
						sname = "神秘人";
					}
				}

				// 判断是否是守护
				if (chat_object.has("g_level") && chat_object.has("g_type")
						&& !Utils.isEmpty(chat_object.getString("g_level"))
						&& !Utils.isEmpty(chat_object.getString("g_type"))
						&& Utils.isInteger(chat_object.getString("g_level"))
						&& Utils.isInteger(chat_object.getString("g_type"))) {
					int g_level = Integer.parseInt(chat_object
							.getString("g_level"));
					int g_type = Integer.parseInt(chat_object
							.getString("g_type"));
					// 年守护(名字和说话内容都是红色)
					if (g_level > 8) {
						message.setIs_shouhu("1");
					} else {
						// 银守护
						if (g_type == 1) {
							message.setIs_shouhu("2");
						} else if (g_type == 2) {
							message.setIs_shouhu("1");
							// 金守护
						} else {

						}
					}
				}
				// 无座驾
				if (Utils.isEmpty(mountlist)) {
					message.setSay("欢迎  " + sname + " 进入直播间");
					message.setSname(sname);
					message.setTid(7);
				} else {
					// 有座驾
					String mountinfo[] = mountlist.split(",");
					// &&!"1".equals(is_stealth)
					if (mountinfo.length > 5 && !"1".equals(is_stealth)) {
						message.setSay(sname + " " + mountinfo[5] + " "
								+ mountinfo[1] + "进入直播间");
						message.setSname(sname);
						message.setTname(mountinfo[5]);
						message.setContent(mountinfo[1]);
						message.setIcon(mountinfo[2]);
						message.setTid(8);
					} else {
						message.setSay("欢迎  " + sname + " 进入直播间");
						message.setSname(sname);
						message.setTid(7);
					}
				}
				message.setS_id(suid);
				return message;
			} else if (msg.getTid() == 3) {
				// 礼物
				String itemid = Utils.uncodeParser(chat_object
						.getString("itemid"));
				// 判断是否是守护
				if (chat_object.has("g_level") && chat_object.has("g_type")
						&& !Utils.isEmpty(chat_object.getString("g_level"))
						&& !Utils.isEmpty(chat_object.getString("g_type"))
						&& Utils.isInteger(chat_object.getString("g_level"))
						&& Utils.isInteger(chat_object.getString("g_type"))) {
					int g_level = Integer.parseInt(chat_object
							.getString("g_level"));
					int g_type = Integer.parseInt(chat_object
							.getString("g_type"));
					// 年守护(名字和说话内容都是红色)
					if (g_level > 8) {
						message.setIs_shouhu("1");
					} else {
						// 银守护
						if (g_type == 1) {
							message.setIs_shouhu("2");
						} else if (g_type == 2) {
							message.setIs_shouhu("1");
							// 金守护
						} else {

						}
					}
				}
				// 红豆
				if ("100000".equals(itemid)) {
					String sname = Utils.uncodeParser(chat_object
							.getString("sname"));
					if (chat_object.has("s_stealth")) {
						String steal = chat_object.getString("s_stealth");
						if (steal.equals("1")) {
							sname = "神秘人";
						}
					}
					message.setSname(sname);
					message.setTid(3);
				} else {
					// 礼物
					String sname = Utils.uncodeParser(chat_object
							.getString("sname"));
					if (chat_object.has("s_stealth")) {
						String steal = chat_object.getString("s_stealth");
						if (steal.equals("1")) {
							sname = "神秘人";
						}
					}
					String itemnum = chat_object.getString("num_t");
					String itemname = chat_object.getString("itemname");
					int num_gaga = chat_object.getInt("itemnum");
					String icon = chat_object.getString("icon");
					String price = chat_object.getString("price");
					message.setIcon(icon);
					message.setSname(sname);
					message.setContent(price);
					message.setTid(33);
					message.setSay(itemnum);
					// 礼物的价格大于100000或者礼物数量大于50个显示
					// if (!Utils.isEmpty(price)) {
					// if (Integer.parseInt(price) >= 100000
					// || Integer.parseInt(itemnum) >= 0) {
					// PubChatFragment.StartGiftAnimation(sname, itemnum,
					// icon,num_gaga);
					// }
					// }
					// 礼物是幸运礼物，并且中奖
					int reward_type = chat_object.getInt("reward_type");
					int total_beans = 0;
					if (chat_object.has("total_beans")) {
						total_beans = chat_object.getInt("total_beans");
					}
					int gifttimes = 0;
					if (reward_type == 1 && total_beans != 0) {
						int one = chat_object.getInt("one");
						int two = chat_object.getInt("two");
						int five = chat_object.getInt("five");
						int ten = chat_object.getInt("ten");
						int fifty = chat_object.getInt("fifty");
						int hd = chat_object.getInt("hd");
						int fh = chat_object.getInt("fh");
						if (one != 0) {
							gifttimes = 1;
						} else if (two != 0) {
							gifttimes = 2;
						} else if (five != 0) {
							gifttimes = 5;
						} else if (ten != 0) {
							gifttimes = 10;
						} else if (fifty != 0) {
							gifttimes = 50;
						} else if (hd != 0) {
							gifttimes = 100;
						} else if (fh != 0) {
							gifttimes = 500;
						}
						String text = sname + "送" + itemname + " 人品大爆发获得"
								+ gifttimes + "倍奖励，共" + total_beans + "乐币";
						message.setTname(text);
					}
				}
				message.setGift_id(itemid);
				return message;
			} else if (msg.getTid() == 4) {
				// 踢人
				int type = chat_object.getInt("type");
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				String tname = Utils.uncodeParser(chat_object
						.getString("tname"));
				String tuid = chat_object.getString("tuid");
				message.setSname(sname);
				if (type == 0) {
					message.setSay(tname + " 被 " + sname + " 取消了踢出房间");
					if (!Utils.isEmpty(tuid)
							&& tuid.equals(sharedPreferences.getString(
									APP.USER_ID, ""))) {
					}
				} else {
					message.setSay(tname + " 被 " + sname + " 踢出房间1小时");
					if (!Utils.isEmpty(tuid)
							&& tuid.equals(sharedPreferences.getString(
									APP.USER_ID, ""))) {
						message.setContent("踢");
					}
				}
				message.setTid(7);
				return message;
			} else if (msg.getTid() == 5) {
				// 禁言或解禁
				int type = chat_object.getInt("type");
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				String tname = Utils.uncodeParser(chat_object
						.getString("tname"));
				String tuid = chat_object.getString("tuid");

				if (type == 0) {
					message.setSay(tname + " 被 " + sname + " 解禁了");
					if (!Utils.isEmpty(tuid)
							&& tuid.equals(sharedPreferences.getString(
									APP.USER_ID, ""))) {
						LiveRoomActivity.is_shutup = true;
					}
				} else {
					if (!Utils.isEmpty(tuid)
							&& tuid.equals(sharedPreferences.getString(
									APP.USER_ID, ""))) {
						LiveRoomActivity.is_shutup = false;
					}
					message.setSay(tname + " 被 " + sname + " 禁言了,还剩5分钟");
				}
				message.setSname(sname);
				message.setTid(7);
				return message;
			} else if (msg.getTid() == 11) {
				// 恭喜升级通知
				String tname = Utils.uncodeParser(chat_object
						.getString("tname"));
				String tuid = chat_object.getString("tuid");
				String type = chat_object.getString("type");
				int lev = chat_object.getInt("lev");
				if ("star".equals(type)) {
					message.setSay("恭喜 " + tname + " 明星等级升到 " + lev + " 级");
				} else {
					message.setSay("恭喜 " + tname + " 财富等级升到 " + lev + " 级");
				}
				message.setTid(42);
				return message;
			} else if (msg.getTid() == 13) {
				// 设置管理或者取消管理
				String tname = Utils.uncodeParser(chat_object
						.getString("tname"));
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				String tuid = chat_object.getString("tuid");
				String suid = chat_object.getString("suid");
				int type = chat_object.getInt("type");
				if (type == 0) {
					message.setSay(tname + " 被 " + sname + "　取消了管理权限！");
				} else {
					message.setSay(tname + " 被 " + sname + "　提升为房间管理！");
				}
				message.setTid(42);
				return message;
				// 抽卡牌中奖消息
			}
			// else if (msg.getTid() == 230) {
			// String sname = Utils.uncodeParser(chat_object
			// .getString("sname"));
			// JSONObject gift = chat_object.getJSONObject("gift");
			// String name = Utils.uncodeParser(gift.getString("name"));
			// String icon = chat_object.getString("icon");
			// message.setSay("恭喜 " + sname + " 在魔幻卡牌游戏中抽取1个" + name);
			// message.setTid(230);
			// message.setTname(icon);
			// return message;
			// }
			else if (msg.getTid() == 50) {
				// Log.i("lvjian", "m----50---------------->" + msg.getMsg());
				// 系统消息
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				JSONObject gift = chat_object.getJSONObject("gift");
				String name = Utils.uncodeParser(gift.getString("name"));
				String icon = chat_object.getString("icon");
				message.setSay("系统公告：" + sname + " 在魔幻卡牌里获得" + name);
				message.setTid(42);
				message.setTname(icon);
				return message;
			} else if (msg.getTid() == 27) {
				// 上播
			} else if (msg.getTid() == 28) {
				// 下播
			} else if (msg.getTid() == 29) {
				// 骰子游戏
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				String tname = Utils.uncodeParser(chat_object
						.getString("tname"));
				message.setSay(sname + " 对 " + tname + " :");
				int point = chat_object.getInt("point");
				message.setContent(point + "");
				message.setSname(sname);
				message.setTname(tname);
				message.setTid(29);
				return message;
			} else if (msg.getTid() == 30) {
				// 猜拳游戏
				String sname = Utils.uncodeParser(chat_object
						.getString("sname"));
				String tname = Utils.uncodeParser(chat_object
						.getString("tname"));
				message.setSay(sname + " 对 " + tname + " :");
				int point = chat_object.getInt("point");
				message.setContent(point + "");
				message.setSname(sname);
				message.setTname(tname);
				message.setTid(30);
				return message;
			} else if (msg.getTid() == 34) {
				String text = Utils.uncodeParser(chat_object.getString("text"));
				message.setSay(text);
				message.setTid(42);
				return message;
				// 系统通知
			} else if (msg.getTid() == 42) {
				// 后台管理发送消息
				String text = chat_object.getString("text");
				message.setSay(text);
				message.setTid(42);
				return message;
			} else if (msg.getTid() == 145) {
				JSONArray text = chat_object.getJSONArray("text");
				String t = "捷报:在刚刚结束的财神游戏中乐币获得，";
				for (int i = 0; i < text.length(); i++) {
					t = t
							+"第"+(i+1)+"名:"+ Utils.uncodeParser(text.getJSONObject(i)
									.getString("name")) + " "
							+ text.getJSONObject(i).getInt("money")+"乐币;";
				}
				message.setTname(t);
				message.setTid(33);
				return message;

			} else if (msg.getTid() == 36) {
				String sname = chat_object.getString("sname");
				String text = chat_object.getString("text");
				String t_name = chat_object.getString("anchor_name");
				message.setTname(t_name);
				message.setSname(sname);
				message.setContent(text);
				return message;
			}else if(msg.getTid()==14){
				message.setSay(chat_object.getString("app_text"));
				message.setTid(14);
				String tuid = chat_object.getString("tuid");
				message.setT_id(tuid);
				return message;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
