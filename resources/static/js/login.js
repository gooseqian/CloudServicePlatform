// 登录页面JavaScript功能

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    initializeLoginPage();
    setupEventListeners();
});

// 初始化登录页面
function initializeLoginPage() {
    console.log('初始化登录页面...');
    
    // 检查是否有错误消息
    checkForErrorMessages();
    
    // 设置表单验证
    setupFormValidation();
    
    // 设置用户类型切换
    setupUserTypeSwitch();
}

// 设置事件监听器
function setupEventListeners() {
    const loginForm = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    
    if (loginForm) {
        loginForm.addEventListener('submit', handleLoginSubmit);
    }
    
    if (usernameInput) {
        usernameInput.addEventListener('input', validateUsername);
        usernameInput.addEventListener('blur', validateUsername);
    }
    
    if (passwordInput) {
        passwordInput.addEventListener('input', validatePassword);
        passwordInput.addEventListener('blur', validatePassword);
    }
    
    // 实时验证
    setupRealTimeValidation();
}

// 检查错误消息
function checkForErrorMessages() {
    const errorAlert = document.querySelector('.alert-error');
    if (errorAlert) {
        // 错误消息显示动画
        errorAlert.style.opacity = '0';
        errorAlert.style.transform = 'translateY(-10px)';
        
        setTimeout(() => {
            errorAlert.style.transition = 'all 0.3s ease';
            errorAlert.style.opacity = '1';
            errorAlert.style.transform = 'translateY(0)';
        }, 100);
    }
}

// 设置表单验证
function setupFormValidation() {
    const inputs = document.querySelectorAll('input[required]');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
    });
}

// 实时验证设置
function setupRealTimeValidation() {
    const inputs = document.querySelectorAll('input[required]');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            clearFieldError(this);
        });
    });
}

// 验证用户名/邮箱
function validateUsername() {
    const username = document.getElementById('username');
    const value = username.value.trim();
    
    if (!value) {
        showFieldError(username, '请输入用户名或邮箱');
        return false;
    }
    
    // 检查是否为邮箱格式
    if (value.includes('@')) {
        if (!isValidEmail(value)) {
            showFieldError(username, '请输入有效的邮箱地址');
            return false;
        }
    } else {
        // 检查用户名格式（字母数字下划线，3-20位）
        if (!/^[a-zA-Z0-9_]{3,20}$/.test(value)) {
            showFieldError(username, '用户名应为3-20位字母、数字或下划线');
            return false;
        }
    }
    
    clearFieldError(username);
    return true;
}

// 验证密码
function validatePassword() {
    const password = document.getElementById('password');
    const value = password.value;
    
    if (!value) {
        showFieldError(password, '请输入密码');
        return false;
    }
    
    if (value.length < 6) {
        showFieldError(password, '密码长度不能少于6位');
        return false;
    }
    
    clearFieldError(password);
    return true;
}

// 通用字段验证
function validateField(field) {
    const value = field.value.trim();
    
    if (!value) {
        showFieldError(field, '此字段为必填项');
        return false;
    }
    
    clearFieldError(field);
    return true;
}

// 显示字段错误
function showFieldError(field, message) {
    clearFieldError(field);
    
    field.style.borderColor = '#ff4444';
    field.style.background = '#fff5f5';
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.style.cssText = `
        color: #ff4444;
        font-size: 0.85rem;
        margin-top: 5px;
        display: flex;
        align-items: center;
        animation: slideIn 0.3s ease;
    `;
    
    errorDiv.innerHTML = `
        <i class="fas fa-exclamation-circle" style="margin-right: 5px;"></i>
        ${message}
    `;
    
    field.parentNode.appendChild(errorDiv);
}

// 清除字段错误
function clearFieldError(field) {
    field.style.borderColor = '';
    field.style.background = '';
    
    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            if (existingError.parentNode) {
                existingError.parentNode.removeChild(existingError);
            }
        }, 300);
    }
}

// 邮箱验证函数
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// 设置用户类型切换
function setupUserTypeSwitch() {
    const userTypeBtns = document.querySelectorAll('.user-type-btn');
    
    userTypeBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // 移除所有激活状态
            userTypeBtns.forEach(b => b.classList.remove('active'));
            // 添加当前激活状态
            this.classList.add('active');
            
            const userType = this.getAttribute('data-type');
            updateLoginFormForUserType(userType);
        });
    });
}

// 根据用户类型更新登录表单
function updateLoginFormForUserType(userType) {
    const loginTitle = document.querySelector('.login-form h2');
    const usernameLabel = document.querySelector('label[for="username"]');
    
    if (userType === 'admin') {
        if (loginTitle) loginTitle.textContent = '管理员登录';
        if (usernameLabel) usernameLabel.innerHTML = '<i class="fas fa-user-shield"></i> 管理员账号';
    } else {
        if (loginTitle) loginTitle.textContent = '用户登录';
        if (usernameLabel) usernameLabel.innerHTML = '<i class="fas fa-user"></i> 用户名/邮箱';
    }
}

