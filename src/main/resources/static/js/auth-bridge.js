(function () {
    const token = sessionStorage.getItem('basic');
    if (!token) {
        window.location.replace('/');
        return;
    }

    const origFetch = window.fetch.bind(window);
    window.fetch = (input, init = {}) => {
        try {
            const url = typeof input === 'string' ? input : input instanceof Request ? input.url : '';
            if (url.startsWith('/api') || url.startsWith(location.origin + '/api')) {
                const headers = new Headers(init.headers || {});
                if (!headers.has('Authorization')) headers.set('Authorization', 'Basic ' + token);
                init.headers = headers;
            }
        } catch {}
        return origFetch(input, init);
    };

    window.AuthBridge = {
        token,
        logout() {
            sessionStorage.removeItem('basic');
            window.location.replace('/');
        },
    };
})();
