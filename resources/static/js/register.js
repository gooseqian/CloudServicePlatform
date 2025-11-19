// 注册页面JavaScript功能

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    initializeRegisterPage();
    setupEventListeners();
});

// 初始化注册页面
function initializeRegisterPage() {
    console.log('初始化注册页面...');
    
    // 设置实时验证
    setupRealTimeValidation();
    
    // 设置密码强度检测
    setupPasswordStrength();
    
    // 初始化步骤
    updateStepProgress();
}

// 设置事件监听器
function setupEventListeners() {
    const registerForm = document.getElementById('registerForm');
    const inputs = document.querySelectorAll('input[required]');
    
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegisterSubmit);
    }
    
    // 为所有输入框添加验证监听
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
        
        input.addEventListener('input', function() {
            clearFieldError(this);
            
            // 实时更新密码强度
            if (this.id === 'password') {
                updatePasswordStrength(this.value);
            }
            
            // 实时验证密码匹配
            if (this.id === 'password' || this.id === 'confirmPassword') {
                validatePasswordMatch();
            }
        });
    });
    
    // 用户名和邮箱的实时可用性检查
    setupAvailabilityChecks();
}

// 设置实时验证
function setupRealTimeValidation() {
    const debounce = (func, wait) => {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    };
    
    // 用户名实时验证
    const usernameInput = document.getElementById('username');
    if (usernameInput) {
        usernameInput.addEventListener('input', debounce(function() {
            validateUsername(this.value);
        }, 500));
    }
    
    // 邮箱实时验证
    const emailInput = document.getElementById('email');
    if (emailInput) {
        emailInput.addEventListener('input', debounce(function() {
            validateEmail(this.value);
        }, 500));
    }
}

// 设置密码强度检测
function setupPasswordStrength() {
    const passwordInput = document.getElementById('password');
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            updatePasswordStrength(this.value);
        });
    }
}

// 设置可用性检查
function setupAvailabilityChecks() {
    const usernameInput = document.getElementById('username');
    const emailInput = document.getElementById('email');
    
    if (usernameInput) {
        usernameInput.addEventListener('blur', function() {
            if (this.value.trim() && validateUsername(this.value)) {
                checkUsernameAvailability(this.value);
            }
        });
    }
    
    if (emailInput) {
        emailInput.addEventListener('blur', function() {
            if (this.value.trim() && validateEmail(this.value)) {
                checkEmailAvailability(this.value);
            }
        });
    }
}

// 验证用户名
function validateUsername(value) {
    const username = value.trim();
    const feedback = document.querySelector('#username + .input-feedback');
    
    if (!username) {
        showFieldError(document.getElementById('username'), '请输入用户名');
        return false;
    }
    
    if (username.length < 3 || username.length > 20) {
        showFieldError(document.getElementById('username'), '用户名长度应为3-20位');
        return false;
    }
    
    if (!/^[a-zA-Z0-9_]+$/.test(username)) {
        showFieldError(document.getElementById('username'), '用户名只能包含字母、数字和下划线');
        return false;
    }
    
    if (feedback) {
        feedback.innerHTML = '<i class="fas fa-check"></i> 用户名格式正确';
        feedback.className = 'input-feedback success';
    }
    
    return true;
}

// 验证邮箱
function validateEmail(value) {
    const email = value.trim();
    const feedback = document.querySelector('#email + .input-feedback');
    
    if (!email) {
        showFieldError(document.getElementById('email'), '请输入邮箱地址');
        return false;
    }
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        showFieldError(document.getElementById('email'), '请输入有效的邮箱地址');
        return false;
    }
    
    if (feedback) {
        feedback.innerHTML = '<i class="fas fa-check"></i> 邮箱格式正确';
        feedback.className = 'input-feedback success';
    }
    
    return true;
}

// 验证密码
function validatePassword(value) {
    const password = value;
    
    if (!password) {
        showFieldError(document.getElementById('password'), '请输入密码');
        return false;
    }
    
    if (password.length < 6) {
        showFieldError(document.getElementById('password'), '密码长度不能少于6位');
        return false;
    }
    
    return true;
}

// 验证密码匹配
function validatePasswordMatch() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const feedback = document.querySelector('#confirmPassword + .input-feedback');
    
    if (!password || !confirmPassword) {
        return false;
    }
    
    if (password !== confirmPassword) {
        showFieldError(document.getElementById('confirmPassword'), '两次输入的密码不一致');
        return false;
    }
    
    if (feedback) {
        feedback.innerHTML = '<i class="fas fa-check"></i> 密码匹配';
        feedback.className = 'input-feedback success';
    }
    
    return true;
}

