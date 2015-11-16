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
			var div1 = document.createElement("div");
			var div2 = document.createElement("div");
			div1.innerHTML = data.title;
			div2.innerHTML = data.news;
			output.appendChild(div1);
			output.appendChild(document.createElement("br"));
			output.appendChild(div2);

			//output.innerHTML = AFL.sprintf("<span id='%d' onclick='onClick(this)'>%s</span><hr><br>",data.id,data.title) + output.innerHTML;

		}

	}

	//データ受信要求
	var kijiSend = {"cmd":"read"};
	AFL.sendJson("Ajax10",kijiSend,onRecv);
}
