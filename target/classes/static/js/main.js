/* ============================================================
   Smart Logistics SCM - Main JavaScript
   ============================================================ */

(function() {
    'use strict';

    // ============================================================
    // Theme Management
    // ============================================================
    const THEME_KEY = 'slscm_theme';

    function applyTheme(theme) {
        document.body.setAttribute('data-theme', theme);
        const icon = document.getElementById('themeIcon');
        if (icon) {
            icon.className = theme === 'dark' ? 'bi bi-sun-fill' : 'bi bi-moon-stars-fill';
        }
        localStorage.setItem(THEME_KEY, theme);
    }

    function initTheme() {
        const saved = localStorage.getItem(THEME_KEY);
        const preferred = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
        applyTheme(saved || preferred);
    }

    window.toggleTheme = function() {
        const current = document.body.getAttribute('data-theme');
        applyTheme(current === 'dark' ? 'light' : 'dark');
    };

    // ============================================================
    // Sidebar Toggle
    // ============================================================
    function initSidebar() {
        const sidebarToggle = document.getElementById('sidebarToggle');
        const mobileSidebarToggle = document.getElementById('mobileSidebarToggle');
        const sidebar = document.getElementById('sidebar');

        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', function() {
                document.body.classList.toggle('sidebar-collapsed');
                const icon = this.querySelector('i');
                if (document.body.classList.contains('sidebar-collapsed')) {
                    icon.className = 'bi bi-chevron-right';
                } else {
                    icon.className = 'bi bi-chevron-left';
                }
                localStorage.setItem('slscm_sidebar_collapsed', document.body.classList.contains('sidebar-collapsed'));
            });
        }

        if (mobileSidebarToggle && sidebar) {
            mobileSidebarToggle.addEventListener('click', function() {
                sidebar.classList.toggle('mobile-open');
            });
        }

        // Close sidebar on overlay click (mobile)
        document.addEventListener('click', function(e) {
            if (window.innerWidth < 992 && sidebar) {
                if (!sidebar.contains(e.target) && mobileSidebarToggle && !mobileSidebarToggle.contains(e.target)) {
                    sidebar.classList.remove('mobile-open');
                }
            }
        });

        // Restore collapsed state
        if (localStorage.getItem('slscm_sidebar_collapsed') === 'true') {
            document.body.classList.add('sidebar-collapsed');
            const icon = sidebarToggle && sidebarToggle.querySelector('i');
            if (icon) icon.className = 'bi bi-chevron-right';
        }
    }

    // ============================================================
    // Theme Toggle Button
    // ============================================================
    function initThemeToggle() {
        const btn = document.getElementById('themeToggle');
        if (btn) {
            btn.addEventListener('click', window.toggleTheme);
        }
    }

    // ============================================================
    // Counter Animation
    // ============================================================
    function animateCounters() {
        const counters = document.querySelectorAll('.counter');
        counters.forEach(function(counter) {
            const target = parseInt(counter.getAttribute('data-target') || counter.textContent || '0');
            if (isNaN(target) || target === 0) return;

            let current = 0;
            const duration = 1200;
            const step = Math.ceil(target / (duration / 16));
            const timer = setInterval(function() {
                current = Math.min(current + step, target);
                counter.textContent = current.toLocaleString();
                if (current >= target) clearInterval(timer);
            }, 16);
        });
    }

    // ============================================================
    // Confirm Delete
    // ============================================================
    window.confirmDelete = function(form, entityName) {
        if (typeof Swal !== 'undefined') {
            Swal.fire({
                title: 'Delete ' + (entityName || 'item') + '?',
                text: 'This action cannot be undone.',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#EF4444',
                cancelButtonColor: '#64748B',
                confirmButtonText: 'Yes, delete it',
                cancelButtonText: 'Cancel',
                borderRadius: '12px'
            }).then(function(result) {
                if (result.isConfirmed) form.submit();
            });
            return false;
        }
        return confirm('Are you sure you want to delete this ' + entityName + '? This action cannot be undone.');
    };

    // ============================================================
    // CSRF Setup for AJAX
    // ============================================================
    function setupCsrf() {
        const csrfToken = document.querySelector('meta[name="_csrf"]');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
        if (csrfToken && csrfHeader) {
            window._csrf = {
                token: csrfToken.getAttribute('content'),
                header: csrfHeader.getAttribute('content')
            };

            // Setup jQuery AJAX
            if (typeof $ !== 'undefined') {
                $.ajaxSetup({
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(window._csrf.header, window._csrf.token);
                    }
                });
            }
        }
    }

    // ============================================================
    // DataTables Default Config
    // ============================================================
    function initDataTables() {
        if (typeof $.fn !== 'undefined' && typeof $.fn.DataTable !== 'undefined') {
            $.extend(true, $.fn.DataTable.defaults, {
                language: {
                    search: '',
                    searchPlaceholder: 'Search...',
                    lengthMenu: 'Show _MENU_ entries',
                    info: 'Showing _START_ to _END_ of _TOTAL_ entries',
                    infoEmpty: 'No entries available',
                    infoFiltered: '(filtered from _MAX_ total)',
                    paginate: {
                        previous: '<i class="bi bi-chevron-left"></i>',
                        next: '<i class="bi bi-chevron-right"></i>'
                    }
                },
                dom: "<'row mb-3'<'col-sm-6'l><'col-sm-6'f>>" +
                     "<'row'<'col-sm-12'tr>>" +
                     "<'row mt-3'<'col-sm-6'i><'col-sm-6'p>>",
                pageLength: 10,
                responsive: true
            });
        }
    }

    // ============================================================
    // FAB Button
    // ============================================================
    function initFab() {
        const fabBtn = document.querySelector('.fab-btn');
        const fabMenu = document.querySelector('.fab-menu');
        if (fabBtn && fabMenu) {
            let open = false;
            fabBtn.addEventListener('click', function() {
                open = !open;
                fabMenu.style.display = open ? 'flex' : 'none';
            });
            fabMenu.style.display = 'none';
        }
    }

    // ============================================================
    // Tooltips
    // ============================================================
    function initTooltips() {
        if (typeof bootstrap !== 'undefined') {
            const tooltipEls = document.querySelectorAll('[data-bs-toggle="tooltip"]');
            tooltipEls.forEach(function(el) {
                new bootstrap.Tooltip(el, { trigger: 'hover' });
            });
        }
    }

    // ============================================================
    // Avatar Letter (auto-generate from username)
    // ============================================================
    function initAvatarLetters() {
        const avatarEl = document.getElementById('navAvatarLetter');
        if (avatarEl && avatarEl.textContent) {
            avatarEl.textContent = avatarEl.textContent.trim().charAt(0).toUpperCase();
        }
    }

    // ============================================================
    // Flash Message Auto-dismiss
    // ============================================================
    function initAlerts() {
        document.querySelectorAll('.alert:not(.alert-permanent)').forEach(function(alert) {
            setTimeout(function() {
                if (typeof bootstrap !== 'undefined') {
                    const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
                    if (bsAlert) bsAlert.close();
                } else {
                    alert.style.opacity = '0';
                    setTimeout(() => alert.remove(), 300);
                }
            }, 5000);
        });
    }

    // ============================================================
    // Keyboard Shortcut (Cmd+K / Ctrl+K for search)
    // ============================================================
    function initKeyboardShortcuts() {
        document.addEventListener('keydown', function(e) {
            if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
                e.preventDefault();
                const searchInput = document.querySelector('.search-input');
                if (searchInput) searchInput.focus();
            }
        });
    }

    // ============================================================
    // Global Search Auto-Complete & Navigation
    // ============================================================
    function initGlobalSearch() {
        const searchInput = document.querySelector('.search-input');
        const resultsDropdown = document.querySelector('.search-results-dropdown');
        if (!searchInput || !resultsDropdown) return;

        let debounceTimeout;

        searchInput.addEventListener('input', function() {
            const query = this.value.trim();
            clearTimeout(debounceTimeout);

            if (query.length < 2) {
                resultsDropdown.classList.add('d-none');
                resultsDropdown.innerHTML = '';
                return;
            }

            debounceTimeout = setTimeout(function() {
                fetch('/api/search?q=' + encodeURIComponent(query))
                    .then(response => response.json())
                    .then(data => {
                        resultsDropdown.innerHTML = '';
                        if (data.length === 0) {
                            resultsDropdown.innerHTML = '<div class="search-no-results">No matching results found</div>';
                        } else {
                            // Group by category
                            const groups = {};
                            data.forEach(item => {
                                if (!groups[item.category]) {
                                    groups[item.category] = [];
                                }
                                groups[item.category].push(item);
                            });

                            for (const category in groups) {
                                const header = document.createElement('div');
                                header.className = 'search-category-header';
                                header.textContent = category;
                                resultsDropdown.appendChild(header);

                                groups[category].forEach(item => {
                                    const a = document.createElement('a');
                                    a.className = 'search-result-item';
                                    a.href = item.url;
                                    a.innerHTML = `
                                        <div class="search-result-title">${item.title}</div>
                                        <div class="search-result-subtitle">${item.subtitle}</div>
                                    `;
                                    resultsDropdown.appendChild(a);
                                });
                            }
                        }
                        resultsDropdown.classList.remove('d-none');
                    })
                    .catch(err => {
                        console.error('Search error:', err);
                    });
            }, 300);
        });

        // Hide when clicking outside
        document.addEventListener('click', function(e) {
            if (!searchInput.contains(e.target) && !resultsDropdown.contains(e.target)) {
                resultsDropdown.classList.add('d-none');
            }
        });

        // Show dropdown again on focus if there's text
        searchInput.addEventListener('focus', function() {
            if (this.value.trim().length >= 2 && resultsDropdown.innerHTML !== '') {
                resultsDropdown.classList.remove('d-none');
            }
        });
    }

    // ============================================================
    // Init on DOM Ready
    // ============================================================
    document.addEventListener('DOMContentLoaded', function() {
        initTheme();
        initSidebar();
        initThemeToggle();
        setupCsrf();
        initDataTables();
        initFab();
        initTooltips();
        initAvatarLetters();
        initAlerts();
        initKeyboardShortcuts();
        initGlobalSearch();

        // Run counter animation after a short delay for better UX
        setTimeout(animateCounters, 200);
    });

})();