// 更新密码强度
function updatePasswordStrength(password) {
    const strengthBar = document.querySelector('.strength-level');
    const strengthText = document.querySelector('.strength-text');
    
    if (!strengthBar || !strengthText) return;
    
    let strength = 0;
    let text = '密码强度';
    let className = '';
    
    if (password.length >= 6) strength += 1;
    if (password.length >= 8) strength += 1;
    if (/[a-z]/.test(password)) strength += 1;
    if (/[A-Z]/.test(password)) strength += 1;
    if (/[0-9]/.test(password)) strength += 1;
    if (/[^a-zA-Z0-9]/.test(password)) strength += 1;
    
    if (password.length === 0) {
        strengthBar.style.width = '0%';
        strengthBar.className = 'strength-level';
        strengthText.textContent = '密码强度';
        return;
    }
    
    if (strength <= 2) {
        strengthBar.style.width = '33%';
        strengthBar.className = 'strength-level weak';
        text = '弱';
    } else if (strength <= 4) {
        strengthBar.style.width = '66%';
        strengthBar.className = 'strength-level medium';
        text = '中等';
    } else {
        strengthBar.style.width = '100%';
        strengthBar.className = 'strength-level strong';
        text = '强';
    }
    
    strengthText.textContent = text;
}

// 检查用户名可用性
function checkUsernameAvailability(username) {
    const feedback = document.querySelector('#username + .input-feedback');
    
    // 模拟API调用
    setTimeout(() => {
        // 这里应该调用真实的API
        // fetch(`/api/auth/check-username?username=${username}`)
        // .then(response => response.json())
        // .then(data => {
        //     if (data.available) {
        //         feedback.innerHTML = '<i class="fas fa-check"></i> 用户名可用';
        //         feedback.className = 'input-feedback success';
        //     } else {
        //         feedback.innerHTML = '<i class="fas fa-times"></i> 用户名已存在';
        //         feedback.className = 'input-feedback error';
        //     }
        // });
        
        // 模拟成功
        feedback.innerHTML = '<i class="fas fa-check"></i> 用户名可用';
        feedback.className = 'input-feedback success';
    }, 1000);
}

// 检查邮箱可用性
function checkEmailAvailability(email) {
    const feedback = document.querySelector('#email + .input-feedback');
    
    // 模拟API调用
    setTimeout(() => {
        // 这里应该调用真实的API
        // fetch(`/api/auth/check-email?email=${email}`)
        // .then(response => response.json())
        // .then(data => {
        //     if (data.available) {
        //         feedback.innerHTML = '<i class="fas fa-check"></i> 邮箱可用';
        //         feedback.className = 'input-feedback success';
        //     } else {
        //         feedback.innerHTML = '<i class="fas fa-times"></i> 邮箱已注册';
        //         feedback.className = 'input-feedback error';
        //     }
        // });
        
        // 模拟成功
        feedback.innerHTML = '<i class="fas fa-check"></i> 邮箱可用';
        feedback.className = 'input-feedback success';
    }, 1000);
}

