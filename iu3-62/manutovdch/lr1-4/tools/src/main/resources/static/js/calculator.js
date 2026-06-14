document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('productSearch');
    const searchBtn = document.getElementById('searchBtn');
    const resultsDiv = document.getElementById('productSearchResults');
    const selectedItemsDiv = document.getElementById('selectedItems');
    const emptyMsg = document.getElementById('emptyItemsMsg');
    const estimateForm = document.getElementById('estimateForm');
    const resultDiv = document.getElementById('estimateResult');

    let selectedProducts = [];

    searchBtn.addEventListener('click', searchProducts);
    searchInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') searchProducts();
    });

    function searchProducts() {
        const q = searchInput.value.trim();
        if (!q) return;
        resultsDiv.innerHTML = '<p class="text-muted small">Поиск...</p>';
        fetch('/api/products/search?name=' + encodeURIComponent(q))
            .then(r => r.json())
            .then(products => {
                if (!products || products.length === 0) {
                    resultsDiv.innerHTML = '<p class="text-muted small">Ничего не найдено</p>';
                    return;
                }
                let html = '<div class="list-group list-group-flush">';
                products.forEach(p => {
                    const alreadyAdded = selectedProducts.some(sp => sp.productId === p.id);
                    html += `
                        <div class="list-group-item bg-transparent border-secondary text-white d-flex justify-content-between align-items-center py-2">
                            <div class="text-truncate me-2">
                                <strong>${escapeHtml(p.name)}</strong>
                                <span class="text-muted small"> — ${p.article ? escapeHtml(p.article) : '—'}</span>
                                <span class="brand-highlight ms-2">${p.price ? p.price + ' ₽' : '—'}</span>
                            </div>
                            ${alreadyAdded
                                ? '<span class="badge bg-secondary">Добавлено</span>'
                                : `<button class="btn btn-sm btn-orange add-product-btn" data-id="${p.id}" data-name="${escapeHtml(p.name)}" data-article="${escapeHtml(p.article || '')}" data-price="${p.price || 0}">
                                    <i class="fas fa-plus"></i>
                                </button>`
                            }
                        </div>
                    `;
                });
                html += '</div>';
                resultsDiv.innerHTML = html;

                document.querySelectorAll('.add-product-btn').forEach(btn => {
                    btn.addEventListener('click', function () {
                        addProduct({
                            productId: parseInt(this.dataset.id),
                            name: this.dataset.name,
                            article: this.dataset.article,
                            price: parseFloat(this.dataset.price)
                        });
                        searchProducts();
                    });
                });
            })
            .catch(() => {
                resultsDiv.innerHTML = '<p class="text-danger small">Ошибка поиска</p>';
            });
    }

    function addProduct(p) {
        if (selectedProducts.some(sp => sp.productId === p.productId)) return;
        selectedProducts.push({ productId: p.productId, name: p.name, article: p.article, price: p.price, quantity: 1 });
        renderSelected();
    }

    function removeProduct(productId) {
        selectedProducts = selectedProducts.filter(p => p.productId !== productId);
        renderSelected();
    }

    function updateQuantity(productId, qty) {
        const p = selectedProducts.find(sp => sp.productId === productId);
        if (p) {
            p.quantity = Math.max(1, parseInt(qty) || 1);
        }
    }

    function renderSelected() {
        if (selectedProducts.length === 0) {
            selectedItemsDiv.innerHTML = '<p class="text-muted small" id="emptyItemsMsg">Товары не добавлены. Найдите товары выше и добавьте их в расчёт.</p>';
            return;
        }
        let html = '';
        selectedProducts.forEach(p => {
            html += `
                <div class="d-flex align-items-center gap-2 mb-2 p-2 bg-dark rounded">
                    <div class="flex-grow-1">
                        <strong class="small">${escapeHtml(p.name)}</strong>
                        <span class="text-muted small ms-2">${p.article}</span>
                        <span class="brand-highlight small ms-2">${p.price} ₽</span>
                    </div>
                    <input type="number" class="form-control form-control-sm bg-dark border-secondary text-white" style="width: 70px;"
                        value="${p.quantity}" min="1" data-id="${p.productId}">
                    <button class="btn btn-sm btn-outline-danger remove-btn" data-id="${p.productId}">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            `;
        });
        selectedItemsDiv.innerHTML = html;

        selectedItemsDiv.querySelectorAll('input[type="number"]').forEach(inp => {
            inp.addEventListener('change', function () {
                updateQuantity(parseInt(this.dataset.id), this.value);
            });
        });
        selectedItemsDiv.querySelectorAll('.remove-btn').forEach(btn => {
            btn.addEventListener('click', function () {
                removeProduct(parseInt(this.dataset.id));
            });
        });
    }

    estimateForm.addEventListener('submit', function (e) {
        e.preventDefault();
        if (selectedProducts.length === 0) {
            resultDiv.innerHTML = '<p class="text-danger small">Добавьте хотя бы один товар для расчёта.</p>';
            return;
        }

        const items = selectedProducts.map(p => ({
            productId: p.productId,
            quantity: p.quantity
        }));

        const body = {
            items: items,
            expressDelivery: document.getElementById('expressDelivery').checked,
            promoCode: document.getElementById('promoCode').value.trim()
        };

        const submitBtn = this.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Расчёт...';

        fetch('/api/quick-calc/estimate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        })
            .then(r => r.json())
            .then(data => {
                let linesHtml = '';
                if (data.lines) {
                    data.lines.forEach(line => {
                        linesHtml += `
                            <tr>
                                <td class="small">${escapeHtml(line.productName)}</td>
                                <td class="text-end small">${line.unitPrice} ₽</td>
                                <td class="text-center small">${line.quantity}</td>
                                <td class="text-end small">${line.lineTotal} ₽</td>
                            </tr>
                        `;
                    });
                }

                resultDiv.innerHTML = `
                    <div class="table-responsive">
                        <table class="table table-dark table-sm table-borderless mb-3">
                            <thead>
                                <tr>
                                    <th class="small text-muted">Товар</th>
                                    <th class="small text-muted text-end">Цена</th>
                                    <th class="small text-muted text-center">Кол-во</th>
                                    <th class="small text-muted text-end">Сумма</th>
                                </tr>
                            </thead>
                            <tbody>${linesHtml}</tbody>
                        </table>
                    </div>
                    <hr class="border-secondary">
                    <div class="d-flex justify-content-between mb-1">
                        <span class="text-muted small">Сумма</span>
                        <span class="text-white fw-bold">${data.subtotal} ₽</span>
                    </div>
                    ${data.discount && data.discount !== '0' ? `
                    <div class="d-flex justify-content-between mb-1">
                        <span class="text-muted small">Скидка</span>
                        <span class="text-success fw-bold">−${data.discount} ₽</span>
                    </div>
                    <div class="text-muted small mb-2">${escapeHtml(data.discountDescription)}</div>
                    ` : ''}
                    <div class="d-flex justify-content-between mb-1">
                        <span class="text-muted small">Налог (20%)</span>
                        <span class="text-white">${data.tax} ₽</span>
                    </div>
                    <div class="d-flex justify-content-between mb-1">
                        <span class="text-muted small">Доставка</span>
                        <span class="text-white">${data.shipping} ₽</span>
                    </div>
                    <hr class="border-secondary">
                    <div class="d-flex justify-content-between">
                        <span class="text-white h5">Итого</span>
                        <span class="brand-highlight h5">${data.total} ₽</span>
                    </div>
                    ${data.warning ? `<div class="alert alert-warning mt-3 py-2 small mb-0">${escapeHtml(data.warning)}</div>` : ''}
                `;
            })
            .catch(() => {
                resultDiv.innerHTML = '<p class="text-danger small">Ошибка расчёта. Попробуйте позже.</p>';
            })
            .finally(() => {
                submitBtn.disabled = false;
                submitBtn.innerHTML = '<i class="fas fa-calculator me-2"></i>Рассчитать стоимость';
            });
    });

    function escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
});
