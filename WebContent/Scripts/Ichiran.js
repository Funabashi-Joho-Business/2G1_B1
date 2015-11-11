//ページ読み込みイベントに登録
document.addEventListener("DOMContentLoaded", Main, false);

function Main()
{
	//セレクターで各要素のインスタンスを取得
	var output = document.querySelector("div#output");

	//データ受信処理
	function onRecv(datas)
	{
		//内容のクリア
		output.innerHTML = "";
		for(var index in datas)
		{
			var data = datas[index];
			output.innerHTML += AFL.sprintf("<a href=\"Kiji.html\">[%d]%s</a><hr><br>",data.id,data.title) + output.innerHTML;
		}
	}
	//データ受信要求
	var ichiranSend = {"cmd":"read"};
	AFL.sendJson("Ajax10",ichiranSend,onRecv);

	//ボタンクリック時の送信処理
	function onClick()
	{
		//データ送信
		var ichiranRecv = {};
		ichiranRecv.name = output.id;
		ichiranRecv.msg = output.title;
		AFL.sendJson("Ajax10",ichiranRecv,onRecv);
	}
}

