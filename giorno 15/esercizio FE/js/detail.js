// placeholder
const API_BASE = 'https://jsonplaceholder.typicode.com/users';

const initials = name =>
    name.split(' ').slice(0, 2).map(w => w[0].toUpperCase()).join('');

// render degli utenti
function renderUser(user) {
    // creo la card per renderizzare
    const card = `
    <div class="detail-card">

        <div class="detail-header">
        <div class="detail-avatar">${initials(user.name)}</div>
        <div class="detail-title">
            <h2>${user.name}</h2>
            <span class="role-badge">@${user.username}</span>
        </div>
        </div>

        <div class="detail-divider"></div>

        <p class="section-title">Contatti</p>
        <div class="detail-fields">
        <div class="detail-field">
            <span class="field-label">Email</span>
            <span class="field-value">
            <a href="mailto:${user.email}">${user.email}</a>
            </span>
        </div>
        <div class="detail-field">
            <span class="field-label">Telefono</span>
            <span class="field-value">${user.phone}</span>
        </div>
        <div class="detail-field">
            <span class="field-label">Sito web</span>
            <span class="field-value">
            <a href="https://${user.website}" target="_blank" rel="noopener">${user.website}</a>
            </span>
        </div>
        </div>

        <div class="detail-divider"></div>

        <p class="section-title">Indirizzo</p>
        <div class="detail-fields">
        <div class="detail-field">
            <span class="field-label">Città</span>
            <span class="field-value">${user.address.city}</span>
        </div>
        <div class="detail-field">
            <span class="field-label">Via</span>
            <span class="field-value">${user.address.street}, ${user.address.suite}</span>
        </div>
        <div class="detail-field">
            <span class="field-label">CAP</span>
            <span class="field-value">${user.address.zipcode}</span>
        </div>
        </div>

        <div class="detail-divider"></div>

        <p class="section-title">Azienda</p>
        <div class="detail-fields">
        <div class="detail-field">
            <span class="field-label">Nome</span>
            <span class="field-value">${user.company.name}</span>
        </div>
        <div class="detail-field">
            <span class="field-label">Slogan</span>
            <span class="field-value" style="font-style:italic;color:var(--text-muted)">${user.company.catchPhrase}</span>
        </div>
        </div>

    </div>`;

    document.getElementById('content').innerHTML = card;
}

// msg di errore
function renderError(msg) {
    document.getElementById('content').innerHTML = `
    <div class="state-message">
        <span class="icon">⚠️</span>
        ${msg}
    </div>`;
}

// fetcho
async function loadUser() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');

    if (!id || isNaN(id)) {
        renderError('ID utente non valido. <a href="index.html" style="color:var(--accent)">Torna alla lista</a>.');
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/${id}`);
        if (res.status === 404) throw new Error('Utente non trovato.');
        if (!res.ok) throw new Error(`Errore HTTP ${res.status}`);
        const user = await res.json();
        renderUser(user);
    } catch (err) {
        renderError(`${err.message} <a href="index.html" style="color:var(--accent)">Torna alla lista</a>.`);
    }
}

// e carico
loadUser();