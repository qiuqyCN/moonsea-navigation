// 防抖函数
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// 全局变量
let categoryModal, websiteModal;
let currentEditingCategoryId = null;
let currentEditingWebsiteId = null;
let editMode = false; // 添加编辑模式状态

// DOM加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    // 初始化模态框
    initModals();
    // 初始化侧边栏
    initSidebar();
    // 初始化编辑模式切换按钮
    initEditModeToggle();
    // 恢复编辑模式状态
    restoreEditModeState();
    // 初始化分类管理
    initCategoryManagement();
    // 初始化网站管理
    initWebsiteManagement();
    // 初始化拖拽排序
    initSortable();
    // 平滑滚动
    initSmoothScroll();
});

// 初始化编辑模式切换按钮
function initEditModeToggle() {
    const editModeToggle = document.getElementById('editModeToggle');
    if (editModeToggle) {
        // 检查用户是否已认证，未认证用户无法使用编辑模式
        if (!window.isPageAuthenticated) {
            // 隐藏编辑模式按钮或禁用它
            editModeToggle.style.display = 'none';
        } else {
            editModeToggle.addEventListener('click', function() {
                toggleEditMode();
            });
        }
    }
}

// 切换编辑模式
function toggleEditMode() {
    editMode = !editMode;
    const editModeToggle = document.getElementById('editModeToggle');

    if (editMode) {
        // 进入编辑模式
        editModeToggle.classList.add('btn-primary');
        editModeToggle.classList.remove('btn-outline');
        showEditElements();
        // 重新初始化拖拽排序
        initSortable();
        // 保存编辑模式状态
        localStorage.setItem('editMode', 'true');
    } else {
        // 退出编辑模式
        editModeToggle.classList.remove('btn-primary');
        editModeToggle.classList.add('btn-outline');
        hideEditElements();
        // 移除拖拽排序
        removeSortable();
        // 保存编辑模式状态
        localStorage.removeItem('editMode');
    }
}

// 显示编辑相关元素
function showEditElements() {
    // 显示添加分类按钮
    const addCategoryBtn = document.getElementById('addCategoryBtn');
    if (addCategoryBtn) {
        addCategoryBtn.style.display = 'flex';
    }

    // 显示分类操作按钮
    const categoryActions = document.querySelectorAll('.category-actions');
    categoryActions.forEach(action => {
        action.style.display = 'flex';
    });

    // 显示添加网站按钮
    const addWebsiteBtns = document.querySelectorAll('.add-website-btn');
    addWebsiteBtns.forEach(btn => {
        btn.style.display = 'flex';
    });

    // 显示网站操作按钮
    const websiteActions = document.querySelectorAll('.website-actions');
    websiteActions.forEach(action => {
        action.classList.add('group-hover:flex');

    });
}

// 隐藏编辑相关元素
function hideEditElements() {
    // 隐藏添加分类按钮
    const addCategoryBtn = document.getElementById('addCategoryBtn');
    if (addCategoryBtn) {
        addCategoryBtn.style.display = 'none';
    }

    // 隐藏分类操作按钮
    const categoryActions = document.querySelectorAll('.category-actions');
    categoryActions.forEach(action => {
        action.style.display = 'none';
    });

    // 隐藏添加网站按钮
    const addWebsiteBtns = document.querySelectorAll('.add-website-btn');
    addWebsiteBtns.forEach(btn => {
        btn.style.display = 'none';
    });

    // 隐藏网站操作按钮
    const websiteActions = document.querySelectorAll('.website-actions');
    websiteActions.forEach(action => {
        action.classList.remove('group-hover:flex');
    });
}

// 初始化模态框
function initModals() {
    categoryModal = document.getElementById('categoryModal');
    websiteModal = document.getElementById('websiteModal');
}

// 初始化侧边栏
function initSidebar() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    const toggleIcon = document.getElementById('toggleIcon');

    if (sidebarToggle && sidebar && toggleIcon) {
        sidebarToggle.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            sidebar.classList.toggle('collapsed');

            // 保存状态到localStorage
            const isCollapsed = sidebar.classList.contains('collapsed');
            localStorage.setItem('sidebarCollapsed', isCollapsed);
        });

        // 恢复上次的状态
        const savedState = localStorage.getItem('sidebarCollapsed');
        if (savedState === 'true') {
            sidebar.classList.add('collapsed');
        }
    }
}