// 切换密码显示/隐藏
function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.querySelector('.toggle-password i');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.className = 'fas fa-eye-slash';
    } else {
        passwordInput.type = 'password';
        toggleIcon.className = 'fas fa-eye';
    }
}

// 处理登录表单提交
function handleLoginSubmit(event) {
    event.preventDefault();
    
    console.log('处理登录提交...');
    
    // 验证表单
    if (!validateForm()) {
        return false;
    }
    
    // 显示加载状态
    showLoadingState();
    
    // 获取表单数据
    const formData = new FormData(event.target);
    const loginData = {
        username: formData.get('username').trim(),
        password: formData.get('password')
    };
    
    console.log('提交登录数据:', loginData);
    
    // 发送登录请求
    submitLoginRequest(loginData);
    
    return false;
}

// 验证整个表单
function validateForm() {
    const usernameValid = validateUsername();
    const passwordValid = validatePassword();
    
    return usernameValid && passwordValid;
}

// 显示加载状态
function showLoadingState() {
    const submitBtn = document.querySelector('.login-btn');
    const originalText = submitBtn.innerHTML;
    
    submitBtn.disabled = true;
    submitBtn.innerHTML = `
        <i class="fas fa-spinner fa-spin"></i>
        登录中...
    `;
    submitBtn.style.opacity = '0.7';
    
    // 3秒后恢复（防止无限加载）
    setTimeout(() => {
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalText;
        submitBtn.style.opacity = '1';
    }, 3000);
}

// 提交登录请求
function submitLoginRequest(loginData) {
    // 这里使用表单的原始提交方式，由Spring Security处理
    // 如果需要AJAX方式，可以取消注释下面的代码
    /*
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('登录失败');
    })
    .then(data => {
        if (data.success) {
            // 登录成功，跳转到首页
            window.location.href = '/';
        } else {
            showErrorMessage(data.message || '登录失败');
        }
    })
    .catch(error => {
        console.error('登录错误:', error);
        showErrorMessage('网络错误，请稍后重试');
    });
    */
    
    // 直接提交表单（由Spring Security处理）
    document.getElementById('loginForm').submit();
}

// 显示错误消息
function showErrorMessage(message) {
    // 创建错误消息元素
    const errorDiv = document.createElement('div');
    errorDiv.className = 'alert alert-error';
    errorDiv.innerHTML = `
        <i class="fas fa-exclamation-circle"></i>
        ${message}
    `;
    
    // 添加到表单前面
    const form = document.querySelector('.login-form');
    const firstChild = form.firstChild;
    form.insertBefore(errorDiv, firstChild);
    
    // 添加动画
    errorDiv.style.opacity = '0';
    errorDiv.style.transform = 'translateY(-10px)';
    
    setTimeout(() => {
        errorDiv.style.transition = 'all 0.3s ease';
        errorDiv.style.opacity = '1';
        errorDiv.style.transform = 'translateY(0)';
    }, 100);
    
    // 5秒后自动移除
    setTimeout(() => {
        if (errorDiv.parentNode) {
            errorDiv.style.opacity = '0';
            errorDiv.style.transform = 'translateY(-10px)';
            setTimeout(() => {
                if (errorDiv.parentNode) {
                    errorDiv.parentNode.removeChild(errorDiv);
                }
            }, 300);
        }
    }, 5000);
}

// 添加CSS动画
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            opacity: 0;
            transform: translateY(-5px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    @keyframes slideOut {
        from {
            opacity: 1;
            transform: translateY(0);
        }
        to {
            opacity: 0;
            transform: translateY(-5px);
        }
    }
    
    .field-error {
        animation: slideIn 0.3s ease;
    }
`;

document.head.appendChild(style);

// 键盘快捷键支持
document.addEventListener('keydown', function(event) {
    // Enter键提交表单
    if (event.key === 'Enter' && !event.ctrlKey && !event.shiftKey) {
        const activeElement = document.activeElement;
        if (activeElement.tagName === 'INPUT' && activeElement.type !== 'checkbox') {
            event.preventDefault();
            document.getElementById('loginForm').dispatchEvent(new Event('submit'));
        }
    }
    
    // Ctrl+Enter 切换密码显示
    if (event.ctrlKey && event.key === 'Enter') {
        togglePassword();
    }
});

// 页面卸载前的清理
window.addEventListener('beforeunload', function() {
    const submitBtn = document.querySelector('.login-btn');
    if (submitBtn && submitBtn.disabled) {
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-sign-in-alt"></i> 登录';
        submitBtn.style.opacity = '1';
    }
});