(function() {
  let results = [];
  document.querySelectorAll("div.b_algoheader").forEach(result => {
    results.push({
      text: result.querySelector("h2").querySelectorAll("a")[1].innerHTML
    });
  });
  return { results };
})();

(function() {
  return { results: document.getElementsByTagName('html')[0].innerHTML };
})();
