(function() {
  let results = [];
  document.querySelectorAll("div.b_algoheader").forEach(result => {
    results.push({
      text: result.querySelector("h2").querySelectorAll("a")[1].innerHTML
    });
  });
  return { results };
})();

// HITOKOTO

fetch("https://v1.hitokoto.cn").then((response) => {
    return response.json();
}).then((json) => {
	return {
		iconUrl: 'https://hitokoto.cn/favicon.ico',
		title: 'Hitokoto',
		imgUrl: 'https://piccdn.freejishu.com/images/2016/09/25/930f5212c99ccc71accd4615cb03e255.jpg',
		content: `${json.hitokoto} - ${json.from}`,
		targetUrl: 'https://hitokoto.cn'
	};
}).then((result) => {
	App.Return(result);
});

fetch("https://v1.hitokoto.cn").then((response) => {return response.json();}).then((json) => {return {iconUrl: 'https://hitokoto.cn/favicon.ico',title: 'Hitokoto',imgUrl: 'https://piccdn.freejishu.com/images/2016/09/25/930f5212c99ccc71accd4615cb03e255.jpg',content: `${json.hitokoto} - ${json.from}`,targetUrl: 'https://hitokoto.cn'};}).then((result) => {App.Return(JSON.stringify(result));});

// G-CORES

var articleDiv = document.querySelector(".showcase-article");

App.Return(JSON.stringify({
    title: articleDiv.querySelector("h4 a").innerHTML.trim(),
    content: articleDiv.querySelector(".showcase_info").innerHTML.trim(),
    imgUrl: articleDiv.querySelector(".showcase_img a img").src,
    targetUrl: articleDiv.querySelector("h4 a").href,
    iconUrl: document.querySelector(".navbar_brand-affix_white").src
}));

var articleDiv = document.querySelector(".showcase-article");App.Return(JSON.stringify({title: articleDiv.querySelector("h4 a").innerHTML,content: articleDiv.querySelector(".showcase_info").innerHTML,imgUrl: articleDiv.querySelector(".showcase_img a img").src,targetUrl: articleDiv.querySelector("h4 a").href,iconUrl: document.querySelector(".navbar_brand-affix_white").src}));
