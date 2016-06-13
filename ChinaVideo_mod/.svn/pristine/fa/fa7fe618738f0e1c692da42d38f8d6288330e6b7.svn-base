/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.zhipu.chinavideo.alipayutils;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088801901361184";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "pay@0058.com";

	//商户私钥，自助生成
	public static final String RSA_PRIVATE ="MIICXAIBAAKBgQCog0K9IYFiTbTb0hr2Y0wCv5A7jZkbz/cRAtMNBR3ITe0eKF8RJbcv6L5ABCtYo2FLufzbS4Ak3EF8gcT+kpPh/35S6EXUvnklVIPKQfklSLQqZ1SAZ/zhPbjRjOYq81pfzUWy7iLYj8ndW/tdPSc9sRo0PNEGMDi5Louo6t5hlwIDAQABAoGAEl0rmrzaAsKBISuGcK9liy8hIrxNoTBKtnneUh7qnfeQDadMpOu9SOVORl+t8Zzsb3o3ShayQlIWeOETfMKwhtUw9EJn1qywxxg4jPtGl3skQ+g9tjtGE4TakT/5gSTyXXHBM8UGq8+JIxQyMEsE+lAf2Sxd2L02HK/ebgwK9cECQQDSjBSyXbNMdWvvZa7rUwO860HDMlnXGRffQl+tWfUsf31DdPUuOFzfO/stmJjlxUsN269L7zffpf/KryBoKSUHAkEAzOQj2iRPc1MXV8vWPKQrFxW4qc96DEE9eKA8TWtWKfI9yywW7A9xygqZg0qXIlvzK/LXYAjWKZ2f3p++i/3K8QJAN9/U3kDfhDrlGfLyKKEvLMOCADssfFi8bRNAMNDer6BlrbEsH8f1XYidb8w/RzrEYDkcnBovybpT14YCI6JHoQJBAJ2mJJfiPGel1V4ubuc2Ahm/EtlF1Mi9IglAcWW4YxHfXACKKhD0ST+GLFxC9krvqQl1zpYL9o0KxUnMbBbW7mECQCxGe2ayzBDw+LjsxKTkc1GPObseP1MdOS4dRp2VSok/km4Nu/WCVrcqJjT5diM+ryBVGF4Kb4R5Bvk6+3JehiA=";
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCog0K9IYFiTbTb0hr2Y0wCv5A7jZkbz/cRAtMNBR3ITe0eKF8RJbcv6L5ABCtYo2FLufzbS4Ak3EF8gcT+kpPh/35S6EXUvnklVIPKQfklSLQqZ1SAZ/zhPbjRjOYq81pfzUWy7iLYj8ndW/tdPSc9sRo0PNEGMDi5Louo6t5hlwIDAQAB";

}
