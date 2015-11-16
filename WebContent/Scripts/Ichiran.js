//ページ読み込みイベントに登録
document.addEventListener("DOMContentLoaded", Main, false);

function Main()
{
	//セレクターで各要素のインスタンスを取得
	var output = document.querySelector("div.news");

	//データ受信処理
	function onRecv(datas)
	{
		//内容のクリア
		output.innerHTML = "";
		for(var index in datas)
		{
			var data = datas[index];
			var div = document.createElement("div");
			div.id = data.id;
			div.innerHTML = data.title;
			output.appendChild(div);
			div.addEventListener("click",onClick);
			output.appendChild(document.createElement("hr"));

			//output.innerHTML = AFL.sprintf("<span id='%d' onclick='onClick(this)'>%s</span><hr><br>",data.id,data.title) + output.innerHTML;

		}

	}
	//ボタンクリック時の送信処理
	function onClick()
	{
		//データ送信
		var ichiranRecv = {};
		ichiranRecv.id = this.id;
		AFL.sendJson("Ajax10",ichiranRecv,onRecv);
	}
	//データ受信要求
	var ichiranSend = {"cmd":"read"};
	AFL.sendJson("Ajax10",ichiranSend,onRecv);
}






