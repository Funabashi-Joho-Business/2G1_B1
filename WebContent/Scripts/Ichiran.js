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
		output.innerHTML += "<br>";
		for(var index in datas)
		{
			var data = datas[index];
			var div = document.createElement("div");
			div.id = data.id;
			div.innerHTML = data.title;
			output.appendChild(div);
			div.addEventListener("click",onClick);
			output.appendChild(document.createElement("hr"));
		}
	}

		//データ受信処理
		function onRecv2(data)
		{
			output.innerHTML = "<br>";
			output.innerHTML += data.title;
			output.innerHTML += "<br>";
			output.innerHTML += "<hr>";
			output.innerHTML += "<br>";
			output.innerHTML += data.news;
		}



	//ボタンクリック時の送信処理
	function onClick()
	{
		//データ送信
		//データ受信要求
		var sendData = {"cmd":"read2"};
		sendData.ichiranRecv = {};
		sendData.ichiranRecv.id = this.id;
		AFL.sendJson("Ajax10",sendData,onRecv2);
	}
	//データ受信要求
	var sendData = {"cmd":"read"};
	AFL.sendJson("Ajax10",sendData,onRecv);
}