// 初始化分类管理
function initCategoryManagement() {
    // 添加分类按钮事件
    const addCategoryBtn = document.getElementById('addCategoryBtn');
    if (addCategoryBtn) {
        addCategoryBtn.addEventListener('click', function(e) {
            if (!editMode) {
                e.preventDefault();
                alert('请先进入编辑模式');
                return;
            }
            currentEditingCategoryId = null;
            document.getElementById('categoryModalTitle').textContent = '添加分类';
            document.getElementById('categoryForm').reset();
            categoryModal.showModal();
        });
    }

    // 编辑分类
    document.addEventListener('click', function(e) {
        if (e.target.closest('.edit-category')) {
            if (!editMode) {
                e.preventDefault();
                alert('请先进入编辑模式');
                return;
            }
            e.preventDefault();
            e.stopPropagation();
            const btn = e.target.closest('.edit-category');
            const categoryId = btn.getAttribute('data-id');
            currentEditingCategoryId = categoryId;

            // 通过API获取分类的完整信息
            fetch(`/api/categories/${categoryId}`)
                .then(response => response.json())
                .then(categoryData => {
                    document.getElementById('categoryModalTitle').textContent = '编辑分类';
                    document.getElementById('categoryName').value = categoryData.name;
                    document.getElementById('categoryIcon').value = categoryData.icon || '';

                    categoryModal.showModal();
                })
                .catch(error => {
                    console.error('获取分类信息失败:', error);
                    alert('获取分类信息失败');
                });
        }
    });

    // 删除分类
    document.addEventListener('click', function(e) {
        if (e.target.closest('.delete-category')) {
            if (!editMode) {
                e.preventDefault();
                alert('请先进入编辑模式');
                return;
            }
            e.preventDefault();
            e.stopPropagation();
            if (confirm('确定要删除此分类吗？分类下的所有网址也将被删除。')) {
                const btn = e.target.closest('.delete-category');
                const categoryId = btn.getAttribute('data-id');
                debouncedDeleteCategory(categoryId);
            }
        }
    });

    // 保存分类
    const saveCategoryBtn = document.getElementById('saveCategoryBtn');
    if (saveCategoryBtn) {
        saveCategoryBtn.addEventListener('click', function() {
            if (!editMode) {
                alert('请先进入编辑模式');
                return;
            }
            saveCategory();
        });
    }
}

// 保存分类
function saveCategory() {
    const name = document.getElementById('categoryName').value.trim();
    const icon = document.getElementById('categoryIcon').value.trim(); // 现在支持图标路径或SVG文本内容

    if (!name) {
        alert('请输入分类名称');
        // 将焦点设置到分类名称输入框
        document.getElementById('categoryName').focus();
        return;
    }

    const data = {
        name: name,
        icon: icon,
        sortOrder: 0
    };

    const url = currentEditingCategoryId
        ? `/api/categories/${currentEditingCategoryId}`
        : '/api/categories';

    const method = currentEditingCategoryId ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        categoryModal.close();
        // 保存编辑模式状态到URL参数中，以便在刷新后恢复
        const currentUrl = new URL(window.location);
        currentUrl.searchParams.set('editMode', 'true');
        window.location = currentUrl.toString();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('保存失败，请重试');
    });
}

