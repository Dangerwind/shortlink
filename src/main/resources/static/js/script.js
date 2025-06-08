
let currentPage = 0;

function generateShortLink() {
  const url = document.getElementById("originalUrl").value;
  fetch("/generate", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ original: url })
  })
  .then(res => res.json())
  .then(data => {
    const resultDiv = document.getElementById("shortenedResult");
    resultDiv.innerHTML = '<div class="copy-box" onclick="copyToClipboard(\'' + data.link + '\')">Вот ваша сокращенная ссылка: ' + data.link + '</div>';
  });
}

function copyToClipboard(text) {
  navigator.clipboard.writeText(text);
  alert("Ссылка скопирована в буфер обмена!");
}

function showCheck() {
  hideAll();
  document.getElementById("check").classList.remove("d-none");
}

function showStats() {
  hideAll();
  document.getElementById("stats").classList.remove("d-none");
}

function goHome() {
  hideAll();
  document.getElementById("main").classList.remove("d-none");
}

function hideAll() {
  ["main", "check", "stats"].forEach(id => document.getElementById(id).classList.add("d-none"));
}

function checkShort() {
  const link = document.getElementById("checkUrl").value;
  fetch(`/stats/${link}`)
    .then(res => res.json())
    .then(data => {
      const result = `Ваша сокращенная ссылка ${data.link} ведет на ресурс ${data.original}, ее рейтинг ${data.rank}, переходов ${data.count}`;
      document.getElementById("checkResult").textContent = result;
    });
}

function loadStats() {
  const page = document.getElementById("statPage").value || 0;
  const count = document.getElementById("statCount").value || 10;
  currentPage = parseInt(page);
  fetch(`/stats?page=${page}&count=${count}`)
    .then(res => res.json())
    .then(data => {
      const container = document.getElementById("statsResult");
      container.innerHTML = data.map(link =>
        `<div class="card mb-2"><div class="card-body">
          <b>${link.link}</b><br>Ведет на: ${link.original}<br>Рейтинг: ${link.rank}, Переходов: ${link.count}
        </div></div>`
      ).join("");
    });
}

function nextPage() {
  currentPage++;
  document.getElementById("statPage").value = currentPage;
  loadStats();
}

function prevPage() {
  if (currentPage > 0) {
    currentPage--;
    document.getElementById("statPage").value = currentPage;
    loadStats();
  }
}

function firstPage() {
  currentPage = 0;
  document.getElementById("statPage").value = 0;
  loadStats();
}
