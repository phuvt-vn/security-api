const apiUrl = 'http://localhost:8080';

function processLoginSubmit(e) {
  e.preventDefault();

  let username = document.getElementById('username').value;
  let password = document.getElementById('password').value;

  let credential = 'Basic ' + btoa(username + ':' + password);

  fetch(apiUrl + '/api/auth/session-cookie/v1/login', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Authorization': credential
      }
    })
    .then(res => {
      if (res.ok) {
        res.text().then(text => {
          document.cookie = 'csrfToken=' + text + ';SameSite=strict;';
          window.location.replace('/sessionCookieTime.html');
        });
      } else {
        alert('Invalid login');
      }
    })
    .catch(error => console.error('Error logging in: ', error));
}