// 删除分类
function deleteCategory(categoryId) {
    fetch(`/api/categories/${categoryId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            // 保存编辑模式状态到URL参数中，以便在刷新后恢复
            const currentUrl = new URL(window.location);
            currentUrl.searchParams.set('editMode', 'true');
            window.location = currentUrl.toString();
        } else {
            alert('删除失败，请重试');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('删除失败，请重试');
    });
}

// 防抖版本的删除分类
const debouncedDeleteCategory = debounce(deleteCategory, 500);

// 初始化网址管理
function initWebsiteManagement() {
    // 添加网址
    document.addEventListener('click', function(e) {
        if (e.target.closest('.add-website-btn')) {
            if (!editMode) {
                e.preventDefault();
                alert('请先进入编辑模式');
                return;
            }
            e.preventDefault();
            const btn = e.target.closest('.add-website-btn');
            currentEditingWebsiteId = null;
            const categoryId = btn.getAttribute('data-category-id');

            document.getElementById('websiteModalTitle').textContent = '添加网址';
            document.getElementById('websiteForm').reset();
            document.getElementById('websiteCategoryId').value = categoryId;

            websiteModal.showModal();
        }
    });

    // 编辑网址
    document.addEventListener('click', function(e) {
        if (e.target.closest('.edit-website')) {
            if (!editMode) {
                e.preventDefault();
                alert('请先进入编辑模式');
                return;
            }
            e.preventDefault();
            e.stopPropagation();
            const btn = e.target.closest('.edit-website');
            currentEditingWebsiteId = btn.getAttribute('data-id');
            const categoryId = btn.getAttribute('data-category-id');

            // 获取网址信息
            fetch(`/api/websites/${currentEditingWebsiteId}`)
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(errorText => {
                            // 尝试解析错误信息
                            try {
                                const errorObj = JSON.parse(errorText);
                                if (errorObj.message) {
                                    alert('获取网址信息失败：' + errorObj.message);
                                } else {
                                    alert('获取网址信息失败：' + errorText);
                                }
                            } catch (e) {
                                alert('获取网址信息失败：' + errorText);
                            }
                            throw new Error('获取网址信息失败');
                        });
                    }
                    return response.json();
                })
                .then(website => {
                    document.getElementById('websiteModalTitle').textContent = '编辑网址';
                    document.getElementById('websiteName').value = website.name;
                    document.getElementById('websiteUrl').value = website.url;
                    document.getElementById('websiteDescription').value = website.description || '';
                    document.getElementById('websiteLogo').value = website.logo || '';
                    document.getElementById('websiteCategoryId').value = categoryId;

                    websiteModal.showModal();
                })
                .catch(error => {
                    console.error('Error:', error);
                    // 错误处理已经在上面完成，这里不再重复提示
                });
        }
    });

    // 删除网址
    document.addEventListener('click', function(e) {
        if (e.target.closest('.delete-website')) {
            if (!editMode) {
                e.preventDefault();
                alert('请先进入编辑模式');
                return;
            }
            e.preventDefault();
            e.stopPropagation();
            if (confirm('确定要删除此网址吗？')) {
                const btn = e.target.closest('.delete-website');
                const websiteId = btn.getAttribute('data-id');
                debouncedDeleteWebsite(websiteId);
            }
        }
    });

    // 保存网址
    const saveWebsiteBtn = document.getElementById('saveWebsiteBtn');
    if (saveWebsiteBtn) {
        saveWebsiteBtn.addEventListener('click', function() {
            if (!editMode) {
                alert('请先进入编辑模式');
                return;
            }
            saveWebsite();
        });
    }
}

// 保存网址
function saveWebsite() {
    const name = document.getElementById('websiteName').value.trim();
    const url = document.getElementById('websiteUrl').value.trim();
    const description = document.getElementById('websiteDescription').value.trim();
    const logo = document.getElementById('websiteLogo').value.trim();
    const categoryId = document.getElementById('websiteCategoryId').value;

    if (!name) {
        alert('请输入网站名称');
        document.getElementById('websiteName').focus();
        return;
    }
    
    if (!url) {
        alert('请输入网站地址');
        document.getElementById('websiteUrl').focus();
        return;
    }

    // 验证URL格式
    try {
        new URL(url);
    } catch (e) {
        alert('请输入有效的网站地址格式（如：https://example.com）');
        document.getElementById('websiteUrl').focus();
        return;
    }

    const data = {
        name: name,
        url: url,
        description: description,
        logo: logo,
        categoryId: categoryId,
        sortOrder: 0
    };

    const apiUrl = currentEditingWebsiteId
        ? `/api/websites/${currentEditingWebsiteId}`
        : '/api/websites';

    const method = currentEditingWebsiteId ? 'PUT' : 'POST';

    fetch(apiUrl, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        websiteModal.close();
        // 保存编辑模式状态到URL参数中，以便在刷新后恢复
        const currentUrl = new URL(window.location);
        currentUrl.searchParams.set('editMode', 'true');
        window.location = currentUrl.toString();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('保存失败，请重试');
    });
}

// 删除网址
function deleteWebsite(websiteId) {
    fetch(`/api/websites/${websiteId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            // 保存编辑模式状态到URL参数中，以便在刷新后恢复
            const currentUrl = new URL(window.location);
            currentUrl.searchParams.set('editMode', 'true');
            window.location = currentUrl.toString();
        } else {
            alert('删除失败，请重试');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('删除失败，请重试');
    });
}

