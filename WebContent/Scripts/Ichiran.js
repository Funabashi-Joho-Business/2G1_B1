//ページ読み込みイベントに登録
document.addEventListener("DOMContentLoaded", Main, false);

function Main()
{
//セレクターで各要素のインスタンスを取得

//データ受信処理
function onRecv(datas)
{
	//内容のクリア
	output.innerHTML = "";
	for(var index in datas)
	{
		var data = datas[index];
		output.innerHTML = "<a href=\"Kiji.html\">"
		output.innerHTML += AFL.sprintf("[%d]%s",data.id,data.title)
		output.innerHTML += "</a><hr><br>"
	}
}
//ボタンクリック時の送信処理
function onClick()
{
	//データ送信
	var sendData = {};
	sendData.cmd = "write";
	sendData.name = data1.value;
	sendData.msg = data2.value;
	AFL.sendJson("Ajax10",sendData,onRecv);
}


//データ受信要求
var sendData = {"cmd":"read"};
AFL.sendJson("Ajax10",sendData,onRecv);
}