// 显示字段错误
function showFieldError(field, message) {
    clearFieldError(field);
    
    field.classList.add('error');
    
    const feedback = field.nextElementSibling;
    if (feedback && feedback.classList.contains('input-feedback')) {
        feedback.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${message}`;
        feedback.className = 'input-feedback error';
    }
}

// 清除字段错误
function clearFieldError(field) {
    field.classList.remove('error', 'success');
    
    const feedback = field.nextElementSibling;
    if (feedback && feedback.classList.contains('input-feedback')) {
        feedback.innerHTML = '';
        feedback.className = 'input-feedback';
    }
}

// 验证字段
function validateField(field) {
    const value = field.value.trim();
    
    if (field.hasAttribute('required') && !value) {
        showFieldError(field, '此字段为必填项');
        return false;
    }
    
    switch (field.id) {
        case 'username':
            return validateUsername(value);
        case 'email':
            return validateEmail(value);
        case 'password':
            return validatePassword(value);
        case 'confirmPassword':
            return validatePasswordMatch();
        default:
            return true;
    }
}

// 验证当前步骤
function validateCurrentStep() {
    const currentStep = document.querySelector('.form-step.active');
    const inputs = currentStep.querySelectorAll('input[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!validateField(input)) {
            isValid = false;
        }
    });
    
    return isValid;
}

// 下一步
function nextStep() {
    if (!validateCurrentStep()) {
        showErrorMessage('请正确填写所有必填字段');
        return;
    }
    
    const currentStep = document.querySelector('.form-step.active');
    const nextStepNumber = parseInt(currentStep.dataset.step) + 1;
    const nextStep = document.querySelector(`.form-step[data-step="${nextStepNumber}"]`);
    
    if (nextStep) {
        currentStep.classList.remove('active');
        nextStep.classList.add('active');
        updateStepProgress();
        
        // 如果是最后一步，更新汇总信息
        if (nextStepNumber === 3) {
            updateSummaryInfo();
        }
    }
}

// 上一步
function prevStep() {
    const currentStep = document.querySelector('.form-step.active');
    const prevStepNumber = parseInt(currentStep.dataset.step) - 1;
    const prevStep = document.querySelector(`.form-step[data-step="${prevStepNumber}"]`);
    
    if (prevStep) {
        currentStep.classList.remove('active');
        prevStep.classList.add('active');
        updateStepProgress();
    }
}

// 更新步骤进度
function updateStepProgress() {
    const currentStep = document.querySelector('.form-step.active');
    const currentStepNumber = parseInt(currentStep.dataset.step);
    const steps = document.querySelectorAll('.step');
    
    steps.forEach((step, index) => {
        if (index + 1 <= currentStepNumber) {
            step.classList.add('active');
        } else {
            step.classList.remove('active');
        }
    });
}

// 更新汇总信息
function updateSummaryInfo() {
    const fields = ['username', 'email', 'realName', 'phoneNumber', 'company'];
    
    fields.forEach(field => {
        const input = document.getElementById(field);
        const summary = document.getElementById(`summary-${field}`);
        if (input && summary) {
            summary.textContent = input.value || '未填写';
        }
    });
    

}

// 处理注册表单提交
function handleRegisterSubmit(event) {
    event.preventDefault();
    
    console.log('处理注册提交...');
    
    // 验证所有步骤
    if (!validateAllSteps()) {
        showErrorMessage('请正确填写所有必填信息');
        return false;
    }
    
    // 验证条款同意
    const agreeTerms = document.getElementById('agreeTerms');
    if (!agreeTerms.checked) {
        showErrorMessage('请同意用户服务协议和隐私政策');
        return false;
    }
    
    // 显示加载状态
    showLoadingState();
    
    // 获取表单数据
    const formData = new FormData(event.target);
    const registerData = {
        username: formData.get('username').trim(),
        email: formData.get('email').trim(),
        password: formData.get('password'),
        confirmPassword: formData.get('confirmPassword'),
        realName: formData.get('realName')?.trim() || '',
        phoneNumber: formData.get('phoneNumber')?.trim() || '',
        company: formData.get('company')?.trim() || '',
        idCard: formData.get('idCard')?.trim() || '',
        userRole: formData.get('userRole') || 'SERVICE_REQUESTER'
    };
    
    console.log('提交注册数据:', registerData);
    
    // 发送注册请求
    submitRegisterRequest(registerData);
    
    return false;
}

// 验证所有步骤
function validateAllSteps() {
    const steps = document.querySelectorAll('.form-step');
    let isValid = true;
    
    steps.forEach(step => {
        const inputs = step.querySelectorAll('input[required]');
        inputs.forEach(input => {
            if (!validateField(input)) {
                isValid = false;
            }
        });
    });
    
    return isValid;
}

// 显示加载状态
function showLoadingState() {
    const submitBtn = document.querySelector('.btn-submit');
    const originalText = submitBtn.innerHTML;
    
    submitBtn.disabled = true;
    submitBtn.innerHTML = `
        <i class="fas fa-spinner fa-spin"></i>
        注册中...
    `;
    submitBtn.style.opacity = '0.7';
    
    // 5秒后恢复（防止无限加载）
    setTimeout(() => {
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalText;
        submitBtn.style.opacity = '1';
    }, 5000);
}

// 提交注册请求
function submitRegisterRequest(registerData) {
    // 使用真实的API调用
    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(registerData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('注册失败');
    })
    .then(data => {
        if (data.success) {
            // 注册成功，跳转到登录页面
            alert('注册成功！即将跳转到登录页面...');
            window.location.href = '/login';
        } else {
            showErrorMessage(data.message || '注册失败');
        }
    })
    .catch(error => {
        console.error('注册错误:', error);
        showErrorMessage('网络错误，请稍后重试');
    });
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
    
    errorDiv.style.cssText = `
        background: #fee;
        color: #c33;
        border: 1px solid #fcc;
        padding: 12px 15px;
        border-radius: 8px;
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        animation: slideIn 0.3s ease;
    `;
    
    // 添加到表单前面
    const form = document.querySelector('.register-form');
    const firstChild = form.querySelector('.form-steps');
    form.insertBefore(errorDiv, firstChild.nextSibling);
    
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
    .alert {
        animation: slideIn 0.3s ease;
    }
    
    @keyframes slideIn {
        from {
            opacity: 0;
            transform: translateY(-10px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
`;

document.head.appendChild(style);

// 键盘快捷键支持
document.addEventListener('keydown', function(event) {
    // Enter键在非最后一步执行下一步，在最后一步提交表单
    if (event.key === 'Enter' && !event.ctrlKey && !event.shiftKey) {
        const activeElement = document.activeElement;
        if (activeElement.tagName === 'INPUT' && activeElement.type !== 'checkbox') {
            event.preventDefault();
            
            const currentStep = document.querySelector('.form-step.active');
            const currentStepNumber = parseInt(currentStep.dataset.step);
            
            if (currentStepNumber < 3) {
                nextStep();
            } else {
                document.getElementById('registerForm').dispatchEvent(new Event('submit'));
            }
        }
    }
    
    // Ctrl+Enter 直接提交
    if (event.ctrlKey && event.key === 'Enter') {
        event.preventDefault();
        document.getElementById('registerForm').dispatchEvent(new Event('submit'));
    }
});

// 页面卸载前的清理
window.addEventListener('beforeunload', function() {
    const submitBtn = document.querySelector('.btn-submit');
    if (submitBtn && submitBtn.disabled) {
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-user-plus"></i> 完成注册';
        submitBtn.style.opacity = '1';
    }
});