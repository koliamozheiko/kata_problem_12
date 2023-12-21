const URL = 'http://localhost:8080/api/default/'

function getUser() {
    fetch(URL + getUserId())
        .then(res => res.json())
        .then(data => createUserTable(data))
}


function createUserTable(user) {
    document.getElementById('defaultUserTableBody').innerHTML = `
        <tr>
            <td>${user.id}</td>
            <td>${user.firstname}</td>
            <td>${user.lastname}</td>
            <td>${user.age}</td>
            <td>${user.username}</td>
            <td>${user.roles.map(r => r.name.replace('ROLE_', '')).join(' ')}</td>
               
        </tr>`
}

function getUserId() {
    const urlArr = window.location.pathname.split('/');
    const userId = urlArr[urlArr.length - 1]
    console.log(userId)
    return userId
}

document.getElementById('UserButton').addEventListener('click', e => {
    e.preventDefault()
    getUser()
})

getUser()