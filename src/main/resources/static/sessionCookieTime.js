const apiUrl = 'http://localhost:8080';

function loadTime() {
  let time = document.getElementById('time');
  let csrfToken = getCookie('csrfToken');

  fetch(apiUrl + '/api/auth/session-cookie/v1/time', {
      method: 'GET',
      credentials: 'include',
      headers: {
        'X-CSRF': csrfToken
      }
    })
    .then(res => {
      res.text().then(text => {
        time.innerHTML = text;
      });
    })
    .catch(error => console.error('Error getting time : ', error));
}

function getCookie(cookieName) {
  var cookieValue = document.cookie.split(';')
    .map(item => item.split('=')
      .map(x => decodeURIComponent(x.trim())))
    .filter(item => item[0] === cookieName)[0]

  if (cookieValue) {
    return cookieValue[1];
  }
}