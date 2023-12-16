const URL = 'http://localhost:8080/api/user/'

// Загрузка страницы на экран
function getAllUsers() {
    fetch(URL)
        .then(res => res.json())
        .then(data => createUsersTable(data))
}
// Загрузка страницы одного админа на экран
function getAdminUser() {
    fetch(URL + 'root')
        .then(res => res.json())
        .then(data => createAdminPage(data))
}

// Функция для создания таблицы пользователей
function createUsersTable(allUsers) {
    let row = ``
    for (let user of allUsers) {
        row += `
        <tr>
            <td>${user.id}</td>
            <td>${user.firstname}</td>
            <td>${user.lastname}</td>
            <td>${user.age}</td>
            <td>${user.username}</td>
            <td>${user.roles.map(r => r.name.replace('ROLE_', '')).join(' ')}</td>
            <td>
                <button class="btn btn-sm btn-primary" type="button"
                    data-bs-toggle="modal" data-bs-target="#editModal" aria-controls="editModal"
                    onclick="editModal(${user.id})">Edit
                </button>
            </td>
            <td>
                <button class="btn btn-sm btn-danger" type="button"
                    data-bs-toggle="modal" data-bs-target="#deleteModal" aria-controls="deleteModal"
                    onclick="deleteModal(${user.id})">Delete
                </button>
            </td>
        </tr>
        `
    }
    document.getElementById('users-table').innerHTML = row
    document.getElementById('UsersInfoTable').style.display = 'block'
    document.getElementById('UserInfoTable').style.display = 'none'
}


// Функция для создания персональной страницы админа

function createAdminPage(user) {
    document.getElementById('UserInformation').innerHTML = `
    <tr>
            <td>${user.id}</td>
            <td>${user.firstname}</td>
            <td>${user.lastname}</td>
            <td>${user.age}</td>
            <td>${user.username}</td>
            <td>${user.roles.map(r => r.name.replace('ROLE_', '')).join(' ')}</td>
    </tr>
    `
    document.getElementById('UserInfoTable').style.display = 'block'
    document.getElementById('UsersInfoTable').style.display = 'none'
}

// Функция для создания нового пользователя

function createNewUser(form) {
        fetch(URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({
                firstname: document.getElementById('newName').value,
                lastname: document.getElementById('newLastname').value,
                age: document.getElementById('newAge').value,
                username: document.getElementById('newUsername').value,
                password: document.getElementById('newPassword').value,
                roles: Array.from(document.getElementById('newRoles').selectedOptions).map(option => {
                    return {id: option.value}
                })
            })
        })
            .then(res => {
                    form.reset()
                    document.getElementById('UserTab-tab').click()
                    getAllUsers()
            })
}


// Функция для закрытия модального окна редактирования
function closeEditModal() {
    let myModalEl = document.getElementById('editModal')
    let modal = bootstrap.Modal.getInstance(myModalEl)
    modal.hide()
}


// Функция для закрытия модального окна удаления
function closeDeleteModal() {
    let myModalEl = document.getElementById('deleteModal')
    let modal = bootstrap.Modal.getInstance(myModalEl)
    modal.hide()
}


// Функция для выводаи информации и пользователе в модальное окно
function editModal(id) {
    let editURL = URL + id
    fetch(editURL, {
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    })
        .then(res => res.json())
        .then(user => {
            document.getElementById('editId').value = user.id
            document.getElementById('editFirstname').value = user.firstname
            document.getElementById('editLastname').value = user.lastname
            document.getElementById('editAge').value = user.age
            document.getElementById('editEmail').value = user.username
            document.getElementById('editPassword').value = user.password
            document.getElementById('editRoles').value = user.roles.map(r => r.id)

        })

}

// Функция для изменения пользователя

function editUser() {
        fetch(URL, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({
                id: document.getElementById('editId').value,
                firstname: document.getElementById('editFirstname').value,
                lastname: document.getElementById('editLastname').value,
                age: document.getElementById('editAge').value,
                username: document.getElementById('editEmail').value,
                password: document.getElementById('editPassword').value,
                roles: Array.from(document.getElementById('editRoles').selectedOptions).map(option => {
                    return {id: option.value, name: option.text}
                })
            })
        })
            .then(res => {
                closeEditModal()
                document.getElementById('UserTab-tab').click()
                getAllUsers()
            })

}

// Функция для вывода информации в модальное окно
function deleteModal(id) {
    let delURL = URL + id
    fetch(delURL, {
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    })
        .then(res => res.json())
        .then(user => {
            document.getElementById('deleteId').value = user.id
            document.getElementById('firstname1').value = user.firstname
            document.getElementById('lastname1').value = user.lastname
            document.getElementById('age1').value = user.age
            document.getElementById('email1').value = user.username
            document.getElementById('password1').value = user.password
            document.getElementById('roles1').value = user.roles.map(r => r.id)
        })
}


// Функция для удаления пользователя
function deleteUser() {
    fetch(URL + document.getElementById('deleteId').value, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            id: document.getElementById('deleteId')
        })
    })
        .then(res => {
            closeDeleteModal()
            document.getElementById('UserTab-tab').click()
            getAllUsers()
        })
}

// Отмена перезагруззки страницы, перехватываю нажатие кнопки подтверждения
document.getElementById('newUserForm').addEventListener('submit', e => {
    e.preventDefault()
    const form = e.target
    createNewUser(form)
})

document.getElementById('editForm').addEventListener('submit', e => {
    e.preventDefault()
    editUser()
})

document.getElementById('deleteForm').addEventListener('submit', e => {
    e.preventDefault()
    deleteUser()
})

// Перехват события нажатия по кнопке user для отображения личной страницы
document.getElementById('UserButton').addEventListener("click", e => {
    e.preventDefault()
    getAdminUser()
})

// Перехват события нажатия по кнопке admin для отображения таблицы пользователей

document.getElementById('AdminButton').addEventListener('click', e => {
    e.preventDefault()
    getAllUsers()
})

getAllUsers()