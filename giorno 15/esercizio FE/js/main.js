// placeholders
const API = 'https://jsonplaceholder.typicode.com/users';
let allUsers = [];

const initials = name =>
    name.split(' ').slice(0, 2).map(w => w[0].toUpperCase()).join('');

// render degli utenti
function renderUsers(users) {
    const list = document.getElementById('userList');
    list.innerHTML = '';

    if (users.length === 0) {
        list.innerHTML = `
            <li class="state-message">
            <span class="icon">🔍</span>
            Nessun utente trovato per "<strong>${searchInput.value}</strong>"
            </li>`;
        return;
    }

    users.forEach((user, i) => {
        const li = document.createElement('li');
        li.style.animationDelay = `${i * 40}ms`;
        li.innerHTML = `
            <a href="detail.html?id=${user.id}" class="card">
            <div class="card-avatar">${initials(user.name)}</div>
            <div class="card-info">
                <span class="card-name">${user.name}</span>
                <span class="card-email">${user.email}</span>
            </div>
            <span class="card-arrow">→</span>
            </a>`;
        list.appendChild(li);
    });
}

// filtro per selezionare gli utenti
const searchInput = document.getElementById('searchInput');
searchInput.addEventListener('input', () => {
    const q = searchInput.value.trim().toLowerCase();
    const filtered = allUsers.filter(u =>
    u.name.toLowerCase().includes(q) ||
    u.email.toLowerCase().includes(q)
    );
    renderUsers(filtered);
});

// facciamo la fetch
async function loadUsers() {
    try {
        const res = await fetch(API);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        allUsers = await res.json();

        document.getElementById('subtitle').textContent =
            `${allUsers.length} utenti registrati`;
        searchInput.disabled = false;
        searchInput.focus();
        renderUsers(allUsers);

    } catch (err) {
        const list = document.getElementById('userList');
        list.innerHTML = `
            <li class="state-message">
            <span class="icon">⚠️</span>
            Errore nel caricamento degli utenti.<br>
            <small>${err.message}</small>
            </li>`;
        document.getElementById('subtitle').textContent = 'Errore di rete';
    }
}

// carico
loadUsers();