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
			output.innerHTML = AFL.sprintf("<h2>%s</h2><br>%s",data.title,data.news)
		}
	}


	//データ受信要求
	var kijiSend = {"cmd":"read"};
	AFL.sendJson("Ajax10",kijiSend,onRecv);
}
