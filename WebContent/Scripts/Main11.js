﻿//ページ読み込みイベントに登録
document.addEventListener("DOMContentLoaded", Main, false);

// ログイン処理
// userName ユーザ名
// userPass パスワード
// func 認証完了通知を行う関数
function login(userName, userPass, func) {
	// 認証要求の戻り値を受け取る
	function onData(value) {
		var code = value["code"];
		if (code == 1) {
			// ログイン成功
			AFL.setCookie("USER_NAME", userName);
			AFL.setCookie("USER_PASS", userPass);
			AFL.setCookie("USER_GROUP", value["group"]);
		}

		if (func) {
			func(code);
		}
	}
	if (userName != null) {
		// ログイン時、認証要求
		var data = {};
		data["UserName"] = userName;
		data["UserPass"] = userPass;
		// サーバに接続
		AFL.sendJson("AuthTest", data, onData);
	} else {
		// ログアウト処理
		AFL.setCookie("USER_NAME", null);
		AFL.setCookie("USER_PASS", null);
		AFL.setCookie("USER_GROUP", null);
	}

}

function start() {
	// -------------------------------------
	// 入力領域の作成
	var group = document.createElement("div");
	document.body.appendChild(group);

	var inputUser = document.createElement("input");
	group.appendChild(inputUser);
	var inputPass = document.createElement("input");
	group.appendChild(inputPass);
	inputPass.type = "password";
	var buttonLogin = document.createElement("input");
	group.appendChild(buttonLogin);
	buttonLogin.type = "button";
	buttonLogin.value = "ログイン";
	var buttonLogout = document.createElement("input");
	group.appendChild(buttonLogout);
	buttonLogout.type = "button";
	buttonLogout.value = "ログアウト";

	// ボタンが押された場合の処理
	buttonLogin.onclick = function() {
		login(inputUser.value, inputPass.value, onLogin);
	}
	buttonLogout.onclick = function() {
		login(); // 引数無しでログアウト
		msg.innerHTML = "ログアウト";
	}
	// -------------------------------------

	// メッセージ出力用
	var msg = document.createElement("div");
	document.body.appendChild(msg);

	// -------------------------------------
	// クッキーが設定されていたらログインを試す(前回のログインの復元)
	if (AFL.getCookie("USER_NAME")) {
		login(AFL.getCookie("USER_NAME"), AFL.getCookie("USER_PASS"), onLogin);
	}
	// -------------------------------------

	// -------------------------------------
	// ログイン処理後
	// この中に認証判別後のメイン処理を書く
	function onLogin(code) {
		if (code == 0)
			msg.innerHTML = "エラー";
		else
			msg.innerHTML =
				AFL.sprintf("USER:%s GROUP:%s",
						AFL.getCookie("USER_NAME"),
						AFL.getCookie("USER_GROUP"));
	}
	// -------------------------------------
}

function Main() {
	// タイトルの設定
	document.title = "サンプル11";
	start();

}
