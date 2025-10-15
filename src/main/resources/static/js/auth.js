(function () {
    const API_BASE = location.origin;

    const $ = (sel) => document.querySelector(sel);
    const show = (el) => { el.classList.remove('d-none'); };
    const hide = (el) => { el.classList.add('d-none'); };
    const setAlert = (el, ok, msg) => {
        el.className = 'alert mt-2 ' + (ok ? 'alert-success' : 'alert-danger');
        el.textContent = msg;
        show(el);
    };

    // Display origin in navbar
    const serverOriginEl = document.getElementById('serverOrigin');
    if (serverOriginEl) serverOriginEl.textContent = API_BASE;

    // Helpers for Basic auth stored in sessionStorage
    const b64 = (s) => (typeof btoa !== 'undefined' ? btoa(s) : Buffer.from(s, 'utf8').toString('base64'));
    const saveCreds = (u, p) => sessionStorage.setItem('basic', b64(`${u}:${p}`));
    const clearCreds = () => sessionStorage.removeItem('basic');
    const getAuthHeader = () => {
        const token = sessionStorage.getItem('basic');
        return token ? { 'Authorization': 'Basic ' + token } : {};
    };

    // LOGIN
    $('#loginForm')?.addEventListener('submit', async (e) => {
        e.preventDefault();
        hide($('#loginAlert'));
        const u = $('#login-username').value.trim();
        const p = $('#login-password').value;

        try {
            const res = await fetch(`${API_BASE}/api/me`, {
                headers: { 'Accept': 'application/json', 'Authorization': 'Basic ' + b64(`${u}:${p}`) },
                credentials: 'include'
            });

            if (!res.ok) {
                const t = await res.text();
                setAlert($('#loginAlert'), false, `Login failed (${res.status}). ${t || 'Check username/password.'}`);
                return;
            }

            const me = await res.json();
            saveCreds(u, p);

            // show success, then redirect to products page
            setAlert($('#loginAlert'), true,
                `Hello ${me.username}! Roles: ${me.roles?.join(', ') || 'N/A'}. Redirecting...`
            );
            setTimeout(() => {
                window.location.assign('/products.html');
            }, 600);
        } catch (err) {
            setAlert($('#loginAlert'), false, `Network error: ${err}`);
        }
    });

    // CLEAR CREDS
    $('#clearCreds')?.addEventListener('click', () => {
        clearCreds();
        setAlert($('#loginAlert'), true, 'Saved credentials cleared (this browser tab).');
    });

    // REGISTER
    $('#registerForm')?.addEventListener('submit', async (e) => {
        e.preventDefault();
        hide($('#registerAlert'));
        const u = $('#reg-username').value.trim();
        const p = $('#reg-password').value;

        if (u.length < 3 || p.length < 6) {
            setAlert($('#registerAlert'), false, 'Username must be ≥ 3 chars, password ≥ 6 chars.');
            return;
        }

        try {
            const res = await fetch(`${API_BASE}/api/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: u, password: p })
            });

            if (!res.ok) {
                const t = await res.text();
                setAlert($('#registerAlert'), false, `Registration failed (${res.status}). ${t || ''}`);
                return;
            }

            setAlert($('#registerAlert'), true, `User “${u}” registered. You can login now.`);
            $('#login-username').value = u;
            $('#login-password').focus();
        } catch (err) {
            setAlert($('#registerAlert'), false, `Network error: ${err}`);
        }
    });

    $('#productsLink')?.addEventListener('click', () => {
    });
})();