// 防抖版本的删除网址
const debouncedDeleteWebsite = debounce(deleteWebsite, 500);

// 初始化拖拽排序
function initSortable() {
    // 移除现有的Sortable实例
    removeSortable();

    if (!editMode) return; // 只在编辑模式下启用拖拽排序

    // 分类拖拽排序
    const categoryList = document.getElementById('categoryList');
    if (categoryList && typeof Sortable !== 'undefined') {
        new Sortable(categoryList, {
            animation: 150,
            handle: '.category-link',
            onEnd: function(evt) {
                const items = categoryList.querySelectorAll('li[data-category-id]');
                const categoryIds = Array.from(items).map(item => {
                    return item.getAttribute('data-category-id');
                });

                debouncedUpdateCategorySortOrder(categoryIds);
            }
        });
    }

    // 网址拖拽排序
    document.querySelectorAll('.grid[data-category-id]').forEach(grid => {
        if (typeof Sortable !== 'undefined') {
            new Sortable(grid, {
                animation: 150,
                onEnd: function(evt) {
                    const categoryId = grid.getAttribute('data-category-id');
                    const items = grid.querySelectorAll('[data-website-id]');
                    const websiteIds = Array.from(items).map(item => {
                        return item.getAttribute('data-website-id');
                    });

                    debouncedUpdateWebsiteSortOrder(websiteIds);
                }
            });
        }
    });
}

// 移除拖拽排序
function removeSortable() {
    // 通过将el._sortable属性设置为null来移除现有的Sortable实例
    document.querySelectorAll('.sortable-ghost, .sortable-chosen').forEach(el => {
        if (el._sortable) {
            el._sortable.destroy();
        }
    });

    // 移除可能存在的Sortable实例
    const categoryList = document.getElementById('categoryList');
    if (categoryList && categoryList._sortable) {
        categoryList._sortable.destroy();
    }

    document.querySelectorAll('.grid[data-category-id]').forEach(grid => {
        if (grid._sortable) {
            grid._sortable.destroy();
        }
    });
}

// 更新分类排序
function updateCategorySortOrder(sortData) {
    fetch('/api/categories/reorder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(sortData)
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
const debouncedUpdateCategorySortOrder = debounce(updateCategorySortOrder, 500);

// 更新网址排序
function updateWebsiteSortOrder(sortData) {
    fetch('/api/websites/reorder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(sortData)
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
const debouncedUpdateWebsiteSortOrder = debounce(updateWebsiteSortOrder, 500);

// 初始化平滑滚动
function initSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const href = this.getAttribute('href');
            if (href !== '#' && href.length > 1) {
                e.preventDefault();
                const target = document.querySelector(href);
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            }
        });
    });
}

// 恢复编辑模式状态
function restoreEditModeState() {
    // 检查用户是否已认证
    const isAuthenticated = window.isPageAuthenticated;
    
    // 如果用户未认证，编辑模式默认关闭
    if (!isAuthenticated) {
        editMode = false;
        const editModeToggle = document.getElementById('editModeToggle');
        if (editModeToggle) {
            editModeToggle.classList.remove('btn-primary');
            editModeToggle.classList.add('btn-outline');
        }
        hideEditElements();
        removeSortable();
        return;
    }
    
    // 检查URL参数中是否有editMode=true
    const urlParams = new URLSearchParams(window.location.search);
    const urlEditMode = urlParams.get('editMode');

    // 检查localStorage中的编辑模式状态
    const localStorageEditMode = localStorage.getItem('editMode');

    // 如果URL参数或localStorage中有编辑模式状态，则启用编辑模式
    if (urlEditMode === 'true' || localStorageEditMode === 'true') {
        // 清除URL参数中的editMode，避免重复设置
        if (urlEditMode === 'true') {
            const currentUrl = new URL(window.location);
            currentUrl.searchParams.delete('editMode');
            // 使用replaceState避免再次触发页面加载
            window.history.replaceState({}, document.title, currentUrl.toString());
        }

        // 设置编辑模式状态并更新UI
        editMode = true;
        const editModeToggle = document.getElementById('editModeToggle');
        if (editModeToggle) {
            editModeToggle.classList.add('btn-primary');
            editModeToggle.classList.remove('btn-outline');
        }
        showEditElements();
        initSortable();
    }
}
