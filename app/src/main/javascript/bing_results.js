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
var taskResult = {};
taskResult.messages = [];
fetch("https://v1.hitokoto.cn")
  .then(response => {
    return response.json();
  })
  .then(json => {
    taskResult.iconUrl = "https://hitokoto.cn/favicon.ico";
    return {
      title: "Hitokoto",
      imgUrl:
        "https://piccdn.freejishu.com/images/2016/09/25/930f5212c99ccc71accd4615cb03e255.jpg",
      content: `${json.hitokoto} - ${json.from}`,
      targetUrl: "https://hitokoto.cn"
    };
  })
  .then(result => {
    taskResult.messages.push(result);
  })
  .then(() => {
    return fetch("https://v1.hitokoto.cn");
  })
  .then(response => {
    return response.json();
  })
  .then(json => {
    taskResult.iconUrl = "https://hitokoto.cn/favicon.ico";
    return {
      title: "Hitokoto",
      imgUrl:
        "https://piccdn.freejishu.com/images/2016/09/25/930f5212c99ccc71accd4615cb03e255.jpg",
      content: `${json.hitokoto} - ${json.from}`,
      targetUrl: "https://hitokoto.cn"
    };
  })
  .then(result => {
    taskResult.messages.push(result);
  })
  .then(() => {
    console.log(taskResult);
  });

// G-CORES

var taskResult = {};
taskResult.iconUrl = document.querySelector(".navbar_brand-affix_white").src;
taskResult.messages = [];
var trIndex = 0;
document.querySelectorAll(".showcase-article").forEach(articleDiv => {
  if (trIndex < 5) {
    taskResult.messages.push({
      title: articleDiv.querySelector("h4 a").innerHTML.trim(),
      content: articleDiv.querySelector(".showcase_info").innerHTML.trim(),
      imgUrl: articleDiv.querySelector(".showcase_img a img").src,
      targetUrl: articleDiv.querySelector("h4 a").href
    });
    trIndex++;
  }
});
App.Return(JSON.stringify(taskResult));
